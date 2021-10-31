package com.example.sosyalmedya

import android.app.Activity
import android.app.ProgressDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.example.sosyalmedya.Fragment.HomeFragment
import com.example.sosyalmedya.data.Post
import com.example.sosyalmedya.databinding.ActivityNewPostBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.theartofdev.edmodo.cropper.CropImage
import java.util.*

import kotlinx.android.synthetic.main.activity_new_post.*



class NewPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewPostBinding
    private var imageUri: Uri? = null
    private var myUrl = ""
    private var checker = ""
    private var storageProfilePicRef: StorageReference? = null
    private lateinit var firebaseUser: FirebaseUser
    private var progressBar: ProgressBar? = null



    var secilenGorselUri : Uri? = null
    private lateinit var storage : FirebaseStorage
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_post)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        initializeUI()
    }

    private fun initializeUI() {
        Exit()
        Save_post()
        Add_Image()
    }

    private fun Exit() {
        binding.newpostExitBtn.setOnClickListener{
            onBackPressed()
        }
    }

    private fun Add_Image() {
        binding.Addimage.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK , MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent , 2)
//            CropImage.activity()
//                    .setAspectRatio(1, 1)
//                    .start(this)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(resultCode == RESULT_OK){
            when(requestCode){
                1 -> {}
                2 -> {
                    data?.let {
                        secilenGorselUri = data.data
                        binding.imageviewPost.setImageURI(secilenGorselUri)
                    }
                }
                else -> {}
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

//
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  &&  resultCode == Activity.RESULT_OK  &&  data != null)
//        {
//            val result = CropImage.getActivityResult(data)
//            secilenGorselUri = result.uri
//            binding.imageviewPost.setImageURI(secilenGorselUri)
//        }
//        super.onActivityResult(requestCode, resultCode, data)
//
//    }



    private fun Save_post() {
        binding.SavePostBtn.setOnClickListener{
            uploadImagetoFirebaseStorage()

        }

    }

    private fun updateUserInfoOnly() {
        TODO("Not yet implemented")
    }


    private fun uploadImagetoFirebaseStorage() {
        val reference = storage.reference
        val siradakiGorselIsmi = getGorselIsmi()
        val gorselReference = reference.child("images").child(siradakiGorselIsmi)
        val builder = AlertDialog.Builder(this)
        var dialog: Dialog? = null //obj
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle(R.string.NewPost)
        progressDialog.show()
        secilenGorselUri?.let{
            gorselReference.putFile(it)
                    .addOnSuccessListener { taskSnapshot ->
                        //Buradaki olay eğer görsel başarılı olarak yüklenmişse veri tabanına aktarmak olacak
                        //Veri tabanına görselin urli eklenebilir string olarak.
                        //Ancak geri dönen taskSnapshot objesi içinde bu url değeri bulunmamaktadır
                        //Bu url değerine ulaşabilmek için tekrar bir referans oluşturup görselin adı ile eşleştirme yapılabilir.
                        val yuklenenGorselReference = reference.child("images").child(siradakiGorselIsmi)
                        var yuklenenGorselUrl : String
                        yuklenenGorselReference.downloadUrl.addOnSuccessListener { uri ->
                            //Uri burada alınmış oldu
                            yuklenenGorselUrl = uri.toString()
                            //burada database e ekleme yapılacak
                            val yorum = aciklama.text.toString()
                            val myModel = Post().apply {
                                imageUri = yuklenenGorselUrl
                                aciklama = yorum
                                kullaniciEmail=firebaseUser.email.toString()
                                like=false

                            }
                            val uid = FirebaseAuth.getInstance().uid ?: ""
                            if (yuklenenGorselUrl.isEmpty()){
                                builder.setMessage(R.string.ResimEkle)
                                builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                                }
                                builder.show()
                                progressDialog.dismiss()
                            }
                            else{
                                database.collection("posts").add(myModel)
                                        .addOnSuccessListener {
                                            builder.setMessage(R.string.basarili)
                                            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                                            }
                                            builder.show()
                                            finish()
                                            progressDialog.dismiss()

                                        }
                                        .addOnFailureListener{ e ->
                                            builder.setMessage(R.string.basarisiz)
                                            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                                            }
                                            builder.show()
                                            progressDialog.dismiss()

                                        }
                            }


                        }.addOnFailureListener{
                            builder.setMessage(R.string.problem)
                            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                            }
                            builder.show()
                            progressDialog.dismiss()

                        }
                    }
                    .addOnFailureListener{ e ->
                        progressDialog.dismiss()
                        Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()

                    }
        }

    }
    private fun getGorselIsmi(): String {
        return "${UUID.randomUUID()}.jpg"
    }

    private fun savePost(profileImageUrl: String)
    {
        val usersRef = FirebaseDatabase.getInstance().reference.child("Users")

        val ref = FirebaseDatabase.getInstance().reference.child("posts")
        val builder = AlertDialog.Builder(this)

        myUrl= binding.imageviewPost.setImageURI(imageUri).toString()
        val Post = Post(
            aciklama = binding.aciklama.text.toString(),
            imageUri = profileImageUrl
        )

        ref.push().setValue(Post).addOnCompleteListener {
            if (it.isSuccessful) {
                builder.setMessage(R.string.gonderiOlustu)
                builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                }
                builder.show()
                val intent = Intent(this,HomeFragment::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}
