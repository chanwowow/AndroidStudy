package com.example.rcthelper

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import android.util.Log
import androidx.annotation.RequiresApi

class FlightToggle : AppCompatActivity() {

    private lateinit var telephonyManager: TelephonyManager
    private lateinit var telephonyCallback: TelephonyCallback
    private lateinit val test: Boolean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_toggle)

        mutable, immutable
        primitive, reference
        객체가 뭘까;
        garbage collector;
        lateinit, var, val;
        data class
        null 안정성
        test = true

        val checkInterval = intent.getIntExtra("value", 0) // Main Acticity에서 체크간격 받음

        // Get the TelephonyManager
        telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager

        // Create a TelephonyCallback to monitor data connection status
        telephonyCallback = @RequiresApi(Build.VERSION_CODES.S)
        object : TelephonyCallback() {
            override fun onDataConnectionStateChanged(state: Int, networkType: Int) {
                super.onDataConnectionStateChanged(state, networkType)

                when (state) {
                    TelephonyManager.DATA_CONNECTED -> Log.d(TAG, "Data connection is connected")
                    TelephonyManager.DATA_DISCONNECTED -> Log.d(TAG, "Data connection is disconnected")
                    TelephonyManager.DATA_SUSPENDED -> Log.d(TAG, "Data connection is suspended")
                    else -> Log.d(TAG, "Unknown data connection state")
                }
            }
        }


    }

}

class secondClass : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
}