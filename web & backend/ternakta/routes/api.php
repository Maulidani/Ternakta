<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\UserController;
use App\Http\Controllers\ProductController;
use App\Http\Controllers\CartController;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::middleware('auth:sanctum')->get('/user', function (Request $request) {
    return $request->user();
});


Route::post('login', 'UserController@login');
Route::post('register-seller', 'UserController@registerSeller');
Route::post('register-customer', 'UserController@registerCustomer');
Route::post('edit-seller', 'UserController@editSeller');
Route::post('edit-customer', 'UserController@editCustomer');
Route::post('edit-account-image', 'UserController@editImage');

Route::post('product-all', 'ProductController@index');
// Route::post('product-category', 'ProductController@categoryProduct');
Route::post('product-seller', 'ProductController@sellerProduct');
Route::post('upload', 'ProductController@upload');
Route::post('edit-product', 'ProductController@editProduct');
Route::post('edit-product-image', 'ProductController@editImage');
Route::post('delete-product', 'ProductController@deleteProduct');

Route::post('add-cart', 'CartController@addCart');
Route::post('my-cart', 'CartController@myCart');
Route::post('delete-cart', 'CartController@deleteCart');