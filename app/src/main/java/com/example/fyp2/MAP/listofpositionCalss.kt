package com.example.fyp2.MAP

import com.example.fyp2.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.listofpositionview.view.*

class listofpositionCalss(val product: ProductInformation): Item<GroupieViewHolder> (){

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

            viewHolder.itemView.ry_ProductName.text = product.productName

            viewHolder.itemView.latitude_list.text = product.latitude

            viewHolder.itemView.longitude_list.text = product.longitude

    }

    override fun getLayout(): Int {

        return R.layout.listofpositionview

    }

}