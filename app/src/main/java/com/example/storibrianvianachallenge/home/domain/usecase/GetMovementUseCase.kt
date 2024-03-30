package com.example.storibrianvianachallenge.home.domain.usecase

import com.example.storibrianvianachallenge.home.data.BankInformationRepository
import javax.inject.Inject

class GetMovementUseCase @Inject constructor(private val repository: BankInformationRepository) {
    suspend operator fun invoke(id: Int) = repository.getMovement(id)
}