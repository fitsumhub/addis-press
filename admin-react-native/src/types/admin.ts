export type AdminLanguage = 'en' | 'am';
export type AdminThemeMode = 'dark' | 'light';
export type AdminTabKey = 'overview' | 'articles' | 'directory' | 'ads' | 'moderation';

export interface ArticleRecord {
  id: string;
  titleEn: string;
  titleAm: string;
  categoryId: string;
  categoryLabelEn: string;
  categoryLabelAm: string;
  authorName: string;
  publishedDate: string;
  readingMinutes: number;
  isHidden: boolean;
  isBreaking: boolean;
  isFeatured: boolean;
  isTrending: boolean;
  isCustom?: boolean;
}

export interface DirectoryAppRecord {
  id: string;
  name: string;
  developer: string;
  categoryId: string;
  categoryLabelEn: string;
  taglineEn: string;
  taglineAm: string;
  playStoreUrl: string;
  accentColor: string;
  isHidden: boolean;
  isFeatured: boolean;
  isTrending: boolean;
  isCustom?: boolean;
}

export interface DirectoryAiToolRecord {
  id: string;
  name: string;
  provider: string;
  categoryId: string;
  pricingTier: string;
  isFreeTier: boolean;
  taglineEn: string;
  taglineAm: string;
  officialUrl: string;
  accentColor: string;
  isHidden: boolean;
  isFeatured: boolean;
  isCustom?: boolean;
}

export type AdPlacementType = 'native' | 'banner';

export interface AdCampaignRecord {
  id: string;
  placementType: AdPlacementType;
  sponsorName: string;
  headlineEn: string;
  headlineAm: string;
  bodyEn: string;
  ctaEn: string;
  badgeText: string;
  destinationUrl: string;
  accentColor: string;
  isActive: boolean;
  isCustom?: boolean;
}

export interface MonetizationConfig {
  adsEnabled: boolean;
  highYieldAds: boolean;
  bannerAdsEnabled: boolean;
  admobBannerUnitId: string;
  admobNativeUnitId: string;
  impressions: number;
  clicks: number;
}

export interface HomeLayoutConfig {
  showBreakingBanner: boolean;
  showFeaturedCarousel: boolean;
  showAppReviewsRail: boolean;
  showTrendingSection: boolean;
  homeFeedPageSize: 12 | 20 | 28 | 50;
  tickerMessageEn: string;
  tickerMessageAm: string;
}

export interface ModerationReportRecord {
  id: string;
  targetId: string;
  targetType: 'article' | 'app' | 'ai_tool';
  reason: string;
  details: string;
  createdAtLabel: string;
}

export type SubmissionStatus = 'draft' | 'review' | 'published' | 'archived';

export interface CommunitySubmissionRecord {
  id: string;
  titleEn: string;
  titleAm: string;
  authorName: string;
  categoryId: string;
  status: SubmissionStatus;
}
