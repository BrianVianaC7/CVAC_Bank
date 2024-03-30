package com.example.storibrianvianachallenge.home.domain.usecase

import com.example.storibrianvianachallenge.home.data.BankInformationRepository
import javax.inject.Inject

class GetMovementsUseCase @Inject constructor(private val repository: BankInformationRepository) {
    suspend operator fun invoke(id: String) = repository.getAllMovements(id)
}