package com.example.rcthelper

import android.provider.Settings
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
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
import android.telephony.ims.ImsReasonInfo
import android.util.Log
import android.widget.Button
import android.widget.Toast
import android.widget.ToggleButton
import androidx.annotation.RequiresApi
import kotlinx.coroutines.delay


class FlightToggle : AppCompatActivity() {

    private lateinit var printOnScreen: TextView
    private lateinit var telephonyManager: TelephonyManager
    private lateinit var telephonyManager2: TelephonyManager

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_toggle)

        printOnScreen=findViewById(R.id.text_network)


        val checkInterval = intent.getIntExtra("value", 0) // Main Acticity에서 체크간격 받음

        // Get the TelephonyManager
        telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        registerTelephonyCallback(telephonyManager)

        // 비행기모드 토글버튼
        val toggleAirplaneModeButton =findViewById<ToggleButton>(R.id.toggleButton)
        toggleAirplaneModeButton.setOnClickListener{
            toggleAirplaneMode()
        }

        //setAirplaneMode(true)
        //setAirplaneMode(false)
    }
    // 아래 코드 reuse 가능하게 바꿔야할듯
    private fun registerTelephonyCallback(telephonyManager: TelephonyManager){
        telephonyManager.registerTelephonyCallback(
            mainExecutor,
            object : TelephonyCallback(), DataConnectionStateListener {
                override fun onDataConnectionStateChanged(state: Int, networkType: Int) {
                    printOnScreen.text ="Changing Something!!!!!####"

                    when (state) {
                        TelephonyManager.DATA_CONNECTED -> {
                            printOnScreen.text ="Connected CHANWOO"
                        }
                        TelephonyManager.DATA_DISCONNECTED -> {
                            printOnScreen.text ="Disconnected CHanwoo"
                        }
                        TelephonyManager.DATA_SUSPENDED -> {
                            printOnScreen.text ="Suspended CHanwoo"
                        }
                    }
                }
            })
    }

    // IMS Call drop 감지 시도
//    private val imsCallDisconnectListener = object : TelephonyCallback.ImsCallDisconnectCauseListener() {
//        override fun onImsCallDisconnectCauseChanged(imsReasonInfo: ImsReasonInfo?) {
//            super.onImsCallDisconnectCauseChanged(imsReasonInfo)
//            Log.d("IMS_CALL", "IMS call disconnected with reason: ${imsReasonInfo?.code}")
//            // add your own logic here to handle the IMS call disconnect cause
//        }
//    }


    private fun toggleAirplaneMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val settingsPanelIntent = Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS)
            startActivityForResult(settingsPanelIntent, 0)
        } else {
            val isAirplaneModeOn = Settings.Global.getInt(contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) != 0
            Settings.Global.putInt(contentResolver, Settings.Global.AIRPLANE_MODE_ON, if (isAirplaneModeOn) 0 else 1)
           // Toast.makeText(this, "Airplane mode has been toggled", Toast.LENGTH_SHORT).show()
        }
    }

}

