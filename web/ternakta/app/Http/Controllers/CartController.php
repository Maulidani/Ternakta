<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Cart;
use App\Models\Product;

use function PHPUnit\Framework\isEmpty;
use function PHPUnit\Framework\isNull;

class CartController
{
    public function addProductCart(Request $request)
    {   
        $product_id = $request->product_id;
        $user_customer_id = $request->user_customer_id;
        $user_store_id = $request->user_store_id;

        $exist = Product::where([
            ['id', '=', $product_id],
            ['user_store_id', '=', $user_store_id]
        ])->exists();
        

        if($exist){

            $add_cart = new Cart;
            $add_cart->product_id = $product_id;
            $add_cart->user_customer_id = $user_customer_id;
            $add_cart->user_store_id = $user_store_id;
            $add_cart->save();
           
            if ($add_cart) {
    
                return response()->json([
                    'message' => 'Success',
                    'errors' => false,
                ]);
    
            } else {
    
                return response()->json([
                    'message' => 'Failed',
                    'errors' => true,
                ]);
            }
    
        } else {

            return response()->json([
                'message' => 'Failed',
                'errors' => true,
            ]);
        }
       
    }

    public function deleteProductCart(Request $request)
    {
        $cart_id = $request->cart_id;
        $product_id = $request->product_id;
        $user_customer_id = $request->user_customer_id;
        $user_store_id = $request->user_store_id;

        $delete = Cart::where(
            [
                'id'=> $cart_id,
                'product_id'=> $product_id,
                'user_customer_id'=> $user_customer_id,
                'user_store_id'=> $user_store_id,
            ]
        )->delete();

        if($delete) {

            return response()->json([
                'message' => 'Success',
                'errors' => false,
            ]);

        } else {

            return response()->json([
                'message' => 'Failed',
                'errors' => true,
            ]);
        }
    }

    public function showCart(Request $request)
    {
        $user_customer_id = $request->user_customer_id;
        $user_store_id = $request->user_store_id;

        if($user_customer_id != '') {
            
            $cart = Cart::join('products', 'products.id', '=', 'carts.product_id')
                        ->join('user_stores', 'user_stores.id', 'carts.user_store_id')
                        ->where('carts.user_customer_id', $user_customer_id)
                        ->orderBy('carts.updated_at', 'DESC')
                        ->get(['carts.*','user_stores.name as store_name',
                                'user_stores.image as store_image','products.name', 
                                'products.price', 'products.price_promo',
                                'products.description', 'products.image']
                    );
            
            $unique = $cart->unique('user_store_id')->values();
            $unique2 = $cart->unique('product_id')->values();

            if ($cart->isEmpty()) {
                return response()->json([
                    'message' => 'Failed',
                    'errors' => true,
                ]);

            } else {
    
                return response()->json([
                    'message' => 'Success',
                    'errors' => false,
                    'data' => $unique,
                    'product' => $unique2,
                ]);
            }

        } else if($user_store_id != '') {
            
            $cart = Cart::join('products', 'products.id', '=', 'carts.product_id')
                        ->join('user_stores', 'user_stores.id', 'carts.user_store_id')
                        ->where('carts.user_store_id', $user_store_id)
                        ->orderBy('carts.updated_at', 'DESC')
                        ->get(['carts.*','user_stores.name as store_name',
                                'user_stores.image as store_image','products.name', 
                                'products.price', 'products.price_promo',
                                'products.description', 'products.image']
                    );
            $unique = $cart->unique('user_store_id')->values();

            if ($cart->isEmpty()) {
                return response()->json([
                    'message' => 'Failed',
                    'errors' => true,
                ]);

            } else {
    
                return response()->json([
                    'message' => 'Success',
                    'errors' => false,
                    'data' => $unique,
                    'product' => $cart,
                ]);
            }

        } else{

            return response()->json([
                'message' => 'Failed',
                'errors' => true,
            ]);
           
        }
    }

}