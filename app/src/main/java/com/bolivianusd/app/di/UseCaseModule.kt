package com.bolivianusd.app.di

import com.bolivianusd.app.data.repository.PriceRepository
import com.bolivianusd.app.domain.GetPriceUsdtUseCase
import com.bolivianusd.app.domain.GetPriceUsdtUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Singleton
    @Provides
    fun providesGetPriceUsdtUseCase(
        repository: PriceRepository
    ): GetPriceUsdtUseCase = GetPriceUsdtUseCaseImpl(
        repository = repository
    )
}