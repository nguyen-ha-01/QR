package com.example.camerax

import android.graphics.*
import android.graphics.ImageFormat.*
import android.media.Image
import android.util.Log
import android.util.Size
import androidx.camera.camera2.internal.compat.quirk.DeviceQuirks.get
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.core.graphics.BitmapCompat
import com.google.zxing.*
import com.google.zxing.common.BitMatrix
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeReader
import com.google.zxing.qrcode.decoder.Decoder
import java.nio.Buffer
import java.nio.ByteBuffer
import kotlin.random.Random

class Analyzes(val onQrCodeScanned: (String) -> Unit):ImageAnalysis.Analyzer {
    private val supportedImageFormats = listOf(
        ImageFormat.YUV_420_888,
        ImageFormat.YUV_422_888,
        ImageFormat.YUV_444_888,
    )
    override fun analyze(image: ImageProxy) {
        if(image.format in supportedImageFormats) {
            val bytes = image.planes.first().buffer.toByteArray()
            Log.i("checkingByteArr","bufferedSize: "+bytes.size.toString())
            Log.i("checkingByteArr","detail: "+bytes.first().toString())
            val source = PlanarYUVLuminanceSource(bytes,image.width,image.height,0,0,image.width,image.height,false)
            val binaryBmp = BinaryBitmap(HybridBinarizer(source))
            try {
                val result = MultiFormatReader().apply {
                    setHints(mapOf(DecodeHintType.POSSIBLE_FORMATS to arrayListOf(BarcodeFormat.QR_CODE)))}
                    .decode(binaryBmp)
                onQrCodeScanned(result?.text?:"scanning")
                Log.e("res",result?.text?:"")
            } catch(e: Exception) {
                e.printStackTrace()
                Log.e("code","scanning")
            } finally {
                image.close()
            }
        }
    }

    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()
        val byteArray = ByteArray(remaining())
        get(byteArray)
        return byteArray
    }
    fun bitMatrixSee(mt:BitMatrix):Bitmap{
        val pixels = IntArray(mt.width*mt.height)
        val w = mt.width
        val h = mt.height
        val bitmap = Bitmap.createBitmap(300,300,Bitmap.Config.ARGB_8888)
        for (row in 0..w)
            for (col in 0..h){
                pixels[row+ row*col] =if(mt[row,col]) Color.GREEN else Color.WHITE
            }
        bitmap.setPixels(pixels,0,300,0,0,300,300)
        return bitmap
    }
}