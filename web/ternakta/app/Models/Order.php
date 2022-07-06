<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Factories\HasFactory;

class Order extends Model
{
    use HasFactory;

    protected $table = 'orders';

    protected $fillable = [
       'id','user_customer_id','user_store_id', 'image_transaction', 'status','updated_at','created_at',
    ];

}