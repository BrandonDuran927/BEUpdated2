package com.example.beupdated.search.presentation

sealed class SearchAction {
    data object OnResetState : SearchAction()
    data object OnResetError : SearchAction()
    data class OnSearchProduct(val searchQuery: String) : SearchAction()
    data class OnChangeSearchQuery(val searchQuery: String) : SearchAction()
}