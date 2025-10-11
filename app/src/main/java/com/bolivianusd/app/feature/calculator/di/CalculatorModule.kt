package com.bolivianusd.app.feature.calculator.di

import com.bolivianusd.app.feature.calculator.domain.usecase.GetPriceRangePollingUseCase
import com.bolivianusd.app.feature.calculator.domain.usecase.GetPriceRangePollingUseCaseImpl
import com.bolivianusd.app.shared.domain.repository.PriceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CalculatorModule {

    @Singleton
    @Provides
    fun provideGetPriceRangePollingUseCase(
        priceRepository: PriceRepository
    ): GetPriceRangePollingUseCase = GetPriceRangePollingUseCaseImpl(
        priceRepository = priceRepository
    )

}
