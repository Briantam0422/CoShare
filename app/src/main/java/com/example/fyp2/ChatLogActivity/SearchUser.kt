package com.example.fyp2.ChatLogActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.fyp2.Class.User
import com.example.fyp2.MAP.ShowProductInformation
import com.example.fyp2.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_search_user.*
import kotlinx.android.synthetic.main.new_message_row.view.*

class SearchUser : AppCompatActivity() {

companion object{

    val USER_KEY = "USER_KEY"

}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_user)

        supportActionBar?.title = "Select User"


//  val adapter = GroupAdapter<GroupieViewHolder>()

//     adapter.add(UserItem())

//       recyclerview_selectuser.adapter = adapter

        fetchUser()

    }

    private fun fetchUser(){
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()
                p0.children.forEach {
                    Log.d("Message", it.toString())
                    val user = it.getValue(User::class.java)
                   val uid = FirebaseAuth.getInstance().uid
                    if (user !=null){

                 //      val i = intent.getStringExtra(ShowProductInformation.PROVIDER_KEY)
                 //      if (i == user.uid) {
                 //          adapter.add(UserItem(user))
                 //      }else{

                            adapter.add(UserItem(user))
                   //     }
                    }

                    adapter.setOnItemClickListener { item, view ->
                        val userItem = item as UserItem

                        val intent = Intent(view.context, ChatLogActivity::class.java)
                        intent.putExtra(USER_KEY, userItem.user)
                        startActivity(intent)

                        finish()
                    }


                }

                recyclerview_selectuser.adapter = adapter

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}



class UserItem(val user: User): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

            viewHolder.itemView.searchmessage_username.text = user.username

        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.searchmessage_image)
    }

    override fun getLayout(): Int {
    return R.layout.new_message_row
    }
}