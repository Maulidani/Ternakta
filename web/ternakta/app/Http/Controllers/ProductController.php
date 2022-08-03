<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Product;

use function PHPUnit\Framework\isEmpty;
use function PHPUnit\Framework\isNull;

class ProductController
{
    public function addProduct(Request $request)
    {   
        $user_store_id = $request->user_store_id;
        $name = $request->name;
        $price = $request->price;
        $price_promo = $request->price_promo;
        $description = $request->description;
        $image = $request->image;

        $allowedfileExtension = ['jpeg', 'jpg', 'png', 'JPG', 'JPEG'];
            if ($request->hasfile('image')) {

                $filename = time() . '.' . $image->getClientOriginalName();
                $extension = $image->getClientOriginalExtension();

                $check = in_array($extension, $allowedfileExtension);

                if ($check) {

                    $image->move(public_path() . '/image/product/', $filename);

                    $add_product = new Product;
                    $add_product->user_store_id = $user_store_id;
                    $add_product->name = $name;
                    $add_product->price = $price;
                    $add_product->price_promo = $price_promo;
                    $add_product->description = $description;
                    $add_product->image = $filename;
                    $add_product->save();
                   
                    if ($add_product) {

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

            } else {
                return response()->json([
                    'message' => 'Failed',
                    'errors' => true,
                ]);
            }
    }

    public function editProduct(Request $request)
    {
        $product_id = $request->product_id;
        $user_store_id = $request->user_store_id;
        $name = $request->name;
        $price = $request->price;
        $price_promo = $request->price_promo;
        $description = $request->description;
        $image = $request->image;

        $exist = Product::where(
            [
                'id'=> $product_id,
                'user_store_id'=> $user_store_id,
            ]
        )->exists();

        if ($exist){

            $allowedfileExtension = ['jpeg', 'jpg', 'png', 'JPG', 'JPEG'];
            if ($request->hasfile('image')) {
    
                $filename = time() . '.' . $image->getClientOriginalName();
                $extension = $image->getClientOriginalExtension();
    
                $check = in_array($extension, $allowedfileExtension);
    
                if ($check) {
            
                    $image->move(public_path() . '/image/product/', $filename);
    
                    $edit_product = Product::find($product_id);
                    $edit_product->name = $request->name;
                    $edit_product->price = $price;
                    $edit_product->price_promo = $price_promo;
                    $edit_product->description = $description;   
                    $edit_product->image = $filename;
                    $edit_product->save();
    
                    if ($edit_product) {
    
                        return response()->json([
                            'message' => 'Success',
                            'errors' => false,
                            'product' => $edit_product
                        ]);
    
                    } else {
    
                        return response()->json([
                            'message' => 'Failed',
                            'errors' => true,
                        ]);
                    }

                } 

            } else {

                $edit_product = Product::find($product_id);
                $edit_product->name = $request->name;
                $edit_product->price = $price;
                $edit_product->price_promo = $price_promo;
                $edit_product->description = $description;            
                $edit_product->save();

                if ($edit_product) {

                    return response()->json([
                        'message' => 'Success',
                        'errors' => false,
                        'user' => $edit_product
                    ]);

                } else {

                    return response()->json([
                        'message' => 'Failed',
                        'errors' => true,
                    ]);
                }   
            }

        } else {
            return response()->json([
                'message' => 'Failed',
                'errors' => true,
            ]);
        }
    }

    public function deleteProduct(Request $request)
    {
        $product_id = $request->product_id;
        $user_store_id = $request->user_store_id;

        $delete = Product::where(
            [
                'id'=> $product_id,
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

    public function showProduct(Request $request)
    {
        $user_store_id = $request->user_store_id;
        $search = $request->search;

        if($user_store_id == '') {
            
            $product = Product::orderBy('created_at', 'DESC')
            ->where('name', 'like', "%" . $search . "%")
            ->get();
    
            if ($product->isEmpty()) {
                return response()->json([
                    'message' => 'Failed',
                    'errors' => true,
                ]);
            } else {
    
                return response()->json([
                    'message' => 'Success',
                    'errors' => false,
                    'data' => $product,
                ]);
            }
        } else {

            $product = Product::orderBy('updated_at', 'DESC')
            ->where('user_store_id', $user_store_id)
            ->where('name', 'like', "%" . $search . "%")
            ->get();
    
            if ($product->isEmpty()) {
                return response()->json([
                    'message' => 'Failed',
                    'errors' => true,
                ]);
            } else {
    
                return response()->json([
                    'message' => 'Success',
                    'errors' => false,
                    'data' => $product,
                ]);
            }
        }
    }

    public function showDetailProduct(Request $request)
    {
        $product_id = $request->product_id;

        $product = Product::where('id',$product_id)
        ->first();

        if ($product) {
            return response()->json([
                'message' => 'Success',
                'errors' => false,
                'product' => $product,
            ]);
        } else {
            return response()->json([
                'message' => 'Failed',
                'errors' => true,
            ]);
           
        }
    }

}