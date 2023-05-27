package com.example.tfg

import android.content.Intent
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

class DeleteAddProfActivity : AppCompatActivity() {
    private lateinit var profRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var profList: ArrayList<Model>
    private lateinit var profElegido:TextView
    private var selectecProfName by Delegates.notNull<String>()
    private var selectecProfApellido by Delegates.notNull<String>()
    private var selectecProf by Delegates.notNull<String>()
    companion object {


        lateinit var profesorSend: String

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_add_prof)

        profRecyclerView = findViewById(R.id.rvProfAD)
        profRecyclerView.layoutManager = LinearLayoutManager(this)
        profRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingDataAD)
        profList = arrayListOf<Model>()
        profElegido = findViewById(R.id.ProfElegido)
        getProfData()

    }

    private fun getProfData() {
        profRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE
        FirebaseFirestore.getInstance().collection("/Sensei/JesusMorales/Profesores").get()
            .addOnSuccessListener { teachers ->

                for (profSnap in teachers) {
                    val profData = profSnap.toObject(Model::class.java)
                    profList.add(profData)
                }
                val mAdapter = profAdapter(profList)
                profRecyclerView.adapter = mAdapter

                mAdapter.setOnItemClickListener(object : profAdapter.onItemClickListener {
                    override fun onItemClick(position: Int) {
                        selectecProfName = profList[position].Nombre.toString()
                        selectecProfApellido = profList[position].Apellido1.toString()
                        selectecProf = selectecProfName + selectecProfApellido
                        profElegido.text = selectecProfName + selectecProfApellido
                    }
                })

                profRecyclerView.visibility = View.VISIBLE
                tvLoadingData.visibility = View.GONE

            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error ${exception}", Toast.LENGTH_SHORT).show()
            }

    }


    fun CallEliminarProfesor(view: View){

        EliminarProfesor()
    }


    private fun EliminarProfesor() {
        // Obtén la instancia de la base de datos de Firebase
        val db = FirebaseFirestore.getInstance()

        // Define la ruta de la colección de origen y la colección de destino
        val rutaColeccionOrigen = "/Sensei/JesusMorales/Profesores/$selectecProf/Alumnos"
        val rutaColeccionDestino = "/Sensei/JesusMorales/Profesores/SinProfesor/Alumnos"
        val rutaEliminar = "/Sensei/JesusMorales/Profesores/$selectecProf"

        // Obtiene una referencia a la colección de origen
        val coleccionOrigenRef = db.collection(rutaColeccionOrigen)

        // Obtiene todos los documentos de la colección de origen
        coleccionOrigenRef.get().addOnSuccessListener { querySnapshot ->
            // Recorre cada documento de la consulta
            for (documentSnapshot in querySnapshot) {
                val datos = documentSnapshot.data
                datos?.set("Profesor", "SinProfesor")
                val nombre = datos["Nombre"].toString() + datos["Apellido1"].toString()

                // Crea una referencia al nuevo documento en la colección de destino
                val nuevoDocRef = db.collection(rutaColeccionDestino).document(documentSnapshot.id)

                // Escribe los datos en el nuevo documento
                nuevoDocRef.set(datos).addOnSuccessListener {
                    // Documento movido exitosamente

                    // Opcional: Elimina el documento original de la colección de origen
                    documentSnapshot.reference.delete().addOnSuccessListener {
                        Toast.makeText(this, "Documento original eliminado exitosamente", Toast.LENGTH_SHORT).show()
                        // Documento original eliminado exitosamente
                    }.addOnFailureListener { exception ->
                        Toast.makeText(this, "Error al eliminar el documento original", Toast.LENGTH_SHORT).show()
                        // Error al eliminar el documento original
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(this, "Error al escribir los datos en el nuevo documento", Toast.LENGTH_SHORT).show()
                    // Error al escribir los datos en el nuevo documento
                }
            }

            eliminarColeccion(rutaEliminar)
        }.addOnFailureListener { exception ->
            Toast.makeText(this, "Error al obtener los documentos de la colección de origen", Toast.LENGTH_SHORT).show()
            // Error al obtener los documentos de la colección de origen
        }
    }


    private fun eliminarColeccion(rutaColeccion: String) {
        // Obtén la instancia de la base de datos de Firebase
                val db = FirebaseFirestore.getInstance()

        // Define la ruta del documento original y la colección de destino
                val rutaDocumentoOriginal = rutaColeccion
                val rutaColeccionDestino = "/Sensei/JesusMorales/Profesores/SinProfesor/Alumnos"

        // Obtiene una referencia al documento original
                val docRef = db.document(rutaDocumentoOriginal)

        // Obtiene los datos del documento original
                docRef.get().addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val datos = documentSnapshot.data
                        datos?.set("Profesor", "SinProfesor")


                        // Crea una referencia al nuevo documento en la colección de destino
                        val nuevoDocRef = db.collection(rutaColeccionDestino).document(documentSnapshot.id)

                        // Escribe los datos en el nuevo documento
                        if (datos != null) {
                            nuevoDocRef.set(datos).addOnSuccessListener {
                                // Documento movido exitosamente

                                // Opcional: Elimina el documento original de la colección original
                                docRef.delete().addOnSuccessListener {
                                    // Documento original eliminado exitosamente
                                }.addOnFailureListener { exception ->
                                    // Error al eliminar el documento original
                                }
                            }.addOnFailureListener { exception ->
                                // Error al escribir los datos en el nuevo documento
                            }
                        }
                    } else {
                        // El documento original no existe
                    }
                }.addOnFailureListener { exception ->
                    // Error al obtener los datos del documento original
                }

    }
////////////




    fun CallGestionarAlumno(view: View){

        GestionarAlumno(selectecProf)
    }



    private fun GestionarAlumno(profesor: String){

        profesorSend = profesor

        val intent = Intent(this, GestionarAlumnoActivity::class.java)
        startActivity(intent)
    }



    fun CallAddProf(view: View){

        AddProf()
    }



    private fun AddProf(){


        val intent = Intent(this, AddProfActivity::class.java)
        startActivity(intent)
    }


}


