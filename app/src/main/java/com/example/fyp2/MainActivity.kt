package com.example.fyp2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar1)

        loginButton.setOnClickListener {
            startActivity(Intent(this, login::class.java))
        }

        registrationButton.setOnClickListener {
            startActivity(Intent(this, registration::class.java))
        }
    }
    }
