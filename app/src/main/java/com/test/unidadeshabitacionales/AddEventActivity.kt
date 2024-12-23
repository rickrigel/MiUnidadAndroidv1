package com.test.unidadeshabitacionales

import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson

class AddEventActivity: AppCompatActivity() {
    private val viewModel: EventViewModel by viewModels()
    private lateinit var dataModel: EventsListDataModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_event_activity) // Replace with the name of your XML layout file

        // Retrieve the passed data
        val gson = Gson()
        dataModel = gson.fromJson(intent.getStringExtra("identifier"), EventsListDataModel::class.java)
        // Initialize Views
        val eventNameEditText = findViewById<EditText>(R.id.eventName)
        val eventAreaEditText = findViewById<EditText>(R.id.eventArea)
        val startTimePicker = findViewById<TimePicker>(R.id.startTimePicker)
        val endTimePicker = findViewById<TimePicker>(R.id.endTimePicker)
        val addEventButton = findViewById<Button>(R.id.addEventButton)
        val eventDatePicker = findViewById<DatePicker>(R.id.eventDatePicker)

        // Set time picker to 24-hour format
        startTimePicker.setIs24HourView(false)
        endTimePicker.setIs24HourView(false)

        // Handle "Agregar Evento" button click
        addEventButton.setOnClickListener {
            // Get values from the input fields
            val eventName = eventNameEditText.text.toString()
            val eventArea = eventAreaEditText.text.toString()

            // Get start time
            val startHour = startTimePicker.hour
            val startMinute = startTimePicker.minute
            val finalStartTime = "$startHour:$startMinute"

            // Get end time
            val endHour = endTimePicker.hour
            val endMinute = endTimePicker.minute
            val finalEndTime = "$endHour:$endMinute"

            // Get selected date
            val day = eventDatePicker.dayOfMonth
            val month = eventDatePicker.month + 1 // Month is zero-based
            val year = eventDatePicker.year
            val finalDate = " $day/$month/$year"

            // Validate input (optional)
            if (eventName.isBlank() || eventArea.isBlank()) {
                Toast.makeText(this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.addEvent(eventName, eventArea, finalStartTime, finalEndTime, finalDate, dataModel)
            // Display collected data
            val eventDetails = """
                Nombre del Evento: $eventName
                Área del Evento: $eventArea
                Hora de Inicio: $startHour:$startMinute
                Hora de Fin: $endHour:$endMinute
                Día del Evento: $day/$month/$year
            """.trimIndent()

            Toast.makeText(this, eventDetails, Toast.LENGTH_LONG).show()
            finish()
        }
    }
}