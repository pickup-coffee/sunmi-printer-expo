import React from 'react';
import { View, Button, Alert, StyleSheet, Text } from 'react-native';
import {
  initPrinter,
  printText,
  printTextWithSize,
  printBarcode,
  printQRCode,
  lineWrap,
  cutPaper
} from 'sunmi-printer-expo';

export default function App() {
  const handleTestPrint = async () => {
    try {
      // Inicializar la impresora
      const success = await initPrinter();
      if (!success) {
        Alert.alert('Error', 'No se pudo inicializar la impresora Sunmi');
        return;
      }

      // Imprimir encabezado
      await printTextWithSize('RECIBO DE PRUEBA', 28);
      await lineWrap(1);
      
      // Información de la tienda
      await printText('Mi Negocio S.A.');
      await printText('Dirección: Calle Principal #123');
      await printText('Tel: (123) 456-7890');
      await printText('================================');
      await lineWrap(1);

      // Información de la venta
      await printText(`Fecha: ${new Date().toLocaleDateString()}`);
      await printText(`Hora: ${new Date().toLocaleTimeString()}`);
      await printText('Cajero: Usuario Test');
      await printText('--------------------------------');

      // Productos
      await printText('Producto 1           $10.00');
      await printText('Producto 2           $25.50');
      await printText('Producto 3            $8.75');
      await printText('--------------------------------');
      
      // Total
      await printTextWithSize('TOTAL: $44.25', 24);
      await lineWrap(1);

      // Código QR con información
      await printText('Escanea para más info:');
      await printQRCode('https://tu-sitio.com/recibo/12345');
      await lineWrap(1);

      // Código de barras
      await printText('Código de transacción:');
      await printBarcode('123456789012');
      await lineWrap(2);

      await printText('¡Gracias por su compra!');
      await printText('Vuelva pronto');
      await lineWrap(3);
      
      // Cortar papel
      await cutPaper();
      
      Alert.alert('Éxito', 'Recibo impreso correctamente');
      
    } catch (error) {
      console.error('Error de impresión:', error);
      Alert.alert('Error', `Error al imprimir: ${(error as Error).message}`);
    }
  };

  const handleSimplePrint = async () => {
    try {
      const success = await initPrinter();
      if (!success) {
        Alert.alert('Error', 'No se pudo inicializar la impresora');
        return;
      }

      await printTextWithSize('PRUEBA SIMPLE', 24);
      await lineWrap(1);
      await printText('Esta es una prueba básica');
      await printText('de impresión con Sunmi');
      await lineWrap(3);
      await cutPaper();
      
      Alert.alert('Éxito', 'Prueba simple completada');
    } catch (error) {
      Alert.alert('Error', `Error: ${(error as Error).message}`);
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Sunmi Printer Example</Text>
      
      <View style={styles.buttonContainer}>
        <Button
          title="Prueba Simple"
          onPress={handleSimplePrint}
          color="#2196F3"
        />
      </View>
      
      <View style={styles.buttonContainer}>
        <Button
          title="Imprimir Recibo Completo"
          onPress={handleTestPrint}
          color="#4CAF50"
        />
      </View>
      
      <Text style={styles.note}>
        Nota: Asegúrate de que el dispositivo sea una impresora Sunmi 
        y que esté conectada correctamente.
      </Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20,
    backgroundColor: '#f5f5f5',
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 30,
    color: '#333',
  },
  buttonContainer: {
    marginVertical: 10,
    width: '80%',
  },
  note: {
    marginTop: 30,
    textAlign: 'center',
    fontSize: 14,
    color: '#666',
    fontStyle: 'italic',
    paddingHorizontal: 20,
  },
});