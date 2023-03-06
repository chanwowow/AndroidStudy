package com.example.rcthelper

import android.provider.Settings
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.TextView
import android.os.PersistableBundle
import android.provider.Telephony
import android.telephony.TelephonyCallback
import android.telephony.TelephonyCallback.DataConnectionStateListener
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Button
import androidx.annotation.RequiresApi


class FlightToggle : AppCompatActivity() {

    private lateinit var printOnScreen: TextView
    private lateinit var telephonyManager: TelephonyManager
    private lateinit var telephonyCallback: TelephonyCallback

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_toggle)

        printOnScreen=findViewById(R.id.text_network)


        val checkInterval = intent.getIntExtra("value", 0) // Main Acticity에서 체크간격 받음

        // Get the TelephonyManager
        telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager

        // Create a TelephonyCallback to monitor data connection status
        telephonyCallback = @RequiresApi(Build.VERSION_CODES.S)
        object : TelephonyCallback(), DataConnectionStateListener {
            override fun onDataConnectionStateChanged(state: Int, networkType: Int) {
                when (state) {
                    TelephonyManager.DATA_CONNECTED -> printOnScreen.text ="Connected CHANWOO"
                    TelephonyManager.DATA_CONNECTING -> printOnScreen.text ="Connecting CHanwoo"
                    TelephonyManager.DATA_DISCONNECTED ->printOnScreen.text ="Disconnected CHanwoo"
                }
            }
        }
        setAirplaneMode(true)
        //setAirplaneMode(false)
    }

    private fun setAirplaneMode(enable : Boolean) {
        Settings.System.putInt(
            contentResolver,
            Settings.Global.AIRPLANE_MODE_ON,
            if (isEnabled) 0 else 1
        )

        // Send broadcast to notify the system that airplane mode has changed
        val intent =  Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        intent.putExtra("state", enable)
        sendBroadcast(intent)

        // Prompt the user to grant the "WRITE_SETTINGS" permission
        if( !Settings.System.canWrite(this)){ // 시스템쓰기 권한이 없다면
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.data = Uri.parse("package:$packageName")
            startActivityForResult(intent,0)
        }

    }

}

