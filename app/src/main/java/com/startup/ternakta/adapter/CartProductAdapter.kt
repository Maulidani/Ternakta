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
import com.startup.ternakta.network.Model
import com.startup.ternakta.utils.Constant


class CartProductAdapter(
    private val list: List<Model.DataModel>
) :
    RecyclerView.Adapter<CartProductAdapter.ListViewHolder>() {

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imgProductCart: ImageView by lazy { itemView.findViewById(R.id.imgProductCart) }
        private val nameProduct: TextView by lazy { itemView.findViewById(R.id.tvNameProduct) }
        private val priceProduct: TextView by lazy { itemView.findViewById(R.id.tvPrice) }
        private val delete: ImageView by lazy { itemView.findViewById(R.id.imgDelete) }
        private val item: CardView by lazy { itemView.findViewById(R.id.itemCardCartChild) }

        fun bindData(list: Model.DataModel) {

            val urlProductCart = Constant.IMAGE_URL_PRODUCT + list.image
            imgProductCart.load(urlProductCart)

            nameProduct.text = list.name

            if (list.price_promo != "") {
                priceProduct.text = list.price_promo
            } else {
                priceProduct.text = list.price
            }

            delete.setOnClickListener {
                Toast.makeText(itemView.context, "hapus produk", Toast.LENGTH_SHORT).show()
            }

            item.setOnClickListener {
                Toast.makeText(itemView.context, list.name, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_cart_child, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bindData(list[position])
    }

    override fun getItemCount(): Int = list.size
}
