package com.example.fyp2.Search

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class requestUpload_database(val uid: String, val requestUploadProductName: String, val requestUploadProductDescription: String): Parcelable {

    constructor(): this("", "" , "")

}