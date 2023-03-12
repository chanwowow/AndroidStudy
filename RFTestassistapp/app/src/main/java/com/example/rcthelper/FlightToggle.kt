package com.example.rcthelper

import android.provider.Settings
import android.annotation.SuppressLint
import android.content.*
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.net.Uri
import android.view.View
import android.os.PersistableBundle
import android.provider.Telephony
import android.telephony.ServiceState
import android.telephony.TelephonyCallback
import android.telephony.TelephonyCallback.DataConnectionStateListener
import android.telephony.TelephonyCallback.ImsCallDisconnectCauseListener
import android.telephony.TelephonyCallback.ServiceStateListener
import android.telephony.TelephonyManager
import android.telephony.ims.ImsReasonInfo
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay


class FlightToggle : AppCompatActivity() {

    private lateinit var printOnScreen: TextView
    private lateinit var BCR : BroadcastReceiver

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_toggle)

        printOnScreen=findViewById(R.id.text_network)

        BCR = object :BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent != null) {
                    if(intent.hasExtra("serviceState")){
                        val serviceState=intent?.getStringExtra("serviceState")
                        printOnScreen.text=serviceState
                    }
                }
            }
        }


        val checkInterval = intent.getIntExtra("value", 0) // Main Acticity에서 체크간격 받음

        val serviceIntent= Intent(this, ForegroundService::class.java)
        // 서비스 시작 버튼
        val toggleServiceButton = findViewById<ToggleButton>(R.id.serviceButton)
        toggleServiceButton.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                ContextCompat.startForegroundService(this,serviceIntent)
                Toast.makeText(this,"Service start...",Toast.LENGTH_SHORT).show()
            }else{
                // close는 어케하지???
                Toast.makeText(this,"Service End!",Toast.LENGTH_SHORT).show()
            }

        }

        // #2#  비행기모드 토글버튼
        val setAirplaneModeButton =findViewById<Button>(R.id.flightButton)
        setAirplaneModeButton.setOnClickListener{
            toggleAirplaneMode()
        }

    }

    // 아래 코드 reuse 가능하게 바꿔야할듯

    ///#2# 비행기토글. API17 이후로 막혔다. 간접방법 써야한다!
    private fun toggleAirplaneMode() {
        val settingsPanelIntent = Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS)
        startActivityForResult(settingsPanelIntent, 0)
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(BCR, IntentFilter("PhoneServiceStateBCR"))
    }

    override fun onDestroy() {
        super.onDestroy()

    }

}


