package com.test.unidadeshabitacionales

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class EventsFragment : Fragment() {

    private lateinit var calendarView: CalendarView
    private lateinit var eventsRecyclerView: RecyclerView
    private val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    private val viewModel: EventViewModel by viewModels()
    private var data = EventsListDataModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event, container, false)

        calendarView = view.findViewById(R.id.calendarView)
        eventsRecyclerView = view.findViewById(R.id.eventsRecyclerView)
        eventsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        eventsRecyclerView.adapter = EventsAdapter(emptyList())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Observe LiveData from ViewModel and update UI when data changes
        val btnAdd = view.findViewById<Button>(R.id.buttonAdd)

        // Trigger data loading
        lifecycleScope.launch {
            viewModel.getEvent()
        }

        btnAdd.setOnClickListener {
            val gson = Gson()
            val intent = Intent(requireContext(), AddEventActivity::class.java)
            intent.putExtra("identifier", gson.toJson(data))
            startActivity(intent)
        }
        viewModel.eventData.observe(viewLifecycleOwner) { eventDataModel ->
            eventDataModel?.event?.let { _ ->
                data = eventDataModel
                calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
                    val selectedDate = LocalDate.of(year, month + 1, dayOfMonth) // Month is 0-indexed
                    updateEventList(selectedDate, eventDataModel)
                }
            }
        }
    }

    private fun updateEventList(date: LocalDate, data: EventsListDataModel) {
        val eventsForSelectedDate = EventsListDataModel()
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        data.event.forEach { event ->
            val eventDate = try {
                val cleanedDate = event.eventDate.trim() // Elimina posibles espacios en blanco
                println("Parsing date: '$cleanedDate'") // Registro para depuración
                LocalDate.parse(cleanedDate, dateFormatter)
            } catch (e: DateTimeParseException) {
                println("Error parsing date: '${event.eventDate}' - ${e.message}")
                null
            }

            if (eventDate == date) {
                eventsForSelectedDate.event.add(event)
            }
        }

        (eventsRecyclerView.adapter as? EventsAdapter)?.updateEvents(eventsForSelectedDate.event)
    }



    class EventsAdapter(private var events: List<EventsDataModel>) : RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {

        inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val eventTextView: TextView = itemView.findViewById(R.id.eventTextView)
            val areaTextView: TextView = itemView.findViewById(R.id.areaTextView)
            val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.calendar_day_layout, parent, false)
            return EventViewHolder(view)
        }

        override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
            val item = events[position]
            holder.eventTextView.text = item.title
            holder.areaTextView.text = item.area
            holder.timeTextView.text = "${item.startTime} - ${item.endTime}"
        }

        override fun getItemCount(): Int = events.size

        fun updateEvents(newEvents: List<EventsDataModel>) {
            events = newEvents
            notifyDataSetChanged()
        }
    }
}
