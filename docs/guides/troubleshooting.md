# Troubleshooting Guide

## Common Issues and Solutions

### 1. Printer Initialization Fails

**Symptoms:**
- `initPrinter()` returns `false`
- Error messages about printer service

**Solutions:**

#### Check Device Compatibility
```typescript
// Verify you're on a Sunmi device
const checkDevice = () => {
  const { Platform } = require('react-native');
  if (Platform.OS !== 'android') {
    console.log('Sunmi printers only work on Android');
    return false;
  }
  return true;
};
```

#### Restart Printer Service
Sometimes the printer service needs to be restarted:
1. Go to Android Settings > Apps
2. Find "Printer Service" or similar Sunmi service
3. Force stop and restart

#### Check Permissions
Ensure your app has necessary permissions in `android/app/src/main/AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

### 2. Build Errors

**Error: "Cannot resolve module 'sunmi-printer-expo'"**

**Solutions:**
- Clear cache: `npx react-native start --reset-cache`
- Reinstall node modules: `rm -rf node_modules && npm install`
- For Expo: Ensure you're using a custom development client

**Error: "Native module not found"**

This happens when running on non-Sunmi devices or simulators.
```typescript
// Add device check
const isSunmiDevice = async () => {
  try {
    await initPrinter();
    return true;
  } catch (error) {
    console.log('Not a Sunmi device or printer unavailable');
    return false;
  }
};
```

### 3. Print Quality Issues

**Text appears cut off or formatted incorrectly:**

1. **Check text length**: Sunmi printers typically support 32-48 characters per line
2. **Use proper spacing**:
   ```typescript
   // Good: Proper spacing for receipt format
   await printText('Item 1               $10.99');
   
   // Bad: Too long, will wrap oddly
   await printText('Very long item name that exceeds printer width     $10.99');
   ```

3. **Test different font sizes**:
   ```typescript
   // Try different sizes to find what works best
   await printTextWithSize('Small text', 16);
   await printTextWithSize('Medium text', 20);
   await printTextWithSize('Large text', 28);
   ```

### 4. QR Code/Barcode Issues

**QR codes not scanning properly:**

1. **Keep data concise**: Long URLs or data may not scan well
2. **Test QR code content**:
   ```typescript
   // Good: Short, clean URL
   await printQRCode('https://bit.ly/abc123');
   
   // May cause issues: Very long URL
   await printQRCode('https://very-long-domain.com/extremely/long/path/with/many/parameters?param1=value1&param2=value2');
   ```

3. **Add spacing around codes**:
   ```typescript
   await lineWrap(1);
   await printQRCode('https://example.com');
   await lineWrap(1);
   ```

### 5. Performance Issues

**Slow printing or app freezes:**

1. **Use proper async/await**:
   ```typescript
   // Good: Proper async handling
   const printSequence = async () => {
     await printText('Line 1');
     await printText('Line 2');
     await cutPaper();
   };
   
   // Bad: No await, may cause issues
   const badPrintSequence = () => {
     printText('Line 1');
     printText('Line 2');
     cutPaper();
   };
   ```

2. **Add delays for large prints**:
   ```typescript
   const printLargeReceipt = async () => {
     await printText('Header');
     
     // Add small delay for processing
     await new Promise(resolve => setTimeout(resolve, 100));
     
     // Continue printing...
     await printText('Body content');
   };
   ```

### 6. Testing and Debugging

#### Enable Debug Mode
```typescript
const debugPrint = async (text: string) => {
  try {
    console.log(`Printing: ${text}`);
    await printText(text);
    console.log(`Successfully printed: ${text}`);
  } catch (error) {
    console.error(`Failed to print "${text}":`, error);
  }
};
```

#### Test on Real Hardware
Always test on actual Sunmi devices:
- Sunmi T2 series
- Sunmi V2 series  
- Sunmi P2 series
- Other Sunmi POS devices

#### Mock for Development
For development on non-Sunmi devices:
```typescript
const mockPrint = __DEV__ && Platform.OS !== 'android';

export const safePrintText = async (text: string) => {
  if (mockPrint) {
    console.log(`[MOCK PRINT] ${text}`);
    return;
  }
  return await printText(text);
};
```

## Getting Help

### Check Logs
Enable detailed logging to diagnose issues:
```bash
# View Android logs
adb logcat | grep -i sunmi

# Or filter for your app
adb logcat | grep YourAppName
```

### Community Support
- GitHub Issues: Report bugs and ask questions
- Expo Forums: Community discussions
- React Native Community: General RN help

### Hardware Support
For hardware-specific issues:
- Contact Sunmi support
- Check device firmware updates
- Verify printer hardware functionality

## Prevention Tips

1. **Always handle errors**: Wrap all print functions in try-catch
2. **Test thoroughly**: Use real devices for testing
3. **Keep it simple**: Start with basic prints before complex layouts
4. **Monitor performance**: Watch for memory leaks in long print sessions
5. **Update regularly**: Keep the package and dependencies updated