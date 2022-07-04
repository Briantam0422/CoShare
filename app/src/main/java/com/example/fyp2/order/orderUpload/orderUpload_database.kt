package com.example.fyp2.order.orderUpload

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.FileDescriptor

@Parcelize
class orderUpload_database(val uid: String, val orderProductName: String, val orderProductDescription: String): Parcelable {

    constructor(): this("", "" , "")

}