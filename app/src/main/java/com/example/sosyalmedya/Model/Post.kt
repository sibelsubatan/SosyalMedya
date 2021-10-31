package com.example.sosyalmedya.Model

class Post {
    private var aciklama:String=""
    private var image:String=""



    constructor()


    constructor(aciklama:String,image:String){
        this.aciklama=aciklama;
        this.image=image;
    }

    fun getAciklama():String{
        return aciklama
    }
    fun setUser(aciklama:String){
        this.aciklama=aciklama
    }


    fun getImage():String{
        return image
    }
    fun setImage(image:String){
        this.image=image
    }

}