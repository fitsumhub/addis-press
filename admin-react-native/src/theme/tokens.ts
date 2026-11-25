import { AdminThemeMode } from '../types/admin';

export interface ThemePalette {
  background: string;
  surface: string;
  surfaceElevated: string;
  border: string;
  textPrimary: string;
  textSecondary: string;
  primary: string;
  primarySoft: string;
  emerald: string;
  emeraldSoft: string;
  amber: string;
  amberSoft: string;
  danger: string;
  dangerSoft: string;
}

export const DARK_PALETTE: ThemePalette = {
  background: '#0B0F17',
  surface: '#131926',
  surfaceElevated: '#1A2233',
  border: '#263147',
  textPrimary: '#F3F6FC',
  textSecondary: '#94A3B8',
  primary: '#FF6B2C',
  primarySoft: 'rgba(255, 107, 44, 0.16)',
  emerald: '#10B981',
  emeraldSoft: 'rgba(16, 185, 129, 0.16)',
  amber: '#F59E0B',
  amberSoft: 'rgba(245, 158, 11, 0.16)',
  danger: '#EF4444',
  dangerSoft: 'rgba(239, 68, 68, 0.16)',
};

export const LIGHT_PALETTE: ThemePalette = {
  background: '#F4F6FA',
  surface: '#FFFFFF',
  surfaceElevated: '#EEF2F8',
  border: '#DCE3EE',
  textPrimary: '#0F172A',
  textSecondary: '#475569',
  primary: '#EA580C',
  primarySoft: 'rgba(234, 88, 12, 0.12)',
  emerald: '#059669',
  emeraldSoft: 'rgba(5, 150, 105, 0.12)',
  amber: '#D97706',
  amberSoft: 'rgba(217, 119, 6, 0.14)',
  danger: '#DC2626',
  dangerSoft: 'rgba(220, 38, 38, 0.12)',
};

export function getPalette(mode: AdminThemeMode): ThemePalette {
  return mode === 'dark' ? DARK_PALETTE : LIGHT_PALETTE;
}
