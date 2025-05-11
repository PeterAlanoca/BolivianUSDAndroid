package com.bolivianusd.app.di

import com.bolivianusd.app.data.repository.PriceRepository
import com.bolivianusd.app.data.repository.datasource.PriceDataSource
import com.bolivianusd.app.data.repository.datasource.PriceRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun providePriceRepository(
        priceDataSource: PriceDataSource
    ): PriceRepository =
        PriceRepositoryImpl(priceDataSource = priceDataSource)

}
