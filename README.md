# Sunmi Printer Expo

> [🇺🇸 English version](README-en.md)

Un módulo de Expo para integración con impresoras térmicas Sunmi en aplicaciones React Native.

## Características

- ✅ Inicialización de impresora
- ✅ Impresión de texto con diferentes tamaños
- ✅ Códigos de barras y QR
- ✅ Impresión de imágenes (desde assets y base64)
- ✅ Control de salto de línea y corte de papel
- ✅ Soporte completo para TypeScript

## Instalación

```bash
npm install sunmi-printer-expo
# o
yarn add sunmi-printer-expo
```

### Configuración en Expo

Si usas Expo managed workflow, necesitarás usar un custom development client:

```bash
expo install expo-dev-client
```

## Uso

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

// Inicializar la impresora
const success = await initPrinter();
if (success) {
  console.log('Impresora inicializada correctamente');
}

// Imprimir texto simple
await printText('¡Hola mundo!');

// Imprimir texto con tamaño específico
await printTextWithSize('Texto grande', 24);

// Saltar líneas
await lineWrap(2);

// Imprimir código de barras
await printBarcode('1234567890');

// Imprimir código QR
await printQRCode('https://ejemplo.com');

// Imprimir imagen desde assets
await printImage('logo.png');

// Imprimir imagen desde base64
const base64Image = 'iVBORw0KGgoAAAANSUhEUgAA...';
await printImageBase64(base64Image);

// Cortar papel
await cutPaper();
```

## API

### `initPrinter(): Promise<boolean>`
Inicializa la impresora Sunmi. Retorna `true` si la inicialización fue exitosa.

### `printText(text: string): Promise<void>`
Imprime texto simple con el tamaño por defecto.

### `printTextWithSize(text: string, size: number): Promise<void>`
Imprime texto con un tamaño específico de fuente.

### `lineWrap(lines: number): Promise<void>`
Agrega saltos de línea (espacios en blanco).

### `printBarcode(data: string): Promise<void>`
Imprime un código de barras con los datos proporcionados.

### `printQRCode(data: string): Promise<void>`
Imprime un código QR con los datos proporcionados.

### `printImage(assetName: string): Promise<void>`
Imprime una imagen desde los assets de la aplicación.

### `printImageBase64(base64String: string): Promise<void>`
Imprime una imagen desde una cadena base64.

### `cutPaper(): Promise<void>`
Corta el papel (si la impresora soporta esta función).

## Compatibilidad

- ✅ Android (Impresoras Sunmi)
- ❌ iOS (No soportado - específico para Sunmi)
- ✅ Expo SDK 49+
- ✅ React Native 0.70+

## Ejemplo completo

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
      // Inicializar impresora
      const success = await initPrinter();
      if (!success) {
        Alert.alert('Error', 'No se pudo inicializar la impresora');
        return;
      }

      // Imprimir recibo de ejemplo
      await printTextWithSize('MI TIENDA', 28);
      await lineWrap(1);
      await printText('Fecha: ' + new Date().toLocaleDateString());
      await printText('--------------------------------');
      await printText('Producto 1          $10.00');
      await printText('Producto 2          $15.50');
      await printText('--------------------------------');
      await printTextWithSize('TOTAL: $25.50', 24);
      await lineWrap(2);
      await printText('¡Gracias por su compra!');
      await lineWrap(2);
      
      // Cortar papel
      await cutPaper();
      
    } catch (error) {
      Alert.alert('Error', 'Error al imprimir: ' + error.message);
    }
  };

  return (
    <View style={{ flex: 1, justifyContent: 'center', padding: 20 }}>
      <Button title="Imprimir Recibo de Prueba" onPress={handlePrint} />
    </View>
  );
}
```

## Contribuir

Las contribuciones son bienvenidas. Por favor:

1. Fork el repositorio
2. Crea tu branch de feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push al branch (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## Licencia

Este proyecto está licenciado bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

## Soporte

Si encuentras algún problema o tienes preguntas:

1. Revisa los [Issues existentes](https://github.com/tu-usuario/sunmi-printer-expo/issues)
2. Crea un nuevo issue si no encuentras tu problema
3. Proporciona toda la información relevante (versión de Expo, modelo de impresora, etc.)