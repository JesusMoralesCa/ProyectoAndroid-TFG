package com.example.tfg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.text.TextUtils
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.SimpleAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates


class LoginActivity : AppCompatActivity() {

    companion object {

        lateinit var usermail: String
        lateinit var providerSession: String


    }


    private var RC_SIGN_IN = 9001



    private var email by Delegates.notNull<String>()
    private var password by Delegates.notNull<String>()
    private  lateinit var etEmail: EditText
    private  lateinit var etPassword: EditText


    private lateinit var mAuth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        etEmail = findViewById(R.id.email)
        etPassword = findViewById(R.id.contraseña)
        mAuth = FirebaseAuth.getInstance()

        manageButtonLogin()
        etEmail.doOnTextChanged{ text, start, before, count -> manageButtonLogin()}
        etPassword.doOnTextChanged{ text, start, before, count -> manageButtonLogin()}
    }


    //Para cuando hay un usuario ya registrado
    public override fun onStart() {
        super.onStart()

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null){
            goHome(currentUser.email.toString(), currentUser.providerId)
        }
    }


    //Iniciar sesion con google

    fun callSingInGoogle(view: View){
        singInGoogle()

    }

    private fun singInGoogle(){

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

       var googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleSignInClient.signOut()
        startActivityForResult(googleSignInClient.signInIntent, RC_SIGN_IN)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!

                if (account != null) {
                    val email = account.email!!
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                    mAuth.signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val dbRegister = FirebaseFirestore.getInstance()
                            val registroInicialRef = dbRegister.collection("usuarios").document(email)

                            // Obtener el valor actual de "registroInicial" de la base de datos de Firebase
                            registroInicialRef.get().addOnSuccessListener { document ->
                                if (document != null && document.exists()) {
                                    val registroInicial = document.getBoolean("RegistroInicial") ?: false

                                    if (!registroInicial) {
                                        // El valor de "registroInicial" es "false", inicie una nueva actividad para que el usuario complete el formulario
                                        goFormulario(email, "Google")
                                    } else {
                                        // El valor de "registroInicial" es "true", llame a la función "goHome"
                                        goHome(email, "Google")
                                    }

                                    registroInicialRef.update("RegistroInicial", true).addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(this, "Registro Inicial comenzando", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                } else {
                                    // El documento no existe, por lo que se asume que es un nuevo usuario
                                    val registroInicial = false

                                    dbRegister.collection("usuarios").document(email).set(
                                        hashMapOf(
                                            "Email" to email,
                                            "RegistroInicial" to registroInicial
                                        )
                                    )

                                    // Inicie una nueva actividad para que el usuario complete el formulario
                                    goFormulario(email, "Google")
                                }
                            }
                        }
                    }
                }
            } catch (e: ApiException) {
                Toast.makeText(this, "Error en la conexión", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // [END onactivityresult]



    //Para salir de la app
    override fun onBackPressed() {
        val startMain = Intent(Intent.ACTION_MAIN)
        startMain.addCategory(Intent.CATEGORY_HOME)
        startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(startMain)
    }


    fun registerFun(view: View){
        manageButtonRegister()
    }
    private fun manageButtonRegister(){
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }


    private fun manageButtonLogin(){
        var btnIniciarS =findViewById<TextView>(R.id.btnIniciarS)
        email = etEmail.text.toString()
        password = etPassword.text.toString()

        if(TextUtils.isEmpty(password) || ValidateEmail.isEmail(email) == false) {
            btnIniciarS.setBackgroundColor(ContextCompat.getColor(this, R.color.black))
            btnIniciarS.isEnabled = false

        }else{
            btnIniciarS.setBackground(ContextCompat.getDrawable(this, R.drawable.style_iniciarsesion))
            btnIniciarS.isEnabled = true
        }
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

            }
    }


    private fun goHome(email: String, provider: String){

        usermail = email
        providerSession = provider




        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }


    private fun goFormulario(email: String,provider: String){
        usermail = email
        providerSession = provider

        val intent = Intent(this, FormularioActivity::class.java)
        startActivity(intent)
        finish()

    }


    //Forgot Password -- Actividad para cuando se olvida la contraseña

    fun forgotPassword(view: View){
        //startActivity(Intent(this, ForgotPasswordActivity::class.java))
        resetPassword()
    }

    private fun resetPassword(){
        var em = etEmail.text.toString()
        if (!TextUtils.isEmpty(em)){
            mAuth.sendPasswordResetEmail(em)
                .addOnCompleteListener{task ->
                    if (task.isSuccessful) Toast.makeText(this,"Email enviado a $em", Toast.LENGTH_SHORT).show()
                    else Toast.makeText(this,"No se se encontro el usuario con este correo $em", Toast.LENGTH_SHORT).show()
                }
        }
        else Toast.makeText(this,"Indica un email", Toast.LENGTH_SHORT).show()
    }







}