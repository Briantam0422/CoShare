package com.example.fyp2.order.orderHelp

import com.example.fyp2.MAP.ProductInformation
import com.example.fyp2.R
import com.example.fyp2.Search.requestProductInformation
import com.example.fyp2.Search.requestUpload_database
import com.example.fyp2.order.orderUpload.orderUpload_database
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.order_help_record.view.*
import kotlinx.android.synthetic.main.order_search_record.view.*

class orderHelpClass (val productInformation: requestUpload_database): Item<GroupieViewHolder>() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.orderHelp_productName.text = productInformation.requestUploadProductName
        viewHolder.itemView.orderHelp_productDesciption.text = productInformation.requestUploadProductDescription


        val ref = FirebaseDatabase.getInstance().getReference("/Request-Product_Information")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {

                    val productImage = it.getValue(requestProductInformation::class.java) ?: return


                    if (productImage.requestProductDescription == productInformation.requestUploadProductDescription) {
                        val productInformationImage = productImage.requestProductImage
                        val productImage = viewHolder.itemView.orderHelp_image
                        Picasso.get().load(productInformationImage).into(productImage)

                    }


                }
            }
            override fun onCancelled(p0: DatabaseError) {

            }

        })

    }

    override fun getLayout(): Int {

        return R.layout.order_help_record
    }

}