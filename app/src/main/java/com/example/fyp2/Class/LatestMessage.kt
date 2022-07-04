package com.example.fyp2.Class

import com.example.fyp2.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.message_row.view.*

class LatestMessage(val chatMessage: ChatMessage): Item<GroupieViewHolder>() {

    var chatPartnerUser: User? = null

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.order_upload_productDesciption.text = chatMessage.text

        val chatPartnerId: String
        if (chatMessage.fromId == FirebaseAuth.getInstance().uid){
            chatPartnerId = chatMessage.toId
        }else {
            chatPartnerId = chatMessage.fromId

        }

        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                chatPartnerUser = p0.getValue(User::class.java)


                viewHolder.itemView.orderUpload_productName.text = chatPartnerUser?.username

                Picasso.get().load(chatPartnerUser?.profileImageUrl).into(viewHolder.itemView.orderUpload_image)
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

    }

    override fun getLayout(): Int {
        return R.layout.message_row

    }

}

