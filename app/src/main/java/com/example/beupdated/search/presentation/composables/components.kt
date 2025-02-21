package com.example.beupdated.search.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beupdated.authentication.presentation.AuthAction
import com.example.beupdated.common.composables.imageFinder
import com.example.beupdated.productdisplay.domain.Product
import com.example.beupdated.registration.presentation.SignUpAction
import com.example.beupdated.ui.theme.BeUpdatedTheme

@Composable
fun SearchItem(
    modifier: Modifier = Modifier,
    description: String
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = description,
            fontSize = 18.sp
        )
        IconButton(
            onClick = {}
        ) {
            Icon(
                modifier = Modifier.size(25.dp),
                imageVector = Icons.Default.Clear,
                contentDescription = null
            )
        }
    }
}

@Composable
fun ProductGrid(
    modifier: Modifier = Modifier,
    products: List<Product>,
    onProductScreen: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(products, key = { it.name }) { product ->
            ProductItem(
                product = product,
                onProductScreen = {
                    onProductScreen(it)
                }
            )
        }
    }
}


@Composable
fun ProductItem(
    product: Product,
    onProductScreen: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onProductScreen(product.name)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Image(
                modifier = Modifier.size(150.dp),
                painter = painterResource(imageFinder(product.name)),
                contentDescription = "Product Image",
                contentScale = ContentScale.Crop
            )
            Text(
                text = product.name.uppercase(),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = "Size: ${product.size.joinToString(", ")}",
                fontSize = 18.sp
            )
            Text(
                text = "Color: ${product.color.joinToString(", ")}",
                fontSize = 18.sp
            )
            Spacer(Modifier.height(5.dp))
            Text(
                text = "₱${product.price}.00",
                fontSize = 18.sp
            )
        }
    }
}


@Preview
@Composable
private fun Prev() {
    BeUpdatedTheme {
        val products = listOf(
            Product(
                name = "tote bag",
                price = 120
            ),
            Product(
                name = "tote bag",
                price = 120
            ),
            Product(
                name = "tote bag",
                price = 120
            ),
            Product(
                name = "tote bag",
                price = 120
            )
        )
        ProductGrid(
            products = products,
            onProductScreen = {}
        )
    }
}