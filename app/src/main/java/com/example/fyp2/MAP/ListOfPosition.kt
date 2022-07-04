package com.example.fyp2.MAP

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp2.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_list_of_position.*

class ListOfPosition : AppCompatActivity() {

    val adapter = GroupAdapter<GroupieViewHolder>()
    lateinit var product: ProductInformation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_position)



        Productlist()

    //    adapter.add(listofpositionCalss())
    //    adapter.add(listofpositionCalss())
//
        list_of_position.adapter = adapter
    }

    private fun Productlist(){


        val ref = FirebaseDatabase.getInstance().getReference("/ProductInformation")
        ref.addChildEventListener(object : ChildEventListener{

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val product = p0.getValue(ProductInformation::class.java) ?: return
                adapter.add(listofpositionCalss(product))
            }

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

        })

    }

}
