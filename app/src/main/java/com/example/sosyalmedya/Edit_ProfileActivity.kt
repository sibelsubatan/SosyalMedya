package com.example.sosyalmedya

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.example.sosyalmedya.data.Post
import com.example.sosyalmedya.databinding.ActivityEditProfileBinding
import com.example.sosyalmedya.data.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_new_post.*
import java.util.*

class Edit_ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var database: DatabaseReference
    private var checker = ""
    var getImage2 = String()
    val getImage : Uri? = null
    private lateinit var databaseFirestore : FirebaseFirestore
    var secilenGorselUri : Uri? = null
    private lateinit var storage : FirebaseStorage
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_edit__profile)



        auth = FirebaseAuth.getInstance()
        databaseFirestore = Firebase.firestore


        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        storage = FirebaseStorage.getInstance()


        binding.editProfileExitBtn.setOnClickListener{
            onBackPressed()
        }

//        binding.changeImageTextBtn.setOnClickListener{
//            val intent = Intent(Intent.ACTION_PICK , MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            startActivityForResult(intent , 2)
//
//        }
        userInfo()
        Save_post()
        Add_Image()
    }

    override fun onBackPressed() {
        super.onBackPressed()

    }
    private fun Add_Image() {
        binding.changeImageTextBtn.setOnClickListener {
            checker = "clicked"
//            CropImage.activity()
//                    .setAspectRatio(1, 1)
//                    .start(this)
                      val intent = Intent(Intent.ACTION_PICK , MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent , 2)
        }
    }



//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  &&  resultCode == Activity.RESULT_OK  &&  data != null)
//        {
//            val result = CropImage.getActivityResult(data)
//            secilenGorselUri = result.uri
//            binding.profileImageViewProfileFragment.setImageURI(secilenGorselUri)
//        }
//        super.onActivityResult(requestCode, resultCode, data)
//
//    }



    var selectedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(resultCode == RESULT_OK){
            when(requestCode){
                1 -> {}
                2 -> {
                    data?.let {
                        secilenGorselUri = data.data
                        binding.profileImageViewProfileFragment.setImageURI(secilenGorselUri)
                    }
                }
                else -> {}
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }



    private fun Save_post() {
        binding.SaveBtn.setOnClickListener{
            EditBtn()


        }

    }

    private fun uploadImagetoFirebaseStorage() {

        val builder = AlertDialog.Builder(this)
        //obj
        val progressDialog = ProgressDialog(this)
        val reference = storage.reference
        val siradakiGorselIsmi = getGorselIsmi()
        val gorselReference = reference.child("images").child(siradakiGorselIsmi)
        progressDialog.setTitle(R.string.EditProfile)
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
                            val myModel = User().apply {
                                bio = binding.bioEdit.text.toString()
                                password = binding.PasswordEdit.text.toString()
                                username = binding.KullaniciAdiEdit.text.toString()
                                uid = firebaseUser.uid
                                image = yuklenenGorselUrl
                                email= FirebaseAuth.getInstance().currentUser!!.email.toString()

                            }
                            databaseFirestore.collection("users").document(firebaseUser.uid).set(myModel).addOnCompleteListener {
                                builder.setMessage(R.string.Guncellendi)
                                builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                                }
                                builder.show()
                                progressDialog.dismiss()

                            }
                                    .addOnFailureListener {
                                        builder.setMessage(R.string.problem)
                                        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                                        }
                                        builder.show()
                                        progressDialog.dismiss()

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





    private fun EditBtn() {

        val builder = AlertDialog.Builder(this)
        //obj
        val progressDialog = ProgressDialog(this)
        when{
            checker=="clicked" ->uploadImagetoFirebaseStorage()

         else->{
              val usersRef = FirebaseDatabase.getInstance().reference.child("{users/${firebaseUser.uid}}")
             progressDialog.setTitle(R.string.EditProfile)
             progressDialog.show()
             val myModel = User().apply {
                 bio = binding.bioEdit.text.toString()
                 password = binding.PasswordEdit.text.toString()
                 username = binding.KullaniciAdiEdit.text.toString()
                 uid = firebaseUser.uid
                 image=getImage2
                 email= FirebaseAuth.getInstance().currentUser!!.email.toString()
             }

             databaseFirestore.collection("users").document(firebaseUser.uid).set(myModel).addOnCompleteListener {
                  builder.setMessage(R.string.Guncellendi)
                  builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                  }
                  builder.show()
                 progressDialog.dismiss()

             }
                      .addOnFailureListener {
                          builder.setMessage(R.string.problem)
                          builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                          }
                          builder.show()
                          progressDialog.dismiss()

                      }
              //usersRef.updateChildren(user.toMap())

          }
      }
    }

    private fun userInfo() {
        databaseFirestore.collection("users")
                .whereEqualTo("uid", FirebaseAuth.getInstance().currentUser!!.uid)
                //   .orderBy("tarih" , Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) throw exception

                    snapshot?.let {
                        if (!it.isEmpty) {
                            var user=snapshot.firstOrNull()?.toObject<User>()
                            binding.bioEdit.setText(user!!.bio)
                            binding.PasswordEdit.setText(user!!.password)
                            binding.KullaniciAdiEdit.setText(user!!.username)
                            if (user!!.image == ""){
                                binding.profileImageViewProfileFragment.setImageResource(R.drawable.ic_user_1)

                            }
                            else {
                                Picasso.get().load(user!!.image).into(binding.profileImageViewProfileFragment)
                                getImage2=user!!.image.toString()
                            }

                        }

                    }
                }

    }

}