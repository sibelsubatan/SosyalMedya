package com.example.sosyalmedya.data

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.Exclude

data class Post (
        var kullaniciEmail: String = FirebaseAuth.getInstance().currentUser!!.email.toString(),
        var aciklama:String= "",
        var imageUri:String= "",
        var like:Boolean=false,
        var id: String = ""

)
{
    constructor():this("","","",false)
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "aciklama" to aciklama,
            "imageUri" to imageUri,
            "kullaniciEmail" to kullaniciEmail,
            "like" to like
        )
    }
}
