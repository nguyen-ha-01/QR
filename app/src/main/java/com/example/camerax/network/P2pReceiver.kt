package com.example.camerax.network

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.core.app.ActivityCompat
import com.example.camerax.MainActivity

class P2pReceiver(val p2pManager: WifiP2pManager,val  p2pChannel : WifiP2pManager.Channel,val activity: MainActivity):BroadcastReceiver() {
    var devices =  mutableStateListOf<WifiP2pDevice>()

//    var devices : MutableList<WifiP2pDevice> = mutableListOf()
    val dv = WifiP2pDevice().apply {
        this.deviceName = "iphone"

    }
    override fun onReceive(ctx: Context?, intent: Intent?) {
        when(intent?.action){
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,-1)
                when(state){
                    WifiP2pManager.WIFI_P2P_STATE_ENABLED->{
                        devices.add(dv)
                        Log.i("wifi","wifi is on now")
                    }
                    else ->{
                        devices.add(dv)
                        Log.i("wifi", "wifi is off now")
                    }
                }

            }
            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                Log.i("peer_change","has change")
                if (ActivityCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    p2pManager.requestPeers(p2pChannel){ changeList->
                        Toast.makeText(activity, changeList.deviceList.size.toString(),Toast.LENGTH_SHORT).show()
                        if (!changeList.deviceList.equals(devices)){
                            devices.clear()
                            devices.addAll(changeList.deviceList)
                        }else{
                            return@requestPeers
                        }
                    }
                    Log.d("devices","peer requested")
                }


            }
            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {
                // Respond to new connection or disconnections
            }
            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {
                // Respond to this device's wifi state changing
            }
            else ->{}
        }
    }
}