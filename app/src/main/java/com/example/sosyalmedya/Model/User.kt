package com.example.sosyalmedya.Model

class User {
    private var username:String=""
    private var password:String=""
    private var uid:String=""
    private var email:String=""
    private var image:String=""



    constructor()


    constructor(username:String,password:String,uid:String,email:String,image:String){
        this.email=email;
        this.username=username;
        this.password=password;
        this.image=image;
        this.uid=uid;
    }

    fun getUser():String{
        return username
    }
    fun setUser(username:String){
        this.username=username
    }


    fun getEmail():String{
        return email
    }
    fun setEmail(email:String){
        this.email=email
    }


    fun getPassword():String{
        return password
    }
    fun setPassword(password:String){
        this.password=password
    }


    fun getImage():String{
        return image
    }
    fun setImage(image:String){
        this.image=image
    }

    fun getUid():String{
        return uid
    }
    fun setUid(uid:String){
        this.uid=uid
    }



}