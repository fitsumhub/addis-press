import React, { useEffect, useState } from 'react';
import {
  Alert,
  ScrollView,
  StyleSheet,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from 'react-native';
import { Bolt, RotateCcw } from 'lucide-react-native';
import { FilterPill, KpiCard, ToggleRow } from '../components/AdminUiPrimitives';
import { useAdminStore } from '../store/AdminContext';
import { ThemePalette } from '../theme/tokens';
import { AdminTabKey } from '../types/admin';

export function OverviewScreen({
  palette,
  onSwitchTab,
}: {
  palette: ThemePalette;
  onSwitchTab: (tab: AdminTabKey) => void;
}) {
  const {
    language,
    articles,
    apps,
    aiTools,
    adCampaigns,
    monetization,
    homeLayout,
    reports,
    submissions,
    updateHomeLayout,
    resetAllDefaults,
  } = useAdminStore();

  const [tickerEn, setTickerEn] = useState(homeLayout.tickerMessageEn);
  const [tickerAm, setTickerAm] = useState(homeLayout.tickerMessageAm);

  useEffect(() => {
    setTickerEn(homeLayout.tickerMessageEn);
    setTickerAm(homeLayout.tickerMessageAm);
  }, [homeLayout.tickerMessageEn, homeLayout.tickerMessageAm]);

  const liveArticles = articles.filter((a) => !a.isHidden).length;
  const hiddenArticles = articles.length - liveArticles;
  const liveApps = apps.filter((a) => !a.isHidden).length;
  const liveTools = aiTools.filter((t) => !t.isHidden).length;
  const activeAds = adCampaigns.filter((c) => c.isActive).length;
  const ctr =
    monetization.impressions > 0
      ? ((monetization.clicks / monetization.impressions) * 100).toFixed(1)
      : '0.0';
  const modQueue = reports.length + submissions.filter((s) => s.status === 'review').length;

  const isAm = language === 'am';

  return (
    <ScrollView
      style={styles.container}
      contentContainerStyle={styles.content}
      showsVerticalScrollIndicator={false}
    >
      <Text style={[styles.sectionTitle, { color: palette.textPrimary }]}>
        {isAm ? 'የመድረኩ ቀጥታ አኃዞች' : 'Platform Live Metrics'}
      </Text>

      <View style={styles.kpiRow}>
        <KpiCard
          title={isAm ? 'ንቁ ጽሑፎች' : 'Live Articles'}
          value={String(liveArticles)}
          subtitle={isAm ? `${hiddenArticles} ተደብቀዋል` : `${hiddenArticles} hidden`}
          palette={palette}
          onPress={() => onSwitchTab('articles')}
        />
        <KpiCard
          title={isAm ? 'መተግበሪያ እና AI' : 'Apps & AI'}
          value={String(liveApps + liveTools)}
          subtitle={`${liveApps} apps · ${liveTools} AI`}
          palette={palette}
          onPress={() => onSwitchTab('directory')}
        />
      </View>

      <View style={styles.kpiRow}>
        <KpiCard
          title={isAm ? 'የማስታወቂያ አፈጻጸም' : 'Ad Performance'}
          value={`${monetization.impressions} imp`}
          subtitle={`${activeAds} active · ${ctr}% CTR`}
          palette={palette}
          onPress={() => onSwitchTab('ads')}
        />
        <KpiCard
          title={isAm ? 'የቁጥጥር ወረፋ' : 'Moderation Queue'}
          value={String(modQueue)}
          subtitle={`${reports.length} reports · ${submissions.length} stories`}
          palette={palette}
          onPress={() => onSwitchTab('moderation')}
        />
      </View>

      <View style={[styles.card, { backgroundColor: palette.surface, borderColor: palette.border }]}>
        <Text style={[styles.cardTitle, { color: palette.textPrimary }]}>
          {isAm ? 'የመነሻ ገጽ ክፍሎች መቆጣጠሪያ' : 'Home Feed Layout Controls'}
        </Text>
        <ToggleRow
          label={isAm ? 'ሰበር ዜና ባነር' : 'Breaking Alert Banner'}
          value={homeLayout.showBreakingBanner}
          palette={palette}
          onValueChange={(showBreakingBanner) => updateHomeLayout({ showBreakingBanner })}
        />
        <ToggleRow
          label={isAm ? 'የተመረጡ ጽሑፎች ካሩሴል' : 'Featured Editorial Carousel'}
          value={homeLayout.showFeaturedCarousel}
          palette={palette}
          onValueChange={(showFeaturedCarousel) => updateHomeLayout({ showFeaturedCarousel })}
        />
        <ToggleRow
          label={isAm ? 'የመተግበሪያ ግምገማዎች መደርደሪያ' : 'App Reviews Highlight Rail'}
          value={homeLayout.showAppReviewsRail}
          palette={palette}
          onValueChange={(showAppReviewsRail) => updateHomeLayout({ showAppReviewsRail })}
        />
        <ToggleRow
          label={isAm ? 'ተወዳጅ ጽሑፎች ክፍል' : 'Trending Stories Section'}
          value={homeLayout.showTrendingSection}
          palette={palette}
          onValueChange={(showTrendingSection) => updateHomeLayout({ showTrendingSection })}
        />

        <Text style={[styles.fieldLabel, { color: palette.textSecondary }]}>
          {isAm ? 'የመነሻ ገጽ ጽሑፎች ብዛት' : 'Default Home Feed Page Length'}
        </Text>
        <View style={styles.chipGroup}>
          {([12, 20, 28, 50] as const).map((size) => (
            <FilterPill
              key={size}
              label={size === 50 ? (isAm ? 'ሁሉም' : 'All') : String(size)}
              selected={homeLayout.homeFeedPageSize === size}
              palette={palette}
              onPress={() => updateHomeLayout({ homeFeedPageSize: size })}
            />
          ))}
        </View>
      </View>

      <View style={[styles.card, { backgroundColor: palette.surface, borderColor: palette.amber }]}>
        <View style={styles.iconHeading}>
          <Bolt size={18} color={palette.amber} />
          <Text style={[styles.cardTitle, { color: palette.textPrimary, marginLeft: 6 }]}>
            {isAm ? 'የሰበር ዜና ማስታወቂያ መቀየሪያ' : 'Breaking Alert Ticker Override'}
          </Text>
        </View>
        <TextInput
          value={tickerEn}
          onChangeText={setTickerEn}
          placeholder="Custom ticker message (English)"
          placeholderTextColor={palette.textSecondary}
          style={[styles.input, { color: palette.textPrimary, borderColor: palette.border, backgroundColor: palette.surfaceElevated }]}
        />
        <TextInput
          value={tickerAm}
          onChangeText={setTickerAm}
          placeholder="ብጁ የሰበር ዜና ጽሑፍ (አማርኛ)"
          placeholderTextColor={palette.textSecondary}
          style={[styles.input, { color: palette.textPrimary, borderColor: palette.border, backgroundColor: palette.surfaceElevated }]}
        />
        <View style={styles.buttonRow}>
          <TouchableOpacity
            style={[styles.primaryBtn, { backgroundColor: palette.primary }]}
            onPress={() => updateHomeLayout({ tickerMessageEn: tickerEn.trim(), tickerMessageAm: tickerAm.trim() })}
          >
            <Text style={styles.primaryBtnText}>{isAm ? 'አስቀምጥ' : 'Apply Ticker'}</Text>
          </TouchableOpacity>
          <TouchableOpacity
            style={[styles.outlineBtn, { borderColor: palette.border }]}
            onPress={() => {
              setTickerEn('');
              setTickerAm('');
              updateHomeLayout({ tickerMessageEn: '', tickerMessageAm: '' });
            }}
          >
            <Text style={[styles.outlineBtnText, { color: palette.textPrimary }]}>{isAm ? 'አጽዳ' : 'Clear'}</Text>
          </TouchableOpacity>
        </View>
      </View>

      <TouchableOpacity
        style={[styles.resetBtn, { borderColor: palette.danger }]}
        onPress={() =>
          Alert.alert(
            isAm ? 'ሁሉንም ለውጦች ይመልሱ?' : 'Reset All CMS Overrides?',
            isAm ? 'ነባሪ ውሂቦች ይመለሳሉ።' : 'This restores default articles, apps, ads, and layout config.',
            [
              { text: isAm ? 'ሰርዝ' : 'Cancel', style: 'cancel' },
              { text: isAm ? 'መልስ' : 'Reset', style: 'destructive', onPress: resetAllDefaults },
            ]
          )
        }
      >
        <RotateCcw size={17} color={palette.danger} />
        <Text style={[styles.resetBtnText, { color: palette.danger }]}>
          {isAm ? 'ሁሉንም የአስተዳደር ለውጦች መልስ' : 'Reset All Admin CMS Overrides'}
        </Text>
      </TouchableOpacity>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1 },
  content: { padding: 16, paddingBottom: 40, gap: 12 },
  sectionTitle: { fontSize: 17, fontWeight: '800' },
  kpiRow: { flexDirection: 'row', gap: 10 },
  card: { borderRadius: 16, borderWidth: 1, padding: 16, gap: 6 },
  cardTitle: { fontSize: 15, fontWeight: '800', marginBottom: 4 },
  fieldLabel: { fontSize: 12, fontWeight: '700', marginTop: 8 },
  chipGroup: { flexDirection: 'row', flexWrap: 'wrap', gap: 8, marginTop: 6 },
  iconHeading: { flexDirection: 'row', alignItems: 'center', marginBottom: 6 },
  input: {
    borderWidth: 1,
    borderRadius: 12,
    paddingHorizontal: 12,
    paddingVertical: 10,
    fontSize: 14,
    marginTop: 4,
  },
  buttonRow: { flexDirection: 'row', gap: 10, marginTop: 10 },
  primaryBtn: {
    flex: 1,
    borderRadius: 12,
    paddingVertical: 12,
    alignItems: 'center',
    justifyContent: 'center',
  },
  primaryBtnText: { color: '#FFFFFF', fontWeight: '800', fontSize: 14 },
  outlineBtn: {
    borderRadius: 12,
    borderWidth: 1,
    paddingHorizontal: 18,
    paddingVertical: 12,
    alignItems: 'center',
    justifyContent: 'center',
  },
  outlineBtnText: { fontWeight: '700', fontSize: 14 },
  resetBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    gap: 8,
    borderRadius: 12,
    borderWidth: 1,
    paddingVertical: 13,
    marginTop: 4,
  },
  resetBtnText: { fontWeight: '700', fontSize: 14 },
});
