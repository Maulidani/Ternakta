<?php

namespace App\Http\Controllers;

use App\Models\Cart;
use App\Models\CartCustomer;
use App\Models\Product;
use App\Models\ProductImage;
use App\Models\User;
use App\Models\File;
use App\Models\ProductCategory;
use App\Models\ProductSeller;
use Symfony\Component\Console\Input\Input;
use Illuminate\Http\Request;
use App\Models\UserAccount;
use App\Models\UserAddress;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Auth;

class CartController extends Controller
{
    public function addCart(Request $request)
    {
        $user_customer_id = $request->user_customer_id;
        $product_seller_id = $request->product_seller_id;

        $exist = CartCustomer::where([
            ['user_customer_id', '=', $user_customer_id],
            ['product_seller_id', '=', $product_seller_id]
        ])->exists();

        if ($exist) {

            return response()->json([
                'message' => 'Fail',
                'errors' => true,
            ]);
        } else {

            $cart = new CartCustomer();
            $cart->user_customer_id = $user_customer_id;
            $cart->product_seller_id = $product_seller_id;
            $cart->save();

            return response()->json([
                'message' => 'Success',
                'errors' => false,
            ]);
        }
    }

    public function myCart(Request $request)
    {
        $product = ProductSeller::join('cart_customers', 'product_sellers.id', '=', 'cart_customers.product_seller_id')
            ->where('cart_customers.user_customer_id', $request->user_customer_id)
            ->orderBy('cart_customers.created_at', 'DESC')
            ->get();

        return response()->json([
            'message' => 'Success',
            'errors' => false,
            'cart' => $product,
        ]);
    }

    public function deleteCart(Request $request)
    {
        CartCustomer::where(
            'id',
            $request->id
        )->delete();

        return response()->json([
            'message' => 'Success',
            'errors' => false,
        ]);
    }
}