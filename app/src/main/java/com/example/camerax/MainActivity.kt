package com.example.camerax

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.camerax.ui.theme.CameraXTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CameraXTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Camera()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CameraXTheme {
        Greeting("Android")
    }
}

@Composable
fun Camera(){
    PermissionGet(permission = Manifest.permission.CAMERA)
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraFeature =remember{ ProcessCameraProvider.getInstance(context)}
    AndroidView(factory = { context ->

       val previewView =  PreviewView(context)

        var preview = androidx.camera.core.Preview.Builder().build()

        preview.setSurfaceProvider(previewView.surfaceProvider)

        cameraFeature.addListener({
            val camera= cameraFeature.get()
            try{camera.unbindAll()
            camera.bindToLifecycle(lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA,preview)}
            catch (e:Exception){

            }
        }, ContextCompat.getMainExecutor(context))
        previewView
    },Modifier.fillMaxSize())
}
@Composable
fun PermissionGet(permission: String){
    val context= LocalContext.current
    var permissionStatus by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context,permission)== PackageManager.PERMISSION_GRANTED)
    }
    val launcher= rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission(), onResult ={
        permissionStatus = it} )
    if(permissionStatus!= true){
        LaunchedEffect(key1 = true, block = {
            launcher.launch(permission)
        })
    }

}