package com.example.fyp2.Class

import android.provider.ContactsContract
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ValueListenerAdapter(val handler: (DataSnapshot)-> Unit): ValueEventListener {

    val TAG = "ValueListenerAdapter"

    override fun onDataChange(p0: DataSnapshot) {



        handler(p0)

    }

    override fun onCancelled(p0: DatabaseError) {

    }
}

