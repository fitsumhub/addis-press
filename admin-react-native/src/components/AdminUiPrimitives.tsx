import React from 'react';
import { StyleSheet, Switch, Text, TouchableOpacity, View } from 'react-native';
import { ThemePalette } from '../theme/tokens';

export function StatusPill({
  label,
  color,
  backgroundColor,
}: {
  label: string;
  color: string;
  backgroundColor: string;
}) {
  return (
    <View style={[styles.pill, { backgroundColor }]}>
      <Text style={[styles.pillText, { color }]}>{label}</Text>
    </View>
  );
}

export function KpiCard({
  title,
  value,
  subtitle,
  palette,
  onPress,
}: {
  title: string;
  value: string;
  subtitle: string;
  palette: ThemePalette;
  onPress: () => void;
}) {
  return (
    <TouchableOpacity
      activeOpacity={0.8}
      onPress={onPress}
      style={[
        styles.kpiCard,
        {
          backgroundColor: palette.surface,
          borderColor: palette.border,
        },
      ]}
    >
      <Text style={[styles.kpiTitle, { color: palette.textSecondary }]} numberOfLines={1}>
        {title}
      </Text>
      <Text style={[styles.kpiValue, { color: palette.textPrimary }]}>{value}</Text>
      <Text style={[styles.kpiSubtitle, { color: palette.primary }]} numberOfLines={1}>
        {subtitle}
      </Text>
    </TouchableOpacity>
  );
}

export function ToggleRow({
  label,
  value,
  palette,
  onValueChange,
}: {
  label: string;
  value: boolean;
  palette: ThemePalette;
  onValueChange: (next: boolean) => void;
}) {
  return (
    <View style={styles.toggleRow}>
      <Text style={[styles.toggleLabel, { color: palette.textPrimary }]}>{label}</Text>
      <Switch
        value={value}
        onValueChange={onValueChange}
        trackColor={{ false: palette.border, true: palette.primarySoft }}
        thumbColor={value ? palette.primary : palette.textSecondary}
      />
    </View>
  );
}

export function FilterPill({
  label,
  selected,
  palette,
  onPress,
}: {
  label: string;
  selected: boolean;
  palette: ThemePalette;
  onPress: () => void;
}) {
  return (
    <TouchableOpacity
      activeOpacity={0.75}
      onPress={onPress}
      style={[
        styles.filterChip,
        {
          backgroundColor: selected ? palette.primarySoft : palette.surfaceElevated,
          borderColor: selected ? palette.primary : palette.border,
        },
      ]}
    >
      <Text
        style={[
          styles.filterChipText,
          { color: selected ? palette.primary : palette.textSecondary },
        ]}
      >
        {label}
      </Text>
    </TouchableOpacity>
  );
}

const styles = StyleSheet.create({
  pill: {
    borderRadius: 6,
    paddingHorizontal: 8,
    paddingVertical: 3,
    alignSelf: 'flex-start',
  },
  pillText: {
    fontSize: 10,
    fontWeight: '800',
    letterSpacing: 0.4,
  },
  kpiCard: {
    flex: 1,
    borderRadius: 16,
    borderWidth: 1,
    padding: 14,
  },
  kpiTitle: {
    fontSize: 12,
    fontWeight: '600',
  },
  kpiValue: {
    fontSize: 22,
    fontWeight: '800',
    marginTop: 6,
  },
  kpiSubtitle: {
    fontSize: 11,
    fontWeight: '600',
    marginTop: 3,
  },
  toggleRow: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingVertical: 8,
  },
  toggleLabel: {
    fontSize: 14,
    fontWeight: '500',
    flex: 1,
    paddingRight: 12,
  },
  filterChip: {
    borderRadius: 10,
    borderWidth: 1,
    paddingHorizontal: 11,
    paddingVertical: 7,
    minHeight: 36,
    justifyContent: 'center',
  },
  filterChipText: {
    fontSize: 12,
    fontWeight: '700',
  },
});
