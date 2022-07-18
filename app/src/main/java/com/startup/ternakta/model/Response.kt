package com.startup.ternakta.model

//data class Response ()

data class ProductModel(
    var name : String,
    var price : String,
    var image : String,
)

data class ArticleModel(
    var name : String,
    var description : String,
    var image : String,
    var imageProfile : String,
)

data class CartShopModel(
    var name : String,
    var image : String,
    var productCount : String,
)

data class CartProductModel(
    var name : String,
    var count : String,
    var price : String,
    var image : String,
)