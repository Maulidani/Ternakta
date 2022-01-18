<?php

namespace App\Http\Controllers;

use App\Models\User;
use Illuminate\Http\Request;
use App\Models\UserAccount;
use App\Models\UserAddress;
use App\Models\UserCustomer;
use App\Models\UserSeller;
use Illuminate\Support\Facades\Auth;

class UserController extends Controller
{
    public function registerSeller(Request $request)
    {

        $request->validate([
            'email' => 'required|email',
            'password' => 'required',
        ]);

        $files = $request->image;
        $allowedfileExtension = ['jpeg', 'jpg', 'png', 'JPG', 'JPEG'];
        if ($request->hasfile('image')) {

            $filename = time() . '.' . $files->getClientOriginalName();
            $extension = $files->getClientOriginalExtension();

            $check = in_array($extension, $allowedfileExtension);

            if ($check) {

                $files->move(public_path() . '/app/image/user/', $filename);

                $user_seller = new UserSeller();
                $user_seller->name = $request->name;
                $user_seller->email = $request->email;
                $user_seller->password = $request->password;
                $user_seller->image = $filename;
                $user_seller->phone = $request->phone;
                $user_seller->address = $request->address;
                $user_seller->province = $request->province;
                $user_seller->city = $request->city;
                $user_seller->districts = $request->districts;
                $user_seller->zip_code = $request->zip_code;
                $user_seller->save();

                if ($user_seller) {

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

    public function registerCustomer(Request $request)
    {

        $request->validate([
            'email' => 'required|email',
            'password' => 'required',
        ]);

        $files = $request->image;
        $allowedfileExtension = ['jpeg', 'jpg', 'png', 'JPG', 'JPEG'];
        if ($request->hasfile('image')) {

            $filename = time() . '.' . $files->getClientOriginalName();
            $extension = $files->getClientOriginalExtension();

            $check = in_array($extension, $allowedfileExtension);

            if ($check) {

                $files->move(public_path() . '/app/image/user/', $filename);

                $user_customer = new UserCustomer();
                $user_customer->name = $request->name;
                $user_customer->email = $request->email;
                $user_customer->password = $request->password;
                $user_customer->image = $filename;
                $user_customer->save();

                if ($user_customer) {

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

    public function editSeller(Request $request)
    {
        $request->validate([
            'email' => 'required|email',
        ]);

        $user_seller = UserSeller::find($request->id);
        $user_seller->name = $request->name;
        $user_seller->email = $request->email;
        $user_seller->phone = $request->phone;
        $user_seller->province = $request->province;
        $user_seller->city = $request->city;
        $user_seller->districts = $request->districts;
        $user_seller->zip_code = $request->zip_code;
        $user_seller->save();

        if ($user_seller) {

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
    public function editCustomer(Request $request)
    {
        $request->validate([
            'email' => 'required|email',
        ]);

        $user_customer = UserCustomer::find($request->id);
        $user_customer->name = $request->name;
        $user_customer->email = $request->email;
        $user_customer->save();

        if ($user_customer) {

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

                if ($request->type === 'seller') {

                    $files->move(public_path() . '/app/image/user/', $filename);
                    $user = UserSeller::find($request->id);
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
                } else if ($request->type === 'customer') {

                    $files->move(public_path() . '/app/image/user/', $filename);
                    $user = UserCustomer::find($request->id);
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
        } else {
            return response()->json([
                'message' => 'Fail',
                'errors' => true,
            ]);
        }
    }

    public function login(Request $request)
    {
        $email = $request->email;
        $password = $request->password;
        $type = $request->type;

        if ($type === 'seller') {
            $exist = UserCustomer::where([
                ['email', '=', $email],
                ['password', '=', $password]
            ])->exists();

            if ($exist) {
                $data =  UserCustomer::where([
                    ['email', '=', $email],
                    ['password', '=', $password]
                ])->first();

                return response()->json([
                    'message' => 'Success',
                    'errors' => false,
                    'data' => $data
                ]);
            } else {

                return response()->json([
                    'message' => 'Fail',
                    'errors' => true,
                ]);
            }

        } else if ($type === 'customer') {

            $exist = UserSeller::where([
                ['email', '=', $email],
                ['password', '=', $password]
            ])->exists();

            if ($exist) {
                $data =  UserCustomer::where([
                    ['email', '=', $email],
                    ['password', '=', $password]
                ])->first();

                return response()->json([
                    'message' => 'Success',
                    'errors' => false,
                    'data' => $data
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
    }
}