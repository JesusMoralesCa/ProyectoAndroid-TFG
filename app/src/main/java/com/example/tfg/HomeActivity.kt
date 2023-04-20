package com.example.tfg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.example.tfg.LoginActivity.Companion.usermail

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        Toast.makeText(this, "Bienvenido $usermail", Toast.LENGTH_SHORT).show()
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


}