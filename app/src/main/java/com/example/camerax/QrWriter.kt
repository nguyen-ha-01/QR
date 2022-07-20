package com.example.camerax

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.applyCanvas
import com.google.zxing.BarcodeFormat
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter



class QrWriter{
    var width: Int = 800
    var height: Int  = 800
    public fun setSize(w:Int,h:Int){
        width = w
        height = h
    }

    private fun StringToBitMatrix(content: String,width: Int ,height: Int ): BitMatrix{
       return QRCodeWriter().encode(content,BarcodeFormat.QR_CODE,width,height)

    }
    private fun convertBitMatrixToBitmap(mtx:BitMatrix):Bitmap{
       val bitmapHeight = mtx.height
       val bitmapWidth = mtx.width

        val pixels = IntArray(bitmapWidth*bitmapHeight)
        for (y in 0 until bitmapHeight) {
            for (x in 0 until bitmapWidth) {
                pixels[y * bitmapWidth + x] = if (mtx.get(x, y)) Color.GREEN else Color.WHITE
            }
        }
        val bitmap  = Bitmap.createBitmap(bitmapWidth,bitmapHeight,Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels,0,bitmapWidth,0,0,bitmapWidth,bitmapHeight)
        return bitmap

    }
    private fun bitmapToImageBitmap(bm:Bitmap):ImageBitmap{
        return bm.asImageBitmap()
    }
    fun convert(content: String):Bitmap{
        val bitMatrix = StringToBitMatrix(content,width, height)
        return convertBitMatrixToBitmap(bitMatrix)
    }
    fun overlayLogo(bm:Bitmap,logo:Bitmap):Bitmap{
        val logoPosW = bm.width/2-logo.width
        val logoPosH = bm.height/2-logo.height
        val result  = Bitmap.createBitmap(bm.width,bm.height,bm.config)
        val canvas = Canvas(result)
        canvas.drawBitmap(bm, Matrix(),null)
        canvas.drawBitmap(logo,logoPosW.toFloat(),logoPosH.toFloat(),null)
        return  result
    }

}