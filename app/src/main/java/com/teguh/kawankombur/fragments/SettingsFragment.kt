package com.teguh.kawankombur.fragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.teguh.kawankombur.R
import com.teguh.kawankombur.model.Users
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.view.*

class SettingsFragment : Fragment() {

    // primary konstruktor
    var refUsers: DatabaseReference? = null
    var firebaseUser: FirebaseUser? = null
    private val requestCodeProperti = 438 // request code, sebagai penanda perpindahan data intent
    private var imageUri: Uri? = null // variabel penampung gambar yg dipilih
    private var storageRef: StorageReference? = null
    private var coverCheck: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_settings, container, false)

        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid) // path database
        storageRef = FirebaseStorage.getInstance().reference.child("User Image") // path storage

        return view
    }

    //function untuk mengambil gambar dari galeri secara intent implisit
    private fun ambilGambar() {
        val intent = Intent() // objek dari class intent
        intent.type = "image/*" // semua jenis gambar
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, requestCodeProperti)
    }


    // upload data ke storage
    private fun uploadKeStorage() {
        val progressBar = ProgressDialog(context)
        progressBar.setMessage("Gambar sedang diupload...")
        progressBar.show()

        if (imageUri != null){
            val fileRef = storageRef!!.child(System.currentTimeMillis().toString() + ".jpg") // membuat nama file menjadi detikskrg.jpg

            var uploadTask: StorageTask<*>
            uploadTask = fileRef.putFile(imageUri!!) // upload task valuenya skrg gambar yg dipilih pengguna

            uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>> { task->
                if (!task.isSuccessful){
                    task.exception?. let {
                        throw it
                    }
                }
                return@Continuation fileRef.downloadUrl
            }).addOnCompleteListener() { task->
                if (task.isSuccessful){
                    val downloadUrl = task.result
                    val url = downloadUrl.toString()

                    if (coverCheck == "cover"){
                        val mapCover = HashMap<String, Any>()
                        mapCover["cover"] = url
                        refUsers!!.updateChildren(mapCover)
                        coverCheck = ""
                    } else{
                        val mapPicture = HashMap<String, Any>()
                        mapPicture["picture"] = url
                        refUsers!!.updateChildren(mapPicture)
                        coverCheck = ""
                    }
                    progressBar.dismiss()
                }
            }

        }
    }


}