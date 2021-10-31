package com.example.sosyalmedya

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.example.sosyalmedya.data.User
import com.example.sosyalmedya.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SignUpActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)


        auth = FirebaseAuth.getInstance()
        database = Firebase.firestore

        initializeUI()

    }

    private fun initializeUI() {
        singIn()
        exit()
    }

    private fun exit() {
        binding.ExitSignUpbtn.setOnClickListener{
            auth.signOut()
            val intent = Intent(this,SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun singIn() {
        binding.signUpBtnn.setOnClickListener {
            userRegister()
        }
    }
    private fun userRegister() {
        val userName = binding.KullaniciAdiSignUp.text.toString()
        val password = binding.PasswordSignUp.text.toString()
        val Tekrarpassword = binding.TekrarPasswordSignUp.text.toString()
        val eMail = binding.emailSignUp.text.toString()

        val builder = AlertDialog.Builder(this)
        val progressDialog = ProgressDialog(this)
        if (userName.isEmpty() || password.isEmpty() || eMail.isEmpty() || Tekrarpassword.isEmpty()) {
            builder.setMessage(R.string.gerekli_alan)
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            }
            builder.show()
            return
        }
        if(password != Tekrarpassword){
            builder.setMessage(R.string.sifrelerAyniDeğil)
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            }
            builder.show()
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(eMail, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                savedUserDatabase()
            }
            .addOnFailureListener {
                Toast.makeText(this, "{${it.message}}", Toast.LENGTH_LONG).show()
            }
    }

    private fun savedUserDatabase() {

        val builder = AlertDialog.Builder(this)
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle(R.string.Register)
        progressDialog.show()
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().reference.child("/users/$uid")
        val user = User(
            email = binding.emailSignUp.text.toString(),
            password = binding.PasswordSignUp.text.toString(),
            username = binding.KullaniciAdiSignUp.text.toString(),
            uid = uid,
            image = "",
                bio=""
        )
        database.collection("users").document(uid).set(user).addOnCompleteListener {
            builder.setMessage(R.string.KayitYapildi)
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            }
            builder.show()
            progressDialog.dismiss()
        }

//        ref.setValue(user).addOnCompleteListener {
//            if (it.isSuccessful) {
//                val intent = Intent(this,SignInActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
//        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

    }

    }


