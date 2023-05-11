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

class ListaProfesoresActivity : AppCompatActivity() {

    private lateinit var profRecyclerView: RecyclerView
    private lateinit var  tvLoadingData: TextView
    private lateinit var profList: ArrayList<Model>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_profesores)

        profRecyclerView = findViewById(R.id.rvProf)
        profRecyclerView.layoutManager = LinearLayoutManager(this)
        profRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        profList = arrayListOf<Model>()

        getProfData()

    }

    private fun getProfData(){
        profRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        FirebaseFirestore.getInstance().collection("/Sensei/JesusMorales/Profesores").get()
            .addOnSuccessListener {teachers ->

                    for (profSnap in teachers){
                        val profData = profSnap.toObject(Model::class.java)
                        profList.add(profData)
                    }
                    val mAdapter = profAdapter(profList)
                    profRecyclerView.adapter = mAdapter

                    profRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE


            }
            .addOnFailureListener { exception ->Toast.makeText(this, "Error ${exception}", Toast.LENGTH_SHORT).show()}

    }

}
