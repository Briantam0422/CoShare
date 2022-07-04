package com.example.fyp2.order.orderRequestRecord

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fyp2.DashboardActivity
import com.example.fyp2.MAP.ProductInformation
import com.example.fyp2.R
import com.example.fyp2.Search.requestProductInformation
import com.example.fyp2.order.order
import com.example.fyp2.order.orderUpload.orderUploadClass
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_order_request_record.*
import kotlinx.android.synthetic.main.activity_order_search.*

class orderRequestRecord : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_request_record)

        orderInfromation()

        back_to_home_page_btn_requestOrderSearch.setOnClickListener {

            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)

        }

    }

    val adapter = GroupAdapter<GroupieViewHolder>()
    private fun orderInfromation(){

        val ref = FirebaseDatabase.getInstance().getReference("/Request-Product_Information")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {

                p0.children.forEach {

                    val product = it.getValue(requestProductInformation::class.java) ?: return

                    val provider = product.uid
                    val currentUser = intent.getStringExtra(order.CURRENT_USER)
                    if (provider == currentUser) {

                        adapter.add(orderRequestRecordClass(product))

                    }

                    order_request_record.adapter = adapter
                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })

    }

}
