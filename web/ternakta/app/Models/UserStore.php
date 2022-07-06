<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Factories\HasFactory;

class UserStore extends Model
{
    use HasFactory;

    protected $table = 'user_stores';

    protected $fillable = [
       'id','business_permit','name', 'image', 'phone','passsword','province','city','districts','address','status','updated_at','created_at',
    ];

}