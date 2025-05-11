package com.bolivianusd.app.di

import com.bolivianusd.app.data.repository.datasource.PriceDataSource
import com.bolivianusd.app.data.repository.datasource.realtime.database.PriceReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
}