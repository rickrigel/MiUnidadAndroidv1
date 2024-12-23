package com.test.unidadeshabitacionales

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.Gson

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var dataModel: StoreDataModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_detail_activity)

        // Retrieve the passed data
        val gson = Gson()
        dataModel = gson.fromJson(intent.getStringExtra("identifier"), StoreDataModel::class.java)
        // Get references to the views
        val imageView: ImageView = findViewById(R.id.imageView)
        val titleText: TextView = findViewById(R.id.titleText)
        val detailText: TextView = findViewById(R.id.detailText)
        val priceText: TextView = findViewById(R.id.priceText)


        // Set the dynamic data to the views
        titleText.text = dataModel.title
        detailText.text = dataModel.detail
        priceText.text = "$${dataModel.price}"

        // Load the image using Glide
        Glide.with(this)
            .load(dataModel.image)
            .placeholder(R.drawable.ic_launcher_background) // Placeholder image
            .into(imageView)
    }

}