package com.bolivianusd.app.feature.price.di

import android.content.Context
import com.bolivianusd.app.feature.price.data.old.repository.PriceRepository
import com.bolivianusd.app.feature.price.data.old.repository.datasource.PriceDataSource
import com.bolivianusd.app.feature.price.data.old.repository.datasource.PriceRepositoryImpl
import com.bolivianusd.app.feature.price.data.old.repository.datasource.realtime.database.PriceReference
import com.bolivianusd.app.feature.price.domain.usecase.GetChartPriceUsdtUseCase
import com.bolivianusd.app.feature.price.domain.usecase.GetChartPriceUsdtUseCaseImpl
import com.bolivianusd.app.feature.price.domain.usecase.GetPriceUsdtUseCase
import com.bolivianusd.app.feature.price.domain.usecase.GetPriceUsdtUseCaseImpl
import com.bolivianusd.app.feature.price.domain.usecase.GetRangePriceUsdtUseCase
import com.bolivianusd.app.feature.price.domain.usecase.GetRangePriceUsdtUseCaseImpl
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun providePriceReference(firebaseDatabase: FirebaseDatabase) =
        PriceReference(firebaseDatabase)

    @Provides
    @Singleton
    fun providePriceDataSource(priceReference: PriceReference) =
        PriceDataSource(priceReference = priceReference)

    @Singleton
    @Provides
    fun providePriceRepository(
        @ApplicationContext context: Context,
        priceDataSource: PriceDataSource
    ): PriceRepository =
        PriceRepositoryImpl(
            context = context,
            priceDataSource = priceDataSource
        )

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
