package com.example.fyp2

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp2.Class.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_registration.*
import java.util.*

class registration : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        auth = FirebaseAuth.getInstance()

        RegistrationButton.setOnClickListener {
            signUpUser()
        }

        Already_have_account.setOnClickListener {
            val intent = Intent(this, login::class.java)
            startActivity(intent)
        }

        UserSelectPhoto()

    }


    private fun signUpUser(){
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
        auth.createUserWithEmailAndPassword(signupEmail.text.toString(), signupPassword.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user : FirebaseUser? = auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                startActivity(Intent(this, login::class.java))
                                finish()

                                uploadImageToFirebaseStrotage()
                            }
                        }
                } else {
                    Toast.makeText(baseContext, "Sign Up failed. Try again after some time.",
                        Toast.LENGTH_SHORT).show()
                }

                // ...
            }
    }

    private fun uploadImageToFirebaseStrotage(){

        if (selectedPhotoUri == null)return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/image/$filename")
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("Registration", "Successfully uploaded image: ${it.metadata?.path}")
                Toast.makeText(this, "Successfully uploaded image", Toast.LENGTH_SHORT).show()

            ref.downloadUrl.addOnSuccessListener {

                Log.d("Registration", "File Location: $it")

                saveUserToDatabase(it.toString())

             }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show()
                }

            }
    }

    private fun saveUserToDatabase(profileImageUrl: String){
        val uid = FirebaseAuth.getInstance().uid ?:""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, Register_UserName.text.toString(), profileImageUrl)
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("Registration", "Saved user to Database")
            }
    }

    private fun UserSelectPhoto(){
        User_Select_button.setOnClickListener {
            Log.d("Registration", "Photo Selected")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data !=null) {
            Log.d("Registration", "photo was selected")
            Toast.makeText(this, "Photo selected", Toast.LENGTH_SHORT).show()

            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            selected_photo_imageview.setImageBitmap(bitmap)
            User_Select_button.alpha = 0f
            //           val bitmapDrawable = BitmapDrawable(bitmap)
 //           User_Select_button.setBackgroundDrawable(bitmapDrawable)


        } else {
            Toast.makeText(this, "Failed selecting photo", Toast.LENGTH_SHORT).show()
        }
    }


}