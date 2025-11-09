# Sunmi Printer Example App

Esta es una aplicación de ejemplo que demuestra el uso del paquete `sunmi-printer-expo`.

## Instalación

1. Navega a la carpeta example:
```bash
cd example
```

2. Instala las dependencias:
```bash
npm install
```

3. Ejecuta la aplicación:
```bash
# Para Android
npm run android

# Para iniciar el servidor de desarrollo
npm start
```

## Características

- **Prueba Simple**: Imprime un texto básico de prueba
- **Recibo Completo**: Demuestra todas las funcionalidades incluyendo:
  - Texto con diferentes tamaños
  - Códigos QR
  - Códigos de barras
  - Saltos de línea
  - Corte de papel

## Requisitos

- Dispositivo Android con impresora Sunmi integrada
- Expo CLI
- React Native development environment

## Estructura del Código

- `App.tsx` - Componente principal con ejemplos de uso
- `package.json` - Configuración del proyecto de ejemplo
- `app.json` - Configuración de Expo

## Nota Importante

Esta aplicación debe ejecutarse en un dispositivo Sunmi real para probar las funcionalidades de impresión. No funcionará en simuladores o dispositivos que no tengan hardware Sunmi.