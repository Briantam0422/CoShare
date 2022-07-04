package com.example.fyp2.order.orderRequestRecord

import com.example.fyp2.MAP.ProductInformation
import com.example.fyp2.R
import com.example.fyp2.Search.requestProductInformation
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.order_request_upload_record.view.*
import kotlinx.android.synthetic.main.order_search_record.view.*
import kotlinx.android.synthetic.main.order_upload_record.view.*

class orderRequestRecordClass(val productInformation: requestProductInformation): Item<GroupieViewHolder>() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.orderUpload_request_productName.text = productInformation.requestProductName
        viewHolder.itemView.order_upload_request_productDesciption.text = productInformation.requestProductDescription

        val productImage = viewHolder.itemView.orderUpload_request_image

        Picasso.get().load(productInformation.requestProductImage).into(productImage)



    }

    override fun getLayout(): Int {

        return R.layout.order_request_upload_record
    }

}