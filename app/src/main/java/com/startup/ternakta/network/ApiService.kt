package com.startup.ternakta.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @Multipart
    @POST("add-user")
    fun addUser(
        @Part("type") userType: RequestBody, // (store, customer)
        @Part("business_permit") businessPermission: RequestBody, // for user store
        @Part("name") name: RequestBody,
        @Part parts: MultipartBody.Part, // image
        @Part("phone") phone: RequestBody,
        @Part("password") password: RequestBody,
        @Part("province") province: RequestBody,
        @Part("city") city: RequestBody,
        @Part("districts") districts: RequestBody,
        @Part("address") address: RequestBody,
        @Part("status") status: RequestBody, // for user store
    ): Call<Model.ResponseModel> // response : user{}

    @Multipart
    @POST("edit-user")
    fun editUser(
        @Part("id_user") userId: RequestBody,
        @Part("type") userType: RequestBody, // (store, customer)
        @Part("name") name: RequestBody,
        @Part parts: MultipartBody.Part, // image
        @Part("phone") phone: RequestBody,
        @Part("password") password: RequestBody,
        @Part("province") province: RequestBody,
        @Part("city") city: RequestBody,
        @Part("districts") districts: RequestBody,
        @Part("address") address: RequestBody,
    ): Call<Model.ResponseModel> // response : user{}

    @FormUrlEncoded
    @POST("edit-user")
    fun editWithoutImgUser(
        @Field("id_user") userId: String,
        @Field("type") userType: String,
        @Field("name") name: String,
        @Field("phone") phone: String,
        @Field("password") password: String,
        @Field("province") province: String,
        @Field("city") city: String,
        @Field("districts") districts: String,
        @Field("address") address: String,
    ): Call<Model.ResponseModel> // response : user{}

    @FormUrlEncoded
    @POST("delete-user")
    fun deleteUser(
        @Field("type") userType: String,
        @Field("id_user") userId: String,
    ): Call<Model.ResponseModel>

    @FormUrlEncoded
    @POST("login-user")
    fun loginUser(
        @Field("type") userType: String, // (store, customer)
        @Field("phone") phone: String,
        @Field("password") password: String,
        @Field("username") username: String, // admin
    ): Call<Model.ResponseModel>  // response : user{}

    @FormUrlEncoded
    @POST("add-status-user")
    fun addStatusStoreUser(
        @Field("id_user") userId: String,
        @Field("status") status: String,
    ): Call<Model.ResponseModel>

    @FormUrlEncoded
    @POST("show-user")
    fun showUser(
        @Field("type") userType: String,
        @Field("search") search: String,
    ): Call<Model.ResponseModel> // response : data{[]}

    @Multipart
    @POST("add-product")
    fun addProduct(
        @Part("user_store_id") userId: RequestBody,
        @Part("name") name: RequestBody,
        @Part("price") price: RequestBody,
        @Part("price_promo") pricePromo: RequestBody,
        @Part("description") description: RequestBody,
        @Part parts: MultipartBody.Part, // image
    ): Call<Model.ResponseModel> // response : product{}

    @Multipart
    @POST("edit-product")
    fun editProduct(
        @Part("product_id") productId: RequestBody,
        @Part("user_store_id") userId: RequestBody,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("price") price: RequestBody,
        @Part("price_promo") pricePromo: RequestBody,
        @Part parts: MultipartBody.Part, // image
    ): Call<Model.ResponseModel> // response : product{}

    @FormUrlEncoded
    @POST("edit-product")
    fun editWithoutImgProduct(
        @Field("product_id") productId: String,
        @Field("user_store_id") userId: String,
        @Field("name") name: String,
        @Field("description") description: String,
        @Field("price") price: String,
        @Field("price_promo") pricePromo: String,
    ): Call<Model.ResponseModel> // response : product{}

    @FormUrlEncoded
    @POST("delete-product")
    fun deleteProduct(
        @Field("product_id") productId: String,
        @Field("user_store_id") userId: String,
    ): Call<Model.ResponseModel>

    @FormUrlEncoded
    @POST("show-product")
    fun showProduct(
        @Field("user_store_id") userId: String, // ( ''/empty , not empty )
        @Field("search") search: String,
    ): Call<Model.ResponseModel> // response : data{[]}

    @FormUrlEncoded
    @POST("show-detail-product")
    fun showDetailProduct(
        @Field("product_id") productId: String,
    ): Call<Model.ResponseModel> // response : product{}

    @Multipart
    @POST("add-article")
    fun addArticle(
        @Part("user_store_id") userId: RequestBody,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part parts: MultipartBody.Part, // image
    ): Call<Model.ResponseModel> // response : article{}

    @Multipart
    @POST("edit-article")
    fun editArticle(
        @Part("article_id") articleId: RequestBody,
        @Part("user_store_id") userId: RequestBody,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part parts: MultipartBody.Part, // image
    ): Call<Model.ResponseModel> // response : article{}

    @FormUrlEncoded
    @POST("edit-article")
    fun editWithoutImgArticle(
        @Field("article_id") productId: String,
        @Field("user_store_id") userId: String,
        @Field("title") name: String,
        @Field("description") description: String,
    ): Call<Model.ResponseModel> // response : article{}

    @FormUrlEncoded
    @POST("delete-article")
    fun deleteArticle(
        @Field("article_id") articleId: String,
        @Field("user_store_id") userId: String,
    ): Call<Model.ResponseModel>

    @FormUrlEncoded
    @POST("show-article")
    fun showArticle(
        @Field("user_store_id") userId: String, // ( ''/empty , not empty )
        @Field("search") search: String,
    ): Call<Model.ResponseModel>

    @FormUrlEncoded
    @POST("add-cart")
    fun addCart(
        @Field("product_id") userId: String,
        @Field("user_customer_id") userCustomerId: String,
        @Field("user_store_id") userStoreId: String,
    ): Call<Model.ResponseModel>

    @FormUrlEncoded
    @POST("delete-cart")
    fun deleteCart(
        @Field("cart_id") cartId: String,
        @Field("product_id") userId: String,
        @Field("user_customer_id") userCustomerId: String,
        @Field("user_store_id") userStoreId: String,
    ): Call<Model.ResponseModel>

    @FormUrlEncoded
    @POST("show-cart")
    fun showCart(
        @Field("user_customer_id") userCustomerId: String,  // ( ''/empty , not empty )
        @Field("user_store_id") userStoreId: String,  // ( ''/empty , not empty )
    ): Call<Model.ResponseModel> // response : data{[]}, product{[]}

