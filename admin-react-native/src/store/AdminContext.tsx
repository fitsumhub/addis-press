import AsyncStorage from '@react-native-async-storage/async-storage';
import React, { createContext, useCallback, useContext, useEffect, useState } from 'react';
import {
  INITIAL_AD_CAMPAIGNS,
  INITIAL_AI_TOOLS,
  INITIAL_APPS,
  INITIAL_ARTICLES,
  INITIAL_HOME_LAYOUT,
  INITIAL_MONETIZATION,
  INITIAL_REPORTS,
  INITIAL_SUBMISSIONS,
} from '../data/seedCatalog';
import {
  AdCampaignRecord,
  AdminLanguage,
  AdminThemeMode,
  ArticleRecord,
  CommunitySubmissionRecord,
  DirectoryAiToolRecord,
  DirectoryAppRecord,
  HomeLayoutConfig,
  ModerationReportRecord,
  MonetizationConfig,
  SubmissionStatus,
} from '../types/admin';

const STORAGE_KEY = 'addispress_admin_state_v1';

interface PersistedAdminState {
  language: AdminLanguage;
  themeMode: AdminThemeMode;
  articles: ArticleRecord[];
  apps: DirectoryAppRecord[];
  aiTools: DirectoryAiToolRecord[];
  adCampaigns: AdCampaignRecord[];
  monetization: MonetizationConfig;
  homeLayout: HomeLayoutConfig;
  reports: ModerationReportRecord[];
  submissions: CommunitySubmissionRecord[];
}

interface AdminContextValue extends PersistedAdminState {
  isHydrated: boolean;
  setLanguage: (lang: AdminLanguage) => void;
  toggleThemeMode: () => void;
  updateHomeLayout: (patch: Partial<HomeLayoutConfig>) => void;
  toggleArticleFlag: (id: string, flag: 'isHidden' | 'isBreaking' | 'isFeatured' | 'isTrending') => void;
  updateArticleMetadata: (id: string, titleEn: string, titleAm: string, categoryId: string, categoryLabelEn: string, categoryLabelAm: string) => void;
  createArticle: (payload: Omit<ArticleRecord, 'id' | 'publishedDate' | 'isHidden' | 'isCustom'>) => void;
  toggleAppFlag: (id: string, flag: 'isHidden' | 'isFeatured' | 'isTrending') => void;
  addApp: (payload: Omit<DirectoryAppRecord, 'id' | 'isHidden' | 'isFeatured' | 'isTrending' | 'isCustom'>) => void;
  deleteApp: (id: string) => void;
  toggleAiToolFlag: (id: string, flag: 'isHidden' | 'isFeatured') => void;
  addAiTool: (payload: Omit<DirectoryAiToolRecord, 'id' | 'isHidden' | 'isFeatured' | 'isCustom'>) => void;
  deleteAiTool: (id: string) => void;
  updateMonetization: (patch: Partial<MonetizationConfig>) => void;
  toggleAdActive: (id: string) => void;
  createAdCampaign: (payload: Omit<AdCampaignRecord, 'id' | 'isActive' | 'isCustom'>) => void;
  deleteAdCampaign: (id: string) => void;
  hideReportedTarget: (report: ModerationReportRecord) => void;
  dismissReport: (id: string) => void;
  updateSubmissionStatus: (id: string, status: SubmissionStatus) => void;
  deleteSubmission: (id: string) => void;
  resetAllDefaults: () => void;
}

const DEFAULT_STATE: PersistedAdminState = {
  language: 'en',
  themeMode: 'dark',
  articles: INITIAL_ARTICLES,
  apps: INITIAL_APPS,
  aiTools: INITIAL_AI_TOOLS,
  adCampaigns: INITIAL_AD_CAMPAIGNS,
  monetization: INITIAL_MONETIZATION,
  homeLayout: INITIAL_HOME_LAYOUT,
  reports: INITIAL_REPORTS,
  submissions: INITIAL_SUBMISSIONS,
};

const AdminContext = createContext<AdminContextValue | null>(null);

