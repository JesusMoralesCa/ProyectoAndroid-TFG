package com.example.tfg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.properties.Delegates

class RegisterActivity : AppCompatActivity() {


    private var email by Delegates.notNull<String>()
    private var password by Delegates.notNull<String>()
    private var email2 by Delegates.notNull<String>()
    private var password2 by Delegates.notNull<String>()
    private  lateinit var etEmail: EditText
    private  lateinit var etPassword: EditText
    private  lateinit var etEmail2: EditText
    private  lateinit var etPassword2: EditText

    private lateinit var mAuth: FirebaseAuth

   private lateinit var regButton: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()
        etEmail = findViewById(R.id.userEmail)
        etEmail2 = findViewById(R.id.userEmail2)
        etPassword = findViewById(R.id.UserPass)
        etPassword2 = findViewById(R.id.UserPass2)
        regButton = findViewById(R.id.registerUser)

    }
    fun userRegister(view: View){
        regUser()
    }

    private fun goLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun regUser(){

        email = etEmail.text.toString()
        password = etPassword.text.toString()
        email2 = etEmail.text.toString()
        password2 = etPassword.text.toString()

        if (email.isEmpty() || password.isEmpty() || email2.isEmpty() || password2.isEmpty()){
            Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
        }
        if (email != email2 || password != password2){
            Toast.makeText(this, "Compruebe que usuario o contraseña coinciden", Toast.LENGTH_SHORT).show()
        }else {
            mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener{
                    if (it.isSuccessful){
                        var dbRegister = FirebaseFirestore.getInstance()
                        var registroInicial = false
                        var titulo = "Alumno"
                        dbRegister.collection("usuarios").document(email).set(hashMapOf(
                            "usuario" to email,
                            "RegistroInicial" to registroInicial,
                            "Titulo" to titulo
                        ))

                        goLogin()

                    }
                    else Toast.makeText(this, "Ha habido un error", Toast.LENGTH_SHORT).show()
                }
        }


    }

}