package com.example.tfg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.example.tfg.LoginActivity.Companion.usermail
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {


    private  lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        Toast.makeText(this, "Bienvenido $usermail", Toast.LENGTH_SHORT).show()

        bottomNavigationView = findViewById(R.id.bottonNavView)
        bottomNavigationView.selectedItemId=R.id.homeB

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeB -> true
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
                R.id.closesessionB -> {
                    startActivity(Intent(applicationContext, CloseActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_letf)
                    finish()
                    true
                }
                else -> false
            }
        }


    }

    /////CERRAR SESION/////
    fun callSingOut(view: View){
        singOff()
    }
    private fun singOff(){
        usermail = ""


        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, LoginActivity::class.java))
    }
    /////////


    //////

    fun callListaProf(view: View){
        listProf()
    }

    private fun listProf(){
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, ListaProfesoresActivity::class.java))
    }

    //////

    fun callGestionar(view: View){
        Gestionar()
    }

    private fun Gestionar(){
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, DeleteAddProfActivity::class.java))
    }


}