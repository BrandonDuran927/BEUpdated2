package com.example.beupdated.productdisplay.domain.usecases

import com.example.beupdated.productdisplay.domain.ProductDisplayRepository
import javax.inject.Inject


class GetProductByNameUseCase @Inject constructor(
    private val repository: ProductDisplayRepository
) {
    suspend operator fun invoke(productName: String) = repository.getProductByName(productName)
}