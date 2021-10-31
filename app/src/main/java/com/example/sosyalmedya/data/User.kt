package com.example.sosyalmedya.data

import android.net.wifi.aware.ParcelablePeerHandle
import android.os.Parcelable
import androidx.versionedparcelable.ParcelField
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.Exclude


data class User (
        var username:String?,
        var email:String= FirebaseAuth.getInstance().currentUser!!.email.toString(),
        var password:String?,
        var uid:String?,
        var image:String?,
        var bio:String?
        ) {
    constructor():this("","","","","","")
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "username" to username,
            "email" to email,
            "password" to password,
            "uid" to uid,
            "image" to image,
            "bio" to bio
            )
    }
}
