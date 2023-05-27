package com.example.tfg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg.adapters.profAdapter
import com.example.tfg.models.Model
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.properties.Delegates

class AddProfActivity : AppCompatActivity() {
    private lateinit var AlumRecyclerView: RecyclerView
    private lateinit var tvLoadingDataAlum: TextView
    private lateinit var AlumList: ArrayList<Model>
    private lateinit var AlumElegido:TextView
    private var selectecAlumName by Delegates.notNull<String>()
    private var selectecAlumApellido by Delegates.notNull<String>()
    private var selectecAlum by Delegates.notNull<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_prof)

        AlumRecyclerView = findViewById(R.id.rvProfADAdd)
        AlumRecyclerView.layoutManager = LinearLayoutManager(this)
        AlumRecyclerView.setHasFixedSize(true)
        tvLoadingDataAlum = findViewById(R.id.tvLoadingDataProfAdd)
        AlumList = arrayListOf<Model>()
        AlumElegido = findViewById(R.id.ProfElegidoAdd)
        getProfData()

    }

    private fun getProfData() {
        AlumRecyclerView.visibility = View.GONE
        tvLoadingDataAlum.visibility = View.VISIBLE
        FirebaseFirestore.getInstance().collection("/Sensei/JesusMorales/Profesores/SinProfesor/Alumnos").get()
            .addOnSuccessListener { teachers ->

                for (profSnap in teachers) {
                    val profData = profSnap.toObject(Model::class.java)
                    AlumList.add(profData)
                }
                val mAdapter = profAdapter(AlumList)
                AlumRecyclerView.adapter = mAdapter

                mAdapter.setOnItemClickListener(object : profAdapter.onItemClickListener {
                    override fun onItemClick(position: Int) {
                        selectecAlumName = AlumList[position].Nombre.toString()
                        selectecAlumApellido = AlumList[position].Apellido1.toString()
                        selectecAlum = selectecAlumName + selectecAlumApellido
                        AlumElegido.text = selectecAlumName + selectecAlumApellido
                    }
                })

                AlumRecyclerView.visibility = View.VISIBLE
                tvLoadingDataAlum.visibility = View.GONE

            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error ${exception}", Toast.LENGTH_SHORT).show()
            }

    }


    fun CallProfAdd(view: View){

        ProfAdd()
    }


    private fun ProfAdd() {
        // Obtén la instancia de la base de datos de Firebase
        val db = FirebaseFirestore.getInstance()

        // Define la ruta del documento a mover
        val rutaDocumentoDestino = "/Sensei/JesusMorales/Profesores/$selectecAlum"
        val rutaDocumentoOrigen = "/Sensei/JesusMorales/Profesores/SinProfesor/Alumnos/$selectecAlum"

        // Obtiene una referencia al documento de origen
        val documentoOrigenRef = db.document(rutaDocumentoOrigen)

        // Obtiene una referencia al documento de destino
        val documentoDestinoRef = db.document(rutaDocumentoDestino)

        // Obtiene los datos del documento de origen
        documentoOrigenRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val datos = documentSnapshot.data
                datos?.set("Profesor", "JesusMorales")
                datos?.set("Titulo", "Profesor")
                val email = datos?.get("Email") as? String
                var permisos = "Profesor"
                val updateData = hashMapOf<String, Any>(
                    "Permisos" to permisos,
                    "Profesor" to "JesusMorales"
                )

                if (email != null) {
                    db.collection("usuarios").document(email).update(updateData)
                        .addOnSuccessListener {
                            // Actualización exitosa
                        }
                        .addOnFailureListener { exception ->
                            // Error al actualizar
                        }
                }


                // Mueve el documento a la colección de destino
                documentoDestinoRef.set(datos!!).addOnSuccessListener {
                    // Documento movido exitosamente

                    // Elimina el documento original de la colección de origen
                    documentoOrigenRef.delete().addOnSuccessListener {
                        Toast.makeText(this, "Documento movido exitosamente", Toast.LENGTH_SHORT).show()
                        // Documento original eliminado exitosamente
                    }.addOnFailureListener { exception ->
                        Toast.makeText(this, "Error al eliminar el documento original", Toast.LENGTH_SHORT).show()
                        // Error al eliminar el documento original
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(this, "Error al mover el documento", Toast.LENGTH_SHORT).show()
                    // Error al mover el documento
                }
            } else {
                Toast.makeText(this, "El documento de origen no existe", Toast.LENGTH_SHORT).show()
                // El documento de origen no existe
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(this, "Error al obtener el documento de origen", Toast.LENGTH_SHORT).show()
            // Error al obtener el documento de origen
        }
    }


////////////


}


