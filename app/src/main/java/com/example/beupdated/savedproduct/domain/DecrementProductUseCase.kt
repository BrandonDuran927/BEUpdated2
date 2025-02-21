package com.example.beupdated.savedproduct.domain

import com.example.beupdated.common.utilities.CustomResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DecrementProductUseCase @Inject constructor(
    private val repository: SavedProductRepository
)  {
    suspend operator fun invoke(userId: String, savedProduct: SavedProduct) : Flow<CustomResult<Unit>> {
        return repository.decrementSavedProduct(userId, savedProduct)
    }
}