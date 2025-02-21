package com.example.beupdated.savedproduct.domain

import com.example.beupdated.common.utilities.CustomResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IncrementProductUseCase @Inject constructor(
    private val repository: SavedProductRepository
) {
    suspend operator fun invoke(userId: String, savedAt: String): Flow<CustomResult<Unit>> {
        return repository.incrementSavedProduct(userId, savedAt)
    }
}