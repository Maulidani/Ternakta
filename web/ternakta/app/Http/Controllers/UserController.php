<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\UserCustomer;
use App\Models\UserStore;
use App\Models\UserAdmin;

use function PHPUnit\Framework\isEmpty;
use function PHPUnit\Framework\isNull;

class UserController
{
    public function addUser(Request $request)
    {   
        $type = $request->type;
        $business_permit = $request->business_permit;
        $name = $request->name;
        $image = $request->image;
        $phone = $request->phone;
        $password = $request->password;
        $province = $request->province;
        $city = $request->city;
        $districts = $request->districts;
        $address = $request->address;
        $status = $request->status;

        $exist;

        if($type == 'store') {
            
            $exist = UserStore::where([
                ['phone', '=', $phone],
            ])->exists();
    
        } else if($type == 'customer') {
            
            $exist = UserCustomer::where([
                ['phone', '=', $phone],
            ])->exists();
    
        } else {

            return response()->json([
                'message' => 'Failed',
                'errors' => true,
            ]);
        }

       
        if(!$exist){

            $allowedfileExtension = ['jpeg', 'jpg', 'png', 'JPG', 'JPEG'];
            if ($request->hasfile('image')) {

                $filename = time() . '.' . $image->getClientOriginalName();
                $extension = $image->getClientOriginalExtension();

                $check = in_array($extension, $allowedfileExtension);

                if ($check) {

                    $add_user;

                    if($type == 'store') {
            
                        $image->move(public_path() . '/image/user_store/', $filename);

                        $add_user = new UserStore;
                        $add_user->business_permit = $business_permit;
                        $add_user->name = $request->name;
                        $add_user->image = $filename;
                        $add_user->phone = $phone;
                        $add_user->password = $password;
                        $add_user->province = $province;
                        $add_user->city = $city;
                        $add_user->districts = $districts;
                        $add_user->address = $address;
                        $add_user->status = $status;
                        $add_user->save();
    
                
                    } else if($type == 'customer') {
                        
                        $image->move(public_path() . '/image/user_customer/', $filename);

                        $add_user = new UserCustomer;
                        $add_user->name = $request->name;
                        $add_user->image = $filename;
                        $add_user->phone = $phone;
                        $add_user->password = $password;
                        $add_user->province = $province;
                        $add_user->city = $city;
                        $add_user->districts = $districts;
                        $add_user->address = $address;
                        $add_user->save();
                
                    } else {
            
                        return response()->json([
                            'message' => 'Failed',
                            'errors' => true,
                        ]);
                    }
                   
                    if ($add_user) {

                        return response()->json([
                            'message' => 'Success',
                            'errors' => false,
                            'user' => $add_user
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
                    'message' => 'Exist',
                    'errors' => true,
                ]);
        }
    }

    public function editUser(Request $request)
    {
        $type = $request->type;
        $id_user = $request->id_user;
        $business_permit = $request->business_permit;
        $name = $request->name;
        $image = $request->image;
        $phone = $request->phone;
        $password = $request->password;
        $province = $request->province;
        $city = $request->city;
        $districts = $request->districts;
        $address = $request->address;
        $status = $request->status;

        $exist;
      
        if($type == 'store') {
            
            $exist = UserStore::where([
                ['id', '=', $id_user],
            ])->exists();
    
        } else if($type == 'customer') {
            
            $exist = UserCustomer::where([
                ['id', '=', $id_user],
            ])->exists();
    
        } else {

            return response()->json([
                'message' => 'Failed',
                'errors' => true,
            ]);
        }

        if($exist){

            $allowedfileExtension = ['jpeg', 'jpg', 'png', 'JPG', 'JPEG'];
            if ($request->hasfile('image')) {

                $filename = time() . '.' . $image->getClientOriginalName();
                $extension = $image->getClientOriginalExtension();

                $check = in_array($extension, $allowedfileExtension);

                if ($check) {
                
                    $edit_user;

                    if($type == 'store') {
            
                        $image->move(public_path() . '/image/user_store/', $filename);

                        $edit_user = UserStore::find($id_user);
                        // $edit_user->business_permit = $business_permit;
                        $edit_user->name = $request->name;
                        $edit_user->image = $filename;
                        $edit_user->phone = $phone;
                        $edit_user->password = $password;
                        $edit_user->province = $province;
                        $edit_user->city = $city;
                        $edit_user->districts = $districts;
                        $edit_user->address = $address;
                        $edit_user->save();
    
                
                    } else if($type == 'customer') {
                        
                        $image->move(public_path() . '/image/user_customer/', $filename);

                        $edit_user = UserCustomer::find($id_user);
                        $edit_user->name = $request->name;
                        $edit_user->image = $filename;
                        $edit_user->phone = $phone;
                        $edit_user->password = $password;
                        $edit_user->province = $province;
                        $edit_user->city = $city;
                        $edit_user->districts = $districts;
                        $edit_user->address = $address;
                        $edit_user->save();
                
                    } else {
            
                        return response()->json([
                            'message' => 'Failed',
                            'errors' => true,
                        ]);
                    }
                   
                    if ($edit_user) {

                        return response()->json([
                            'message' => 'Success',
                            'errors' => false,
                            'user' => $edit_user
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

                $edit_user;

                if($type == 'store') {
            
                    $edit_user = UserStore::find($id_user);
                    // $edit_user->business_permit = $business_permit;
                    $edit_user->name = $request->name;
                    $edit_user->phone = $phone;
                    $edit_user->password = $password;
                    $edit_user->province = $province;
                    $edit_user->city = $city;
                    $edit_user->districts = $districts;
                    $edit_user->address = $address;
                    $edit_user->save();

            
                } else if($type == 'customer') {
                    
                    $edit_user = UserCustomer::find($id_user);
                    $edit_user->name = $request->name;
                    $edit_user->phone = $phone;
                    $edit_user->password = $password;
                    $edit_user->province = $province;
                    $edit_user->city = $city;
                    $edit_user->districts = $districts;
                    $edit_user->address = $address;
                    $edit_user->save();
            
                } else {
        
                    return response()->json([
                        'message' => 'Failed',
                        'errors' => true,
                    ]);
                }
               
                if ($edit_user) {

                    return response()->json([
                        'message' => 'Success',
                        'errors' => false,
                        'user' => $edit_user
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

    public function deleteUser(Request $request)
    {
        $id_user = $request->id_user;
        $type = $request->type;

        $delete;
         
        if($type == 'store') {
            
            $delete = UserStore::where(
                'id',
                $request->id_user
            )->delete();
    
        } else if($type == 'customer') {
            
            $delete = UserCustomer::where(
                'id',
                $request->id_user
            )->delete();
    
        } else {

            return response()->json([
                'message' => 'Failed',
                'errors' => true,
            ]);
        }

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

    public function loginUser(Request $request)
    {
        $type = $request->type;
        $username = $request->username;
        $phone = $request->phone;
        $password = $request->password;
        $type = $request->type;

        if($type == 'store') {

            $exist = UserStore::where([
                ['phone', '=', $phone],
                ['password', '=', $password]
            ])->exists();

            if($exist){

                $login = UserStore::where([
                    ['phone', '=', $phone],
                    ['password', '=', $password]
                ])->first();

                return response()->json([
                    'message' => 'Success',
                    'errors' => false,
                    'user' => $login
                ]);

            } else {
                return response()->json([
                    'message' => 'Failed',
                    'errors' => true,
                ]);
            }

        } else if($type == 'customer') {

            $exist = UserCustomer::where([
                ['phone', '=', $phone],
                ['password', '=', $password]
            ])->exists();

            if($exist){

                $login = UserCustomer::where([
                    ['phone', '=', $phone],
                    ['password', '=', $password]
                ])->first();

                return response()->json([
                    'message' => 'Success',
                    'errors' => false,
                    'user' => $login
                ]);

            } else {
                return response()->json([
                    'message' => 'Failed',
                    'errors' => true,
                ]);
            }

        }else if($type == 'admin'){

            $exist = UserAdmin::where([
                ['username', '=', $username],
                ['password', '=', $password]
            ])->exists();

            if($exist){

                $login = UserAdmin::where([
                    ['username', '=', $username],
                    ['password', '=', $password]
                ])->first();

                return response()->json([
                    'message' => 'Success',
                    'errors' => false,
                    'user' => $login
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

    public function addStatusStore(Request $request)
    {

        $id_user = $request->id_user;
        $status = $request->status;

        $status_user = UserStore::find($id_user);
        $status_user->status = $status;
        $status_user->save();

        if($status_user) {

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

    public function showUser(Request $request)
    {
        $type = $request->type;
        $search = $request->search;

        if($type == 'store') {
            
            $user = UserStore::orderBy('updated_at', 'DESC')
            ->where('name', 'like', "%" . $search . "%")
            ->get();
    
            if ($user->isEmpty()) {
                return response()->json([
                    'message' => 'Failed',
                    'errors' => true,
                ]);
            } else {
    
                return response()->json([
                    'message' => 'Success',
                    'errors' => false,
                    'data' => $user,
                ]);
            }
    
        } else if($type == 'customer') {
            
            $user = UserCustomer::orderBy('updated_at', 'DESC')
            ->where('name', 'like', "%" . $search . "%")
            ->get();
    
            if ($user->isEmpty()) {
                return response()->json([
                    'message' => 'Failed',
                    'errors' => true,
                ]);
            } else {
    
                return response()->json([
                    'message' => 'Success',
                    'errors' => false,
                    'data' => $user,
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