package com.teguh.kawankombur

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    // Properti
    private lateinit var mAuth: FirebaseAuth // untuk autentikasi
    private lateinit var refUsers: DatabaseReference // instance dari class DatabaseReference -> realtime database
    private var firebaseUserId: String = "" // id_user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // deklrarasi variabel xml register
        val registerButton = findViewById<Button>(R.id.register_button)

        // deklarasi dialog loading
        val dialog: android.app.AlertDialog? = SpotsDialog.Builder().setContext(this).build()

        // Deklarasi variabel Toolbar setting
        val toolbar: Toolbar = findViewById(R.id.register_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Register" // judul toolbar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // panah kiri kembali
        // ketika toolbar panah diklik
        toolbar.setNavigationOnClickListener(){
            val intent = Intent(this@RegisterActivity, WelcomeActivity::class.java) // variabel intent ke WelcomeActivity.class
            startActivity(intent) // mulai eksekusi perpindahan activity
            finish() // selesai (destroy activity ini), jangan eksekusi kode setelah ini
        }

        // instance objek dari class FirebaseAuth
        mAuth = FirebaseAuth.getInstance()

        // ketika button registes ditap
        registerButton.setOnClickListener(){
            dialog!!.show()
            registerUser()
        }

    }

    // function registrasi user
    private fun registerUser(){
        val emailEdit = findViewById<EditText>(R.id.email_edit)
        val usernameEdit = findViewById<EditText>(R.id.username_edit)
        val passwordEdit = findViewById<EditText>(R.id.password_edit)

        // buat variabel yg menampung nilai inputan editText
        val email: String = emailEdit.text.toString()
        val username: String = usernameEdit.text.toString()
        val password: String = passwordEdit.text.toString()

        // cek apakah inputan pada masing-masing editText kosong
        if (email == ""){
            Toast.makeText(this@RegisterActivity, "Silahkan isi email terlebih dahulu", Toast.LENGTH_SHORT).show()
        }else if (username == ""){
            Toast.makeText(this@RegisterActivity, "Silahkan isi username terlebih dahulu", Toast.LENGTH_SHORT).show()
        }else if (password == ""){
            Toast.makeText(this@RegisterActivity, "Silahkan isi password terlebih dahulu", Toast.LENGTH_SHORT).show()
        }else {
            // buat ekspresi masukkan kefirebase
            /**
             * @param : email dari editText
             * @param : password dari editText
             */
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{ task ->
                if (task.isSuccessful){

                    firebaseUserId = mAuth.currentUser!!.uid // tangkap uid yg diambil dari autentikasi

                    // ganti nilai refUsers dengan instance FirebaseDatabase dan buat child Users->uid
                    refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserId)

                    // buat collection HashMap yg menampung data user utk dimasukka ke db
                    /**
                     * @param : String sebagai key nya itu = string
                     * @param :  Any sebagai valuenya boleh objek apapun
                     */
                    val userHashMap = HashMap<String, Any>()
                    userHashMap["uid"] = firebaseUserId
                    userHashMap["email"] = email
                    userHashMap["username"] = username
                    userHashMap["password"] = password
                    userHashMap["picture"] = "gs://kawankombur.appspot.com/profile.png"
                    userHashMap["cover"] = "gs://kawankombur.appspot.com/cover.jpg"
                    userHashMap["status"] = "offline"
                    userHashMap["seacrh"] = username.toLowerCase()
                    userHashMap["facebook"] = "https://m.facebook.com"
                    userHashMap["instagram"] = "htrps://m.instagram.com"
                    userHashMap["website"] = "https://www.github.com/teguhkrniawan"

                    // lakukan update pada child refUsers
                    /**
                     * @param : userHashMap colletcion data user
                     */
                    refUsers.updateChildren(userHashMap).addOnCompleteListener{ task->
                        if (task.isSuccessful){
                            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        }
                    }

                }else {
                    Toast.makeText(this@RegisterActivity, "Gagal Mendaftar Akun", Toast.LENGTH_SHORT).show()
                    Log.e("Task Register", "error message: " +task.exception!!.message.toString())
                }
            }
        }
    }
}