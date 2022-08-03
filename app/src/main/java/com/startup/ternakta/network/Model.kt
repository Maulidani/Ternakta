package com.startup.ternakta.network

class Model {
    data class ResponseModel(
        val message: String,
        val errors: Boolean,

        val data: ArrayList<DataModel>,
        val user: UserModel,
        val product: ArrayList<DataModel>,
        val article: ArticleModel,
        val order: OrderModel,
    )

    data class DataModel(
        //
        val id: String,

        // user
        val business_permit: String, // store
        val name: String,
        val image: String,
        val phone: String,
        val password: String,
        val province: String,
        val city: String,
        val districts: String,
        val address: String,
        val status: String, // store

        //product, cart
        val product_id: String,
        val user_store_id: String,
        val store_name: String,
        val store_image: String,
//        val name: String,
        val price: String,
        val price_promo: String,
        val description: String,
//        val image: String,

        //article
//        val user_store_id: String,
        val title: String,
//        val description: String,
//        val image: String,

        //order
        val image_transaction: String,
        val user_customer_id: String,
//        val user_store_id: String,
//        val status: String,

        //
        val updated_at: String,
        val created_at: String,
    )

    data class UserModel(
        val id: String,

        val business_permit: String, // store
        val name: String,
        val image: String,
        val phone: String,
        val password: String,
        val province: String,
        val city: String,
        val districts: String,
        val address: String,
        val status: String, // store

        val updated_at: String,
        val created_at: String,
    )

    data class ProductModel( // product, cart
        val id: String,

        val user_store_id: String,
        val name: String,
        val price: String,
        val price_promo: String,
        val description: String,
        val image: String,

        val updated_at: String,
        val created_at: String,
    )
    data class ArticleModel(
        val id: String,

        val user_store_id: String,
        val title: String,
        val description: String,
        val image: String,

        val updated_at: String,
        val created_at: String,
    )
    data class OrderModel(
        val id: String,

        val user_customer_id: String,
        val user_store_id: String,
        val status: String,

        val updated_at: String,
        val created_at: String,
    )
}