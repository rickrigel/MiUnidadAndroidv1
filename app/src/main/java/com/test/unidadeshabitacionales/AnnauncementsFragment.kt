package com.test.unidadeshabitacionales

import android.content.Context
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

class AnnauncementsFragment: Fragment(), AnnancementsCustomAdapter.AnnouncementActionCallback {
    private val viewModel: AnnauncementsViewModel by viewModels()
    private var data = AnnauncementsListDataModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_announcements, container, false)

        val storeRecyclerView: RecyclerView = view.findViewById(R.id.noticeRecyclerView)
        storeRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val storeAdapter = AnnancementsCustomAdapter(this)
        storeRecyclerView.adapter = storeAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences =
            this.activity?.getSharedPreferences("MY_SHARED_PREFERENCES", Context.MODE_PRIVATE)
        val storedUser = sharedPreferences?.getString("email", "")
        val btnAdd = view.findViewById<Button>(R.id.buttonAdd)


        btnAdd.setOnClickListener {
            val gson = Gson()
            val intent = Intent(requireContext(), AddNoticeActivity::class.java)
            intent.putExtra("identifier", gson.toJson(data))
            startActivity(intent)
        }
        // Observe LiveData from ViewModel and update UI when data changes
        viewModel.noticeData.observe(viewLifecycleOwner) { noticeDataModel ->
            noticeDataModel?.notice?.let { notice ->
                data = noticeDataModel
                (view.findViewById<RecyclerView>(R.id.noticeRecyclerView).adapter as? AnnancementsCustomAdapter)?.updateData(notice)
            }
        }

        // Trigger data loading
        lifecycleScope.launch {
            if (storedUser != null) {
                viewModel.getUser(storedUser)
            }
            viewModel.getNotice()
        }
    }

    // Callback for when the like button is clicked
    override fun onLikeClicked(position: Int, item: AnnauncementsDataModel) {
        // Handle like action, e.g., increment like count and update ViewModel or adapter
        item.like += 1
        viewModel.updateNotice(item.title, item.detail, item.like, item.disLike, position, data) // Example of updating data in ViewModel
        (view?.findViewById<RecyclerView>(R.id.noticeRecyclerView)?.adapter as? AnnancementsCustomAdapter)?.notifyItemChanged(position)
    }

    // Callback for when the dislike button is clicked
    override fun onDislikeClicked(position: Int, item: AnnauncementsDataModel) {
        // Handle dislike action, e.g., increment dislike count and update ViewModel or adapter
        item.disLike += 1
        viewModel.updateNotice(item.title, item.detail, item.like, item.disLike, position, data) // Example of updating data in ViewModel
        (view?.findViewById<RecyclerView>(R.id.noticeRecyclerView)?.adapter as? AnnancementsCustomAdapter)?.notifyItemChanged(position)
    }
}