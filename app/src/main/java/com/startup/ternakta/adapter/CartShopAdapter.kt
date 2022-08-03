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
import com.startup.ternakta.network.Model
import com.startup.ternakta.utils.Constant
import de.hdodenhof.circleimageview.CircleImageView


class CartShopAdapter(
    private val listProduct: List<Model.DataModel>,
    private val list: List<Model.DataModel>
) :
    RecyclerView.Adapter<CartShopAdapter.ListViewHolder>() {

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var cartProduct = ArrayList<Model.DataModel>()

        private val imgShop: CircleImageView by lazy { itemView.findViewById(R.id.imgShopCart) }
        private val imgUp: ImageView by lazy { itemView.findViewById(R.id.imgUp) }
        private val imgDown: ImageView by lazy { itemView.findViewById(R.id.imgDown) }
        private val nameShop: TextView by lazy { itemView.findViewById(R.id.tvNameShop) }
        private val rvCartChild: RecyclerView by lazy { itemView.findViewById(R.id.rvCartChild) }
        private val item: CardView by lazy { itemView.findViewById(R.id.itemCardCartShop) }

        fun bindData(list: Model.DataModel) {

            for (i in listProduct) {

                if (list.user_store_id == i.user_store_id) {

                    if (i.price_promo != null) {
                        cartProduct.add(
                            Model.DataModel(
                                "",
                                "",
                                i.name,
                                i.image,
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                i.product_id,
                                "",
                                "",
                                "",
                                i.price,
                                i.price_promo,
                                "",
                                "",
                                "",
                                "",
                                "",
                                ""
                            )
                        )

                    } else {
                        cartProduct.add(
                            Model.DataModel(
                                "",
                                "",
                                i.name,
                                i.image,
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                i.product_id,
                                "",
                                "",
                                "",
                                i.price,
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                ""
                            )
                        )
                    }

                }
            }

            val adapter = CartProductAdapter(cartProduct)
            rvCartChild.layoutManager = LinearLayoutManager(itemView.context)
            rvCartChild.adapter = adapter

            val urlShopCart = Constant.IMAGE_URL_STORE + list.store_image
            imgShop.load(urlShopCart)

            nameShop.text = list.store_name

            item.setOnClickListener {
                Toast.makeText(itemView.context, list.name, Toast.LENGTH_SHORT).show()
            }

            if (imgDown.visibility == View.VISIBLE) {
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
