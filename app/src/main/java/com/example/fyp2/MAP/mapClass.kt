package com.example.fyp2.MAP

import android.net.Uri
import android.os.Parcelable
import android.webkit.GeolocationPermissions
import kotlinx.android.parcel.Parcelize


@Parcelize
class ProductInformation(val uid: String, val productName: String, val productDesciption: String, val latitude: String, val longitude: String, val productPhoto: String, val exchange: String, val delivery: String):
    Parcelable {


    constructor() : this("","","","","", "", "", "")

}