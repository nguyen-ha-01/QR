package com.example.camerax.network

import android.content.Context
import android.net.wifi.WifiManager

class wifiMngx(val context: Context) {
    lateinit var wifiManager : WifiManager
    init {
        wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }
    fun setWifiStatus(status: Boolean) ={ wifiManager.isWifiEnabled = status}

}