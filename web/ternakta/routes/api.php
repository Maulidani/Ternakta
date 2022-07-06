<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\UserController;
use App\Http\Controllers\ProductController;
use App\Http\Controllers\ArticleController;

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


Route::post('add-user', 'UserController@addUser');
Route::post('edit-user', 'UserController@editUser');
Route::post('delete-user', 'UserController@deleteUser');
Route::post('login-user', 'UserController@loginUser');
Route::post('add-status-user', 'UserController@addStatusStore');
Route::post('show-user', 'UserController@showUser');

Route::post('add-product', 'ProductController@addProduct');
Route::post('edit-product', 'ProductController@editProduct');
Route::post('delete-product', 'ProductController@deleteProduct');
Route::post('show-product', 'ProductController@showProduct');

Route::post('add-article', 'ArticleController@addArticle');
Route::post('edit-article', 'ArticleController@editArticle');
Route::post('delete-article', 'ArticleController@deleteArticle');
Route::post('show-article', 'ArticleController@showArticle');


