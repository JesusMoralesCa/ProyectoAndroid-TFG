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
import com.example.tfg.ListaProfesoresActivity.Companion.username

class ListaAlumnosActivity : AppCompatActivity() {

    private lateinit var alumRecyclerView: RecyclerView
    private lateinit var tv2LoadingData: TextView
    private lateinit var profList: ArrayList<Model>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_alumnos)


        alumRecyclerView = findViewById(R.id.rvAlum)
        alumRecyclerView.layoutManager = LinearLayoutManager(this)
        alumRecyclerView.setHasFixedSize(true)
        tv2LoadingData = findViewById(R.id.tv2LoadingData)
        profList = arrayListOf<Model>()

        getAlumData()
    }


    private fun getAlumData() {
        alumRecyclerView.visibility = View.GONE
        tv2LoadingData.visibility = View.VISIBLE
        FirebaseFirestore.getInstance().collection("/Sensei/JesusMorales/Profesores/" + username +"/Alumnos").get()
            .addOnSuccessListener { teachers ->

                for (profSnap in teachers) {
                    val profData = profSnap.toObject(Model::class.java)
                    profList.add(profData)
                }
                val mAdapter = profAdapter(profList)
                alumRecyclerView.adapter = mAdapter


                alumRecyclerView.visibility = View.VISIBLE
                tv2LoadingData.visibility = View.GONE

            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error ${exception}", Toast.LENGTH_SHORT).show()
            }

    }


}