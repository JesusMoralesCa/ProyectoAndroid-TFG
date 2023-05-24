package com.example.tfg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private  lateinit var bottomNavigationView: BottomNavigationView
    private  lateinit var TVNombre: TextView
    private  lateinit var TVApellido1: TextView
    private  lateinit var TVApellido2: TextView
    private  lateinit var TVGrado: TextView
    private  lateinit var TVTitulo: TextView
    private  lateinit var TVTelefono: TextView
    private  lateinit var TVEmail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        bottomNavigationView = findViewById(R.id.bottonNavView)
        bottomNavigationView.selectedItemId=R.id.profileB

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


    //////////////////////////////

// Obtener referencias a los TextViews
        TVNombre = findViewById(R.id.ProfileName)
        TVApellido1 = findViewById(R.id.profileApellido)
        TVApellido2 = findViewById(R.id.profileApellido2)
        TVGrado = findViewById(R.id.GradoPfl)
        TVTitulo = findViewById(R.id.TituloPfl)
        TVTelefono = findViewById(R.id.TelefonoPfl)
        TVEmail = findViewById(R.id.EmailPfl)

// Obtener la instancia de la base de datos de Firebase
        val db = FirebaseFirestore.getInstance()

// Obtener la referencia al documento en la base de datos
        val docRef = db.collection("Sensei")
            .document("JesusMorales")

// Agregar un Listener para obtener los datos del documento
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                // Error al obtener los datos
                Toast.makeText(this, "Error en la conexion", Toast.LENGTH_SHORT)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                // El documento existe, obtener los datos y asignarlos a los TextViews
                val data = snapshot.data
                if (data != null) {
                    val nombre = data["Nombre"] as? String
                    val apellido1 = data["Apellido1"] as? String
                    val apellido2 = data["Apellido2"] as? String
                    val telefono = data["Tlf"] as? String
                    val grado = data["Grado"] as? String
                    val titulo = data["Titulo"] as? String
                    val email = data["Email"] as? String

                    // Asignar los datos a los TextViews
                    TVNombre.text = nombre
                    TVApellido1.text = apellido1
                    TVApellido2.text = apellido2
                    TVGrado.text = grado
                    TVTitulo.text = titulo
                    TVTelefono.text = telefono
                    TVEmail.text = email
                }
            } else {
                // El documento no existe
                Toast.makeText(this, "Error en la conexion", Toast.LENGTH_SHORT)
            }
        }


    }
}