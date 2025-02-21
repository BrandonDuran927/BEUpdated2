package com.example.beupdated.common.composables

import com.example.beupdated.R

fun imageFinder(productName: String): Int {
    return when(productName) {
        "Black t-shirt" -> R.drawable.blackts
        "Go for the B/G t-shirt" -> R.drawable.bgts
        "Hydro coffee" -> R.drawable.hydrocoffee
        "Plastic water bottle" -> R.drawable.plasticwaterbottle
        "Sports bag" -> R.drawable.sportsbag
        "Tote bag" -> R.drawable.totebag
        "Tourism suit" -> R.drawable.tourismsuit
        "Traditional polo for female" -> R.drawable.traditionalunif
        "Traditional female polo for SHS" -> R.drawable.traditionalmaleshs
        "Traditional polo for male" ->  R.drawable.traditionalunif
        "Traditional male polo for SHS" ->  R.drawable.traditionalmaleshs
        "Tumbler" -> R.drawable.tumbler
        else -> R.drawable.whitets
    }
}