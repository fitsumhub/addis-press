const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_ethio_123";
const BOB_UID = "bob_ethio_456";

const [emulatorHost, emulatorPortStr] = (
  process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085"
).split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated users cannot read or write any user collections", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  const past = new Date(Date.now() - 2000);
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).get());
  await assertFails(
    unauthDb.collection("users").doc(ALICE_UID).collection("bookmarks").get()
  );
  await assertFails(
    unauthDb.collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      displayName: "Anon",
      preferredLanguage: "en",
      followedCategories: [],
      interests: [],
      notificationsEnabled: true,
      breakingAlertsEnabled: true,
      aiDigestEnabled: true,
      createdAt: past,
      updatedAt: past,
    })
  );
});

test("User profile create, partial update, and cross-user isolation", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  const created = new Date(Date.now() - 10000);

  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      displayName: "Abebe Bikila",
      email: "abebe@example.com",
      preferredLanguage: "am",
      followedCategories: ["ai", "technology"],
      interests: ["Android", "Fintech"],
      notificationsEnabled: true,
      breakingAlertsEnabled: true,
      aiDigestEnabled: false,
      createdAt: created,
      updatedAt: created,
    })
  );

  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).update({
      preferredLanguage: "en",
      followedCategories: ["ai", "technology", "startups"],
      updatedAt: new Date(Date.now() - 1000),
    })
  );

  // Reject future timestamp
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).update({
      preferredLanguage: "am",
      updatedAt: new Date(Date.now() + 60000),
    })
  );

  // Reject immutable createdAt modification
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).update({
      createdAt: new Date(Date.now() - 500),
      updatedAt: new Date(Date.now() - 500),
    })
  );

  // Reject ghost field
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).update({
      isAdmin: true,
      updatedAt: new Date(Date.now() - 500),
    })
  );

  // Cross-user isolation
  await assertFails(bobDb.collection("users").doc(ALICE_UID).get());
  await assertFails(
    bobDb.collection("users").doc(ALICE_UID).update({
      displayName: "Hijacked",
      updatedAt: new Date(Date.now() - 500),
    })
  );
});

test("Bookmarks: create, list, reading list update, and reject @DocumentId key", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  const created = new Date(Date.now() - 5000);

  await assertSucceeds(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("bookmarks")
      .doc("article_fayda_id")
      .set({
        userId: ALICE_UID,
        itemId: "fayda_id",
        itemType: "article",
        readingListName: "Read Later",
        titleEn: "Fayda National ID Ethiopia Guide",
        titleAm: "የፋይዳ ብሔራዊ መታወቂያ መመሪያ",
        category: "technology",
        coverKey: "fayda",
        createdAt: created,
        updatedAt: created,
      })
  );

  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("bookmarks").get()
  );

  await assertSucceeds(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("bookmarks")
      .doc("article_fayda_id")
      .update({
        readingListName: "Ethiopian Tech",
        updatedAt: new Date(Date.now() - 1000),
      })
  );

  // Reject extra `id` field matching @DocumentId trap
  await assertFails(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("bookmarks")
      .doc("bad_doc")
      .set({
        id: "bad_doc",
        userId: ALICE_UID,
        itemId: "fayda_id",
        itemType: "article",
        readingListName: "Read Later",
        titleEn: "Title",
        titleAm: "ርዕስ",
        category: "technology",
        coverKey: "fayda",
        createdAt: created,
        updatedAt: created,
      })
  );

  await assertFails(
    bobDb.collection("users").doc(ALICE_UID).collection("bookmarks").get()
  );
});

test("Reading history, likes, reports, and editorial submissions lifecycle", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const created = new Date(Date.now() - 5000);

  await assertSucceeds(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("reading_history")
      .doc("fayda_id")
      .set({
        userId: ALICE_UID,
        articleId: "fayda_id",
        titleEn: "Fayda National ID Ethiopia Guide",
        titleAm: "የፋይዳ ብሔራዊ መታወቂያ መመሪያ",
        category: "technology",
        progressPercent: 75,
        createdAt: created,
        updatedAt: created,
      })
  );

  await assertSucceeds(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("likes")
      .doc("fayda_id")
      .set({
        userId: ALICE_UID,
        articleId: "fayda_id",
        createdAt: created,
      })
  );

  await assertSucceeds(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("submissions")
      .doc("story_1")
      .set({
        userId: ALICE_UID,
        titleEn: "Addis Ababa Startup Ecosystem 2026",
        titleAm: "የአዲስ አበባ ስታርትአፕ ምህዳር 2026",
        summaryEn: "An overview of emerging fintech and AI startups in Addis.",
        summaryAm: "በአዲስ አበባ የሚያድጉ የፊንቴክ እና የAI ጅምሮች አጠቃላይ እይታ።",
        contentEn: "Ethiopia's technology ecosystem is accelerating rapidly across mobile payments and AI tools.",
        contentAm: "የኢትዮጵያ የቴክኖሎጂ ምህዳር በሞባይል ክፍያዎች እና በAI መሳሪያዎች በፍጥነት እያደገ ነው።",
        category: "startups",
        tags: ["Ethiopia", "Fintech"],
        coverKey: "fintech",
        authorName: "Abebe Bikila",
        readingMinutes: 4,
        status: "draft",
        createdAt: created,
        updatedAt: created,
      })
  );

  // Publish and then archive; archived submission is locked
  await assertSucceeds(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("submissions")
      .doc("story_1")
      .update({
        status: "published",
        updatedAt: new Date(Date.now() - 1000),
      })
  );

  await assertSucceeds(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("submissions")
      .doc("story_1")
      .update({
        status: "archived",
        updatedAt: new Date(Date.now() - 500),
      })
  );

  await assertFails(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("submissions")
      .doc("story_1")
      .update({
        status: "published",
        updatedAt: new Date(Date.now() - 100),
      })
  );

  await assertSucceeds(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("reports")
      .doc("report_1")
      .set({
        userId: ALICE_UID,
        targetId: "fayda_id",
        targetType: "article",
        reason: "Outdated information",
        details: "Please update registration steps.",
        status: "submitted",
        createdAt: created,
      })
  );
});
