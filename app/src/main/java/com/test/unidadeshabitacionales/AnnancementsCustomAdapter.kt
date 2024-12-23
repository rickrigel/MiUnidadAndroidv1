package com.test.unidadeshabitacionales

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AnnancementsCustomAdapter(private val callback: AnnouncementActionCallback) : RecyclerView.Adapter<AnnancementsCustomAdapter.ViewHolder>() {

    private var items: List<AnnauncementsDataModel> = emptyList()

    interface AnnouncementActionCallback {
        fun onLikeClicked(position: Int, item: AnnauncementsDataModel)
        fun onDislikeClicked(position: Int, item: AnnauncementsDataModel)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val detailTextView: TextView = itemView.findViewById(R.id.detailTextView)
        val likeTextView: TextView = itemView.findViewById(R.id.likeTextView)
        val disLikeTextView: TextView = itemView.findViewById(R.id.disLikeTextView)

        val likeButton: ImageButton = itemView.findViewById(R.id.likeButton)
        val disLikeButton: ImageButton = itemView.findViewById(R.id.disLikeButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_custome_notice_cell, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.titleTextView.text = item.title // set the title text
        holder.detailTextView.text = item.detail // set the subtitle text
        holder.likeTextView.text = "${item.like}"
        holder.disLikeTextView.text = "${item.disLike}"

        holder.likeButton.setOnClickListener {
            callback.onLikeClicked(position, item)
        }

        holder.disLikeButton.setOnClickListener {
            callback.onDislikeClicked(position, item)
        }
    }

    override fun getItemCount() = items.size

    // Method to update the data in the adapter
    fun updateData(newItems: List<AnnauncementsDataModel>) {
        items = newItems
        notifyDataSetChanged()
    }
}