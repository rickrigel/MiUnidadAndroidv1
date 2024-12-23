import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.test.unidadeshabitacionales.R
import com.test.unidadeshabitacionales.StoreDataModel

class StoreCustomAdapter(private val itemClickListener: (StoreDataModel) -> Unit) : RecyclerView.Adapter<StoreCustomAdapter.ViewHolder>() {
    private var items: List<StoreDataModel> = emptyList()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val subtitleTextView: TextView = itemView.findViewById(R.id.subtitleTextView)

        fun bind(item: StoreDataModel, clickListener: (StoreDataModel) -> Unit) {
            itemView.setOnClickListener { clickListener(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_custom_cell, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        // Assuming you're using Coil or Glide for image loading
        holder.imageView.load(item.image) // Adjust 'load' method based on your image loading library
        holder.titleTextView.text = item.title
        holder.subtitleTextView.text = "$${item.price}"

        // Set the click listener on the item view
        holder.bind(item, itemClickListener)
    }

    override fun getItemCount() = items.size

    // Method to update the data in the adapter
    fun updateData(newItems: List<StoreDataModel>) {
        items = newItems
        notifyDataSetChanged()
    }
}

