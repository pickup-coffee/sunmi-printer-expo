package expo.modules.sunmiprinter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.util.Base64
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import expo.modules.kotlin.Promise
import com.sunmi.printerx.PrinterSdk
import com.sunmi.printerx.PrinterSdk.Printer
import com.sunmi.printerx.PrinterSdk.PrinterListen
import java.io.InputStream

class SunmiPrinterModule : Module() {
  private var printer: Printer? = null
  private var isInitialized = false

  private fun alignFromString(align: String?): com.sunmi.printerx.enums.Align {
    return when (align?.lowercase()) {
      "left" -> com.sunmi.printerx.enums.Align.LEFT
      "center" -> com.sunmi.printerx.enums.Align.CENTER
      "right" -> com.sunmi.printerx.enums.Align.RIGHT
      else -> com.sunmi.printerx.enums.Align.DEFAULT
    }
  }

  // Verified via decompiled printerx 1.0.20: QueryApi.getInfo(PrinterInfo.PAPER) returns
  // the paper width in print dots as a string ("576" for 80mm, "384" for 58mm, "" if
  // unknown) — this is the same unit Format.width/TextStyle.setWidth() use throughout the
  // SDK's render/format classes. Falls back to 384 (the narrower, more common kiosk width)
  // if the query fails or the printer doesn't report it.
  private fun queryPaperWidthDots(p: Printer): Int {
    return try {
      p.queryApi().getInfo(com.sunmi.printerx.enums.PrinterInfo.PAPER).toIntOrNull() ?: 384
    } catch (e: Exception) {
      384
    }
  }

