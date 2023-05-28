package com.example.tfg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.example.tfg.LoginActivity.Companion.usermail
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {

    companion object{
        lateinit var CEmail : String
        lateinit var ProfesorGA: String
        lateinit var profesorCompa:String

    }


    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var GestionarProfesores: TextView
    private lateinit var GestionarAlumnos: TextView
    private lateinit var ListaProf: TextView
    private lateinit var ListaCompa: TextView

    private lateinit var permisos: String
    private lateinit var profesor: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        GestionarProfesores = findViewById(R.id.Gestion)
        GestionarAlumnos = findViewById(R.id.GestionAlumnos)
        ListaProf = findViewById(R.id.ListaProf)
        ListaCompa = findViewById(R.id.ListaCompa)


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
                    CEmail = usermail
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
////////////////////


        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("usuarios").document(usermail)

        docRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                permisos = documentSnapshot.getString("Permisos").toString()
                profesor = documentSnapshot.getString("Profesor").toString()
                ProfesorGA = documentSnapshot.getString("Nombre").toString()
                profesorCompa = profesor


                if (permisos == "Profesor"){
                    GestionarProfesores.visibility = View.GONE
                    GestionarAlumnos.visibility = View.VISIBLE


                }

                if (permisos == "Alumno"){
                    GestionarProfesores.visibility = View.GONE
                    ListaProf.visibility = View.GONE
                    ListaCompa.visibility = View.VISIBLE

                }

            } else {
                Toast.makeText(this, "El documento no existe", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(this, "Error en la conexión", Toast.LENGTH_SHORT).show()
        }






    }

    /////CERRAR SESION/////

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


    fun callGestionarAlumnos(view: View){
        GestionarAlumnos()
    }

    private fun GestionarAlumnos(){

        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, GestionarAlumnosPermisosProfesorActivity::class.java))
    }



    fun callListaCompa(view: View){
        ListaCompa()
    }

    private fun ListaCompa(){

        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, ListaAlumnosPermisosAlumnoActivity::class.java))
    }

}