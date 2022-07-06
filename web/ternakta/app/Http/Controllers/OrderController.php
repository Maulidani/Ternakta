<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Order;
use App\Models\OrderItem;
use App\Models\Product;

use function PHPUnit\Framework\isEmpty;
use function PHPUnit\Framework\isNull;

class OrderController
{
    public function addOrder(Request $request)
    {   
        $user_customer_id = $request->user_customer_id;
        $user_store_id = $request->user_store_id;
        $product_id = $request->product_id;
        $status = $request->status;

        $isNotExist = false;

        foreach ($product_id as $product) {

            $exist = Product::where([
                ['id', '=', $product],
                ['user_store_id', '=', $user_store_id]
            ])->exists();

            if(!$exist){
                $isNotExist = true;
            }
        }
        
        if($isNotExist){
            return response()->json([
                        'message' => 'Failed',
                        'errors' => true,
                    ]);
        } else {
         
            $add_order = new Order;
            $add_order->user_customer_id = $user_customer_id;
            $add_order->user_store_id = $user_store_id;
            $add_order->status = $status;
            $add_order->save();
        
            if ($add_order) {
                
                $add_order->id;
                $order_item;
    
                foreach ($product_id as $product) {
                
                    $add_order_item = new OrderItem;
                    $add_order_item->order_id = $add_order->id;
                    $add_order_item->product_id = $product;
                    $add_order_item->save();
    
                    if ($add_order) {
                        $order_item[] = $add_order;
                    }
    
                }        
            
                return response()->json([
                    'message' => 'Success',
                    'errors' => false,
                    'order' => $add_order,
                    'order_item' => $order_item,
                ]);
    
            } else {
    
                return response()->json([
                    'message' => 'Failed',
                    'errors' => true,
                ]);
            }
     
        }
    
    } 
   
    public function showOrder(Request $request)
    {
        $user_customer_id = $request->user_customer_id;
        $user_store_id = $request->user_store_id;

        if($user_customer_id != '') {
            
            $order = Order::join('order_items', 'orders.id', '=', 'order_items.order_id')
                        ->join('products', 'products.id', 'order_items.product_id')
                        ->where('orders.user_customer_id', $user_customer_id)
                        ->orderBy('orders.updated_at', 'DESC')
                        ->get(['orders.*','products.id as product_id','products.name', 
                                'products.price', 'products.price_promo',
                                'products.description', 'products.image']
                    );
            
            $unique = $order->unique('id')->values();

            if ($order->isEmpty()) {
                return response()->json([
                    'message' => 'Failed',
                    'errors' => true,
                ]);

            } else {
    
                return response()->json([
                    'message' => 'Success',
                    'errors' => false,
                    'data' => $unique,
                    'product' => $order,
                ]);
            }

        } else if($user_store_id != '') {
            
            $order = Order::join('order_items', 'orders.id', '=', 'order_items.order_id')
                        ->join('products', 'products.id', 'order_items.product_id')
                        ->where('orders.user_store_id', $user_store_id)
                        ->orderBy('orders.updated_at', 'DESC')
                        ->get(['orders.*','products.id as product_id','products.name', 
                                'products.price', 'products.price_promo',
                                'products.description', 'products.image']
                  );

            $unique = $order->unique('id')->values();

            if ($order->isEmpty()) {
                return response()->json([
                    'message' => 'Failed',
                    'errors' => true,
                ]);

            } else {

                return response()->json([
                    'message' => 'Success',
                    'errors' => false,
                    'data' => $unique,
                    'product' => $order,
                ]);
            }

        } else{

            return response()->json([
                'message' => 'Failed',
                'errors' => true,
            ]);
           
        }
    }

    public function addTransactionProofOrder(Request $request)
    {   
        $order_id = $request->order_id;
        $image = $request->image;

        $exist = Order::where([
            ['id', '=', $order_id]
        ])->exists();

        if($exist){
        
            $allowedfileExtension = ['jpeg', 'jpg', 'png', 'JPG', 'JPEG'];
            if ($request->hasfile('image')) {

                $filename = time() . '.' . $image->getClientOriginalName();
                $extension = $image->getClientOriginalExtension();

                $check = in_array($extension, $allowedfileExtension);

                if ($check) {

                    $image->move(public_path() . '/image/transaction_proof/', $filename);

                    $add_transac_proof = Order::find($order_id);
                    $add_transac_proof->image_transaction = $filename;
                    $add_transac_proof->save();
                   
                    if ($add_transac_proof) {

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
        } else {
            return response()->json([
                'message' => 'Failed',
                'errors' => true,
            ]);   
        }
    }    

    public function addStatusOrder(Request $request){
        
        $order_id = $request->order_id;
        $status = $request->status;

        $exist = Order::where([
            ['id', '=', $order_id]
        ])->exists();

        if($exist){
            $add_status_order = Order::find($order_id);
            $add_status_order->status = $status;
            $add_status_order->save();
           
            if ($add_status_order) {
    
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
}