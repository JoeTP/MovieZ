package com.example.domain.usecases

interface UseCase<in P, out R> {
    suspend operator fun invoke(params: P): R
}