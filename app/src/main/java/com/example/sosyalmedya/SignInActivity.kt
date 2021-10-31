package com.example.sosyalmedya

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.example.sosyalmedya.Fragment.HomeFragment
import com.example.sosyalmedya.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import android.app.ProgressDialog
import android.widget.ProgressBar


class SignInActivity  : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var auth : FirebaseAuth
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
        auth = FirebaseAuth.getInstance()
        var token =getSharedPreferences("mail",Context.MODE_PRIVATE)




        if(auth.currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
        initializeUI()


    }

    private fun getPaylasimData() {
        val intent = Intent(this,HomeFragment::class.java)
        startActivity(intent)
        finish()
    }

    private fun initializeUI() {
        singIn()
        singUp()
    }

    private fun rememberMe() {

    }

    private fun singIn() {
        binding.loginBtn.setOnClickListener{
            Login()
        }
    }

    private fun Login() {
        val builder = AlertDialog.Builder(this)
        val mail = binding.KullaniciAdiSignIn.text.toString()
        val password = binding.PasswordSignIn.text.toString()
        var token =getSharedPreferences("mail",Context.MODE_PRIVATE)

        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle(R.string.LoginAlertLogin)
        progressDialog.show()
        if (mail.isEmpty() || password.isEmpty()) {
            builder.setMessage(R.string.gerekli_alan)
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            }
            builder.show()
            progressDialog.dismiss()

            return
        }
        FirebaseAuth.getInstance().signInWithEmailAndPassword(mail,password)
                .addOnCompleteListener {
                    if (!it.isSuccessful){
                        builder.setMessage(R.string.mail_veya_sifre)
                        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                        }
                        builder.show()
                        progressDialog.dismiss()

                    }
                    else{
                       updateUI(mail)
                    }
                }
    }

    private fun updateUI(mail:String) {
        val intent = Intent(this@SignInActivity, MainActivity::class.java)
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun singUp() {
        binding.SignUpBtn.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

}