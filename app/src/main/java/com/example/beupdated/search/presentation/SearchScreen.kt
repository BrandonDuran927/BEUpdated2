package com.example.beupdated.search.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beupdated.R
import com.example.beupdated.search.presentation.composables.ProductGrid
import com.example.beupdated.search.presentation.composables.SearchItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onProductScreen: (String) -> Unit,
    action: (SearchAction) -> Unit,
    state: SearchState
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth(),
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 5.dp, end = 20.dp),
                            value = state.searchQuery,
                            onValueChange = {
                                action(SearchAction.OnChangeSearchQuery(it))
                                action(SearchAction.OnSearchProduct(it))
                            },
                            placeholder = {
                                Text(
                                    text = "Search..",
                                    fontSize = 20.sp
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    Icons.Default.Search,
                                    null,
                                    modifier = Modifier.size(25.dp)
                                )
                            },
                            shape = RoundedCornerShape(20.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.Black,
                                focusedContainerColor = Color.White,
                                focusedBorderColor = colorResource(R.color.blue),
                                cursorColor = colorResource(R.color.blue),
                                unfocusedContainerColor = Color.White,
                                focusedPlaceholderColor = Color.Black,
                            )
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
        ) {
            if (state.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillParentMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = colorResource(R.color.gold)
                        )
                    }
                }
            } else {
                item {
                    Box(modifier = Modifier.height(600.dp)) {
                        ProductGrid(
                            products = state.products,
                            onProductScreen = {
                                onProductScreen(it)
                            }
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun Prev() {
    SearchScreen(
        onProductScreen = {},
        action = {},
        state = SearchState(),
    )
}

internal val searches = listOf(
    SearchModel("Small uniform for girl"),
    SearchModel("Big tumbler"),
    SearchModel("Sports bag that has a strap"),
)

internal data class SearchModel(
    val description: String
)