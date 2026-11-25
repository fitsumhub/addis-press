import React, { useMemo, useState } from 'react';
import {
  FlatList,
  Modal,
  ScrollView,
  StyleSheet,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from 'react-native';
import { Edit3, Plus, Search } from 'lucide-react-native';
import { FilterPill, StatusPill } from '../components/AdminUiPrimitives';
import { CATEGORY_BEATS } from '../data/seedCatalog';
import { useAdminStore } from '../store/AdminContext';
import { ThemePalette } from '../theme/tokens';
import { ArticleRecord } from '../types/admin';

type ArticleFilter = 'all' | 'live' | 'hidden' | 'featured' | 'breaking';

export function ArticlesCmsScreen({ palette }: { palette: ThemePalette }) {
  const { language, articles, toggleArticleFlag, updateArticleMetadata, createArticle } = useAdminStore();
  const isAm = language === 'am';

  const [query, setQuery] = useState('');
  const [filter, setFilter] = useState<ArticleFilter>('all');
  const [editing, setEditing] = useState<ArticleRecord | null>(null);
  const [isCreating, setIsCreating] = useState(false);

  const [draftTitleEn, setDraftTitleEn] = useState('');
  const [draftTitleAm, setDraftTitleAm] = useState('');
  const [draftCategoryId, setDraftCategoryId] = useState('technology');
  const [draftAuthor, setDraftAuthor] = useState('AddisPress Editorial');

  const filtered = useMemo(() => {
    const q = query.trim().toLowerCase();
    return articles.filter((a) => {
      const matchesFilter =
        filter === 'all' ||
        (filter === 'live' && !a.isHidden) ||
        (filter === 'hidden' && a.isHidden) ||
        (filter === 'featured' && a.isFeatured) ||
        (filter === 'breaking' && a.isBreaking);
      const matchesQuery =
        q.length === 0 ||
        a.titleEn.toLowerCase().includes(q) ||
        a.titleAm.toLowerCase().includes(q) ||
        a.categoryLabelEn.toLowerCase().includes(q) ||
        a.categoryLabelAm.toLowerCase().includes(q);
      return matchesFilter && matchesQuery;
    });
  }, [articles, filter, query]);

  const openEditModal = (article: ArticleRecord) => {
    setIsCreating(false);
    setEditing(article);
    setDraftTitleEn(article.titleEn);
    setDraftTitleAm(article.titleAm);
    setDraftCategoryId(article.categoryId);
    setDraftAuthor(article.authorName);
  };

  const openCreateModal = () => {
    setEditing(null);
    setIsCreating(true);
    setDraftTitleEn('');
    setDraftTitleAm('');
    setDraftCategoryId('technology');
    setDraftAuthor('AddisPress Editorial');
  };

  const handleSaveModal = () => {
    if (!draftTitleEn.trim()) return;
    const cat = CATEGORY_BEATS.find((c) => c.id === draftCategoryId) ?? CATEGORY_BEATS[0];
    if (isCreating) {
      createArticle({
        titleEn: draftTitleEn.trim(),
        titleAm: draftTitleAm.trim() || draftTitleEn.trim(),
        categoryId: cat.id,
        categoryLabelEn: cat.labelEn,
        categoryLabelAm: cat.labelAm,
        authorName: draftAuthor.trim() || 'AddisPress Editorial',
        readingMinutes: 4,
        isBreaking: false,
        isFeatured: true,
        isTrending: false,
      });
      setIsCreating(false);
    } else if (editing) {
      updateArticleMetadata(editing.id, draftTitleEn.trim(), draftTitleAm.trim(), cat.id, cat.labelEn, cat.labelAm);
      setEditing(null);
    }
  };

  return (
    <View style={styles.container}>
      <View style={styles.headerControls}>
        <TouchableOpacity
          style={[styles.addBtn, { backgroundColor: palette.primary }]}
          onPress={openCreateModal}
        >
          <Plus size={18} color="#FFFFFF" />
          <Text style={styles.addBtnText}>
            {isAm ? 'አዲስ ጽሑፍ ፍጠር' : 'Publish New Bilingual Article'}
          </Text>
        </TouchableOpacity>

        <View style={[styles.searchBox, { backgroundColor: palette.surface, borderColor: palette.border }]}>
          <Search size={18} color={palette.textSecondary} />
          <TextInput
            value={query}
            onChangeText={setQuery}
            placeholder={isAm ? 'በእንግሊዝኛ ወይም አማርኛ ይፈልጉ...' : 'Search articles in EN or አማርኛ...'}
            placeholderTextColor={palette.textSecondary}
            style={[styles.searchInput, { color: palette.textPrimary }]}
          />
        </View>

        <ScrollView horizontal showsHorizontalScrollIndicator={false} contentContainerStyle={styles.filtersRow}>
          {([
            ['all', isAm ? 'ሁሉም' : 'All'],
            ['live', isAm ? 'ንቁ' : 'Live'],
            ['hidden', isAm ? 'የተደበቁ' : 'Hidden'],
            ['featured', isAm ? 'ተመራጭ' : 'Featured'],
            ['breaking', isAm ? 'ሰበር' : 'Breaking'],
          ] as Array<[ArticleFilter, string]>).map(([id, label]) => (
            <FilterPill
              key={id}
              label={label}
              selected={filter === id}
              palette={palette}
              onPress={() => setFilter(id)}
            />
          ))}
        </ScrollView>
      </View>

      <FlatList
        data={filtered}
        keyExtractor={(item) => item.id}
        contentContainerStyle={styles.listContent}
        renderItem={({ item }) => (
          <View
            style={[
              styles.card,
              {
                backgroundColor: palette.surface,
                borderColor: item.isHidden ? palette.danger : palette.border,
              },
            ]}
          >
            <View style={styles.cardHeader}>
              <View style={styles.pillsRow}>
                <StatusPill
                  label={isAm ? item.categoryLabelAm : item.categoryLabelEn}
                  color={palette.primary}
                  backgroundColor={palette.primarySoft}
                />
                <StatusPill
                  label={item.isHidden ? 'HIDDEN' : 'LIVE'}
                  color={item.isHidden ? palette.danger : palette.emerald}
                  backgroundColor={item.isHidden ? palette.dangerSoft : palette.emeraldSoft}
                />
              </View>
              <TouchableOpacity onPress={() => openEditModal(item)} style={styles.editIconBtn}>
                <Edit3 size={18} color={palette.primary} />
              </TouchableOpacity>
            </View>

            <Text style={[styles.title, { color: palette.textPrimary }]} numberOfLines={2}>
              {isAm ? item.titleAm : item.titleEn}
            </Text>
            <Text style={[styles.meta, { color: palette.textSecondary }]}>
              {item.authorName} · {item.publishedDate} · {item.readingMinutes} min
            </Text>

            <View style={styles.flagRow}>
              <FilterPill
                label={item.isHidden ? (isAm ? 'ተደብቋል' : 'Hidden') : (isAm ? 'ይታያል' : 'Visible')}
                selected={!item.isHidden}
                palette={palette}
                onPress={() => toggleArticleFlag(item.id, 'isHidden')}
              />
              <FilterPill
                label={isAm ? 'ሰበር' : 'Breaking'}
                selected={item.isBreaking}
                palette={palette}
                onPress={() => toggleArticleFlag(item.id, 'isBreaking')}
              />
              <FilterPill
                label={isAm ? 'ተመራጭ' : 'Featured'}
                selected={item.isFeatured}
                palette={palette}
                onPress={() => toggleArticleFlag(item.id, 'isFeatured')}
              />
              <FilterPill
                label={isAm ? 'ተወዳጅ' : 'Trending'}
                selected={item.isTrending}
                palette={palette}
                onPress={() => toggleArticleFlag(item.id, 'isTrending')}
              />
            </View>
          </View>
        )}
      />

      <Modal visible={isCreating || editing !== null} transparent animationType="slide">
        <View style={styles.modalBackdrop}>
          <View style={[styles.modalSheet, { backgroundColor: palette.surface, borderColor: palette.border }]}>
            <Text style={[styles.modalTitle, { color: palette.textPrimary }]}>
              {isCreating ? (isAm ? 'አዲስ ጽሑፍ' : 'Publish New Article') : (isAm ? 'ጽሑፍ አስተካክል' : 'Edit Article Metadata')}
            </Text>
            <TextInput
              value={draftTitleEn}
              onChangeText={setDraftTitleEn}
              placeholder="English Headline *"
              placeholderTextColor={palette.textSecondary}
              style={[styles.input, { color: palette.textPrimary, borderColor: palette.border, backgroundColor: palette.surfaceElevated }]}
            />
            <TextInput
              value={draftTitleAm}
              onChangeText={setDraftTitleAm}
              placeholder="የአማርኛ ርዕስ (Amharic Headline)"
              placeholderTextColor={palette.textSecondary}
              style={[styles.input, { color: palette.textPrimary, borderColor: palette.border, backgroundColor: palette.surfaceElevated }]}
            />
            {isCreating && (
              <TextInput
                value={draftAuthor}
                onChangeText={setDraftAuthor}
                placeholder="Author Name"
                placeholderTextColor={palette.textSecondary}
                style={[styles.input, { color: palette.textPrimary, borderColor: palette.border, backgroundColor: palette.surfaceElevated }]}
              />
            )}
            <ScrollView horizontal showsHorizontalScrollIndicator={false} contentContainerStyle={styles.filtersRow}>
              {CATEGORY_BEATS.map((beat) => (
                <FilterPill
                  key={beat.id}
                  label={isAm ? beat.labelAm : beat.labelEn}
                  selected={draftCategoryId === beat.id}
                  palette={palette}
                  onPress={() => setDraftCategoryId(beat.id)}
                />
              ))}
            </ScrollView>
            <View style={styles.modalActions}>
              <TouchableOpacity
                style={[styles.modalCancelBtn, { borderColor: palette.border }]}
                onPress={() => {
                  setIsCreating(false);
                  setEditing(null);
                }}
              >
                <Text style={{ color: palette.textPrimary, fontWeight: '700' }}>
                  {isAm ? 'ሰርዝ' : 'Cancel'}
                </Text>
              </TouchableOpacity>
              <TouchableOpacity
                style={[styles.modalSaveBtn, { backgroundColor: palette.primary }]}
                onPress={handleSaveModal}
              >
                <Text style={{ color: '#FFFFFF', fontWeight: '800' }}>
                  {isAm ? 'አስቀምጥ' : 'Save'}
                </Text>
              </TouchableOpacity>
            </View>
          </View>
        </View>
      </Modal>
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1 },
  headerControls: { padding: 16, paddingBottom: 8, gap: 10 },
  addBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    gap: 8,
    borderRadius: 12,
    paddingVertical: 13,
  },
  addBtnText: { color: '#FFFFFF', fontWeight: '800', fontSize: 14 },
  searchBox: {
    flexDirection: 'row',
    alignItems: 'center',
    borderRadius: 12,
    borderWidth: 1,
    paddingHorizontal: 12,
    height: 46,
    gap: 8,
  },
  searchInput: { flex: 1, fontSize: 14 },
  filtersRow: { flexDirection: 'row', gap: 8, paddingVertical: 2 },
  listContent: { padding: 16, paddingTop: 6, gap: 12 },
  card: { borderRadius: 16, borderWidth: 1, padding: 14, gap: 6 },
  cardHeader: { flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between' },
  pillsRow: { flexDirection: 'row', gap: 6 },
  editIconBtn: { padding: 6 },
  title: { fontSize: 15, fontWeight: '800', lineHeight: 20 },
  meta: { fontSize: 12 },
  flagRow: { flexDirection: 'row', flexWrap: 'wrap', gap: 8, marginTop: 6 },
  modalBackdrop: {
    flex: 1,
    backgroundColor: 'rgba(0,0,0,0.65)',
    justifyContent: 'center',
    padding: 20,
  },
  modalSheet: { borderRadius: 18, borderWidth: 1, padding: 18, gap: 12 },
  modalTitle: { fontSize: 17, fontWeight: '800' },
  input: {
    borderWidth: 1,
    borderRadius: 12,
    paddingHorizontal: 12,
    paddingVertical: 10,
    fontSize: 14,
  },
  modalActions: { flexDirection: 'row', gap: 10, marginTop: 6 },
  modalCancelBtn: {
    flex: 1,
    borderRadius: 12,
    borderWidth: 1,
    paddingVertical: 12,
    alignItems: 'center',
  },
  modalSaveBtn: {
    flex: 1,
    borderRadius: 12,
    paddingVertical: 12,
    alignItems: 'center',
  },
});
