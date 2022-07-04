package com.example.fyp2.Class

import com.example.fyp2.MAP.ProductInformation
import com.example.fyp2.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_from_row.view.*
import kotlinx.android.synthetic.main.activity_chat_to_row.view.*

class ChatFromItem(val text: String, val user: User): Item<GroupieViewHolder>() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.textView_from_chatlog.text = text

          val targetImageView = viewHolder.itemView.imageView_from_chatlog
          Picasso.get().load(user.profileImageUrl).into(targetImageView)

    }

    override fun getLayout(): Int {
        return R.layout.activity_chat_from_row
    }

}

class ChatToItem(val text: String, val user: User): Item<GroupieViewHolder>() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {


        viewHolder.itemView.textView_to_chatlog.text = text

        val targetImageView = viewHolder.itemView.imageView_to_chatlog
        Picasso.get().load(user.profileImageUrl).into(targetImageView)
    }

    override fun getLayout(): Int {
        return R.layout.activity_chat_to_row
    }

}