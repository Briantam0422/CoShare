package com.example.fyp2.MAP

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.fyp2.ChatLogActivity.ChatLogActivity
import com.example.fyp2.ChatLogActivity.SearchUser
import com.example.fyp2.ChatLogActivity.SearchUser.Companion.USER_KEY
import com.example.fyp2.Class.LatestMessage
import com.example.fyp2.Class.User
import com.example.fyp2.R
import com.example.fyp2.order.order
import com.example.fyp2.order.orderUpload.orderUpload_database
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_map_product_information.*
import kotlinx.android.synthetic.main.activity_show_product_information.*
import kotlinx.android.synthetic.main.activity_user_account.*

class ShowProductInformation : AppCompatActivity() {

    companion object { val PROVIDER_KEY = "PROVIDER_KEY"}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_product_information)

        val productname = intent.getStringExtra(DisplayMapsActivity.PRODUCT_KEY)


       Log.d("show", "${productname}")


        showInformation()

        show_Backbtn.setOnClickListener {

            val intent = Intent(this, DisplayMapsActivity::class.java)
            startActivity(intent)

        }

        show_order.setOnClickListener {


            val placeFormView = LayoutInflater.from(this).inflate(R.layout.dialog_order_accept, null)
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

        val ref = FirebaseDatabase.getInstance().getReference("/ProductInformation")
        val user = FirebaseDatabase.getInstance().getReference("/users")



        ref.addListenerForSingleValueEvent(object: ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {

                p0.children.forEach {

                    val ref = it.getValue(ProductInformation::class.java) ?: return
                    val productname = intent.getStringExtra(DisplayMapsActivity.PRODUCT_KEY)
                    val product = productname



                    if (ref != null){

                        if (product == ref.productName){

                            user.addListenerForSingleValueEvent(object: ValueEventListener{

                                override fun onCancelled(p0: DatabaseError) {

                                }

                                override fun onDataChange(p0: DataSnapshot) {
                                    p0.children.forEach {

                                        val user = it.getValue(User::class.java) ?: return


                                        if (ref.uid == user.uid){

                                            show_Provider_Name.setText(user.username)

                                            show_interested.setOnClickListener {


                                                val intent = Intent(this@ShowProductInformation, ChatLogActivity::class.java)
                                                intent.putExtra(PROVIDER_KEY, user)
                                                startActivity(intent)

                                            }

                                        }

                                    }
                                }
                            })
                            Log.d("showed", "${ref.productName}")

                            show_product_name.setText(ref.productName)
                            show_product_description.setText(ref.productDesciption)
                            Picasso.get().load(ref.productPhoto).into(Show_Product_image)
                            show_exchange_condition.setText(ref.exchange)
                            show_delivery_service.setText(ref.delivery)

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
        val ref = FirebaseDatabase.getInstance().getReference("/Order-Accept-Record").push()

        val orderAcceptRecord  = orderUpload_database(uid, show_product_name.text.toString(), show_product_description.text.toString())

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
