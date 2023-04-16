package com.example.tfg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.SimpleAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates


class LoginActivity : AppCompatActivity() {

    companion object {

        lateinit var usermail: String
        lateinit var providerSession: String

    }

    private var email by Delegates.notNull<String>()
    private var password by Delegates.notNull<String>()
    private  lateinit var etEmail: EditText
    private  lateinit var etPassword: EditText
    private  lateinit var lvTerms: CheckBox

    private lateinit var mAuth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        lvTerms = findViewById(R.id.Terminos)
        lvTerms.visibility = View.INVISIBLE

        etEmail = findViewById(R.id.email)
        etPassword = findViewById(R.id.contraseña)
        mAuth = FirebaseAuth.getInstance()
    }

    fun login(view: View){
        loginUser()
    }

    private fun loginUser(){
        email = etEmail.text.toString()
        password = etPassword.text.toString()

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){ task->
                if (task.isSuccessful) goHome(email, "email")
                else{
                    if (lvTerms.visibility == View.INVISIBLE) lvTerms.visibility = View.VISIBLE
                    else{
                        var cbAcept = findViewById<CheckBox>(R.id.Terminos)
                        if (cbAcept.isChecked) register()
                    }
                }
            }
    }


    private fun goHome(email: String, provider: String){

        usermail = email
        providerSession = provider

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun register(){
        email = etEmail.text.toString()
        password = etPassword.text.toString()

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                if (it.isSuccessful){

                    var dateRegister = SimpleDateFormat("dd/MM/yyyy").format(Date())
                    var dbRegister = FirebaseFirestore.getInstance()
                    dbRegister.collection("usuarios").document(email).set(hashMapOf(
                        "usuario" to email,
                        "FechaDeRegistro" to dateRegister

                    ))

                    goHome(email,"email")
                }
                else Toast.makeText(this, "Ha habido un error", Toast.LENGTH_SHORT).show()
            }


    }


}