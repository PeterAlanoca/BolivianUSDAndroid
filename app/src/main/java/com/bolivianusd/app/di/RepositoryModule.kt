package com.bolivianusd.app.di

import android.content.Context
import com.bolivianusd.app.data.repository.PriceRepository
import com.bolivianusd.app.data.repository.datasource.PriceDataSource
import com.bolivianusd.app.data.repository.datasource.PriceRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

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

}
