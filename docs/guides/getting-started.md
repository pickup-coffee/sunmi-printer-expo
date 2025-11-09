# Getting Started with sunmi-printer-expo

## Prerequisites

Before you begin, ensure you have:

1. **React Native or Expo development environment** set up
2. **Sunmi device** with integrated thermal printer
3. **Android development** capabilities (iOS is not supported)

## Installation

### Step 1: Install the package

```bash
npm install sunmi-printer-expo
```

or with yarn:

```bash
yarn add sunmi-printer-expo
```

### Step 2: For Expo managed workflow

If you're using Expo managed workflow, you'll need to use a custom development client:

```bash
expo install expo-dev-client
```

### Step 3: Basic setup verification

Create a simple test to verify the installation:

```typescript
import { initPrinter } from 'sunmi-printer-expo';

const testPrinter = async () => {
  try {
    const success = await initPrinter();
    console.log('Printer initialized:', success);
  } catch (error) {
    console.error('Error:', error);
  }
};
```

## Your First Print

Let's create a simple "Hello World" print function:

```typescript
import React from 'react';
import { View, Button, Alert } from 'react-native';
import { initPrinter, printText, cutPaper } from 'sunmi-printer-expo';

export default function MyPrinterApp() {
  const handlePrint = async () => {
    try {
      // Step 1: Initialize the printer
      const success = await initPrinter();
      if (!success) {
        Alert.alert('Error', 'Could not initialize printer');
        return;
      }

      // Step 2: Print text
      await printText('Hello, Sunmi Printer!');
      
      // Step 3: Cut the paper
      await cutPaper();
      
      Alert.alert('Success', 'Printed successfully!');
    } catch (error) {
      Alert.alert('Error', `Printing failed: ${error.message}`);
    }
  };

  return (
    <View style={{ flex: 1, justifyContent: 'center', padding: 20 }}>
      <Button title="Print Hello World" onPress={handlePrint} />
    </View>
  );
}
```

## Building a Receipt

Here's a more comprehensive example of printing a receipt:

```typescript
const printReceipt = async () => {
  try {
    const success = await initPrinter();
    if (!success) return;

    // Header
    await printTextWithSize('MY STORE', 28);
    await printText('123 Main Street');
    await printText('Phone: (555) 123-4567');
    await printText('================================');
    
    // Transaction details
    await printText(`Date: ${new Date().toLocaleDateString()}`);
    await printText(`Time: ${new Date().toLocaleTimeString()}`);
    await lineWrap(1);
    
    // Items
    await printText('Item 1               $10.99');
    await printText('Item 2               $15.50');
    await printText('--------------------------------');
    await printTextWithSize('TOTAL: $26.49', 24);
    
    // QR Code for digital receipt
    await lineWrap(1);
    await printText('Digital receipt:');
    await printQRCode('https://mystore.com/receipt/12345');
    
    await lineWrap(2);
    await printText('Thank you for your purchase!');
    
    // Cut paper
    await cutPaper();
    
  } catch (error) {
    console.error('Receipt printing failed:', error);
  }
};
```

## Testing on Device

1. **Build and install** your app on a Sunmi device
2. **Run the app** on the Sunmi hardware
3. **Test printing** - the functions will only work on actual Sunmi devices

## Common Issues

### Printer Not Initializing
- Ensure you're running on a Sunmi device
- Check that the printer service is available
- Restart the device if needed

### Build Errors
- Make sure you're using a custom development client with Expo
- Verify React Native version compatibility

## Next Steps

- Check out the [API Documentation](../api.md) for all available functions
- See the [example app](../../example/README.md) for a complete implementation
- Review [troubleshooting guide](troubleshooting.md) if you encounter issues