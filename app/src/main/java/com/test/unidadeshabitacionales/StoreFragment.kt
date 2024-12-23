package com.test.unidadeshabitacionales

import StoreCustomAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.coroutines.launch


class StoreFragment : Fragment() {
    private val viewModel: StoreViewModel by viewModels()
    private var data = StoresDataModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val gson = Gson()
        val view = inflater.inflate(R.layout.fragment_store, container, false)

        val btnAdd = view.findViewById<Button>(R.id.buttonAdd)

        btnAdd.setOnClickListener {
            val intent = Intent(requireContext(), AddProductActivity::class.java)
            intent.putExtra("identifier", gson.toJson(data))
            startActivity(intent)
        }

        // Initialize RecyclerView
        val storeRecyclerView: RecyclerView = view.findViewById(R.id.storeRecyclerView)
        storeRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val storeAdapter = StoreCustomAdapter{ item ->
            // Handle the click event here (you can access the clicked item)
            val intent = Intent(requireContext(), ProductDetailActivity::class.java)
            intent.putExtra("identifier", gson.toJson(item))
            startActivity(intent)
        }
        storeRecyclerView.adapter = storeAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe LiveData from ViewModel and update UI when data changes
        viewModel.storeData.observe(viewLifecycleOwner) { storesDataModel ->
            storesDataModel?.product?.let { products ->
                data = storesDataModel
                (view.findViewById<RecyclerView>(R.id.storeRecyclerView).adapter as? StoreCustomAdapter)?.updateData(products)
            }
        }

        // Trigger data loading
        lifecycleScope.launch {
            viewModel.getStore()
        }
    }
}