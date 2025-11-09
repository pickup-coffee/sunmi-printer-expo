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
 * Información del estado de la impresora
 */
export interface PrinterStatus {
  /** Si la impresora está conectada */
  connected: boolean;
  /** Si hay papel disponible */
  paperAvailable: boolean;
  /** Temperatura de la impresora */
  temperature?: number;
  /** Mensaje de estado */
  statusMessage?: string;
}

/**
 * Eventos que puede emitir la impresora
 */
export type PrinterEvent = 
  | 'paperOut'
  | 'overHeat'
  | 'printerReady'
  | 'printerError';

/**
 * Callback para eventos de la impresora
 */
export type PrinterEventCallback = (event: PrinterEvent, data?: any) => void;