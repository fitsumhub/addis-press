import { StatusBar } from 'expo-status-bar';
import React, { useState } from 'react';
import { ScrollView, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import { SafeAreaProvider, SafeAreaView } from 'react-native-safe-area-context';
import {
  FileText,
  LayoutDashboard,
  Megaphone,
  Moon,
  ShieldAlert,
  ShieldCheck,
  Smartphone,
  Sun,
} from 'lucide-react-native';
import { StatusPill } from './src/components/AdminUiPrimitives';
import { AdsRevenueScreen } from './src/screens/AdsRevenueScreen';
import { ArticlesCmsScreen } from './src/screens/ArticlesCmsScreen';
import { DirectoryCmsScreen } from './src/screens/DirectoryCmsScreen';
import { ModerationScreen } from './src/screens/ModerationScreen';
import { OverviewScreen } from './src/screens/OverviewScreen';
import { AdminProvider, useAdminStore } from './src/store/AdminContext';
import { getPalette } from './src/theme/tokens';
import { AdminTabKey } from './src/types/admin';

function AdminDashboardShell() {
  const { language, themeMode, reports, submissions, setLanguage, toggleThemeMode } = useAdminStore();
  const [activeTab, setActiveTab] = useState<AdminTabKey>('overview');
  const palette = getPalette(themeMode);
  const isAm = language === 'am';

  const modBadge = reports.length + submissions.filter((s) => s.status === 'review').length;

  const tabs: Array<{ key: AdminTabKey; label: string; icon: React.ReactNode }> = [
    {
      key: 'overview',
      label: isAm ? 'አጠቃላይ' : 'Overview',
      icon: <LayoutDashboard size={15} color={activeTab === 'overview' ? '#FFFFFF' : palette.textSecondary} />,
    },
    {
      key: 'articles',
      label: isAm ? 'ጽሑፎች' : 'Articles',
      icon: <FileText size={15} color={activeTab === 'articles' ? '#FFFFFF' : palette.textSecondary} />,
    },
    {
      key: 'directory',
      label: isAm ? 'መተግበሪያ እና AI' : 'Apps & AI',
      icon: <Smartphone size={15} color={activeTab === 'directory' ? '#FFFFFF' : palette.textSecondary} />,
    },
    {
      key: 'ads',
      label: isAm ? 'ማስታወቂያ' : 'Ads & Revenue',
      icon: <Megaphone size={15} color={activeTab === 'ads' ? '#FFFFFF' : palette.textSecondary} />,
    },
    {
      key: 'moderation',
      label: modBadge > 0 ? `${isAm ? 'ቁጥጥር' : 'Moderation'} (${modBadge})` : isAm ? 'ቁጥጥር' : 'Moderation',
      icon: <ShieldAlert size={15} color={activeTab === 'moderation' ? '#FFFFFF' : palette.textSecondary} />,
    },
  ];

  return (
    <SafeAreaView style={[styles.root, { backgroundColor: palette.background }]} edges={['top', 'bottom']}>
      <StatusBar style={themeMode === 'dark' ? 'light' : 'dark'} />

      <View style={[styles.topBar, { borderBottomColor: palette.border }]}>
        <View style={styles.brandRow}>
          <View style={[styles.brandIcon, { backgroundColor: palette.primarySoft }]}>
            <ShieldCheck size={20} color={palette.primary} />
          </View>
          <View style={{ flex: 1 }}>
            <View style={styles.titleLine}>
              <Text style={[styles.appTitle, { color: palette.textPrimary }]}>
                {isAm ? 'AddisPress አስተዳደር' : 'AddisPress Admin'}
              </Text>
              <StatusPill label="LIVE" color={palette.emerald} backgroundColor={palette.emeraldSoft} />
            </View>
            <Text style={[styles.appSubtitle, { color: palette.textSecondary }]}>
              {isAm ? 'የይዘት እና ገቢ መቆጣጠሪያ' : 'Control Center · React Native'}
            </Text>
          </View>

          <TouchableOpacity
            style={[styles.iconAction, { backgroundColor: palette.surface, borderColor: palette.border }]}
            onPress={() => setLanguage(isAm ? 'en' : 'am')}
          >
            <Text style={[styles.langBtnText, { color: palette.textPrimary }]}>
              {isAm ? 'EN' : 'አማ'}
            </Text>
          </TouchableOpacity>

          <TouchableOpacity
            style={[styles.iconAction, { backgroundColor: palette.surface, borderColor: palette.border }]}
            onPress={toggleThemeMode}
          >
            {themeMode === 'dark' ? (
              <Sun size={17} color={palette.amber} />
            ) : (
              <Moon size={17} color={palette.textPrimary} />
            )}
          </TouchableOpacity>
        </View>

        <ScrollView horizontal showsHorizontalScrollIndicator={false} contentContainerStyle={styles.tabRow}>
          {tabs.map((tab) => {
            const selected = activeTab === tab.key;
            return (
              <TouchableOpacity
                key={tab.key}
                onPress={() => setActiveTab(tab.key)}
                style={[
                  styles.tabChip,
                  {
                    backgroundColor: selected ? palette.primary : palette.surface,
                    borderColor: selected ? palette.primary : palette.border,
                  },
                ]}
              >
                {tab.icon}
                <Text style={[styles.tabLabel, { color: selected ? '#FFFFFF' : palette.textSecondary }]}>
                  {tab.label}
                </Text>
              </TouchableOpacity>
            );
          })}
        </ScrollView>
      </View>

      <View style={styles.body}>
        {activeTab === 'overview' && <OverviewScreen palette={palette} onSwitchTab={setActiveTab} />}
        {activeTab === 'articles' && <ArticlesCmsScreen palette={palette} />}
        {activeTab === 'directory' && <DirectoryCmsScreen palette={palette} />}
        {activeTab === 'ads' && <AdsRevenueScreen palette={palette} />}
        {activeTab === 'moderation' && <ModerationScreen palette={palette} />}
      </View>
    </SafeAreaView>
  );
}

export default function App() {
  return (
    <SafeAreaProvider>
      <AdminProvider>
        <AdminDashboardShell />
      </AdminProvider>
    </SafeAreaProvider>
  );
}

const styles = StyleSheet.create({
  root: { flex: 1 },
  topBar: { borderBottomWidth: 1, paddingBottom: 10 },
  brandRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 10,
    paddingHorizontal: 16,
    paddingTop: 10,
    paddingBottom: 12,
  },
  brandIcon: {
    width: 40,
    height: 40,
    borderRadius: 12,
    alignItems: 'center',
    justifyContent: 'center',
  },
  titleLine: { flexDirection: 'row', alignItems: 'center', gap: 8 },
  appTitle: { fontSize: 17, fontWeight: '800' },
  appSubtitle: { fontSize: 12 },
  iconAction: {
    width: 40,
    height: 40,
    borderRadius: 10,
    borderWidth: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  langBtnText: { fontSize: 12, fontWeight: '800' },
  tabRow: { paddingHorizontal: 16, gap: 8 },
  tabChip: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 6,
    borderRadius: 11,
    borderWidth: 1,
    paddingHorizontal: 13,
    paddingVertical: 9,
  },
  tabLabel: { fontSize: 13, fontWeight: '700' },
  body: { flex: 1 },
});
