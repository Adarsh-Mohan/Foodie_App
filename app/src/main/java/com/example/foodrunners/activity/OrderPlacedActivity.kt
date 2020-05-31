package com.example.foodrunners.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import com.example.foodrunners.R

class OrderPlacedActivity : AppCompatActivity() {

lateinit var btnOk: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.order_placed_dialog)

        btnOk=findViewById(R.id.btnOK)

        btnOk.setOnClickListener {

            val startAct= Intent(this@OrderPlacedActivity,
                MainActivity::class.java)
            startActivity(startAct)
            finish()
        }
    }
}
