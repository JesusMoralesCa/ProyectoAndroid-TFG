package com.example.tfg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class CloseActivity : AppCompatActivity() {

    private  lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_close)



        bottomNavigationView = findViewById(R.id.bottonNavView)
        bottomNavigationView.selectedItemId=R.id.closesessionB

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeB -> {
                    startActivity(Intent(applicationContext, HomeActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_letf)
                    finish()
                    true
                }
                R.id.libraryB -> {
                    startActivity(Intent(applicationContext, FilesActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_letf)
                    finish()
                    true
                }
                R.id.profileB -> {
                    startActivity(Intent(applicationContext, ProfileActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_letf)
                    finish()
                    true
                }
                R.id.closesessionB -> true
                else -> false
            }
        }







    }


    fun callSingOut(view: View){
        singOff()
    }
    private fun singOff(){
        LoginActivity.usermail = ""


        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, LoginActivity::class.java))
    }
}