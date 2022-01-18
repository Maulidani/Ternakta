<?php

namespace App\Http\Controllers;

use App\Models\Banner;
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

class ProductController extends Controller
{
    public function index()
    {
        $product = ProductSeller::orderBy('created_at', 'DESC')->get();

        return response()->json([
            'message' => 'Success',
            'errors' => false,
            'product' => $product,
        ]);
    }

    // public function categoryProduct(Request $request)
    // {
    //     $product = ProductSeller::where('category', $request->category)->orderBy('created_at', 'DESC')->get;

    //     return response()->json([
    //         'message' => 'Success',
    //         'errors' => false,
    //         'product' => $product,
    //     ]);
    // }

    public function sellerProduct(Request $request)
    {
        $product = ProductSeller::where('user_seller_id', $request->user_seller_id)->orderBy('created_at', 'DESC')->get();

        return response()->json([
            'message' => 'Success',
            'errors' => false,
            'product' => $product,
        ]);
    }

    public function upload(Request $request)
    {

        $files = $request->image;
        $allowedfileExtension = ['jpeg', 'jpg', 'png', 'JPG', 'JPEG'];
        if ($request->hasfile('image')) {

            $filename = time() . '.' . $files->getClientOriginalName();
            $extension = $files->getClientOriginalExtension();

            $check = in_array($extension, $allowedfileExtension);

            if ($check) {

                $files->move(public_path() . '/app/image/product/', $filename);

                $upload_product = new ProductSeller();
                $upload_product->user_seller_id = $request->user_seller_id;
                $upload_product->name = $request->name;
                $upload_product->price = $request->price;
                $upload_product->quantity = $request->quantity;
                $upload_product->image = $filename;
                $upload_product->description = $request->description;
                $upload_product->save();

                if ($upload_product) {

                    return response()->json([
                        'message' => 'Success',
                        'errors' => false,
                    ]);
                } else {

                    return response()->json([
                        'message' => 'Fail',
                        'errors' => true,
                    ]);
                }
            } else {
                return response()->json([
                    'message' => 'Fail',
                    'errors' => true,
                ]);
            }
        } else {
            return response()->json([
                'message' => 'Fail',
                'errors' => true,
            ]);
        }
    }

    public function editProduct(Request $request)
    {

        $edit_product = ProductSeller::find($request->id);
        $edit_product->name = $request->name;
        $edit_product->price = $request->price;
        $edit_product->quantity = $request->quantity;
        $edit_product->description = $request->description;
        $edit_product->save();

        if ($edit_product) {

            return response()->json([
                'message' => 'Success',
                'errors' => false,
            ]);
        } else {

            return response()->json([
                'message' => 'Fail',
                'errors' => true,
            ]);
        }
    }


    public function editImage(Request $request)
    {

        $files = $request->image;
        $allowedfileExtension = ['jpeg', 'jpg', 'png', 'JPG', 'JPEG'];
        if ($request->hasfile('image')) {

            $filename = time() . '.' . $files->getClientOriginalName();
            $extension = $files->getClientOriginalExtension();

            $check = in_array($extension, $allowedfileExtension);

            if ($check) {

                $files->move(public_path() . '/app/image/product/', $filename);
                $user = ProductSeller::find($request->id);
                $user->image = $filename;
                $user->save();
                if ($user) {

                    return response()->json([
                        'message' => 'Success',
                        'errors' => false,
                    ]);
                } else {

                    return response()->json([
                        'message' => 'Fail',
                        'errors' => true,
                    ]);
                }
            } else {
                return response()->json([
                    'message' => 'Fail',
                    'errors' => true,
                ]);
            }
        } else {
            return response()->json([
                'message' => 'Fail',
                'errors' => true,
            ]);
        }
    }

    public function deleteProduct(Request $request)
    {
        ProductSeller::where(
            'id',
            $request->id
        )->delete();

        return response()->json([
            'message' => 'Success',
            'errors' => false,
        ]);
    }

    // public function banner()
    // {
    //     $banner = Banner::get();

    //     return response()->json([
    //         'message' => 'Success',
    //         'errors' => false,
    //         'banner' => $banner,
    //     ]);
    // }
}