package com.test.unidadeshabitacionales

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class EventCustomAdapter: RecyclerView.Adapter<EventCustomAdapter.ViewHolder>()  {

    private var items: List<EventsDataModel> = emptyList()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val subtitleTextView: TextView = itemView.findViewById(R.id.subtitleTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_custom_cell, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.imageView.setImageResource(R.drawable.ic_launcher_background)
        holder.titleTextView.text = item.title // set the title text
        holder.subtitleTextView.text = item.area // set the subtitle text
    }

    override fun getItemCount() = items.size

    // Method to update the data in the adapter
    fun updateData(newItems: List<EventsDataModel>) {
        items = newItems
        notifyDataSetChanged()
    }
}