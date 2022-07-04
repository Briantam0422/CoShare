package com.example.fyp2.order.orderUpload

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fyp2.DashboardActivity
import com.example.fyp2.MAP.ProductInformation
import com.example.fyp2.R
import com.example.fyp2.order.order
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_order_search.*
import kotlinx.android.synthetic.main.activity_order_upload.*

class orderUpload : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_upload)

        orderInfromation()

        back_to_home_page_btn.setOnClickListener {

            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }

    }

    val adapter = GroupAdapter<GroupieViewHolder>()
    private fun orderInfromation(){

        val ref = FirebaseDatabase.getInstance().getReference("/ProductInformation")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {

                p0.children.forEach {

                    val product = it.getValue(ProductInformation::class.java) ?: return

                    val provider = product.uid
                    val currentUser = intent.getStringExtra(order.CURRENT_USER)
                    if (provider == currentUser) {

                        adapter.add(orderUploadClass(product))

                    }

                    recyclerView_orderUpload.adapter = adapter
                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })

    }

}
