package com.example.camerax

import android.Manifest
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.p2p.WifiP2pManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.camerax.network.P2pReceiver
import com.example.camerax.ui.theme.CameraXTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CameraXTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "Screen3"){
                    composable("Screen1"){
                        Screen1(navController)
                    }
                    composable("Screen2"){
                        Screen2()
                    }
                    composable("Screen3"){
                        Screen3()
                    }
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

//
//import android.Manifest
//import android.content.pm.PackageManager
//import android.graphics.Bitmap
//import android.os.Bundle
//import android.util.Size
//import android.widget.ImageView
//import android.widget.Toast
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.compose.setContent
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.camera.core.CameraSelector
//import androidx.camera.core.ImageAnalysis
//import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
//import androidx.camera.core.impl.ImageAnalysisConfig
//import androidx.camera.lifecycle.ProcessCameraProvider
//import androidx.camera.view.PreviewView
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalLifecycleOwner
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.compose.ui.viewinterop.AndroidView
//import androidx.core.content.ContextCompat
//import androidx.core.view.size
//import com.example.camerax.ui.theme.CameraXTheme
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            CameraXTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colors.background
//                ) {
//                    Camera()
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun Greeting(name: String) {
//    Text(text = "Hello $name!")
//}
//
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    CameraXTheme {
//        Greeting("Android")
//    }
//}
//
//@Composable
//fun Camera(){
//    PermissionGet(permission = Manifest.permission.CAMERA)
//
//    val context = LocalContext.current
//    val lifecycleOwner = LocalLifecycleOwner.current
//    val cameraFeature =remember{ ProcessCameraProvider.getInstance(context)}
//    var bitmapIlu:Bitmap? =null
//    var code by remember {
//        mutableStateOf("_")
//    }
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        AndroidView(
//            factory = { context ->
//                val previewView = PreviewView(context)
//                val preview = androidx.camera.core.Preview.Builder().build()
//                val selector = CameraSelector.Builder()
//                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
//                    .build()
//                preview.setSurfaceProvider(previewView.surfaceProvider)
//                val imageAnalysis = ImageAnalysis.Builder()
//                    .setTargetResolution(
//                        Size(
//                            previewView.width,
//                            previewView.height
//                        )
//                    )
//                    .setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)
//                    .build()
//                imageAnalysis.setAnalyzer(
//                    ContextCompat.getMainExecutor(context),
//                    Analyzes { result ->
//                        code = result
//                    }
//                )
//                try {
//                    cameraFeature.get().bindToLifecycle(
//                        lifecycleOwner,
//                        selector,
//                        preview,
//                        imageAnalysis
//                    )
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//                previewView
//            },
//            modifier = Modifier.weight(1f)
//        )
//        Text(
//            text = code,
//            fontSize = 20.sp,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(32.dp)
//        )
//    }
////
////        AndroidView(factory = { context ->
////
////            val previewView =  PreviewView(context)
////
////
////            val preview = androidx.camera.core.Preview.Builder().build()
////            val cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
////
////            preview.setSurfaceProvider(previewView.surfaceProvider)
////            val analyzes = ImageAnalysis.Builder()
////                .setTargetResolution(Size(previewView.width,previewView.height))
////                .setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)
////                .build()
////            analyzes.setAnalyzer(ContextCompat.getMainExecutor(context),Analyzes{
////                    text->
////                    value_qr = text?:"nothing"
////            })
////            cameraFeature.addListener({
////                val camera= cameraFeature.get()
////                try{camera.unbindAll()
////                    camera.bindToLifecycle(lifecycleOwner, cameraSelector,preview,analyzes)}
////                catch (e:Exception){
////
////                }
////            }, ContextCompat.getMainExecutor(context))
////            previewView
////        },Modifier.fillMaxHeight(0.5f))
////        Card(Modifier.padding(16.dp,8.dp).fillMaxWidth()) {
////            Row() {
////                Text(value_qr)
////            }
////        }
//    }
//
//@Composable
//fun PermissionGet(permission: String){
//    val context= LocalContext.current
//    var permissionStatus by remember {
//        mutableStateOf(ContextCompat.checkSelfPermission(context,permission)== PackageManager.PERMISSION_GRANTED)
//    }
//    val launcher= rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission(), onResult ={
//        permissionStatus = it} )
//    if(permissionStatus!= true){
//        LaunchedEffect(key1 = true, block = {
//            launcher.launch(permission)
//        })
//    }
//
//}