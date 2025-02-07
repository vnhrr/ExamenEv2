package com.example.examenev2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    // Declara variables para los elementos de la interfaz de usuario y la base de datos.
    // lateinit indica que estas variables se inicializarán más tarde.
    private lateinit var etNombre: EditText
    private lateinit var etDir: EditText
    private lateinit var etWeb: EditText
    private lateinit var etVal: EditText
    private lateinit var btnAgregar: Button
    private lateinit var btnVerTodos: Button
    private lateinit var btnEliminar: Button
    private lateinit var btnModificar: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var dbHandler: ManejoBBDD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializa las variables con los elementos de la interfaz de usuario.
        etNombre = findViewById(R.id.etNombreBar)
        etDir = findViewById(R.id.etNombreBar)
        etWeb = findViewById(R.id.etWebBar)
        etVal = findViewById(R.id.etValoBar)
        btnAgregar = findViewById(R.id.btnAdd)
        btnVerTodos = findViewById(R.id.btnViewAll)
        recyclerView = findViewById(R.id.rvBares)
        btnEliminar = findViewById(R.id.btnDel)
        btnModificar = findViewById(R.id.btnModif)

        // Inicializa el controlador de la base de datos.
        dbHandler = ManejoBBDD(this)

        // Configura los eventos de clic para los botones.
        btnAgregar.setOnClickListener {
            addBar()
        }
        btnVerTodos.setOnClickListener {
            viewBares() }
        btnEliminar.setOnClickListener {
            deleteBar()
        }
        btnModificar.setOnClickListener {
            modifyBar()
        }
        // Muestra la lista de Gatos al iniciar la actividad.
        viewBares()
    }
    // Método para agregar un nuevo Gato a la base de datos.
    private fun addBar(): Boolean {
        // Obtiene el texto de los EditText y lo convierte en String.
        val nombre = etNombre.text.toString()
        val dir = etDir.text.toString()
        val web = etWeb.text.toString()
        val valo = etVal.text.toString()
        val lat = etVal.text.toString()
        val long = etVal.text.toString()
        // Verifica que los campos no estén vacíos.
        if (nombre.isNotEmpty() && dir.isNotEmpty() && web.isNotEmpty() && valo.isNotEmpty()) {
            // Crea un objeto Gato y lo añade a la base de datos.
            val bar = Bar(nombre_bar = nombre, direccion = dir, web_bar = web, valoracion = valo.toInt(), latitud = valo.toInt(), longitud = valo.toInt())
            val status = dbHandler.addBar(bar)
            // Verifica si la inserción fue exitosa.
            if (status > -1) {
                Toast.makeText(applicationContext, "Bar agregado", Toast.LENGTH_LONG).show()
                // Limpia los campos de texto y actualiza la vista de Gatos.
                clearEditTexts()
                viewBares()
                return true
            }
        } else {
            // Muestra un mensaje si los campos están vacíos.
            Toast.makeText(
                applicationContext,
                "Nombre, direccion, web y valoracion son requeridos",
                Toast.LENGTH_LONG
            ).show()
            return false
        }
        return false
    }

    // Método para mostrar todos los bares en el RecyclerView.
    private fun viewBares() {
        // Obtiene la lista de bares de la base de datos.
        val baresList = dbHandler.getAllBares()
        // Crea un adaptador para el RecyclerView y lo configura.
        val adapter = BaresAdapter(baresList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }


    private fun deleteBar(): Boolean{
        val nombre = etNombre.text.toString()
        val baresList = dbHandler.getAllBares()
        val barBorrar = baresList.find { bar -> bar.nombre_bar == nombre}
        if (barBorrar == null) {
            Toast.makeText(applicationContext, "El bar no existe", Toast.LENGTH_LONG).show()
            return false
        }else{
            val status = dbHandler.deleteBar(barBorrar)
            if (status > -1) {
                Toast.makeText(applicationContext, "Bar eliminado", Toast.LENGTH_LONG).show()
                // Limpia los campos de texto y actualiza la vista de Gatos.
                clearEditTexts()
                viewBares()
                return true
            }
            return false

        }

    }
    private fun modifyBar(): Boolean {
        val nombre = etNombre.text.toString()
        val dir = etDir.text.toString()
        val web = etWeb.text.toString()
        val valo = etVal.text.toString()
        val baresList = dbHandler.getAllBares()
        val barMod = baresList.find { bar -> bar.nombre_bar == nombre}
        if (barMod == null) {
            Toast.makeText(applicationContext,"El bar no existe" , Toast.LENGTH_LONG).show()
            return false
        }else{
            barMod.direccion = dir
            barMod.web_bar = web
            barMod.valoracion = valo.toInt()
            val status = dbHandler.updateBar(barMod)
            if (status > -1) {
                Toast.makeText(applicationContext, "Gato modificado correctamente", Toast.LENGTH_LONG).show()
                clearEditTexts()
                viewBares()
                return true
            }
            return false

        }
    }
    // Método para limpiar los campos de texto.
    private fun clearEditTexts() {
        etNombre.text.clear()
        etDir.text.clear()
        etWeb.text.clear()
        etVal.text.clear()
    }
}