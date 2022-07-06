<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Factories\HasFactory;

class UserItem extends Model
{
    use HasFactory;

    protected $table = 'order_items';

    protected $fillable = [
       'id','order_id','product_id','updated_at','created_at',
    ];

}