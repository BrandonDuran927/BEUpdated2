package com.example.beupdated.search.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beupdated.common.utilities.CustomResult
import com.example.beupdated.search.domain.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository
) : ViewModel() {
    var state by mutableStateOf(SearchState())
        private set

    fun onAction(action: SearchAction) {
        when (action) {
            SearchAction.OnResetError -> state = state.copy(message = "")
            SearchAction.OnResetState -> state = SearchState()
            is SearchAction.OnSearchProduct -> searchProduct(action.searchQuery)
            is SearchAction.OnChangeSearchQuery -> state = state.copy(searchQuery = action.searchQuery)
        }
    }

    private fun searchProduct(query: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            delay(500L)
            repository.searchProducts(query).collectLatest { result ->
                when (result) {
                    is CustomResult.Success -> {
                        println("Query result: ${result.data}")
                        state = state.copy(products = result.data, isLoading = false)
                    }
                    is CustomResult.Failure -> state = state.copy(message = "", isLoading = false)
                }
            }
        }
    }
}