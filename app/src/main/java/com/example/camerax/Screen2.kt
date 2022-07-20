package com.example.camerax

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap

@Composable
fun Screen2(){
    val context = LocalContext.current
    var content by remember {
        mutableStateOf("")
    }
    var check  by remember {
        mutableStateOf(false)
    }
    Surface() {
        Column() {
            Card(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column() {
                    var str by remember {
                        mutableStateOf("")
                    }
                    TextField(value = str, onValueChange = { chane -> str = chane },
                        label = { Text("Type something to convert...") }, maxLines = 3)
                    Button(onClick = { content = str
                    check = true}) {
                        Text("generate")
                    }
                }
            }
            if(check){
                val qrWriter = QrWriter()
                val source = qrWriter.convert(content)
                val logo = context.getDrawable(R.drawable.coffee)!!.toBitmap(20,20, Bitmap.Config.ARGB_8888)
                val finalQRCode = qrWriter.overlayLogo(source,logo)

                Image(bitmap = finalQRCode.asImageBitmap(), contentDescription ="" )
            }
//            Image(painter = painterResource(id = R.drawable.coffee), contentDescription = null)
        }
    }
}

@Composable
fun SetContentText(set: (String)->Unit) {

}


