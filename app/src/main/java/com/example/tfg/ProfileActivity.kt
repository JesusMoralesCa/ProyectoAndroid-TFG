package com.example.tfg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.example.tfg.HomeActivity.Companion.CEmail
import com.google.firebase.firestore.DocumentReference

class ProfileActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var TVNombre: TextView
    private lateinit var TVApellido1: TextView
    private lateinit var TVApellido2: TextView
    private lateinit var TVGrado: TextView
    private lateinit var TVTitulo: TextView
    private lateinit var TVTelefono: TextView
    private lateinit var TVEmail: TextView
    private var permisos: String = ""
    private var profesor: String = ""
    private var Cnombre: String = ""
    private lateinit var docRef2: DocumentReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        bottomNavigationView = findViewById(R.id.bottonNavView)
        bottomNavigationView.selectedItemId = R.id.profileB

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
                R.id.profileB -> true
                R.id.closesessionB -> {
                    startActivity(Intent(applicationContext, CloseActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_letf)
                    finish()
                    true
                }
                else -> false
            }
        }

        TVNombre = findViewById(R.id.ProfileName)
        TVApellido1 = findViewById(R.id.profileApellido)
        TVApellido2 = findViewById(R.id.profileApellido2)
        TVGrado = findViewById(R.id.GradoPfl)
        TVTitulo = findViewById(R.id.TituloPfl)
        TVTelefono = findViewById(R.id.TelefonoPfl)
        TVEmail = findViewById(R.id.EmailPfl)

        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("usuarios").document(CEmail)

        docRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                permisos = documentSnapshot.getString("Permisos").toString()
                profesor = documentSnapshot.getString("Profesor").toString()
                Cnombre = documentSnapshot.getString("Nombre").toString()

                if (permisos == "Sensei") {
                    docRef2 = db.collection("Sensei").document(Cnombre)
                } else if (permisos == "Profesor") {
                    docRef2 = db.collection("Sensei/JesusMorales/Profesores").document(Cnombre)
                } else if (permisos == "Alumno") {
                        docRef2 = db.collection("Sensei/JesusMorales/Profesores/$profesor/Alumnos").document(Cnombre)
                }

                docRef2.addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Toast.makeText(this, "Error en la conexión", Toast.LENGTH_SHORT).show()
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        val data = snapshot.data
                        if (data != null) {
                            val nombre = data["Nombre"] as? String
                            val apellido1 = data["Apellido1"] as? String
                            val apellido2 = data["Apellido2"] as? String
                            val telefono = data["Tlf"] as? String
                            val grado = data["Grado"] as? String
                            val titulo = data["Titulo"] as? String
                            val email = data["Email"] as? String

                            TVNombre.text = nombre
                            TVApellido1.text = apellido1
                            TVApellido2.text = apellido2
                            TVGrado.text = grado
                            TVTitulo.text = titulo
                            TVTelefono.text = telefono
                            TVEmail.text = email
                        }
                    } else {
                        Toast.makeText(this, "Error en la conexión", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "El documento no existe", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(this, "Error en la conexión", Toast.LENGTH_SHORT).show()
        }
    }
}
