package com.example.tfg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.tfg.LoginActivity.Companion.usermail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FilesActivity : AppCompatActivity() {

    private  lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var AddVideos: TextView
    private lateinit var permisos: String
    private lateinit var profesor: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_files)


        AddVideos = findViewById(R.id.Archivos)

        bottomNavigationView = findViewById(R.id.bottonNavView)
        bottomNavigationView.selectedItemId=R.id.libraryB

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeB -> {
                    startActivity(Intent(applicationContext, HomeActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_letf)
                    finish()
                    true
                }
                R.id.libraryB -> true
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


/////////////////////////////

        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("usuarios").document(usermail)

        docRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                permisos = documentSnapshot.getString("Permisos").toString()
                profesor = documentSnapshot.getString("Profesor").toString()
                if (permisos == "Sensei"){
                    AddVideos.visibility = View.VISIBLE



                }

            } else {
                Toast.makeText(this, "El documento no existe", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(this, "Error en la conexión", Toast.LENGTH_SHORT).show()
        }






    }


    fun callListaArchivos(view: View){
        goAddVideos()
    }

    private fun goAddVideos(){
        startActivity(Intent(this, UploadActivity::class.java))
    }


    fun callListaVideos(view: View){
        Videos()
    }

    private fun Videos(){
        startActivity(Intent(this, VideoVerActivity::class.java))
    }

}