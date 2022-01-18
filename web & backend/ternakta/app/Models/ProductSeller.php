<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Factories\HasFactory;

class ProductSeller extends Model
{
    use HasFactory;
    public $timestamps = false;

    protected $table = 'product_sellers';

    protected $fillable = [
        'user_seller_id', 'name', 'price','quantity','img','description'
    ];

}