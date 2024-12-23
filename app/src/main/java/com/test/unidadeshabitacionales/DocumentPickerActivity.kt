package com.test.unidadeshabitacionales

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast

class DocumentPickerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Call the function to open the document picker
        selectDocumentOrImage()
    }

    private fun selectDocumentOrImage() {
        // Intent to open documents and images
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "*/*"  // Allows all types of files
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*", "application/pdf", "text/plain"))
            addCategory(Intent.CATEGORY_OPENABLE)
        }

        // Launch the document picker
        documentPickerLauncher.launch(intent)
    }

    // Registering the document picker result
    private val documentPickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                handleDocumentUri(uri)
            }
        } else {
            Toast.makeText(this, "Selección de documento cancelado", Toast.LENGTH_SHORT).show()
        }
    }

    // Handle the selected document URI
    private fun handleDocumentUri(uri: Uri) {
        // You can now use the selected file URI for further processing
        Toast.makeText(this, "Seleccione ubicación del archivo: $uri", Toast.LENGTH_LONG).show()
        // Add further handling here, such as displaying the file or uploading it
    }
}