export function AdminProvider({ children }: { children: React.ReactNode }) {
  const [state, setState] = useState<PersistedAdminState>(DEFAULT_STATE);
  const [isHydrated, setIsHydrated] = useState(false);

  useEffect(() => {
    let active = true;
    AsyncStorage.getItem(STORAGE_KEY)
      .then((raw) => {
        if (active && raw) {
          const parsed = JSON.parse(raw) as Partial<PersistedAdminState>;
          setState((prev) => ({ ...prev, ...parsed }));
        }
      })
      .catch(() => undefined)
      .finally(() => {
        if (active) setIsHydrated(true);
      });
    return () => {
      active = false;
    };
  }, []);

  const persistUpdate = useCallback((updater: (prev: PersistedAdminState) => PersistedAdminState) => {
    setState((prev) => {
      const next = updater(prev);
      AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(next)).catch(() => undefined);
      return next;
    });
  }, []);

  const setLanguage = useCallback(
    (language: AdminLanguage) => persistUpdate((s) => ({ ...s, language })),
    [persistUpdate]
  );

  const toggleThemeMode = useCallback(
    () => persistUpdate((s) => ({ ...s, themeMode: s.themeMode === 'dark' ? 'light' : 'dark' })),
    [persistUpdate]
  );

  const updateHomeLayout = useCallback(
    (patch: Partial<HomeLayoutConfig>) =>
      persistUpdate((s) => ({ ...s, homeLayout: { ...s.homeLayout, ...patch } })),
    [persistUpdate]
  );

  const toggleArticleFlag = useCallback(
    (id: string, flag: 'isHidden' | 'isBreaking' | 'isFeatured' | 'isTrending') =>
      persistUpdate((s) => ({
        ...s,
        articles: s.articles.map((a) => (a.id === id ? { ...a, [flag]: !a[flag] } : a)),
      })),
    [persistUpdate]
  );

  const updateArticleMetadata = useCallback(
    (id: string, titleEn: string, titleAm: string, categoryId: string, categoryLabelEn: string, categoryLabelAm: string) =>
      persistUpdate((s) => ({
        ...s,
        articles: s.articles.map((a) =>
          a.id === id ? { ...a, titleEn, titleAm: titleAm || titleEn, categoryId, categoryLabelEn, categoryLabelAm } : a
        ),
      })),
    [persistUpdate]
  );

  const createArticle = useCallback(
    (payload: Omit<ArticleRecord, 'id' | 'publishedDate' | 'isHidden' | 'isCustom'>) =>
      persistUpdate((s) => ({
        ...s,
        articles: [
          {
            ...payload,
            id: `art_custom_${Date.now()}`,
            publishedDate: 'Just now',
            isHidden: false,
            isCustom: true,
          },
          ...s.articles,
        ],
      })),
    [persistUpdate]
  );

  const toggleAppFlag = useCallback(
    (id: string, flag: 'isHidden' | 'isFeatured' | 'isTrending') =>
      persistUpdate((s) => ({
        ...s,
        apps: s.apps.map((app) => (app.id === id ? { ...app, [flag]: !app[flag] } : app)),
      })),
    [persistUpdate]
  );

  const addApp = useCallback(
    (payload: Omit<DirectoryAppRecord, 'id' | 'isHidden' | 'isFeatured' | 'isTrending' | 'isCustom'>) =>
      persistUpdate((s) => ({
        ...s,
        apps: [
          {
            ...payload,
            id: `app_custom_${Date.now()}`,
            isHidden: false,
            isFeatured: true,
            isTrending: true,
            isCustom: true,
          },
          ...s.apps,
        ],
      })),
    [persistUpdate]
  );

  const deleteApp = useCallback(
    (id: string) => persistUpdate((s) => ({ ...s, apps: s.apps.filter((a) => a.id !== id) })),
    [persistUpdate]
  );

  const toggleAiToolFlag = useCallback(
    (id: string, flag: 'isHidden' | 'isFeatured') =>
      persistUpdate((s) => ({
        ...s,
        aiTools: s.aiTools.map((t) => (t.id === id ? { ...t, [flag]: !t[flag] } : t)),
      })),
    [persistUpdate]
  );

  const addAiTool = useCallback(
    (payload: Omit<DirectoryAiToolRecord, 'id' | 'isHidden' | 'isFeatured' | 'isCustom'>) =>
      persistUpdate((s) => ({
        ...s,
        aiTools: [
          {
            ...payload,
            id: `tool_custom_${Date.now()}`,
            isHidden: false,
            isFeatured: true,
            isCustom: true,
          },
          ...s.aiTools,
        ],
      })),
    [persistUpdate]
  );

  const deleteAiTool = useCallback(
    (id: string) => persistUpdate((s) => ({ ...s, aiTools: s.aiTools.filter((t) => t.id !== id) })),
    [persistUpdate]
  );

  const updateMonetization = useCallback(
    (patch: Partial<MonetizationConfig>) =>
      persistUpdate((s) => ({ ...s, monetization: { ...s.monetization, ...patch } })),
    [persistUpdate]
  );

  const toggleAdActive = useCallback(
    (id: string) =>
      persistUpdate((s) => ({
        ...s,
        adCampaigns: s.adCampaigns.map((c) => (c.id === id ? { ...c, isActive: !c.isActive } : c)),
      })),
    [persistUpdate]
  );

  const createAdCampaign = useCallback(
    (payload: Omit<AdCampaignRecord, 'id' | 'isActive' | 'isCustom'>) =>
      persistUpdate((s) => ({
        ...s,
        adCampaigns: [
          {
            ...payload,
            id: `ad_custom_${Date.now()}`,
            isActive: true,
            isCustom: true,
          },
          ...s.adCampaigns,
        ],
      })),
    [persistUpdate]
  );

  const deleteAdCampaign = useCallback(
    (id: string) => persistUpdate((s) => ({ ...s, adCampaigns: s.adCampaigns.filter((c) => c.id !== id) })),
    [persistUpdate]
  );

  const hideReportedTarget = useCallback(
    (report: ModerationReportRecord) =>
      persistUpdate((s) => ({
        ...s,
        articles:
          report.targetType === 'article'
            ? s.articles.map((a) => (a.id === report.targetId ? { ...a, isHidden: true } : a))
            : s.articles,
        apps:
          report.targetType === 'app'
            ? s.apps.map((app) => (app.id === report.targetId ? { ...app, isHidden: true } : app))
            : s.apps,
        aiTools:
          report.targetType === 'ai_tool'
            ? s.aiTools.map((t) => (t.id === report.targetId ? { ...t, isHidden: true } : t))
            : s.aiTools,
        reports: s.reports.filter((r) => r.id !== report.id),
      })),
    [persistUpdate]
  );

  const dismissReport = useCallback(
    (id: string) => persistUpdate((s) => ({ ...s, reports: s.reports.filter((r) => r.id !== id) })),
    [persistUpdate]
  );

  const updateSubmissionStatus = useCallback(
    (id: string, status: SubmissionStatus) =>
      persistUpdate((s) => ({
        ...s,
        submissions: s.submissions.map((sub) => (sub.id === id ? { ...sub, status } : sub)),
      })),
    [persistUpdate]
  );

  const deleteSubmission = useCallback(
    (id: string) => persistUpdate((s) => ({ ...s, submissions: s.submissions.filter((sub) => sub.id !== id) })),
    [persistUpdate]
  );

  const resetAllDefaults = useCallback(() => persistUpdate(() => DEFAULT_STATE), [persistUpdate]);

  return (
    <AdminContext.Provider
      value={{
        ...state,
        isHydrated,
        setLanguage,
        toggleThemeMode,
        updateHomeLayout,
        toggleArticleFlag,
        updateArticleMetadata,
        createArticle,
        toggleAppFlag,
        addApp,
        deleteApp,
        toggleAiToolFlag,
        addAiTool,
        deleteAiTool,
        updateMonetization,
        toggleAdActive,
        createAdCampaign,
        deleteAdCampaign,
        hideReportedTarget,
        dismissReport,
        updateSubmissionStatus,
        deleteSubmission,
        resetAllDefaults,
      }}
    >
      {children}
    </AdminContext.Provider>
  );
}

export function useAdminStore(): AdminContextValue {
  const ctx = useContext(AdminContext);
  if (!ctx) throw new Error('useAdminStore must be used inside AdminProvider');
  return ctx;
}