//    @FormUrlEncoded
//    @POST("add-order")
//    fun addOrder(
//        @Field("user_customer_id") name: String,
//        @Field("user_store_id") userStoreId: String,
//        @Field("status") status: String,
//        @Field("product_id") productId: ArrayList<String>, // [product id]
//    ): Call<Model.ResponseModel> // response : order{}, order_item{[]}

  @Multipart
    @POST("add-order")
    fun addOrder(
        @Part("user_customer_id") name: RequestBody,
        @Part("user_store_id") userStoreId: RequestBody,
        @Part("status") status: RequestBody,
        @Part("product_id[]") productId: ArrayList<RequestBody>, // [product id]
    ): Call<Model.ResponseModel> // response : order{}, order_item{[]}

    @Multipart
    @POST("add-transaction-order")
    fun addTransactionProofOrder(
        @Part("order_id") name: RequestBody,
        @Part parts: MultipartBody.Part, // image
    ): Call<Model.ResponseModel>

    @FormUrlEncoded
    @POST("add-status-order")
    fun addStatusOrder(
        @Field("order_id") orderId: String,
        @Field("status") status: String,
    ): Call<Model.ResponseModel>

    @FormUrlEncoded
    @POST("show-order")
    fun showOrder(
        @Field("user_customer_id") userCustomerId: String, // ( ''/empty , not empty )
        @Field("user_store_id") userStoreId: String, // ( ''/empty , not empty )
    ): Call<Model.ResponseModel>  // response : data{[]}, product{[]}

}