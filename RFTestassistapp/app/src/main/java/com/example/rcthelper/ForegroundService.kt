package com.example.rcthelper

import android.app.*
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.telephony.ServiceState
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ForegroundService : Service() {

    private val CHANNEL_ID = "ForgroundServiceChannel"
    private lateinit var printOnScreen: TextView
    private lateinit var telephonyManager: TelephonyManager

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()

        printOnScreen=findViewById(R.id.text_network) /// 이거 appcompatactivity에 있는 놈인데 여기서 바로 출력 말고 다른곳으로 데이터 보내서 거기서 출력해야하는 듯하다

        telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        registerTelephonyCallback(telephonyManager)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        // Set up a notification to keep the service running in the foreground
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,PendingIntent.FLAG_IMMUTABLE)
        val notification = Notification.Builder(this, "default")
            .setContentTitle("RCT Helper")
            .setContentText("실행중")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setChannelId(CHANNEL_ID)
            .build()

        // 서비스 시작
        startForeground( 1, notification)
        return START_STICKY
    }

    private fun createNotificationChannel(){
        val serviceChannel = NotificationChannel(
            CHANNEL_ID, "Foreground Service Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager=getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(serviceChannel)
    }


    // 이 부분 reuse 방법 생각해보자
    private fun registerTelephonyCallback(telephonyManager: TelephonyManager){
        telephonyManager.registerTelephonyCallback(
            mainExecutor,
            object : TelephonyCallback(), TelephonyCallback.ServiceStateListener {
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
                        ServiceState.STATE_POWER_OFF->{
                            printOnScreen.text="비행기모드 상태"
                        }
                        else->{
                            printOnScreen.text="INSERT SIM"
                        }
                    }
                }
            })

    }

    override fun onDestroy() {
        super.onDestroy()
            ///
    }
}