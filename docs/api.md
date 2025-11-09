# API Documentation

## Overview

The `sunmi-printer-expo` module provides a simple interface to interact with Sunmi thermal printers in React Native/Expo applications.

## Installation

```bash
npm install sunmi-printer-expo
```

## Basic Usage

```typescript
import { initPrinter, printText, cutPaper } from 'sunmi-printer-expo';

const printReceipt = async () => {
  const success = await initPrinter();
  if (success) {
    await printText('Hello, World!');
    await cutPaper();
  }
};
```

## API Reference

### Core Functions

#### `initPrinter(): Promise<boolean>`

Initializes the Sunmi printer service.

**Returns:** Promise that resolves to `true` if initialization was successful, `false` otherwise.

**Example:**
```typescript
const isReady = await initPrinter();
if (isReady) {
  console.log('Printer is ready!');
}
```

#### `printText(text: string): Promise<void>`

Prints text using the default font size.

**Parameters:**
- `text` (string): The text to print

**Example:**
```typescript
await printText('Hello, World!');
```

#### `printTextWithSize(text: string, size: number): Promise<void>`

Prints text with a specific font size.

**Parameters:**
- `text` (string): The text to print
- `size` (number): Font size

**Example:**
```typescript
await printTextWithSize('Large Title', 28);
```

### Formatting Functions

#### `lineWrap(lines: number): Promise<void>`

Adds blank lines for spacing.

**Parameters:**
- `lines` (number): Number of blank lines to add

**Example:**
```typescript
await lineWrap(2); // Adds 2 blank lines
```

#### `cutPaper(): Promise<void>`

Cuts the paper (if supported by the printer).

**Example:**
```typescript
await cutPaper();
```

### Barcode Functions

#### `printBarcode(data: string): Promise<void>`

Prints a barcode with the given data.

**Parameters:**
- `data` (string): The data to encode in the barcode

**Example:**
```typescript
await printBarcode('1234567890');
```

#### `printQRCode(data: string): Promise<void>`

Prints a QR code with the given data.

**Parameters:**
- `data` (string): The data to encode in the QR code

**Example:**
```typescript
await printQRCode('https://example.com');
```

### Image Functions

#### `printImage(assetName: string): Promise<void>`

Prints an image from the app's assets.

**Parameters:**
- `assetName` (string): Name of the image asset

**Example:**
```typescript
await printImage('logo.png');
```

#### `printImageBase64(base64String: string): Promise<void>`

Prints an image from a base64 encoded string.

**Parameters:**
- `base64String` (string): Base64 encoded image data

**Example:**
```typescript
const base64Image = 'iVBORw0KGgoAAAANSUhEUgAA...';
await printImageBase64(base64Image);
```

## TypeScript Support

The library includes comprehensive TypeScript definitions. All interfaces and types are exported for use in your TypeScript projects.

### Available Types

- `PrintTextOptions`
- `BarcodeOptions`
- `QRCodeOptions`
- `ImageOptions`
- `PrinterStatus`
- `PrinterEvent`
- `PrinterEventCallback`

## Error Handling

All functions are asynchronous and may throw errors. Always wrap calls in try-catch blocks:

```typescript
try {
  await initPrinter();
  await printText('Success!');
} catch (error) {
  console.error('Printing failed:', error);
}
```

## Platform Support

- ✅ Android (Sunmi devices)
- ❌ iOS (Not supported)
- ❌ Web (Not supported)

## Compatibility

- React Native 0.70+
- Expo SDK 49+
- Android API level 21+