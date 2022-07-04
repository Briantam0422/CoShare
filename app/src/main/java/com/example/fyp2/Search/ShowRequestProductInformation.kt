package com.example.fyp2.Search

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.fyp2.ChatLogActivity.ChatLogActivity
import com.example.fyp2.Class.User
import com.example.fyp2.MAP.ShowProductInformation.Companion.PROVIDER_KEY
import com.example.fyp2.R
import com.example.fyp2.order.order
import com.example.fyp2.order.orderUpload.orderUpload_database
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_show_request_product_information.*

class ShowRequestProductInformation : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_request_product_information)

        val productname = intent.getStringExtra(RequestDisplayMapsActivity.PRODUCT_KEY_REQUEST)

        Log.d("requestprodcut", "HI ${productname}")

      showInformation()

      show_request_Backbtn.setOnClickListener {

          val intent = Intent(this, RequestDisplayMapsActivity::class.java)
          startActivity(intent)

        }

       show_request_order.setOnClickListener {


           val placeFormView = LayoutInflater.from(this).inflate(R.layout.dialog_request_accept, null)
           val dialog = AlertDialog.Builder(this)
               .setView(placeFormView)
               .setNegativeButton("No", null)
               .setPositiveButton("Yes", null)
               .show()

           dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {

               orderSearchAccept()

           }


       }

    }

    private fun showInformation(){

        val ref = FirebaseDatabase.getInstance().getReference("/Request-Product_Information")
        val user = FirebaseDatabase.getInstance().getReference("/users")



        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {

                p0.children.forEach {

                    val ref = it.getValue(requestProductInformation::class.java) ?: return
                    val productname = intent.getStringExtra(RequestDisplayMapsActivity.PRODUCT_KEY_REQUEST)
                    val product = productname



                    if (ref != null){

                        if (product == ref.requestProductName){

                            user.addListenerForSingleValueEvent(object: ValueEventListener {

                                override fun onCancelled(p0: DatabaseError) {

                                }

                                override fun onDataChange(p0: DataSnapshot) {
                                    p0.children.forEach {

                                        val user = it.getValue(User::class.java) ?: return


                                        if (ref.uid == user.uid){

                                            show_request_Provider_Name.setText(user.username)

                                            show_request_interested.setOnClickListener {


                                             val intent = Intent(this@ShowRequestProductInformation, ChatLogActivity::class.java)
                                             intent.putExtra(PROVIDER_KEY, user)
                                             startActivity(intent)

                                            }

                                        }

                                    }
                                }
                            })
                                  show_request_product_name.setText(ref.requestProductName)
                                  show_request_product_description.setText(ref.requestProductDescription)
                                  Picasso.get().load(ref.requestProductImage).into(Show_Request_Product_image)
                        }

                    }

                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }


        })

    }

   private fun orderSearchAccept(){

       val uid = FirebaseAuth.getInstance().uid ?:""
       val ref = FirebaseDatabase.getInstance().getReference("/Request-Accept-Record").push()

       val orderAcceptRecord  = requestUpload_database(uid, show_request_product_name.text.toString(), show_request_product_description.text.toString())

       ref.setValue(orderAcceptRecord)
           .addOnSuccessListener {

               Toast.makeText(this, "Ordered Successfully", Toast.LENGTH_SHORT).show()
               val intent = Intent(this, order::class.java)
               startActivity(intent)


           }
           .addOnFailureListener {

               Toast.makeText(this, "Order failed", Toast.LENGTH_SHORT).show()

           }

   }
}
