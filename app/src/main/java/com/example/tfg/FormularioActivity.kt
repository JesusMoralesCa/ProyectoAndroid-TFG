package com.example.tfg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.example.tfg.LoginActivity.Companion.usermail
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.properties.Delegates

class FormularioActivity : AppCompatActivity() {





    private var nombre by Delegates.notNull<String>()
    private var apellido1 by Delegates.notNull<String>()
    private var apellido2 by Delegates.notNull<String>()
    private var telefono by Delegates.notNull<String>()
    private var grado by Delegates.notNull<String>()
    private lateinit var mAuth: FirebaseAuth
    private  lateinit var ETnombre: EditText
    private  lateinit var ETapellido1: EditText
    private  lateinit var ETapellido2: EditText
    private  lateinit var ETTLF: EditText
    private lateinit var sendData: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario)


        Toast.makeText(
            this,
            "Bienvenido $usermail, rellene el siguiente formulario",
            Toast.LENGTH_SHORT
        ).show()

        mAuth = FirebaseAuth.getInstance()
        ETnombre = findViewById(R.id.ETNombre)
        ETapellido1 = findViewById(R.id.ETApellido1)
        ETapellido2 = findViewById(R.id.ETApellido2)
        ETTLF = findViewById(R.id.ETTlf)
        sendData = findViewById(R.id.SendData)


        val spinnerGrados = findViewById<Spinner>(R.id.spinnerGrados)
        val optionsGrados = arrayOf(
            "9ºKyu",
            "8ºKyu",
            "7ºKyu",
            "6ºKyu",
            "5ºKyu",
            "4ºKyu",
            "3ºKyu",
            "2ºKyu",
            "1ºKyu",
            "Dan"
        )
        spinnerGrados.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, optionsGrados)

        spinnerGrados.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                grado = optionsGrados[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
        fun sendData(view: View) {
            send()
        }


        private fun send() {
            nombre = ETnombre.text.toString()
            apellido1 = ETapellido1.text.toString()
            apellido2 = ETapellido2.text.toString()
            telefono = ETTLF.text.toString()

            if (nombre.isEmpty() || apellido1.isEmpty() || telefono.isEmpty()) {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
            } else {

                var dbRegister = FirebaseFirestore.getInstance()
                var titulo = "Alumno"
                dbRegister.collection("/Sensei/ appjesusmorales@gmail.com /Profesor/ProfesorPrueba/Alumnos")
                    .document(usermail).set(
                    hashMapOf(
                        "Email" to usermail,
                        "Nombre" to nombre,
                        "Apellido1" to apellido1,
                        "Apellido2" to apellido2,
                        "Telefono" to telefono,
                        "Grado" to grado,
                        "Titulo" to titulo

                    )
                )

                var registroInicial = true
                //var titulo = "Alumno"
                dbRegister.collection("usuarios").document(usermail).set(
                    hashMapOf(
                        "RegistroInicial" to registroInicial
                    )
                )

                goHome2(usermail, "Google")

            }
        }


    private fun goHome2(email: String, provider: String){

        usermail = email
        LoginActivity.providerSession = provider

        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    }
