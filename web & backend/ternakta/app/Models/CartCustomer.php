<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Factories\HasFactory;

class CartCustomer extends Model
{
    use HasFactory;
    public $timestamps = false;

    protected $table = 'cart_customers';

    protected $fillable = [
       'id', 'user_customer_id', 'product_seller_id',
    ];

}