package com.teguh.kawankombur

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class WelcomeActivity : AppCompatActivity() {

    // variabel yg menampung data user dari firebase
    var firebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val loginButton = findViewById<Button>(R.id.login_button)
        val registerButton = findViewById<Button>(R.id.register_button)

        // ketika login_button di klik
        loginButton.setOnClickListener(){
            val intent = Intent(this@WelcomeActivity, LoginActivity::class.java) // variabel intent ke LoginActivity.class
            startActivity(intent) // mulai eksekusi perpindahan activity
            finish() // selesai (destroy activity ini), jangan eksekusi kode setelah ini
        }

        // ketika register_button di klik
        registerButton.setOnClickListener(){
            val intent = Intent(this@WelcomeActivity, RegisterActivity::class.java) // variabel intent ke RegisterActivity.class
            startActivity(intent) // mulai eksekusi perpindahan activity
            finish() // selesai (destroy activity ini), jangan eksekusi kode setelah ini
        }
    }

    // setelah melakukan onCreate()
    override fun onStart() {
        super.onStart()

        // ubah value firebaseUser
        firebaseUser = FirebaseAuth.getInstance().currentUser //user terbaru dari auth

        // lakukan pengecekan
        if (firebaseUser != null){
            val intent = Intent(this@WelcomeActivity, MainActivity::class.java) // variabel intent ke MainActivity.class
            startActivity(intent) // mulai eksekusi perpindahan activity
            finish() // selesai, jangan eksekusi kode setelah ini
        }
    }
}