package com.example.fyp2.ChatLogActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.fyp2.Class.ChatFromItem
import com.example.fyp2.Class.ChatMessage
import com.example.fyp2.Class.ChatToItem
import com.example.fyp2.Class.User
import com.example.fyp2.CustomerInformation
import com.example.fyp2.MAP.ProductInformation
import com.example.fyp2.MAP.ShowProductInformation
import com.example.fyp2.R
import com.example.fyp2.menu.message_page
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity() {

    val TAG = "ChatLog"

    val adapter = GroupAdapter<GroupieViewHolder>()

    var toUser: User? = null
    companion object{

        var currentUser: User? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        toUser = intent.getParcelableExtra<User>(ShowProductInformation.PROVIDER_KEY)

        Log.d(TAG, "${toUser?.uid}")

        recyclerview_chatlog.adapter = adapter

       // supportActionBar?.title = toUser?.username

        fetchCurrentUser()

        ListenerForMessage()

        sendbutton_chatlog.setOnClickListener {
            Log.d(TAG, "Attempt to send message....")

          perFromSendMessage()

        }

    }

    private fun fetchCurrentUser(){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {

                currentUser = p0.getValue(User::class.java)
                Log.d(TAG, "Current User: ${currentUser?.username}")

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })

    }

    private fun ListenerForMessage(){
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid

        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")
        ref.addChildEventListener(object : ChildEventListener{

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)


                if(chatMessage != null) {
                    Log.d(TAG, chatMessage.text)

                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                        val currentUser = currentUser
                        adapter.add(ChatFromItem(chatMessage.text, currentUser!!))

                    }else{

                        adapter.add(ChatToItem(chatMessage.text, toUser!!))

                    }

                }

                recyclerview_chatlog.scrollToPosition(adapter.itemCount -1)
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

//          private fun ListenerForMessage(){

//              val toUser = intent.getParcelableExtra<ProductInformation>(ShowProductInformation.PROVIDER_KEY)
//              val fromId = FirebaseAuth.getInstance().uid
//              val toId = toUser
//              val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")

//              ref.addChildEventListener(object : ChildEventListener {

//                  override fun onChildAdded(p0: DataSnapshot, p1: String?) {

//                  }

//                  override fun onChildChanged(p0: DataSnapshot, p1: String?) {

//                  }

//                  override fun onCancelled(p0: DatabaseError) {

//                  }

//                  override fun onChildMoved(p0: DataSnapshot, p1: String?) {

//                  }

//                  override fun onChildRemoved(p0: DataSnapshot) {

//                  }

//              })


//              recyclerview_chatlog.adapter = adapter

//          }

          private fun perFromSendMessage(){

              val text = entermessage_chatlog.text.toString()

              val fromId = FirebaseAuth.getInstance().uid

              val user = intent.getParcelableExtra<User>(ShowProductInformation.PROVIDER_KEY)

              val toId = user.uid

              if (fromId == null) return

             // val ref = FirebaseDatabase.getInstance().getReference("/messages").push()
              val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
              val toRef = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

              val chatMessage = ChatMessage(ref.key!!, text, fromId, toId, System.currentTimeMillis()/1000)
              ref.setValue(chatMessage)
                  .addOnSuccessListener {
                      Log.d(TAG, "Saved our chat message: ${ref.key}")
                      entermessage_chatlog.text.clear()
                      recyclerview_chatlog.scrollToPosition(adapter.itemCount-1)
                  }

                  toRef.setValue(chatMessage)

                  val latestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
                  latestMessageRef.setValue(chatMessage)

                  val LatestToMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")
                  LatestToMessageRef.setValue(chatMessage)

          }

}
