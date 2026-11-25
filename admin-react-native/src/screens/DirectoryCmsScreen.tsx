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
import { Plus, Trash2 } from 'lucide-react-native';
import { FilterPill, StatusPill } from '../components/AdminUiPrimitives';
import { useAdminStore } from '../store/AdminContext';
import { ThemePalette } from '../theme/tokens';

export function DirectoryCmsScreen({ palette }: { palette: ThemePalette }) {
  const {
    language,
    apps,
    aiTools,
    toggleAppFlag,
    addApp,
    deleteApp,
    toggleAiToolFlag,
    addAiTool,
    deleteAiTool,
  } = useAdminStore();

  const isAm = language === 'am';
  const [segment, setSegment] = useState<'apps' | 'tools'>('apps');
  const [showAddModal, setShowAddModal] = useState(false);

  const [name, setName] = useState('');
  const [maker, setMaker] = useState('');
  const [taglineEn, setTaglineEn] = useState('');
  const [taglineAm, setTaglineAm] = useState('');
  const [url, setUrl] = useState('https://');
  const [isFreeTier, setIsFreeTier] = useState(true);

  const handleAdd = () => {
    if (!name.trim() || !taglineEn.trim()) return;
    if (segment === 'apps') {
      addApp({
        name: name.trim(),
        developer: maker.trim() || 'Independent Studio',
        categoryId: 'apps',
        categoryLabelEn: 'Utilities & Productivity',
        taglineEn: taglineEn.trim(),
        taglineAm: taglineAm.trim() || taglineEn.trim(),
        playStoreUrl: url.trim() || 'https://play.google.com/store',
        accentColor: '#10B981',
      });
    } else {
      addAiTool({
        name: name.trim(),
        provider: maker.trim() || 'AI Lab',
        categoryId: 'productivity',
        pricingTier: isFreeTier ? 'Free & Pro' : 'Pro',
        isFreeTier,
        taglineEn: taglineEn.trim(),
        taglineAm: taglineAm.trim() || taglineEn.trim(),
        officialUrl: url.trim() || 'https://addispress.et',
        accentColor: '#3B82F6',
      });
    }
    setName('');
    setMaker('');
    setTaglineEn('');
    setTaglineAm('');
    setUrl('https://');
    setShowAddModal(false);
  };

  return (
    <View style={styles.container}>
      <View style={styles.topControls}>
        <View style={[styles.segmented, { backgroundColor: palette.surface, borderColor: palette.border }]}>
          <TouchableOpacity
            style={[styles.segBtn, segment === 'apps' && { backgroundColor: palette.primary }]}
            onPress={() => setSegment('apps')}
          >
            <Text style={[styles.segText, { color: segment === 'apps' ? '#FFFFFF' : palette.textSecondary }]}>
              {isAm ? `መተግበሪያዎች (${apps.length})` : `Android Apps (${apps.length})`}
            </Text>
          </TouchableOpacity>
          <TouchableOpacity
            style={[styles.segBtn, segment === 'tools' && { backgroundColor: palette.primary }]}
            onPress={() => setSegment('tools')}
          >
            <Text style={[styles.segText, { color: segment === 'tools' ? '#FFFFFF' : palette.textSecondary }]}>
              {isAm ? `የAI መሳሪያዎች (${aiTools.length})` : `AI Tools (${aiTools.length})`}
            </Text>
          </TouchableOpacity>
        </View>

        <TouchableOpacity
          style={[styles.addBtn, { backgroundColor: palette.primary }]}
          onPress={() => setShowAddModal(true)}
        >
          <Plus size={18} color="#FFFFFF" />
          <Text style={styles.addBtnText}>
            {segment === 'apps'
              ? isAm ? 'አዲስ መተግበሪያ ጨምር' : 'Add Android App'
              : isAm ? 'አዲስ የAI መሳሪያ ጨምር' : 'Add AI Tool'}
          </Text>
        </TouchableOpacity>
      </View>

      {segment === 'apps' ? (
        <FlatList
          data={apps}
          keyExtractor={(item) => item.id}
          contentContainerStyle={styles.listContent}
          renderItem={({ item }) => (
            <View
              style={[
                styles.card,
                { backgroundColor: palette.surface, borderColor: item.isHidden ? palette.danger : palette.border },
              ]}
            >
              <View style={styles.rowBetween}>
                <View style={styles.titleBlock}>
                  <View style={styles.titleRow}>
                    <Text style={[styles.title, { color: palette.textPrimary }]}>{item.name}</Text>
                    {item.isCustom && (
                      <StatusPill label="CUSTOM" color={palette.primary} backgroundColor={palette.primarySoft} />
                    )}
                  </View>
                  <Text style={[styles.subtitle, { color: palette.textSecondary }]}>
                    {item.developer} · {item.categoryLabelEn}
                  </Text>
                </View>
                {item.isCustom && (
                  <TouchableOpacity onPress={() => deleteApp(item.id)} style={styles.iconAction}>
                    <Trash2 size={18} color={palette.danger} />
                  </TouchableOpacity>
                )}
              </View>
              <View style={styles.flagRow}>
                <FilterPill
                  label={item.isHidden ? (isAm ? 'ተደብቋል' : 'Hidden') : (isAm ? 'ይታያል' : 'Visible')}
                  selected={!item.isHidden}
                  palette={palette}
                  onPress={() => toggleAppFlag(item.id, 'isHidden')}
                />
                <FilterPill
                  label={isAm ? 'ተመራጭ' : 'Featured'}
                  selected={item.isFeatured}
                  palette={palette}
                  onPress={() => toggleAppFlag(item.id, 'isFeatured')}
                />
                <FilterPill
                  label={isAm ? 'ተወዳጅ' : 'Trending'}
                  selected={item.isTrending}
                  palette={palette}
                  onPress={() => toggleAppFlag(item.id, 'isTrending')}
                />
              </View>
            </View>
          )}
        />
      ) : (
        <FlatList
          data={aiTools}
          keyExtractor={(item) => item.id}
          contentContainerStyle={styles.listContent}
          renderItem={({ item }) => (
            <View
              style={[
                styles.card,
                { backgroundColor: palette.surface, borderColor: item.isHidden ? palette.danger : palette.border },
              ]}
            >
              <View style={styles.rowBetween}>
                <View style={styles.titleBlock}>
                  <View style={styles.titleRow}>
                    <Text style={[styles.title, { color: palette.textPrimary }]}>{item.name}</Text>
                    {item.isCustom && (
                      <StatusPill label="CUSTOM" color={palette.primary} backgroundColor={palette.primarySoft} />
                    )}
                  </View>
                  <Text style={[styles.subtitle, { color: palette.textSecondary }]}>
                    {item.provider} · {item.pricingTier}
                  </Text>
                </View>
                {item.isCustom && (
                  <TouchableOpacity onPress={() => deleteAiTool(item.id)} style={styles.iconAction}>
                    <Trash2 size={18} color={palette.danger} />
                  </TouchableOpacity>
                )}
              </View>
              <View style={styles.flagRow}>
                <FilterPill
                  label={item.isHidden ? (isAm ? 'ተደብቋል' : 'Hidden') : (isAm ? 'ይታያል' : 'Visible')}
                  selected={!item.isHidden}
                  palette={palette}
                  onPress={() => toggleAiToolFlag(item.id, 'isHidden')}
                />
                <FilterPill
                  label={isAm ? 'ተመራጭ' : 'Featured'}
                  selected={item.isFeatured}
                  palette={palette}
                  onPress={() => toggleAiToolFlag(item.id, 'isFeatured')}
                />
              </View>
            </View>
          )}
        />
      )}

      <Modal visible={showAddModal} transparent animationType="slide">
        <View style={styles.modalBackdrop}>
          <View style={[styles.modalSheet, { backgroundColor: palette.surface, borderColor: palette.border }]}>
            <Text style={[styles.modalTitle, { color: palette.textPrimary }]}>
              {segment === 'apps' ? 'Add Android App' : 'Add AI Tool'}
            </Text>
            <TextInput
              value={name}
              onChangeText={setName}
              placeholder="Name *"
              placeholderTextColor={palette.textSecondary}
              style={[styles.input, { color: palette.textPrimary, borderColor: palette.border, backgroundColor: palette.surfaceElevated }]}
            />
            <TextInput
              value={maker}
              onChangeText={setMaker}
              placeholder={segment === 'apps' ? 'Developer / Studio' : 'Provider'}
              placeholderTextColor={palette.textSecondary}
              style={[styles.input, { color: palette.textPrimary, borderColor: palette.border, backgroundColor: palette.surfaceElevated }]}
            />
            <TextInput
              value={taglineEn}
              onChangeText={setTaglineEn}
              placeholder="Tagline (English) *"
              placeholderTextColor={palette.textSecondary}
              style={[styles.input, { color: palette.textPrimary, borderColor: palette.border, backgroundColor: palette.surfaceElevated }]}
            />
            <TextInput
              value={taglineAm}
              onChangeText={setTaglineAm}
              placeholder="መግለጫ (አማርኛ)"
              placeholderTextColor={palette.textSecondary}
              style={[styles.input, { color: palette.textPrimary, borderColor: palette.border, backgroundColor: palette.surfaceElevated }]}
            />
            <TextInput
              value={url}
              onChangeText={setUrl}
              placeholder="Store / Official URL"
              placeholderTextColor={palette.textSecondary}
              style={[styles.input, { color: palette.textPrimary, borderColor: palette.border, backgroundColor: palette.surfaceElevated }]}
            />
            {segment === 'tools' && (
              <View style={styles.rowBetween}>
                <Text style={{ color: palette.textPrimary, fontWeight: '600' }}>Free Tier Badge</Text>
                <Switch value={isFreeTier} onValueChange={setIsFreeTier} />
              </View>
            )}
            <View style={styles.modalActions}>
              <TouchableOpacity
                style={[styles.modalBtn, { borderColor: palette.border, borderWidth: 1 }]}
                onPress={() => setShowAddModal(false)}
              >
                <Text style={{ color: palette.textPrimary, fontWeight: '700' }}>Cancel</Text>
              </TouchableOpacity>
              <TouchableOpacity
                style={[styles.modalBtn, { backgroundColor: palette.primary }]}
                onPress={handleAdd}
              >
                <Text style={{ color: '#FFFFFF', fontWeight: '800' }}>Publish</Text>
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
  topControls: { padding: 16, paddingBottom: 8, gap: 10 },
  segmented: { flexDirection: 'row', borderRadius: 12, borderWidth: 1, padding: 4, gap: 4 },
  segBtn: { flex: 1, borderRadius: 9, paddingVertical: 10, alignItems: 'center' },
  segText: { fontSize: 13, fontWeight: '800' },
  addBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    gap: 8,
    borderRadius: 12,
    paddingVertical: 13,
  },
  addBtnText: { color: '#FFFFFF', fontWeight: '800', fontSize: 14 },
  listContent: { padding: 16, paddingTop: 6, gap: 12 },
  card: { borderRadius: 16, borderWidth: 1, padding: 14, gap: 8 },
  rowBetween: { flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between' },
  titleBlock: { flex: 1, gap: 2 },
  titleRow: { flexDirection: 'row', alignItems: 'center', gap: 8 },
  title: { fontSize: 15, fontWeight: '800' },
  subtitle: { fontSize: 12 },
  iconAction: { padding: 6 },
  flagRow: { flexDirection: 'row', flexWrap: 'wrap', gap: 8 },
  modalBackdrop: { flex: 1, backgroundColor: 'rgba(0,0,0,0.65)', justifyContent: 'center', padding: 20 },
  modalSheet: { borderRadius: 18, borderWidth: 1, padding: 18, gap: 10 },
  modalTitle: { fontSize: 17, fontWeight: '800' },
  input: { borderWidth: 1, borderRadius: 12, paddingHorizontal: 12, paddingVertical: 10, fontSize: 14 },
  modalActions: { flexDirection: 'row', gap: 10, marginTop: 6 },
  modalBtn: { flex: 1, borderRadius: 12, paddingVertical: 12, alignItems: 'center' },
});
