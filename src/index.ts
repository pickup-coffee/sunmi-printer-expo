import { requireNativeModule } from 'expo-modules-core';

const SunmiPrinterModule = requireNativeModule('SunmiPrinter');

// Exportar tipos
export * from './types';

export async function initPrinter(): Promise<boolean> {
  return await SunmiPrinterModule.initPrinter();
}

export async function printText(text: string): Promise<void> {
  return await SunmiPrinterModule.printText(text);
}

export async function printTextWithSize(text: string, size: number): Promise<void> {
  return await SunmiPrinterModule.printTextWithSize(text, size);
}

export async function lineWrap(lines: number): Promise<void> {
  return await SunmiPrinterModule.lineWrap(lines);
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