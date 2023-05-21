package com.example.tfg
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UploadActivity : AppCompatActivity() {
    private lateinit var bottonVideo: Button
    private lateinit var uploadButton: FloatingActionButton
    private lateinit var uploadVideo: VideoView
    private lateinit var uploadCaption: EditText
    private lateinit var progressBar: ProgressBar
    private var videoUri: Uri? = null
    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("Videos")
    private val storageReference: StorageReference = FirebaseStorage.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        uploadButton = findViewById(R.id.uploadButton)
        uploadCaption = findViewById(R.id.uploadCaption)
        uploadVideo = findViewById(R.id.uploadVideo)
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.INVISIBLE
        bottonVideo = findViewById(R.id.bottonVideo)

        val activityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            object : ActivityResultCallback<ActivityResult?> {
                override fun onActivityResult(result: ActivityResult?) {
                    if (result?.resultCode == Activity.RESULT_OK) {
                        val data = result.data
                        videoUri = data?.data
                        uploadVideo.setVideoURI(videoUri)
                    } else {
                        Toast.makeText(this@UploadActivity, "No Video Selected", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        )



        bottonVideo.setOnClickListener {
            val videoPicker = Intent()
            videoPicker.action = Intent.ACTION_GET_CONTENT
            videoPicker.type = "video/*"
            activityResultLauncher.launch(videoPicker)
            Toast.makeText(this@UploadActivity, "Launching Activity", Toast.LENGTH_SHORT).show()

        }

        uploadButton.setOnClickListener {
            if (videoUri != null) {
                uploadToFirebase(videoUri!!)
            } else {
                Toast.makeText(this@UploadActivity, "Please select video", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun uploadToFirebase(uri: Uri) {
        val caption = uploadCaption.text.toString()
        val videoReference =
            storageReference.child("${System.currentTimeMillis()}.mp4")

        videoReference.putFile(uri).addOnSuccessListener { taskSnapshot ->
            videoReference.downloadUrl.addOnSuccessListener { uri ->
                val dataClass = DataClass(uri.toString(), caption)
                val key = databaseReference.push().key
                databaseReference.child(key!!).setValue(dataClass)
                progressBar.visibility = View.INVISIBLE
                Toast.makeText(this@UploadActivity, "Uploaded", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@UploadActivity, FilesActivity::class.java)
                startActivity(intent)
                finish()


            }
        }


    }
}