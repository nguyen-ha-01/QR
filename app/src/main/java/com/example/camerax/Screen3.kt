package com.example.camerax

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.camerax.network.P2pReceiver
import kotlinx.coroutines.delay

@Composable
fun Screen3(){
    Surface() {
        var peers = remember{ mutableStateListOf<WifiP2pDevice>()}
        val context = LocalContext.current
        val activity = context as MainActivity
        val p2pManager = context.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
        val p2pChannel = p2pManager.initialize(context,activity.mainLooper,null)
        val p2pReceiver : P2pReceiver= P2pReceiver(p2pManager,p2pChannel,activity)
        LaunchedEffect(p2pReceiver.devices.size){
            peers.clear()
            peers.addAll(p2pReceiver.devices)
        }
        val intentFilter = IntentFilter().apply {
            addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION )
            addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
            addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
            addAction( WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
        }
        try{
            context.registerReceiver(p2pReceiver,intentFilter)
            Log.d("register","success")
        }catch (e:Exception){
            Log.d("register",e.toString())
        }
        Column(Modifier.fillMaxSize()) {
            DiscoverPeer(p2pManager = p2pManager, channel = p2pChannel)
            ShowPeers(peers)
            Text("?")
            val dv = WifiP2pDevice().apply {
                this.deviceName = "iphone"

            }
            Button(onClick = { peers.add(dv)}) {

            }
            Button(onClick = { context.unregisterReceiver(p2pReceiver)}) {
                Text("unregister")
            }
        }

    }
}
@Composable
fun DiscoverPeer(p2pManager: WifiP2pManager,channel : WifiP2pManager.Channel) {
    val context = LocalContext.current
    var show  by remember {
        mutableStateOf(false)
    }
    var content  by remember {
        mutableStateOf("")
    }
    AnimatedVisibility(visible = show) {
        ShowDiscoverResult(content = content)
    }
    LaunchedEffect(key1 = show){
        delay(1000)
        show=false
        content = ""
    }
    val hasPermission = remember {
        ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }
    if (hasPermission != PackageManager.PERMISSION_GRANTED
    ) {
        val permission = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission(),
            onResult = {

            })
        LaunchedEffect(key1 = true){permission.launch(Manifest.permission.ACCESS_FINE_LOCATION)}

    }


    Button(onClick = {
        p2pManager.discoverPeers(channel,object :WifiP2pManager.ActionListener{
            override fun onSuccess() {
                show = true
                content = "Success"
            }

            override fun onFailure(p0: Int) {
                show = true
                content = "Failure"
            }
        })

    }) {
        Text("Discover")
    }
}
@Composable
fun ShowDiscoverResult(content: String ){
    Snackbar(modifier = Modifier.padding(4.dp)){
        Text(text = content)
    }
}
@Composable
fun ShowPeers(listDevices : SnapshotStateList<WifiP2pDevice>){
        LazyColumn(){
            items(listDevices){item->
                Card() {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)) {
                        Text(text = item.deviceName)
                    }
                }

            }
        }
    if(listDevices.isEmpty()){
        Text("no device found")
    } else{
        Text("somethings wrongs")
    }
    }
