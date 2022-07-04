package com.example.fyp2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import com.example.fyp2.Class.User
import com.example.fyp2.Class.UserName
import com.example.fyp2.menu.Setting
import com.example.fyp2.menu.UserAccountActivity
import com.example.fyp2.menu.message_page
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_customer_information.*

class CustomerInformation : AppCompatActivity() {

    var myAuth = FirebaseAuth.getInstance()
     lateinit var btn : Button
    val TAG = "dashboard"
    companion object{

          var currentUser: User? = null
  //      val USERKEY = "USERKEY"
      }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_information)

          myAuth.addAuthStateListener {
             if (myAuth.currentUser==null) {
                 this.finish()
             }
             }


        button2_customer_next.setOnClickListener {
            val intent = Intent(this, message_page::class.java)
    //        val userItem = it as UserItem
    //        intent.putExtra(USERKEY, userItem.user.username)
            startActivity(intent)
        }
             fetchCurrentUser()

              setDummy()

    }

   private fun fetchCurrentUser(){

       val uid = FirebaseAuth.getInstance().uid
       val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

       ref.addListenerForSingleValueEvent(object: ValueEventListener {

           override fun onDataChange(p0: DataSnapshot) {
               currentUser = p0.getValue(User::class.java)
               Log.d("dashboard", "username${currentUser?.username}")
           }

           override fun onCancelled(p0: DatabaseError) {

           }

       })

   }

   val adapter = GroupAdapter<GroupieViewHolder>()
   private fun setDummy(){

  val ref = FirebaseDatabase.getInstance().getReference("/users")
       ref.addChildEventListener(object: ChildEventListener {

           override fun onChildAdded(p0: DataSnapshot, p1: String?) {

               val userName = p0.getValue(User::class.java)

               if(userName != null){
                   val currentUser = currentUser ?: return
                   Log.d(TAG, userName.username)
                   adapter.add(UserName(userName.username, currentUser))

               }
           }

           override fun onChildChanged(p0: DataSnapshot, p1: String?) {

           }

           override fun onChildMoved(p0: DataSnapshot, p1: String?) {

           }

           override fun onCancelled(p0: DatabaseError) {

           }

           override fun onChildRemoved(p0: DataSnapshot) {

           }
       })

       recyclerview_customer.adapter = adapter

   }


    private fun signOut() {
        myAuth.signOut()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu);
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId;
        if (id == R.id.action_save){
            Toast.makeText(this,"Account", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, UserAccountActivity::class.java))
            return true
        }

        if (id == R.id.messagepage){
            Toast.makeText(this, "Message", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, message_page::class.java))
            return true
        }

        if (id == R.id.action_setting){
            Toast.makeText(this,"Setting", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, Setting::class.java))
            return true
        }


        if (id == R.id.action_Logout){
            signOut()
            return true
        }

        if (id == R.id.action_main) {
            Toast.makeText(this, "Main page", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, DashboardActivity::class.java))
            return true
        }


        return super.onOptionsItemSelected(item)
    }
}

