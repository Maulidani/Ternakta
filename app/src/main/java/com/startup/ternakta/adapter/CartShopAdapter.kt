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


class CartShopAdapter(
    private val list: List<CartShopModel>) :
    RecyclerView.Adapter<CartShopAdapter.ListViewHolder>() {

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cartProduct = ArrayList<CartProductModel>()

        private val imgShop: CircleImageView by lazy { itemView.findViewById(R.id.imgShopCart) }
        private val imgUp: ImageView by lazy { itemView.findViewById(R.id.imgUp) }
        private val imgDown: ImageView by lazy { itemView.findViewById(R.id.imgDown) }
        private val nameShop: TextView by lazy { itemView.findViewById(R.id.tvNameShop) }
        private val rvCartChild: RecyclerView by lazy { itemView.findViewById(R.id.rvCartChild) }
        private val item: CardView by lazy { itemView.findViewById(R.id.itemCardCartShop) }

        fun bindData(list: CartShopModel) {

            val countProduct = list.productCount.toInt()
            for (i in 1..countProduct) {
                val image =
                    "https://img.freepik.com/free-vector/online-shopping-isometric-landing-page-template-web-banner-purple-background_88138-451.jpg?w=740&t=st=1656926462~exp=1656927062~hmac=0e41c487cb0c62b6fe4479722706501c8426997ae931801fd1d29d6ae244a6bf"

                cartProduct.add(CartProductModel("nama produk","1","20000",image))
            }

            val adapter = CartProductAdapter(cartProduct)
            rvCartChild.layoutManager = LinearLayoutManager(itemView.context)
            rvCartChild.adapter = adapter

            imgShop.load(list.image)

            nameShop.text = list.name

            item.setOnClickListener {
                Toast.makeText(itemView.context, list.name, Toast.LENGTH_SHORT).show()
            }

            if (imgDown.visibility == View.VISIBLE){
                imgDown.visibility = View.VISIBLE
                imgUp.visibility = View.GONE
            } else {
                imgUp.visibility = View.VISIBLE
                imgDown.visibility = View.GONE
            }

            imgDown.setOnClickListener {
                imgDown.visibility = View.GONE
                imgUp.visibility = View.VISIBLE
                rvCartChild.visibility = View.VISIBLE
            }

            imgUp.setOnClickListener {
                imgUp.visibility = View.GONE
                imgDown.visibility = View.VISIBLE
                rvCartChild.visibility = View.GONE
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bindData(list[position])
    }

    override fun getItemCount(): Int = list.size
}
