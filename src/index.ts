import { requireNativeModule } from 'expo-modules-core';
import type { PrintTextOptions, PrinterStatusResult } from './types';

const SunmiPrinterModule = requireNativeModule('SunmiPrinter');

// Exportar tipos
export * from './types';

export async function initPrinter(): Promise<boolean> {
  return await SunmiPrinterModule.initPrinter();
}

export async function printText(text: string, options?: PrintTextOptions): Promise<void> {
  return await SunmiPrinterModule.printText(text, options?.align ?? null, options?.bold ?? null);
}

export async function printTextWithSize(
  text: string,
  size: number,
  options?: PrintTextOptions
): Promise<void> {
  return await SunmiPrinterModule.printTextWithSize(text, size, options?.align ?? null, options?.bold ?? null);
}

export async function lineWrap(lines: number): Promise<void> {
  return await SunmiPrinterModule.lineWrap(lines);
}

// A visible rule (SOLID/DOTTED), unlike lineWrap which is blank spacing.
export async function printDivider(style: 'solid' | 'dotted' = 'solid'): Promise<void> {
  return await SunmiPrinterModule.printDivider(style);
}

// Raw Sunmi Status enum name (e.g. 'READY', 'ERR_PAPER_OUT', 'ERR_COVER', 'OFFLINE').
export async function queryPrinterStatus(): Promise<PrinterStatusResult> {
  const status = await SunmiPrinterModule.queryPrinterStatus();
  return { status };
}

export async function cutPaper(): Promise<void> {
  return await SunmiPrinterModule.cutPaper();
}

export async function printBarcode(data: string): Promise<void> {
  return await SunmiPrinterModule.printBarcode(data);
}

export async function printQRCode(data: string): Promise<void> {
  return await SunmiPrinterModule.printQRCode(data);
}

export async function printImage(assetName: string): Promise<void> {
  return await SunmiPrinterModule.printImage(assetName);
}

export async function printImageBase64(base64String: string): Promise<void> {
  return await SunmiPrinterModule.printImageBase64(base64String);
}