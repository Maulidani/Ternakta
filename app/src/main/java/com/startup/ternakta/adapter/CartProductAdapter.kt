package com.startup.ternakta.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.startup.ternakta.R
import com.startup.ternakta.model.ArticleModel
import com.startup.ternakta.model.CartProductModel
import com.startup.ternakta.model.CartShopModel
import com.startup.ternakta.model.ProductModel
import de.hdodenhof.circleimageview.CircleImageView


class CartProductAdapter(
    private val list: List<CartProductModel>) :
    RecyclerView.Adapter<CartProductAdapter.ListViewHolder>() {

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imgProductCart: ImageView by lazy { itemView.findViewById(R.id.imgProductCart) }
        private val nameProduct: TextView by lazy { itemView.findViewById(R.id.tvNameProduct) }
        private val priceProduct: TextView by lazy { itemView.findViewById(R.id.tvPrice) }
        private val countProduct: TextView by lazy { itemView.findViewById(R.id.tvCountProduct) }
        private val minus: TextView by lazy { itemView.findViewById(R.id.tvMinus) }
        private val plus: TextView by lazy { itemView.findViewById(R.id.tvPlus) }
        private val delete: ImageView by lazy { itemView.findViewById(R.id.imgDelete) }
        private val item: CardView by lazy { itemView.findViewById(R.id.itemCardCartChild) }

        fun bindData(list: CartProductModel) {

            imgProductCart.load(list.image)
            var count = list.count.toInt()

            nameProduct.text = list.name
            priceProduct.text = list.price
            countProduct.text = count.toString()

            minus.setOnClickListener {
                count -= 1
                countProduct.text = count.toString()
            }
            plus.setOnClickListener {
                count += 1
                countProduct.text = count.toString()
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
