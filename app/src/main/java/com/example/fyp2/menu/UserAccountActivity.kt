package com.example.fyp2.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fyp2.Class.User
import com.example.fyp2.R
import com.example.fyp2.UserInfromationChange
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user_account.*

class UserAccountActivity : AppCompatActivity() {


    lateinit var mAuth: FirebaseAuth
    lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_account)

         mAuth = FirebaseAuth.getInstance()
         mDatabase = FirebaseDatabase.getInstance().reference

            fun currentUserProfile(): DatabaseReference =
                mDatabase.child("users").child(mAuth.currentUser!!.uid)

        currentUserProfile().addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                val mUser = p0.getValue(User::class.java)

                textView_UserName_Account.setText(mUser?.username)

                Picasso.get().load(mUser?.profileImageUrl).into(Userimage_Account)

                UserId_Account.setText(mUser?.uid)
            }
        })


        Savebtn_UserAccount.setOnClickListener {

            val intent = Intent(this, UserInfromationChange::class.java)

            startActivity(intent)
        }

    }
}
