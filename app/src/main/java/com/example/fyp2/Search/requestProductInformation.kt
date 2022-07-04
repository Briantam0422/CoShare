package com.example.fyp2.Search

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class requestProductInformation(val uid: String, val requestProductName: String, val requestProductDescription: String, val requestLatitude: String, val requestLogitude: String, val requestProductImage: String): Parcelable {

    constructor() : this("", "", "", "", "", "")
}