package com.example.fyp2.order.orderSearch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fyp2.DashboardActivity
import com.example.fyp2.R
import com.example.fyp2.order.order
import com.example.fyp2.order.orderUpload.orderUploadClass
import com.example.fyp2.order.orderUpload.orderUpload_database
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_order_search.*

class orderSearch : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_search)

        orderInformation()

        back_to_home_page_btn_orderSearch.setOnClickListener {

            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)

        }

    }

    val adapter = GroupAdapter<GroupieViewHolder>()

    private fun orderInformation(){
        val ref = FirebaseDatabase.getInstance().getReference("/Order-Accept-Record")

        ref.addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {

                p0.children.forEach {

                   val productSearch = it.getValue(orderUpload_database::class.java) ?: return

                   val currentUser = intent.getStringExtra(order.CURRENT_USER)

                    if (currentUser == productSearch.uid) {
                        adapter.add(orderSearchClass(productSearch))

                    }
                    recyclerView_orderSearch.adapter = adapter
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })


    }
}
