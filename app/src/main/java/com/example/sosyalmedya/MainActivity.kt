package com.example.sosyalmedya

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.sosyalmedya.Fragment.HomeFragment
import com.example.sosyalmedya.Fragment.ProfileFragment

class MainActivity : AppCompatActivity() {

  //  var token =getSharedPreferences("mail", Context.MODE_PRIVATE)


    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_home -> {
                moveToFragment(HomeFragment())
                return@OnNavigationItemSelectedListener true

           }
//            R.id.nav_search -> {
//                return@OnNavigationItemSelectedListener true
//            }
//            R.id.nav_notifications -> {
//                return@OnNavigationItemSelectedListener true
//            }
            R.id.nav_profile -> {
                moveToFragment(ProfileFragment())
                return@OnNavigationItemSelectedListener true

            }
        }

        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        moveToFragment(HomeFragment())

    }
    private fun moveToFragment(fragment: Fragment){

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
// Remember that you should never show the action bar if the status bar is hidden, so hide that too if necessary.
        actionBar?.hide()
        val fragmentTrans=supportFragmentManager.beginTransaction()
        fragmentTrans.replace(R.id.fragment_container,fragment)
        fragmentTrans.commit()
    }

}
