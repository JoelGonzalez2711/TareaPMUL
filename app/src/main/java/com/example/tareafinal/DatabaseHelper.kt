package com.example.tareafinal

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        // Crear tabla para usuarios
        db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT)")

        // Crear tabla para contraseñas
        db.execSQL("CREATE TABLE passwords (userid INTEGER, password TEXT, FOREIGN KEY(userid) REFERENCES users(id))")
        insertDummyData(db)
    }

    private fun insertDummyData(db: SQLiteDatabase) {
        // Datos de usuario de prueba
        val username = "testuser"
        val password = "testpassword" // En un caso real, esto debería ser un hash

        // Insertar en la tabla de usuarios
        val valuesUser = ContentValues()
        valuesUser.put("username", username)
        val userId = db.insert("users", null, valuesUser)

        if (userId != -1L) {
            Log.d("DatabaseHelper", "Usuario insertado correctamente.")
        } else {
            Log.e("DatabaseHelper", "Error al insertar usuario.")
        }

        // Insertar en la tabla de contraseñas
        val valuesPassword = ContentValues()
        valuesPassword.put("userid", userId)
        valuesPassword.put("password", password)
        val passwordId = db.insert("passwords", null, valuesPassword)

        if (passwordId != -1L) {
            Log.d("DatabaseHelper", "Contraseña insertada correctamente.")
        } else {
            Log.e("DatabaseHelper", "Error al insertar contraseña.")
        }
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Eliminar las tablas existentes
        db.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS passwords")

        // Crear nuevas tablas
        onCreate(db)
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "UserDatabase.db"
    }
}

