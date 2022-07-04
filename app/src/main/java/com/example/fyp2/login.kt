package com.example.fyp2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log.d
import android.util.Patterns
import android.widget.Toast
import com.example.fyp2.Class.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_dashboard.view.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.signupEmail
import kotlinx.android.synthetic.main.activity_login.signupPassword
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.content_main.*

class login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        signinButton.setOnClickListener {
            doLogin()
        }


    }


    private fun doLogin() {
        if(signupEmail.text.toString().isEmpty()){
            signupEmail.error = "Please enter email"
            signupEmail.requestFocus()
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(signupEmail.text.toString()).matches()){
            signupEmail.error = "please enter valid email"
            signupEmail.requestFocus()
            return
        }
        if(signupPassword.text.toString().isEmpty()){
            signupPassword.error = "Please enter password"
            signupPassword.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(signupEmail.text.toString(), signupPassword.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user : FirebaseUser? = auth.currentUser
                    updateUI(user)
                } else {

                    updateUI(null)
                }

            }

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser : FirebaseUser? = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser : FirebaseUser?) {

        if(currentUser !=null){
            if(currentUser.isEmailVerified){

            startActivity(Intent(this, DashboardActivity::class.java))

            finish()
        } else{
                Toast.makeText(baseContext, "Please verify your email address.",
                    Toast.LENGTH_SHORT).show()}
        }

            else {
            Toast.makeText(baseContext, "Login failed.",
                Toast.LENGTH_SHORT).show()
        }
        }
    }

