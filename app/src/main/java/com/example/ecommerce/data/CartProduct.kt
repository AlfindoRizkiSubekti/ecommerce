package com.example.ecommerce.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class CartProduct(
    val product: Product,
    val quantity: Int,
    val selectedColor: Int? = null,
    val selectedSize: String? = null
){
    constructor() : this(Product(), 1, null, null)
}