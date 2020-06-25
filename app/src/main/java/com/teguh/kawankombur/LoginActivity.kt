package com.teguh.kawankombur

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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
    }
}