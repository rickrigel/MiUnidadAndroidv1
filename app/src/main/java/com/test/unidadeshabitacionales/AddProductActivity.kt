package com.test.unidadeshabitacionales

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.Gson
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream


class AddProductActivity: AppCompatActivity() {

    private lateinit var btnAddImage: Button
    private lateinit var imgSelected: ImageView
    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var etPrice: EditText
    private lateinit var btnAddProduct: Button

    private var selectedImageUri: Uri? = null
    private lateinit var dataModel: StoresDataModel
    private val viewModel: StoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_product_acivity)
        // Retrieve the passed data
        val gson = Gson()
        dataModel = gson.fromJson(intent.getStringExtra("identifier"), StoresDataModel::class.java)
        // Initialize UI elements
        btnAddImage = findViewById(R.id.addImageButton)
        imgSelected = findViewById(R.id.selectedImage)
        etTitle = findViewById(R.id.titleEditText)
        etDescription = findViewById(R.id.descriptionEditText)
        etPrice = findViewById(R.id.priceEditText)
        btnAddProduct = findViewById(R.id.addProductButton)

        // Set up listeners
        btnAddImage.setOnClickListener { openImagePicker() }
        btnAddProduct.setOnClickListener { uploadProduct() }
    }

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            selectedImageUri = data?.data
            selectedImageUri?.let {
                Glide.with(this).load(it).into(imgSelected)
            }
        } else {
            Toast.makeText(this, "No se pudo hacer la selección de imagen", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        imagePickerLauncher.launch(intent)
    }

    private fun uploadProduct() {
        val title = etTitle.text.toString().trim()
        val description = etDescription.text.toString().trim()
        val priceText = etPrice.text.toString().trim()
        val price = priceText.toIntOrNull()

        if (title.isEmpty() || description.isEmpty() || price == null || selectedImageUri == null) {
            Toast.makeText(this, "Llena todos los campos y selecciona una imagen", Toast.LENGTH_SHORT).show()
            return
        }
        val inputData = contentResolver.openInputStream(selectedImageUri!!)?.readBytes()
        viewModel.uploadProduct(title, description, price.toInt(), inputData!!, dataModel)
        // Simulate product upload (replace this with your actual logic)
        Toast.makeText(
            this,
            "Producto cargado:\nTitle: $title\nDescripción: $description\nPrecio: $price",
            Toast.LENGTH_SHORT
        ).show()
        finish()
    }
}

@Throws(IOException::class)
fun getBytes(inputStream: InputStream): ByteArray {
    val byteBuffer: ByteArrayOutputStream = ByteArrayOutputStream()
    val bufferSize = 1024
    val buffer = ByteArray(bufferSize)

    var len = 0
    while ((inputStream.read(buffer).also { len = it }) != -1) {
        byteBuffer.write(buffer, 0, len)
    }
    return byteBuffer.toByteArray()
}