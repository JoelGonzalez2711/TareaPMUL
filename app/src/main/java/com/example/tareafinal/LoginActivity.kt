package com.example.tareafinal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.database.sqlite.SQLiteDatabase


class LoginActivity : AppCompatActivity() {

    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var database: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {

        val sharedPrefs = getSharedPreferences("MyAppSettings", MODE_PRIVATE)
        if (sharedPrefs.getBoolean("DarkThemeEnabled", false)) {
            setTheme(R.style.AppTheme_Dark)
        } else {
            setTheme(R.style.AppTheme)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editTextUsername = findViewById(R.id.editTextUsername)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        dbHelper = DatabaseHelper(this)
        database = dbHelper.readableDatabase

        buttonLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun validateLogin(username: String, password: String): Boolean {
        val cursor = database.rawQuery("SELECT u.id FROM users u INNER JOIN passwords p ON u.id = p.userid WHERE u.username = ? AND p.password = ?", arrayOf(username, password))
        val isAuthenticated = cursor.count > 0
        cursor.close()
        return isAuthenticated
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}
