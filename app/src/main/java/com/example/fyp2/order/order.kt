package com.example.fyp2.order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fyp2.R
import com.example.fyp2.order.orderHelp.orderHelp
import com.example.fyp2.order.orderRequestRecord.orderRequestRecord
import com.example.fyp2.order.orderSearch.orderSearch
import com.example.fyp2.order.orderUpload.orderUpload
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_order.*

class order : AppCompatActivity() {

    companion object{

        val CURRENT_USER = "CURRENT_USER"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        val ref = FirebaseAuth.getInstance()

        order_search.setOnClickListener {

            val intent = Intent(this, orderSearch::class.java)
            intent.putExtra(CURRENT_USER, ref.uid)
            startActivity(intent)

        }

        order_request.setOnClickListener {

            val intent = Intent(this, orderRequestRecord::class.java)
            intent.putExtra(CURRENT_USER, ref.uid)
            startActivity(intent)

        }

        order_upload.setOnClickListener {

            val intent = Intent(this, orderUpload::class.java)
            intent.putExtra(CURRENT_USER, ref.uid)
            startActivity(intent)

        }

        order_help.setOnClickListener {

            val intent = Intent(this, orderHelp::class.java)
            intent.putExtra(CURRENT_USER, ref.uid)
            startActivity(intent)

        }

    }
}
