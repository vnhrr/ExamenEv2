package com.example.examenev2

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

// Clase DatabaseHelper que extiende SQLiteOpenHelper para manejar la base de datos de la aplicación.
class ManejoBBDD(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // Bloque compcolorn object para definir constantes que serán usadas en toda la clase.
    // Son como los valores estáticos en Java
    companion object {
        // Nombre de la base de datos.
        private const val DATABASE_NAME = "BaresDatabase"
        // Versión de la base de datos, útil para manejar actualizaciones esquemáticas.
        private const val DATABASE_VERSION = 1
        // Nombre de la tabla donde se almacenarán los bares.
        private const val TABLE_BAR = "Bares"
        // Nombres de las columnas de la tabla.
        private const val KEY_ID = "id"
        private const val KEY_NOMBRE = "nombre"
        private const val KEY_DIRECCION = "direccion"
        private const val KEY_VALORACION = "val"
        private const val KEY_LATITUD = "lat"
        private const val KEY_LONGITUD = "long"
        private const val KEY_WEB = "web"
    }

    // Método llamado cuando la base de datos se crea por primera vez.
    override fun onCreate(db: SQLiteDatabase) {
        // Define la sentencia SQL para crear la tabla de bares.
        val createBarTable = ("CREATE TABLE " + TABLE_BAR + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NOMBRE + " TEXT,"
                + KEY_DIRECCION + " TEXT,"
                + KEY_VALORACION + " INTEGER,"
                + KEY_LATITUD + " INTEGER,"
                + KEY_LONGITUD + " INTEGER,"
                + KEY_WEB + " TEXT" + ")")

        // Ejecuta la sentencia SQL para crear la tabla.
        db.execSQL(createBarTable)
    }

    // Método llamado cuando se necesita actualizar la base de datos, por ejemplo, cuando se incrementa DATABASE_VERSION.
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Elimina la tabla existente y crea una nueva.
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BAR")
        onCreate(db)
    }

    // Método para obtener todos los bares de la base de datos.
    fun getAllBares(): ArrayList<Bar> {
        // Lista para almacenar y retornar los Gatos.
        val barList = ArrayList<Bar>()
        // Consulta SQL para seleccionar todos los Gatos.
        val selectQuery = "SELECT  * FROM $TABLE_BAR"

        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            // Ejecuta la consulta SQL.
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            // Maneja la excepción en caso de error al ejecutar la consulta.
            db.execSQL(selectQuery)
            return ArrayList()
        }

        // Variables para almacenar los valores de las columnas.
        var id: Int
        var nombre: String
        var dir: String
        var valo: String
        var lat: String
        var long: String
        var web: String

        // Itera a través del cursor para leer los datos de la base de datos.
        if (cursor.moveToFirst()) {
            do {
                // Obtiene los índices de las columnas.
                val idIndex = cursor.getColumnIndex(KEY_ID)
                val nombreIndex = cursor.getColumnIndex(KEY_NOMBRE)
                val dirIndex = cursor.getColumnIndex(KEY_DIRECCION)
                val valIndex = cursor.getColumnIndex(KEY_VALORACION)
                val latIndex = cursor.getColumnIndex(KEY_LATITUD)
                val longIndex = cursor.getColumnIndex(KEY_LONGITUD)
                val webIndex = cursor.getColumnIndex(KEY_WEB)

                // Verifica que los índices sean válidos.
                if (idIndex != -1 && nombreIndex != -1 && dirIndex != -1 && valIndex != -1 && latIndex != -1 && longIndex != -1
                    && webIndex != -1) {
                    // Lee los valores y los añade a la lista de bares.
                    id = cursor.getInt(idIndex)
                    nombre = cursor.getString(nombreIndex)
                    dir = cursor.getString(dirIndex)
                    valo = cursor.getString(valIndex)
                    lat = cursor.getString(latIndex)
                    long = cursor.getString(longIndex)
                    web = cursor.getString(webIndex)


                    val bar = Bar(id = id, nombre_bar = nombre, direccion = dir, valoracion = valo.toInt(), latitud = lat.toInt(),
                        longitud = long.toInt(), web_bar = web)
                    barList.add(bar)
                }
            } while (cursor.moveToNext())
        }

        // Cierra el cursor para liberar recursos.
        cursor.close()
        return barList
    }

    // Método para actualizar un bar en la base de datos.
    fun updateBar(bar: Bar): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        // Prepara los valores a actualizar.
        contentValues.put(KEY_NOMBRE, bar.nombre_bar)
        contentValues.put(KEY_DIRECCION, bar.direccion)

        // Actualiza la fila correspondiente y retorna el número de filas afectadas.
        return db.update(TABLE_BAR, contentValues, "$KEY_ID = ?", arrayOf(bar.id.toString()))
    }

    // Método para eliminar un bar de la base de datos.
    fun deleteBar(bar: Bar): Int {
        val db = this.writableDatabase
        // Elimina la fila correspondiente y retorna el número de filas afectadas.
        val success = db.delete(TABLE_BAR, "$KEY_ID = ?", arrayOf(bar.id.toString()))
        db.close()
        return success
    }

    // Método para añadir un nuevo bar a la base de datos.
    fun addBar(bar: Bar): Long {
        try {
            val db = this.writableDatabase
            val contentValues = ContentValues()
            // Prepara los valores a insertar.
            contentValues.put(KEY_NOMBRE, bar.nombre_bar)
            contentValues.put(KEY_DIRECCION, bar.direccion)
            contentValues.put(KEY_VALORACION, bar.valoracion)
            contentValues.put(KEY_LATITUD, bar.latitud)
            contentValues.put(KEY_LONGITUD, bar.longitud)
            contentValues.put(KEY_WEB, bar.web_bar)

            // Inserta el nuevo bar y retorna el ID del nuevo bar o -1 en caso de error.
            val success = db.insert(TABLE_BAR, null, contentValues)
            db.close()
            return success
        } catch (e: Exception) {
            // Maneja la excepción en caso de error al insertar.
            Log.e("Error", "Error al agregar bar", e)
            return -1
        }
    }
}


