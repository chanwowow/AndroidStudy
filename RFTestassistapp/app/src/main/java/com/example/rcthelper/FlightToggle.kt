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
import android.telephony.ServiceState
import android.telephony.TelephonyCallback
import android.telephony.TelephonyCallback.DataConnectionStateListener
import android.telephony.TelephonyCallback.ImsCallDisconnectCauseListener
import android.telephony.TelephonyCallback.ServiceStateListener
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

    //private var outOfServiceTime: Long?=null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_toggle)

        printOnScreen=findViewById(R.id.text_network)


        val checkInterval = intent.getIntExtra("value", 0) // Main Acticity에서 체크간격 받음

        // Get the TelephonyManager
        telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        registerTelephonyCallback(telephonyManager)

        // #2#  비행기모드 토글버튼
        val toggleAirplaneModeButton =findViewById<ToggleButton>(R.id.toggleButton)
        toggleAirplaneModeButton.setOnClickListener{
            toggleAirplaneMode()
        }

    }

    // 아래 코드 reuse 가능하게 바꿔야할듯



    private fun registerTelephonyCallback(telephonyManager: TelephonyManager){
        telephonyManager.registerTelephonyCallback(
            mainExecutor,
            object : TelephonyCallback(), ServiceStateListener {
                override fun onServiceStateChanged(serviceState: ServiceState) {
                    when (serviceState.state) {
                        ServiceState.STATE_IN_SERVICE->{
                            printOnScreen.text = "서비스 중 ..."
                            //outOfServiceTime=null
                        }
                        ServiceState.STATE_OUT_OF_SERVICE->{
                            printOnScreen.text = "OUT OF SERVICE XXXX"
                        }
                        ServiceState.STATE_EMERGENCY_ONLY->{
                            printOnScreen.text = "EMERGENCY ONLY"
                            //outOfServiceTime=null
                        }
                    }
                }
            })

    }

    // #1. 텔레포니 onDataConnectionStateChanged 버전
//    private fun registerTelephonyCallback(telephonyManager: TelephonyManager){
//        telephonyManager.registerTelephonyCallback(
//            mainExecutor,
//            object : TelephonyCallback(), DataConnectionStateListener {
//                override fun onDataConnectionStateChanged(state: Int, networkType: Int) {
//                    printOnScreen.text ="########@@!@@"
//
//                    when (state) {
//                        TelephonyManager.DATA_CONNECTED -> {
//                            printOnScreen.text ="Connected"
//                        }
//                        TelephonyManager.DATA_DISCONNECTED -> {
//                            printOnScreen.text ="Disconnected"
//                        }
//                        TelephonyManager.DATA_SUSPENDED -> {
//                            printOnScreen.text ="Suspended"
//                        }
//                        TelephonyManager.DATA_UNKNOWN -> {
//                            printOnScreen.text ="Unknown"
//                        }
//                        TelephonyManager.DATA_CONNECTING -> {
//                            printOnScreen.text ="CONNECTING"
//                        }
//                        TelephonyManager.DATA_DISCONNECTING -> {
//                            printOnScreen.text ="DISCONNECTING"
//                        }
//                        TelephonyManager.DATA_HANDOVER_IN_PROGRESS-> {
//                            printOnScreen.text ="HANDOVER! "
//                        }
//                    }
//                }
//            })
//
//    }

    ///#2# 비행기토글. API17 이후로 막혔다. 간접방법 써야한다!
    private fun toggleAirplaneMode() {
        val settingsPanelIntent = Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS)
        startActivityForResult(settingsPanelIntent, 0)
    }


}


