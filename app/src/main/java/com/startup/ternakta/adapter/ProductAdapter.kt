package com.startup.ternakta.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.startup.ternakta.R
import com.startup.ternakta.model.ProductModel


class ProductAdapter(
    private val type:String,
    private val list: List<ProductModel>) :
    RecyclerView.Adapter<ProductAdapter.ListViewHolder>() {

    val _type = type

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imgProduct: ImageView by lazy { itemView.findViewById(R.id.imgProduct) }
        private val nameProduct: TextView by lazy { itemView.findViewById(R.id.tvProductName) }
        private val priceProduct: TextView by lazy { itemView.findViewById(R.id.tvProductPrice) }
        private val item: CardView by lazy { itemView.findViewById(R.id.itemCardProduct) }

        fun bindData(list: ProductModel) {

            imgProduct.load(list.image)

            nameProduct.text = list.name
            priceProduct.text = list.price

            item.setOnClickListener {
                Toast.makeText(itemView.context, list.name, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bindData(list[position])
    }

    override fun getItemCount(): Int {
        return if (_type == "home"){

            if (list.size <=6 ){
                list.size
            } else {
                6
            }

        } else {
            list.size
        }
    }
}
