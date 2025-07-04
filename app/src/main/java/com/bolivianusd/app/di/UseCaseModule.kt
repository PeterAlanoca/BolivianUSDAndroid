package com.bolivianusd.app.di

import com.bolivianusd.app.data.repository.PriceRepository
import com.bolivianusd.app.domain.GetChartPriceUsdtUseCase
import com.bolivianusd.app.domain.GetChartPriceUsdtUseCaseImpl
import com.bolivianusd.app.domain.GetPriceUsdtUseCase
import com.bolivianusd.app.domain.GetPriceUsdtUseCaseImpl
import com.bolivianusd.app.domain.GetRangePriceUsdtUseCase
import com.bolivianusd.app.domain.GetRangePriceUsdtUseCaseImpl
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

    @Singleton
    @Provides
    fun providesGetChartPriceUsdtUseCase(
        repository: PriceRepository
    ): GetChartPriceUsdtUseCase = GetChartPriceUsdtUseCaseImpl(
        repository = repository
    )

    @Singleton
    @Provides
    fun providesGetRangePriceUsdtUseCase(
        repository: PriceRepository
    ): GetRangePriceUsdtUseCase = GetRangePriceUsdtUseCaseImpl(
        repository = repository
    )

}