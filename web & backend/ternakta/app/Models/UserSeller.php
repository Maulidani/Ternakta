<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Factories\HasFactory;

class UserSeller extends Model
{
    use HasFactory;
    public $timestamps = false;

    protected $table = 'user_sellers';

    protected $fillable = [
       'id', 'name', 'email', 'password','img','phone', 'address', 'province', 'city', 'districts','zip_code',
    ];

}