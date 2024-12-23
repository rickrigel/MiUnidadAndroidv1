package com.test.unidadeshabitacionales

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import kotlinx.coroutines.launch

class AddNoticeActivity: AppCompatActivity() {
    private val viewModel: AnnauncementsViewModel by viewModels()
    private lateinit var dataModel: AnnauncementsListDataModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_notice_activity)

        // Retrieve the passed data
        val gson = Gson()
        dataModel = gson.fromJson(intent.getStringExtra("identifier"), AnnauncementsListDataModel::class.java)
        // Reference to the views
        val titleEditText: EditText = findViewById(R.id.titleEditText)
        val descriptionEditText: EditText = findViewById(R.id.descriptionEditText)
        val addNoticeButton: Button = findViewById(R.id.addNoticeButton)

        // Set a click listener for the button
        addNoticeButton.setOnClickListener {
            val title = titleEditText.text.toString().trim()
            val description = descriptionEditText.text.toString().trim()

            if (title.isEmpty() || description.isEmpty()) {
                // Show an error if fields are empty
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                // Perform the add notice action
                viewModel.addNotice(title, description, dataModel)
                finish()
            }
        }
    }
}