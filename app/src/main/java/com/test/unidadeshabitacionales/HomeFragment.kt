package com.test.unidadeshabitacionales

import StoreCustomAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.coroutines.launch

class HomeFragment: Fragment() {
    private val viewModel: HomeViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val storeRecyclerView: RecyclerView = view.findViewById(R.id.storeRecyclerView)
        storeRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val storeAdapter = StoreCustomAdapter{ item ->
            // Handle the click event here (you can access the clicked item)
            val gson = Gson()
            val intent = Intent(requireContext(), ProductDetailActivity::class.java)
            intent.putExtra("identifier", gson.toJson(item))
            startActivity(intent)
        }
        storeRecyclerView.adapter = storeAdapter

        val eventsRecyclerView: RecyclerView = view.findViewById(R.id.eventsRecyclerView)
        eventsRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val eventAdapter = EventCustomAdapter()
        eventsRecyclerView.adapter = eventAdapter

        return  view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences =
            this.activity?.getSharedPreferences("MY_SHARED_PREFERENCES", Context.MODE_PRIVATE)
        val storedUser = sharedPreferences?.getString("email", "")
        // Get references to the TextViews
        val nameText: TextView = view.findViewById(R.id.nameText)
        //val unitText: TextView = view.findViewById(R.id.unitText)
        //val adressText: TextView = view.findViewById(R.id.adressText)

        // Temporal


        // Observe LiveData from ViewModel and update UI when data changes
        viewModel.userData.observe(viewLifecycleOwner) { user ->
            nameText.text = "Ricardo Rigel"
            // temporal
            // nameText.text = user?.name ?: ""

           // unitText.text = user?.unit ?: ""
           // adressText.text = user?.apartment ?: ""
        }
        // Observe LiveData from ViewModel and update UI when data changes
        viewModel.storeData.observe(viewLifecycleOwner) { storesDataModel ->
            storesDataModel?.product?.let { products ->
                (view.findViewById<RecyclerView>(R.id.storeRecyclerView).adapter as? StoreCustomAdapter)?.updateData(products)
            }
        }

        viewModel.eventData.observe(viewLifecycleOwner) {eventsDtaModel ->
            eventsDtaModel?.event?.let { events ->
                (view.findViewById<RecyclerView>(R.id.eventsRecyclerView).adapter as? EventCustomAdapter)?.updateData(events)
            }
        }

        // Trigger data loading
        lifecycleScope.launch {
            if (storedUser != null) {
                viewModel.getUser(storedUser)
            }
            viewModel.getStore()
            viewModel.getEvents()
        }
    }
}