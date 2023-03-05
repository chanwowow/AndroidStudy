package com.example.rcthelper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private lateinit var buttonA:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        // Example of a call to a native method
        buttonA =findViewById(R.id.startbutton) // 전면

        buttonA.setOnClickListener{
            val intent = Intent(this, FlightToggle::class.java)
            intent.putExtra("value", 0)  // 이 부분에서 toggle 간격을 조절해서 보내주자

            startActivity(intent)
        }
    }
}