  override fun definition() = ModuleDefinition {
    Name("SunmiPrinter")

    AsyncFunction("initPrinter") { promise: Promise ->
      try {
        val context: Context = appContext.reactContext ?: throw Exception("Context is null")
        
        PrinterSdk.getInstance().getPrinter(context, object : PrinterListen {
          override fun onDefPrinter(defPrinter: Printer?) {
            printer = defPrinter
            isInitialized = true
            promise.resolve(true)
          }

          override fun onPrinters(printers: MutableList<Printer>?) {
            // Opcional: manejar múltiples impresoras
          }
        })
      } catch (e: Exception) {
        promise.reject("INIT_ERROR", "Error initializing printer: ${e.message}", e)
      }
    }

    AsyncFunction("printText") { text: String, align: String?, bold: Boolean?, promise: Promise ->
      try {
        if (!isInitialized || printer == null) {
          promise.reject("NOT_INITIALIZED", "Printer not initialized. Call initPrinter first.", null)
          return@AsyncFunction
        }

        printer?.let { p ->
          val style = com.sunmi.printerx.style.TextStyle.getStyle().setAlign(alignFromString(align))
          if (bold == true) style.enableBold(true)
          p.lineApi().printText(text, style)
          promise.resolve(null)
        }
      } catch (e: Exception) {
        promise.reject("PRINT_ERROR", "Error printing text: ${e.message}", e)
      }
    }

    AsyncFunction("printTextWithSize") { text: String, size: Int, align: String?, bold: Boolean?, promise: Promise ->
      try {
        if (!isInitialized || printer == null) {
          promise.reject("NOT_INITIALIZED", "Printer not initialized", null)
          return@AsyncFunction
        }

        printer?.let { p ->
          val style = com.sunmi.printerx.style.TextStyle.getStyle()
            .setTextSize(size)
            .setAlign(alignFromString(align))
          if (bold == true) style.enableBold(true)
          p.lineApi().printText(text, style)
          promise.resolve(null)
        }
      } catch (e: Exception) {
        promise.reject("PRINT_ERROR", "Error printing: ${e.message}", e)
      }
    }

    AsyncFunction("lineWrap") { lines: Int, promise: Promise ->
      try {
        if (!isInitialized || printer == null) {
          promise.reject("NOT_INITIALIZED", "Printer not initialized", null)
          return@AsyncFunction
        }

        printer?.let { p ->
          p.lineApi().printDividingLine(com.sunmi.printerx.enums.DividingLine.EMPTY, lines * 30)
          promise.resolve(null)
        }
      } catch (e: Exception) {
        promise.reject("PRINT_ERROR", "Error line wrap: ${e.message}", e)
      }
    }

    // Single-line two-column layout (e.g. item name + price) using LineApi.printTexts,
    // instead of two separate printText calls — real column widths computed from the
    // printer's actual reported paper width rather than a guessed character count.
    AsyncFunction("printRow") { left: String, right: String, boldLeft: Boolean?, promise: Promise ->
      try {
        if (!isInitialized || printer == null) {
          promise.reject("NOT_INITIALIZED", "Printer not initialized", null)
          return@AsyncFunction
        }

        printer?.let { p ->
          val paperWidth = queryPaperWidthDots(p)
          val leftWidth = (paperWidth * 0.6).toInt()
          val rightWidth = paperWidth - leftWidth

          val leftStyle = com.sunmi.printerx.style.TextStyle.getStyle()
            .setAlign(com.sunmi.printerx.enums.Align.LEFT)
            .setWidth(leftWidth)
          if (boldLeft == true) leftStyle.enableBold(true)

          val rightStyle = com.sunmi.printerx.style.TextStyle.getStyle()
            .setAlign(com.sunmi.printerx.enums.Align.RIGHT)
            .setWidth(rightWidth)

          p.lineApi().printTexts(arrayOf(left, right), intArrayOf(leftWidth, rightWidth), arrayOf(leftStyle, rightStyle))
          promise.resolve(null)
        }
      } catch (e: Exception) {
        promise.reject("PRINT_ERROR", "Error printing row: ${e.message}", e)
      }
    }

    // Real ESC/POS-style rule (unlike lineWrap, which is blank spacing) — verified against
    // the printerx 1.0.20 AAR's DividingLine enum (EMPTY/SOLID/DOTTED).
    AsyncFunction("printDivider") { style: String?, promise: Promise ->
      try {
        if (!isInitialized || printer == null) {
          promise.reject("NOT_INITIALIZED", "Printer not initialized", null)
          return@AsyncFunction
        }

        val dividingLine = when (style?.lowercase()) {
          "dotted" -> com.sunmi.printerx.enums.DividingLine.DOTTED
          else -> com.sunmi.printerx.enums.DividingLine.SOLID
        }

        printer?.let { p ->
          p.lineApi().printDividingLine(dividingLine, 30)
          promise.resolve(null)
        }
      } catch (e: Exception) {
        promise.reject("PRINT_ERROR", "Error printing divider: ${e.message}", e)
      }
    }

    // Verified against printerx 1.0.20's QueryApi.getStatus() -> Status enum
    // (READY, ERR_PAPER_OUT, ERR_COVER, OFFLINE, etc.) — returns the raw enum name
    // so JS can branch on it without duplicating the SDK's status list here.
    AsyncFunction("queryPrinterStatus") { promise: Promise ->
      try {
        if (!isInitialized || printer == null) {
          promise.reject("NOT_INITIALIZED", "Printer not initialized", null)
          return@AsyncFunction
        }

        printer?.let { p ->
          val status = p.queryApi().getStatus()
          promise.resolve(status.name)
        }
      } catch (e: Exception) {
        promise.reject("QUERY_ERROR", "Error querying printer status: ${e.message}", e)
      }
    }

    AsyncFunction("cutPaper") { promise: Promise ->
      try {
        if (!isInitialized || printer == null) {
          promise.reject("NOT_INITIALIZED", "Printer not initialized", null)
          return@AsyncFunction
        }

        printer?.let { p ->
          p.lineApi().autoOut()
          promise.resolve(null)
        }
      } catch (e: Exception) {
        promise.reject("PRINT_ERROR", "Error cutting paper: ${e.message}", e)
      }
    }

    AsyncFunction("printBarcode") { data: String, promise: Promise ->
      try {
        if (!isInitialized || printer == null) {
          promise.reject("NOT_INITIALIZED", "Printer not initialized", null)
          return@AsyncFunction
        }

        printer?.let { p ->
          val barcodeStyle = com.sunmi.printerx.style.BarcodeStyle.getStyle()
            .setAlign(com.sunmi.printerx.enums.Align.CENTER)
            .setDotWidth(2)
            .setBarHeight(100)
            .setReadable(com.sunmi.printerx.enums.HumanReadable.POS_TWO)
          p.lineApi().printBarCode(data, barcodeStyle)
          promise.resolve(null)
        }
      } catch (e: Exception) {
        promise.reject("PRINT_ERROR", "Error printing barcode: ${e.message}", e)
      }
    }

    AsyncFunction("printQRCode") { data: String, promise: Promise ->
      try {
        if (!isInitialized || printer == null) {
          promise.reject("NOT_INITIALIZED", "Printer not initialized", null)
          return@AsyncFunction
        }

        printer?.let { p ->
          val qrStyle = com.sunmi.printerx.style.QrStyle.getStyle()
            .setAlign(com.sunmi.printerx.enums.Align.CENTER)
            .setDot(9)
            .setErrorLevel(com.sunmi.printerx.enums.ErrorLevel.L)
          p.lineApi().printQrCode(data, qrStyle)
          promise.resolve(null)
        }
      } catch (e: Exception) {
        promise.reject("PRINT_ERROR", "Error printing QR code: ${e.message}", e)
      }
    }

    AsyncFunction("printImage") { assetName: String, promise: Promise ->
      try {
        if (!isInitialized || printer == null) {
          promise.reject("NOT_INITIALIZED", "Printer not initialized", null)
          return@AsyncFunction
        }

        val context: Context = appContext.reactContext ?: throw Exception("Context is null")
        
        var bitmap: Bitmap? = null
        var inputStream: InputStream? = null
        
        try {
          // Intentar cargar desde assets
          val assetManager = context.assets
          inputStream = assetManager.open(assetName)
          bitmap = BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
          // Si falla, intentar cargar desde drawable/mipmap
          try {
            val resourceId = context.resources.getIdentifier(
              assetName.replace("/", "_").replace(".png", "").replace(".jpg", ""),
              "drawable",
              context.packageName
            )
            if (resourceId != 0) {
              bitmap = BitmapFactory.decodeResource(context.resources, resourceId)
            }
          } catch (e2: Exception) {
            throw Exception("Cannot find image: $assetName. Error: ${e.message}")
          }
        } finally {
          inputStream?.close()
        }

        if (bitmap == null) {
          promise.reject("IMAGE_ERROR", "Failed to load image: $assetName", null)
          return@AsyncFunction
        }

        // Redimensionar si es necesario (max width 200px para impresoras térmicas)
        val maxWidth = 200
        val scaledBitmap = if (bitmap.width > maxWidth) {
          val ratio = maxWidth.toFloat() / bitmap.width.toFloat()
          val newHeight = (bitmap.height * ratio).toInt()
          Bitmap.createScaledBitmap(bitmap, maxWidth, newHeight, true)
        } else {
          bitmap
        }

        printer?.let { p ->
          val imageStyle = com.sunmi.printerx.style.BitmapStyle.getStyle()
            .setAlign(com.sunmi.printerx.enums.Align.CENTER)
          p.lineApi().printBitmap(scaledBitmap, imageStyle)
          promise.resolve(null)
        }

        if (scaledBitmap != bitmap) {
          scaledBitmap.recycle()
        }
        bitmap.recycle()

      } catch (e: Exception) {
        promise.reject("PRINT_ERROR", "Error printing image: ${e.message}", e)
      }
    }

    AsyncFunction("printImageBase64") { base64String: String, promise: Promise ->
      try {
        if (!isInitialized || printer == null) {
          promise.reject("NOT_INITIALIZED", "Printer not initialized", null)
          return@AsyncFunction
        }

        // Decodificar Base64 a bitmap
        val imageBytes = Base64.decode(base64String, Base64.DEFAULT)
        var bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        if (bitmap == null) {
          promise.reject("IMAGE_ERROR", "Failed to decode base64 image", null)
          return@AsyncFunction
        }

        // Convertir transparencia a fondo blanco
        if (bitmap.hasAlpha()) {
          val bitmapWithWhiteBg = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
          val canvas = Canvas(bitmapWithWhiteBg)
          canvas.drawColor(Color.WHITE)
          canvas.drawBitmap(bitmap, 0f, 0f, null)
          bitmap.recycle()
          bitmap = bitmapWithWhiteBg
        }

        // Redimensionar si es necesario (max width 200px para impresoras térmicas)
        val maxWidth = 200
        val scaledBitmap = if (bitmap.width > maxWidth) {
          val ratio = maxWidth.toFloat() / bitmap.width.toFloat()
          val newHeight = (bitmap.height * ratio).toInt()
          Bitmap.createScaledBitmap(bitmap, maxWidth, newHeight, true)
        } else {
          bitmap
        }

        printer?.let { p ->
          val imageStyle = com.sunmi.printerx.style.BitmapStyle.getStyle()
            .setAlign(com.sunmi.printerx.enums.Align.CENTER)
          p.lineApi().printBitmap(scaledBitmap, imageStyle)
          promise.resolve(null)
        }

        if (scaledBitmap != bitmap) {
          scaledBitmap.recycle()
        }
        bitmap.recycle()

      } catch (e: Exception) {
        promise.reject("PRINT_ERROR", "Error printing image from base64: ${e.message}", e)
      }
    }
  }
}
