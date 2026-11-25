import React from 'react';
import { ScrollView, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import { CheckCircle2, Flag, Trash2 } from 'lucide-react-native';
import { FilterPill } from '../components/AdminUiPrimitives';
import { useAdminStore } from '../store/AdminContext';
import { ThemePalette } from '../theme/tokens';
import { SubmissionStatus } from '../types/admin';

const WORKFLOW_STATES: SubmissionStatus[] = ['draft', 'review', 'published', 'archived'];

export function ModerationScreen({ palette }: { palette: ThemePalette }) {
  const {
    language,
    reports,
    submissions,
    hideReportedTarget,
    dismissReport,
    updateSubmissionStatus,
    deleteSubmission,
  } = useAdminStore();

  const isAm = language === 'am';

  return (
    <ScrollView
      style={styles.container}
      contentContainerStyle={styles.content}
      showsVerticalScrollIndicator={false}
    >
      <Text style={[styles.sectionTitle, { color: palette.textPrimary }]}>
        {isAm ? `የተጠቃሚ ሪፖርቶች (${reports.length})` : `User Reports Queue (${reports.length})`}
      </Text>

      {reports.length === 0 ? (
        <View style={[styles.emptyCard, { backgroundColor: palette.surface, borderColor: palette.border }]}>
          <CheckCircle2 size={20} color={palette.emerald} />
          <Text style={[styles.emptyText, { color: palette.textSecondary }]}>
            {isAm ? 'ምንም ያልተፈታ የተጠቃሚ ሪፖርት የለም።' : 'All clear — no pending reader reports.'}
          </Text>
        </View>
      ) : (
        reports.map((report) => (
          <View
            key={report.id}
            style={[styles.card, { backgroundColor: palette.surface, borderColor: palette.danger }]}
          >
            <View style={styles.flagHeader}>
              <Flag size={16} color={palette.danger} />
              <Text style={[styles.reportMeta, { color: palette.danger }]}>
                {report.targetType.toUpperCase()} · {report.targetId} · {report.createdAtLabel}
              </Text>
            </View>
            <Text style={[styles.reason, { color: palette.textPrimary }]}>{report.reason}</Text>
            {report.details.length > 0 && (
              <Text style={[styles.details, { color: palette.textSecondary }]}>{report.details}</Text>
            )}
            <View style={styles.actionsRow}>
              <TouchableOpacity
                style={[styles.hideBtn, { backgroundColor: palette.danger }]}
                onPress={() => hideReportedTarget(report)}
              >
                <Text style={styles.hideBtnText}>{isAm ? 'ይዘቱን ደብቅ' : 'Hide Content'}</Text>
              </TouchableOpacity>
              <TouchableOpacity
                style={[styles.dismissBtn, { borderColor: palette.border }]}
                onPress={() => dismissReport(report.id)}
              >
                <Text style={[styles.dismissBtnText, { color: palette.textPrimary }]}>
                  {isAm ? 'አጥፋ' : 'Dismiss'}
                </Text>
              </TouchableOpacity>
            </View>
          </View>
        ))
      )}

      <Text style={[styles.sectionTitle, { color: palette.textPrimary, marginTop: 8 }]}>
        {isAm ? `የተጠቃሚ ጽሑፎች ቁጥጥር (${submissions.length})` : `Community Stories Workflow (${submissions.length})`}
      </Text>

      {submissions.map((sub) => (
        <View
          key={sub.id}
          style={[styles.card, { backgroundColor: palette.surface, borderColor: palette.border }]}
        >
          <View style={styles.rowBetween}>
            <View style={{ flex: 1 }}>
              <Text style={[styles.subMeta, { color: palette.primary }]}>
                {sub.categoryId.toUpperCase()} · {sub.authorName}
              </Text>
              <Text style={[styles.reason, { color: palette.textPrimary }]} numberOfLines={2}>
                {isAm ? sub.titleAm : sub.titleEn}
              </Text>
            </View>
            <TouchableOpacity onPress={() => deleteSubmission(sub.id)} style={{ padding: 6 }}>
              <Trash2 size={18} color={palette.danger} />
            </TouchableOpacity>
          </View>
          <View style={styles.statusRow}>
            {WORKFLOW_STATES.map((st) => (
              <FilterPill
                key={st}
                label={st.charAt(0).toUpperCase() + st.slice(1)}
                selected={sub.status === st}
                palette={palette}
                onPress={() => updateSubmissionStatus(sub.id, st)}
              />
            ))}
          </View>
        </View>
      ))}
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1 },
  content: { padding: 16, gap: 12, paddingBottom: 40 },
  sectionTitle: { fontSize: 16, fontWeight: '800' },
  emptyCard: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 10,
    borderRadius: 14,
    borderWidth: 1,
    padding: 16,
  },
  emptyText: { fontSize: 14, flex: 1 },
  card: { borderRadius: 16, borderWidth: 1, padding: 14, gap: 8 },
  flagHeader: { flexDirection: 'row', alignItems: 'center', gap: 6 },
  reportMeta: { fontSize: 11, fontWeight: '800' },
  reason: { fontSize: 14, fontWeight: '800' },
  details: { fontSize: 12 },
  actionsRow: { flexDirection: 'row', gap: 10, marginTop: 4 },
  hideBtn: { flex: 1, borderRadius: 10, paddingVertical: 11, alignItems: 'center' },
  hideBtnText: { color: '#FFFFFF', fontWeight: '800', fontSize: 13 },
  dismissBtn: { flex: 1, borderRadius: 10, borderWidth: 1, paddingVertical: 11, alignItems: 'center' },
  dismissBtnText: { fontWeight: '700', fontSize: 13 },
  rowBetween: { flexDirection: 'row', alignItems: 'center', gap: 8 },
  subMeta: { fontSize: 11, fontWeight: '800', marginBottom: 2 },
  statusRow: { flexDirection: 'row', flexWrap: 'wrap', gap: 8, marginTop: 4 },
});
