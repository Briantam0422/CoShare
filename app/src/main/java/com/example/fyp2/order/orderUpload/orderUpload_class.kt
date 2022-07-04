package com.example.fyp2.order.orderUpload

import com.example.fyp2.MAP.ProductInformation
import com.example.fyp2.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_from_row.view.*
import kotlinx.android.synthetic.main.order_upload_record.view.*

class orderUploadClass(val productInformation: ProductInformation): Item<GroupieViewHolder>() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.orderUpload_productName.text = productInformation.productName
        viewHolder.itemView.order_upload_productDesciption.text = productInformation.productDesciption

        val productImage = viewHolder.itemView.orderUpload_image

        Picasso.get().load(productInformation.productPhoto).into(productImage)

    }

    override fun getLayout(): Int {

        return R.layout.order_upload_record
    }

}