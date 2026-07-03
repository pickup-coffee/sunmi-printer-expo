/**
 * Opciones para configurar la impresión de texto
 */
export interface PrintTextOptions {
  /** Tamaño de la fuente (opcional) */
  size?: number;
  /** Alineación del texto */
  align?: 'left' | 'center' | 'right';
  /** Texto en negrita */
  bold?: boolean;
}

// NOTE: the following types are not yet wired to any exported function in
// index.ts (barcode/QR/image printing still use fixed, hardcoded styling on
// the native side) — kept as a reference for future work, not a working API.

/**
 * Opciones para configurar la impresión de códigos de barras
 */
export interface BarcodeOptions {
  /** Tipo de código de barras */
  type?: 'CODE128' | 'CODE39' | 'EAN13' | 'EAN8';
  /** Altura del código de barras */
  height?: number;
  /** Mostrar texto debajo del código */
  showText?: boolean;
}

/**
 * Opciones para configurar la impresión de códigos QR
 */
export interface QRCodeOptions {
  /** Tamaño del código QR */
  size?: number;
  /** Nivel de corrección de errores */
  errorLevel?: 'L' | 'M' | 'Q' | 'H';
}

/**
 * Opciones para configurar la impresión de imágenes
 */
export interface ImageOptions {
  /** Ancho de la imagen (píxeles) */
  width?: number;
  /** Alto de la imagen (píxeles) */
  height?: number;
  /** Alineación de la imagen */
  align?: 'left' | 'center' | 'right';
}

/**
 * Resultado de queryPrinterStatus() — el nombre crudo del enum Status del SDK
 * de Sunmi (p.ej. 'READY', 'ERR_PAPER_OUT', 'ERR_COVER', 'OFFLINE', 'COMM').
 * Ver com.sunmi.printerx.enums.Status en printerx para la lista completa.
 */
export interface PrinterStatusResult {
  status: string;
}