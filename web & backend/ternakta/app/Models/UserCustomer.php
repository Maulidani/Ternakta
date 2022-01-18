<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Factories\HasFactory;

class UserCustomer extends Model
{
    use HasFactory;
    public $timestamps = false;

    protected $table = 'user_customers';

    protected $fillable = [
       'id', 'name', 'email', 'password','img',
    ];

}