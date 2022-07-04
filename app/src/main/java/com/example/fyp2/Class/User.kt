package com.example.fyp2.Class

import android.os.Parcelable
import com.example.fyp2.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.dashborad_user_infromation.view.*
import kotlinx.android.synthetic.main.new_message_row.view.*


@Parcelize
class User (val uid: String, val username: String, val profileImageUrl: String): Parcelable{

    constructor() : this("", "", "")
}

class UserName(val text:String, val user: User): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.textView3_dashboard_username.text = user.username

        val uri = user.profileImageUrl
        val targetImageView = viewHolder.itemView.imageView3_dashboard
        Picasso.get().load(uri).into(targetImageView)


    }

    override fun getLayout(): Int {
        return R.layout.dashborad_user_infromation
    }
}

