import React, { useState } from 'react';
import {
  FlatList,
  Modal,
  StyleSheet,
  Switch,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from 'react-native';
import { Megaphone, Plus, Trash2 } from 'lucide-react-native';
import { StatusPill, ToggleRow } from '../components/AdminUiPrimitives';
import { useAdminStore } from '../store/AdminContext';
import { ThemePalette } from '../theme/tokens';
import { AdPlacementType } from '../types/admin';

export function AdsRevenueScreen({ palette }: { palette: ThemePalette }) {
  const {
    language,
    monetization,
    adCampaigns,
    updateMonetization,
    toggleAdActive,
    createAdCampaign,
    deleteAdCampaign,
  } = useAdminStore();

  const isAm = language === 'am';
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [showAdMobModal, setShowAdMobModal] = useState(false);

  const [placementType, setPlacementType] = useState<AdPlacementType>('native');
  const [sponsorName, setSponsorName] = useState('');
  const [headlineEn, setHeadlineEn] = useState('');
  const [headlineAm, setHeadlineAm] = useState('');
  const [ctaEn, setCtaEn] = useState('Learn More');
  const [destinationUrl, setDestinationUrl] = useState('https://');

  const [bannerUnit, setBannerUnit] = useState(monetization.admobBannerUnitId);
  const [nativeUnit, setNativeUnit] = useState(monetization.admobNativeUnitId);

  const ctr =
    monetization.impressions > 0
      ? ((monetization.clicks / monetization.impressions) * 100).toFixed(1)
      : '0.0';

  const handleLaunchCampaign = () => {
    if (!sponsorName.trim() || !headlineEn.trim()) return;
    createAdCampaign({
      placementType,
      sponsorName: sponsorName.trim(),
      headlineEn: headlineEn.trim(),
      headlineAm: headlineAm.trim() || headlineEn.trim(),
      bodyEn: '',
      ctaEn: ctaEn.trim() || 'Open',
      badgeText: placementType === 'native' ? 'SPONSORED PARTNER' : 'STICKY BANNER',
      destinationUrl: destinationUrl.trim() || 'https://addispress.et',
      accentColor: placementType === 'native' ? '#10B981' : '#F59E0B',
    });
    setSponsorName('');
    setHeadlineEn('');
    setHeadlineAm('');
    setDestinationUrl('https://');
    setShowCreateModal(false);
  };

  return (
    <View style={styles.container}>
      <FlatList
        data={adCampaigns}
        keyExtractor={(item) => item.id}
        contentContainerStyle={styles.listContent}
        ListHeaderComponent={
          <View style={styles.headerStack}>
            <View style={[styles.card, { backgroundColor: palette.surface, borderColor: palette.amber }]}>
              <View style={styles.iconHeading}>
                <Megaphone size={18} color={palette.amber} />
                <Text style={[styles.cardTitle, { color: palette.textPrimary }]}>
                  {isAm ? 'የማስታወቂያ ገቢ እና ስርጭት መቆጣጠሪያ' : 'Ad Revenue & Delivery Controls'}
                </Text>
              </View>
              <View style={styles.metricsRow}>
                <View style={[styles.metricTile, { backgroundColor: palette.surfaceElevated }]}>
                  <Text style={[styles.metricValue, { color: palette.textPrimary }]}>
                    {adCampaigns.filter((c) => c.isActive).length}
                  </Text>
                  <Text style={[styles.metricLabel, { color: palette.textSecondary }]}>Active Units</Text>
                </View>
                <View style={[styles.metricTile, { backgroundColor: palette.surfaceElevated }]}>
                  <Text style={[styles.metricValue, { color: palette.textPrimary }]}>
                    {monetization.impressions}
                  </Text>
                  <Text style={[styles.metricLabel, { color: palette.textSecondary }]}>Impressions</Text>
                </View>
                <View style={[styles.metricTile, { backgroundColor: palette.surfaceElevated }]}>
                  <Text style={[styles.metricValue, { color: palette.emerald }]}>{ctr}%</Text>
                  <Text style={[styles.metricLabel, { color: palette.textSecondary }]}>CTR</Text>
                </View>
              </View>

              <ToggleRow
                label={isAm ? 'ሁሉንም ማስታወቂያዎች አብራ' : 'Global In-App Ads'}
                value={monetization.adsEnabled}
                palette={palette}
                onValueChange={(adsEnabled) => updateMonetization({ adsEnabled })}
              />
              <ToggleRow
                label={isAm ? 'ከፍተኛ ማስታወቂያ (በየ 3 ረድፍ)' : 'High-Yield Frequency (Every 3 rows)'}
                value={monetization.highYieldAds}
                palette={palette}
                onValueChange={(highYieldAds) => updateMonetization({ highYieldAds })}
              />
              <ToggleRow
                label={isAm ? 'የታችኛው ባነር ማስታወቂያ' : 'Sticky Bottom Banner Bars'}
                value={monetization.bannerAdsEnabled}
                palette={palette}
                onValueChange={(bannerAdsEnabled) => updateMonetization({ bannerAdsEnabled })}
              />

              <View style={styles.actionRow}>
                <TouchableOpacity
                  style={[styles.outlineBtn, { borderColor: palette.border }]}
                  onPress={() => {
                    setBannerUnit(monetization.admobBannerUnitId);
                    setNativeUnit(monetization.admobNativeUnitId);
                    setShowAdMobModal(true);
                  }}
                >
                  <Text style={{ color: palette.primary, fontWeight: '700', fontSize: 13 }}>
                    {monetization.admobBannerUnitId ? 'AdMob Ready' : 'AdMob IDs'}
                  </Text>
                </TouchableOpacity>
                <TouchableOpacity
                  style={[styles.primaryBtn, { backgroundColor: palette.primary }]}
                  onPress={() => setShowCreateModal(true)}
                >
                  <Plus size={16} color="#FFFFFF" />
                  <Text style={styles.primaryBtnText}>{isAm ? 'አዲስ ዘመቻ' : 'New Campaign'}</Text>
                </TouchableOpacity>
              </View>
            </View>

            <Text style={[styles.inventoryHeader, { color: palette.textPrimary }]}>
              {isAm ? 'የማስታወቂያ ክምችት' : 'Active & Paused Ad Inventory'}
            </Text>
          </View>
        }
        renderItem={({ item }) => (
          <View
            style={[
              styles.card,
              {
                backgroundColor: palette.surface,
                borderColor: item.isActive ? item.accentColor : palette.border,
              },
            ]}
          >
            <View style={styles.campaignRow}>
              <View style={styles.campaignInfo}>
                <View style={styles.pillsRow}>
                  <StatusPill
                    label={item.placementType.toUpperCase()}
                    color={item.placementType === 'native' ? palette.primary : palette.amber}
                    backgroundColor={item.placementType === 'native' ? palette.primarySoft : palette.amberSoft}
                  />
                  {item.isCustom && (
                    <StatusPill label="CUSTOM" color={palette.emerald} backgroundColor={palette.emeraldSoft} />
                  )}
                </View>
                <Text style={[styles.sponsorTitle, { color: palette.textPrimary }]}>{item.sponsorName}</Text>
                <Text style={[styles.headlineText, { color: palette.textSecondary }]} numberOfLines={2}>
                  {isAm ? item.headlineAm : item.headlineEn}
                </Text>
              </View>
              {item.isCustom && (
                <TouchableOpacity onPress={() => deleteAdCampaign(item.id)} style={styles.deleteBtn}>
                  <Trash2 size={18} color={palette.danger} />
                </TouchableOpacity>
              )}
              <Switch
                value={item.isActive}
                onValueChange={() => toggleAdActive(item.id)}
                trackColor={{ false: palette.border, true: palette.emeraldSoft }}
                thumbColor={item.isActive ? palette.emerald : palette.textSecondary}
              />
            </View>
          </View>
        )}
      />

      <Modal visible={showCreateModal} transparent animationType="slide">
        <View style={styles.modalBackdrop}>
          <View style={[styles.modalSheet, { backgroundColor: palette.surface, borderColor: palette.border }]}>
            <Text style={[styles.modalTitle, { color: palette.textPrimary }]}>Launch Ad Campaign</Text>
            <View style={[styles.segmented, { borderColor: palette.border }]}>
              {(['native', 'banner'] as AdPlacementType[]).map((type) => (
                <TouchableOpacity
                  key={type}
                  style={[styles.segBtn, placementType === type && { backgroundColor: palette.primary }]}
                  onPress={() => setPlacementType(type)}
                >
                  <Text style={{ color: placementType === type ? '#FFFFFF' : palette.textSecondary, fontWeight: '800' }}>
                    {type === 'native' ? 'Native Feed Card' : 'Sticky Banner'}
                  </Text>
                </TouchableOpacity>
              ))}
            </View>
            <TextInput
              value={sponsorName}
              onChangeText={setSponsorName}
              placeholder="Sponsor Name *"
              placeholderTextColor={palette.textSecondary}
              style={[styles.input, { color: palette.textPrimary, borderColor: palette.border, backgroundColor: palette.surfaceElevated }]}
            />
            <TextInput
              value={headlineEn}
              onChangeText={setHeadlineEn}
              placeholder="Offer / Headline (English) *"
              placeholderTextColor={palette.textSecondary}
              style={[styles.input, { color: palette.textPrimary, borderColor: palette.border, backgroundColor: palette.surfaceElevated }]}
            />
            <TextInput
              value={headlineAm}
              onChangeText={setHeadlineAm}
              placeholder="ዋና ጽሑፍ (አማርኛ)"
              placeholderTextColor={palette.textSecondary}
              style={[styles.input, { color: palette.textPrimary, borderColor: palette.border, backgroundColor: palette.surfaceElevated }]}
            />
            <TextInput
              value={ctaEn}
              onChangeText={setCtaEn}
              placeholder="CTA Button Label"
              placeholderTextColor={palette.textSecondary}
              style={[styles.input, { color: palette.textPrimary, borderColor: palette.border, backgroundColor: palette.surfaceElevated }]}
            />
            <TextInput
              value={destinationUrl}
              onChangeText={setDestinationUrl}
              placeholder="Destination URL"
              placeholderTextColor={palette.textSecondary}
              style={[styles.input, { color: palette.textPrimary, borderColor: palette.border, backgroundColor: palette.surfaceElevated }]}
            />
            <View style={styles.actionRow}>
              <TouchableOpacity
                style={[styles.outlineBtn, { borderColor: palette.border, flex: 1 }]}
                onPress={() => setShowCreateModal(false)}
              >
                <Text style={{ color: palette.textPrimary, fontWeight: '700' }}>Cancel</Text>
              </TouchableOpacity>
              <TouchableOpacity
                style={[styles.primaryBtn, { backgroundColor: palette.primary }]}
                onPress={handleLaunchCampaign}
              >
                <Text style={styles.primaryBtnText}>Launch Live</Text>
              </TouchableOpacity>
            </View>
          </View>
        </View>
      </Modal>

      <Modal visible={showAdMobModal} transparent animationType="fade">
        <View style={styles.modalBackdrop}>
          <View style={[styles.modalSheet, { backgroundColor: palette.surface, borderColor: palette.border }]}>
            <Text style={[styles.modalTitle, { color: palette.textPrimary }]}>AdMob Unit IDs</Text>
            <TextInput
              value={bannerUnit}
              onChangeText={setBannerUnit}
              placeholder="ca-app-pub-xxxxxxxxxxxxxxxx/banner"
              placeholderTextColor={palette.textSecondary}
              style={[styles.input, { color: palette.textPrimary, borderColor: palette.border, backgroundColor: palette.surfaceElevated }]}
            />
            <TextInput
              value={nativeUnit}
              onChangeText={setNativeUnit}
              placeholder="ca-app-pub-xxxxxxxxxxxxxxxx/native"
              placeholderTextColor={palette.textSecondary}
              style={[styles.input, { color: palette.textPrimary, borderColor: palette.border, backgroundColor: palette.surfaceElevated }]}
            />
            <View style={styles.actionRow}>
              <TouchableOpacity
                style={[styles.outlineBtn, { borderColor: palette.border, flex: 1 }]}
                onPress={() => setShowAdMobModal(false)}
              >
                <Text style={{ color: palette.textPrimary, fontWeight: '700' }}>Cancel</Text>
              </TouchableOpacity>
              <TouchableOpacity
                style={[styles.primaryBtn, { backgroundColor: palette.primary }]}
                onPress={() => {
                  updateMonetization({
                    admobBannerUnitId: bannerUnit.trim(),
                    admobNativeUnitId: nativeUnit.trim(),
                  });
                  setShowAdMobModal(false);
                }}
              >
                <Text style={styles.primaryBtnText}>Save IDs</Text>
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
  listContent: { padding: 16, gap: 12 },
  headerStack: { gap: 12 },
  card: { borderRadius: 16, borderWidth: 1, padding: 14, gap: 6 },
  iconHeading: { flexDirection: 'row', alignItems: 'center', gap: 8, marginBottom: 4 },
  cardTitle: { fontSize: 15, fontWeight: '800' },
  metricsRow: { flexDirection: 'row', gap: 8, marginVertical: 4 },
  metricTile: { flex: 1, borderRadius: 12, padding: 10, alignItems: 'center' },
  metricValue: { fontSize: 17, fontWeight: '800' },
  metricLabel: { fontSize: 11, fontWeight: '600', marginTop: 2 },
  actionRow: { flexDirection: 'row', gap: 10, marginTop: 8 },
  outlineBtn: {
    borderRadius: 12,
    borderWidth: 1,
    paddingHorizontal: 14,
    paddingVertical: 11,
    alignItems: 'center',
    justifyContent: 'center',
  },
  primaryBtn: {
    flex: 1,
    flexDirection: 'row',
    gap: 6,
    borderRadius: 12,
    paddingVertical: 11,
    alignItems: 'center',
    justifyContent: 'center',
  },
  primaryBtnText: { color: '#FFFFFF', fontWeight: '800', fontSize: 13 },
  inventoryHeader: { fontSize: 16, fontWeight: '800', marginTop: 4 },
  campaignRow: { flexDirection: 'row', alignItems: 'center', gap: 8 },
  campaignInfo: { flex: 1, gap: 4 },
  pillsRow: { flexDirection: 'row', gap: 6 },
  sponsorTitle: { fontSize: 15, fontWeight: '800' },
  headlineText: { fontSize: 12 },
  deleteBtn: { padding: 6 },
  modalBackdrop: { flex: 1, backgroundColor: 'rgba(0,0,0,0.65)', justifyContent: 'center', padding: 20 },
  modalSheet: { borderRadius: 18, borderWidth: 1, padding: 18, gap: 10 },
  modalTitle: { fontSize: 17, fontWeight: '800' },
  segmented: { flexDirection: 'row', borderRadius: 12, borderWidth: 1, padding: 4, gap: 4 },
  segBtn: { flex: 1, borderRadius: 9, paddingVertical: 9, alignItems: 'center' },
  input: { borderWidth: 1, borderRadius: 12, paddingHorizontal: 12, paddingVertical: 10, fontSize: 14 },
});
