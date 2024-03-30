package com.example.storibrianvianachallenge.main.domain.usecase

import com.example.storibrianvianachallenge.main.data.BankInformationRepository
import javax.inject.Inject

class GetMovementsUseCase @Inject constructor(private val repository: BankInformationRepository) {
    suspend operator fun invoke(id: String) = repository.getAllMovements(id)
}