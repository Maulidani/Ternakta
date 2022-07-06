<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Factories\HasFactory;

class UserCustomer extends Model
{
    use HasFactory;

    protected $table = 'user_customers';

    protected $fillable = [
       'id','name', 'image', 'phone','passsword','province','city','districts','address','updated_at','created_at',
    ];

}