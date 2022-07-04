package com.example.fyp2.order.orderSearch

import com.example.fyp2.MAP.ProductInformation
import com.example.fyp2.R
import com.example.fyp2.order.orderUpload.orderUpload_database
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.order_search_record.view.*
import kotlinx.android.synthetic.main.order_upload_record.view.*

class orderSearchClass (val productInformation: orderUpload_database): Item<GroupieViewHolder>() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.orderSearch_productName.text = productInformation.orderProductName
        viewHolder.itemView.orderSearch_productDesciption.text = productInformation.orderProductDescription


        val ref = FirebaseDatabase.getInstance().getReference("/ProductInformation")
        ref.addListenerForSingleValueEvent(object :ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {

                    val productImage = it.getValue(ProductInformation::class.java) ?: return


                    if (productImage.productDesciption == productInformation.orderProductDescription) {
                        val productInformationImage = productImage.productPhoto
                        val productImage = viewHolder.itemView.orderSearch_image
                        Picasso.get().load(productInformationImage).into(productImage)

                    }


                }
            }
            override fun onCancelled(p0: DatabaseError) {

            }

        })

        }

    override fun getLayout(): Int {

        return R.layout.order_search_record
    }

}