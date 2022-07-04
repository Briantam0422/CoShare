package com.example.fyp2.menu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.fyp2.ChatLogActivity.ChatLogActivity
import com.example.fyp2.Class.ChatMessage
import com.example.fyp2.Class.LatestMessage
import com.example.fyp2.Class.User
import com.example.fyp2.R
import com.example.fyp2.ChatLogActivity.SearchUser
import com.example.fyp2.ChatLogActivity.SearchUser.Companion.USER_KEY
import com.example.fyp2.MAP.ShowProductInformation.Companion.PROVIDER_KEY
import com.example.fyp2.registration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.message.*

class message_page:AppCompatActivity() {


    val adapter = GroupAdapter<GroupieViewHolder>()
    companion object{

        var currentUser: User? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.message)

   //     setUpDummyRow()

        fetchCurrentUser()


        verifyingUserLogin()

        listenForLatestMessage()

    }

    val latestMessageMap = HashMap<String, ChatMessage>()
    private fun refreshRecyclerViewMessage(){

        adapter.clear()
        latestMessageMap.values.forEach {

            adapter.add(LatestMessage(it))
        }

    }

    private fun listenForLatestMessage(){
        val fromId = FirebaseAuth.getInstance().uid

        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
        ref.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)?: return

                latestMessageMap[p0.key!!] = chatMessage
                refreshRecyclerViewMessage()

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

                val chatMessage = p0.getValue(ChatMessage::class.java)?: return

                latestMessageMap[p0.key!!] = chatMessage
                refreshRecyclerViewMessage()
            }

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

        })
        message_recycleview.adapter = adapter
        message_recycleview.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))

        adapter.setOnItemClickListener { item, view ->
            val intent = Intent(this, ChatLogActivity::class.java)

            // intent.putExtra(SearchUser.USER_KEY, )
            val row = item as LatestMessage

            intent.putExtra(PROVIDER_KEY, row.chatPartnerUser)

            startActivity(intent)
        }
    }

 //  private fun setUpDummyRow(){

 //      val adapter = GroupAdapter<GroupieViewHolder>()

 //      adapter.add(LatestMessage())

 //      message_recycleview.adapter = adapter
 //  }

    private fun fetchCurrentUser(){

        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {

                currentUser = p0.getValue(User::class.java)
                Log.d("CurrentUser", "CurrentUSer: ${currentUser?.profileImageUrl}")

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })

    }

    private fun verifyingUserLogin(){

        val uid =  FirebaseAuth.getInstance().uid
        if(uid == null){
            val intent = Intent(this, registration::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)

            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId){
            R.id.MessageUserSearch ->{
                val intent = Intent(this, SearchUser::class.java)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.nav_manu, menu)
        return super.onCreateOptionsMenu(menu)
    }

}