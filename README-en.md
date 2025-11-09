# Sunmi Printer Expo

> [🇪🇸 Versión en español](README.md)

An Expo module for integrating Sunmi thermal printers in React Native applications.

## Features

- ✅ Printer initialization
- ✅ Text printing with different sizes
- ✅ Barcode and QR code printing
- ✅ Image printing (from assets and base64)
- ✅ Line wrap and paper cutting control
- ✅ Full TypeScript support

## Installation

```bash
npm install sunmi-printer-expo
# or
yarn add sunmi-printer-expo
```

### Expo Configuration

If you're using Expo managed workflow, you'll need to use a custom development client:

```bash
expo install expo-dev-client
```

## Usage

```typescript
import {
  initPrinter,
  printText,
  printTextWithSize,
  printBarcode,
  printQRCode,
  printImage,
  printImageBase64,
  lineWrap,
  cutPaper
} from 'sunmi-printer-expo';

// Initialize printer
const success = await initPrinter();
if (success) {
  console.log('Printer initialized successfully');
}

// Print simple text
await printText('Hello world!');

// Print text with specific size
await printTextWithSize('Large text', 24);

// Line breaks
await lineWrap(2);

// Print barcode
await printBarcode('1234567890');

// Print QR code
await printQRCode('https://example.com');

// Print image from assets
await printImage('logo.png');

// Print image from base64
const base64Image = 'iVBORw0KGgoAAAANSUhEUgAA...';
await printImageBase64(base64Image);

// Cut paper
await cutPaper();
```

## API

### `initPrinter(): Promise<boolean>`
Initializes the Sunmi printer. Returns `true` if initialization was successful.

### `printText(text: string): Promise<void>`
Prints simple text with default size.

### `printTextWithSize(text: string, size: number): Promise<void>`
Prints text with a specific font size.

### `lineWrap(lines: number): Promise<void>`
Adds line breaks (blank spaces).

### `printBarcode(data: string): Promise<void>`
Prints a barcode with the provided data.

### `printQRCode(data: string): Promise<void>`
Prints a QR code with the provided data.

### `printImage(assetName: string): Promise<void>`
Prints an image from application assets.

### `printImageBase64(base64String: string): Promise<void>`
Prints an image from a base64 string.

### `cutPaper(): Promise<void>`
Cuts the paper (if the printer supports this function).

## Compatibility

- ✅ Android (Sunmi printers)
- ❌ iOS (Not supported - Sunmi specific)
- ✅ Expo SDK 49+
- ✅ React Native 0.70+

## Complete Example

```typescript
import React from 'react';
import { View, Button, Alert } from 'react-native';
import {
  initPrinter,
  printText,
  printTextWithSize,
  lineWrap,
  cutPaper
} from 'sunmi-printer-expo';

export default function App() {
  const handlePrint = async () => {
    try {
      // Initialize printer
      const success = await initPrinter();
      if (!success) {
        Alert.alert('Error', 'Could not initialize printer');
        return;
      }

      // Print sample receipt
      await printTextWithSize('MY STORE', 28);
      await lineWrap(1);
      await printText('Date: ' + new Date().toLocaleDateString());
      await printText('--------------------------------');
      await printText('Product 1           $10.00');
      await printText('Product 2           $15.50');
      await printText('--------------------------------');
      await printTextWithSize('TOTAL: $25.50', 24);
      await lineWrap(2);
      await printText('Thank you for your purchase!');
      await lineWrap(2);
      
      // Cut paper
      await cutPaper();
      
    } catch (error) {
      Alert.alert('Error', 'Print error: ' + error.message);
    }
  };

  return (
    <View style={{ flex: 1, justifyContent: 'center', padding: 20 }}>
      <Button title="Print Test Receipt" onPress={handlePrint} />
    </View>
  );
}
```

## Contributing

Contributions are welcome! Please:

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

If you encounter any issues or have questions:

1. Check [existing Issues](https://github.com/your-username/sunmi-printer-expo/issues)
2. Create a new issue if you don't find your problem
3. Provide all relevant information (Expo version, printer model, etc.)