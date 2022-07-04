package com.example.fyp2.Supply

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fyp2.ChatLogActivity.SearchUser
import com.example.fyp2.Class.LatestMessage
import com.example.fyp2.MAP.MapProductInformation
import com.example.fyp2.R
import com.example.fyp2.Search.RequestDisplayMapsActivity
import com.example.fyp2.menu.message_page
import kotlinx.android.synthetic.main.activity_supply.*

class supplyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supply)

        Upload_button_Supply.setOnClickListener {
            val intent = Intent(this, MapProductInformation::class.java)
            startActivity(intent)
        }

        offerHelp_button_Supply.setOnClickListener{

            val intent = Intent(this, RequestDisplayMapsActivity::class.java)
            startActivity(intent)

        }

    }


}
