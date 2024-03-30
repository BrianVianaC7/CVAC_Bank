package com.example.storibrianvianachallenge.main.domain.usecase

import com.example.storibrianvianachallenge.main.data.BankInformationRepository
import javax.inject.Inject

class GetMovementUseCase @Inject constructor(private val repository: BankInformationRepository) {
    suspend operator fun invoke(id: Int) = repository.getMovement(id)
}