package com.example.fyp2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.fyp2.Class.User
import com.example.fyp2.Class.ValueListenerAdapter
import com.example.fyp2.MAP.DisplayMapsActivity
import com.example.fyp2.Search.SearchMainPage
import com.example.fyp2.Supply.supplyActivity
import com.example.fyp2.menu.Setting
import com.example.fyp2.menu.UserAccountActivity
import com.example.fyp2.menu.message_page
import com.example.fyp2.order.order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_dashboard.*


class DashboardActivity : AppCompatActivity() {

    var myAuth = FirebaseAuth.getInstance()

    lateinit var mUser: User
    lateinit var mAuth: FirebaseAuth
    lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)


        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference

        fun currentReference(): DatabaseReference =
            mDatabase.child("/users").child(mAuth.currentUser!!.uid)

        currentReference().addListenerForSingleValueEvent(
            ValueListenerAdapter{
                val mUser = it.getValue(User::class.java)
                if (mUser != null)
                textView2_dashboard_text.setText(mUser.username)

                Picasso.get().load(mUser?.profileImageUrl).into(image_dashboard_image)
            }
        )

        Supply_Button_Dashboard.setOnClickListener {
            val intent = Intent(this, supplyActivity::class.java)
            startActivity(intent)
        }

        Search_Button_Dashboard.setOnClickListener{

            val intent = Intent(this, SearchMainPage::class.java)
            startActivity(intent)
        }

        order_Button_Dashboard2.setOnClickListener {

            val intent = Intent(this, order::class.java)
            startActivity(intent)
        }

        chat_Button_Dashboard.setOnClickListener {

            val intent = Intent(this, message_page::class.java)
            startActivity(intent)
        }

        account_Button_Dashboard3.setOnClickListener {

            val intent = Intent(this, UserAccountActivity::class.java)
            startActivity(intent)

        }

        setting_Button_Dashboard4.setOnClickListener {

            val intent = Intent(this, Setting::class.java)
            startActivity(intent)
        }

        signout_dashboard.setOnClickListener {

            signOut()
        }


        }


   private fun signOut() {
       myAuth.signOut()
       finish()
   }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
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
            val intent = Intent(this, login::class.java)
            startActivity(intent)
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


