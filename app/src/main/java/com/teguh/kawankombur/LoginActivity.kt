package com.teguh.kawankombur

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import dmax.dialog.SpotsDialog

class LoginActivity : AppCompatActivity() {

    // Properti
    private lateinit var mAuth: FirebaseAuth // untuk autentikasi
    private lateinit var refUsers: DatabaseReference // instance dari class DatabaseReference -> realtime database
    private var firebaseUserId: String = "" // id_user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // deklrarasi variabel xml login
        val loginButton = findViewById<Button>(R.id.login_button)

        // Deklarasi variabel Toolbar setting
        val toolbar: Toolbar = findViewById(R.id.login_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Login" // judul toolbar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // panah kiri kembali
        // ketika toolbar panah diklik
        toolbar.setNavigationOnClickListener(){
            val intent = Intent(this@LoginActivity, WelcomeActivity::class.java) // variabel intent ke WelcomeActivity.class
            startActivity(intent) // mulai eksekusi perpindahan activity
            finish() // selesai (destroy activity ini), jangan eksekusi kode setelah ini
        }

        // instance objek dari class FirebaseAuth
        mAuth = FirebaseAuth.getInstance()

        // jalankan function login ketika button_login diklik
        loginButton.setOnClickListener(){
            loginUser()
        }

    }

    private fun loginUser(){
        val emailEdit = findViewById<EditText>(R.id.email_edit)
        val passwordEdit = findViewById<EditText>(R.id.password_edit)

        // buat variabel yg menampung nilai inputan editText
        val email: String = emailEdit.text.toString()
        val password: String = passwordEdit.text.toString()

        // dekarasi dialog loading
        val dialog: android.app.AlertDialog = SpotsDialog.Builder().setContext(this).build()

        // lakukan pengecekan
        if (email == ""){
            Toast.makeText(this@LoginActivity, "Silahkan isi email terlebih dahulu", Toast.LENGTH_SHORT).show()
        }else if (password == ""){
            Toast.makeText(this@LoginActivity, "Silahkan isi password terlebih dahulu", Toast.LENGTH_SHORT).show()
        } else {
            // buat ekspresi sign in melalui akun auth yg terdaftar difirebase
            /**
             * @param : email dari editText
             * @param : password dari editText
             */
            dialog.show()
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{ task->
                if (task.isSuccessful){
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }else {
                    dialog.cancel()
                    Toast.makeText(this@LoginActivity, "Cek Kembali username dan password", Toast.LENGTH_SHORT).show()
                    Log.e("Task Login", "error message: " +task.exception!!.message.toString())
                }
            }
        }
    }
}