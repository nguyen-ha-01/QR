package com.example.camerax

import android.util.Log
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.Result
import com.google.zxing.qrcode.QRCodeReader

class QrcodeReader {
    fun read(image: BinaryBitmap):Result?{
        try {
            val reader = QRCodeReader().decode(image, mapOf(DecodeHintType.POSSIBLE_FORMATS to arrayListOf<BarcodeFormat>(BarcodeFormat.QR_CODE)))
            return reader
        }catch (e:Exception){
            Log.e("reader","reader wrong and return null")
            e.printStackTrace()
            return null
        }
    }
}