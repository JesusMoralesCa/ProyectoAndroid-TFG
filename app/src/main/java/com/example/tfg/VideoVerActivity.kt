package com.example.tfg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class VideoVerActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: VideoAdapter
    private lateinit var videoList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_ver)

        recyclerView = findViewById(R.id.recycler_view3)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Aquí obtienes la lista de URLs de video desde Firebase Storage
        videoList = getVideoListFromFirebaseStorage()

        adapter = VideoAdapter(videoList)
        recyclerView.adapter = adapter
    }

    private fun getVideoListFromFirebaseStorage(): ArrayList<String> {
        val videoList = ArrayList<String>()

        // Obtén la referencia a la carpeta en Firebase Storage donde se almacenan los videos
        val storageRef = FirebaseStorage.getInstance().getReference().child("videos")

        // Obtén la lista de archivos en la carpeta
        storageRef.listAll().addOnSuccessListener { listResult ->
            // Itera sobre cada archivo en la lista
            for (item in listResult.items) {
                // Obtén la URL de descarga del archivo
                item.downloadUrl.addOnSuccessListener { uri ->
                    // Agrega la URL a la lista de videos
                    videoList.add(uri.toString())

                    // Notifica al adaptador del RecyclerView para actualizar la lista
                    adapter.notifyDataSetChanged()
                }
            }
        }.addOnFailureListener { exception ->
            // Manejo de errores si ocurre algún problema al obtener la lista de archivos
            // Puedes mostrar un mensaje de error o realizar alguna otra acción aquí
        }

        return videoList
    }
}
