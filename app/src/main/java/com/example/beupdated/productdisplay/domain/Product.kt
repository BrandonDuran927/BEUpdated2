package com.example.beupdated.productdisplay.domain

data class Product(
    val name: String = "",
    val stockQuantity: Int = 0,
    val color: List<String> = emptyList(),
    val size: List<String> = emptyList(),
    val lastUpdated: String = "",
    val category: String = "",
    val description: String = "",
    val price: Int = 0
)


data class ProductRaw(
    val name: String = "",
    val stockQuantity: Int = 0,
    val color: Any = "", // Can be String or List<String>
    val lastUpdated: String = "",
    val category: String = "",
    val price: Int = 0,
    val description: String = "",
    val size: Any = ""
)

fun ProductRaw.toProduct(): Product {
    val colorList = when (color) {
        is String -> listOf(color) // Convert single string to list
        is List<*> -> color.filterIsInstance<String>() // Filter valid strings from list
        else -> emptyList() // Fallback for unexpected types
    }

    val sizeList = when (size) {
        is String -> listOf(size) // Convert single string to list
        is List<*> -> size.filterIsInstance<String>() // Filter valid strings from list
        else -> emptyList() // Fallback for unexpected types
    }

    return Product(
        name = this.name,
        stockQuantity = this.stockQuantity,
        color = colorList,
        lastUpdated = this.lastUpdated,
        category = this.category,
        price = this.price,
        description = this.description,
        size = sizeList
    )
}