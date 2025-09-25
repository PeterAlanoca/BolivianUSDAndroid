package com.bolivianusd.app.feature.price.di

import com.bolivianusd.app.feature.price.data.remote.firebase.firestore.PriceUsdFirestoreDataSource
import com.bolivianusd.app.feature.price.data.remote.firebase.realtime.PriceUsdtRealtimeDataSource
import com.bolivianusd.app.feature.price.data.remote.supabase.postgrest.DailyCandlePostgrestDataSource
import com.bolivianusd.app.feature.price.data.repository.DailyCandleRepositoryImpl
import com.bolivianusd.app.feature.price.data.repository.PriceRepositoryImpl
import com.bolivianusd.app.feature.price.domain.repository.DailyCandleRepository
import com.bolivianusd.app.feature.price.domain.repository.PriceRepository
import com.bolivianusd.app.feature.price.domain.usecase.GetLatestCandlesUseCase
import com.bolivianusd.app.feature.price.domain.usecase.GetLatestCandlesUseCaseImpl
import com.bolivianusd.app.feature.price.domain.usecase.ObservePriceRangeUseCase
import com.bolivianusd.app.feature.price.domain.usecase.ObservePriceRangeUseCaseImpl
import com.bolivianusd.app.feature.price.domain.usecase.ObservePriceUseCase
import com.bolivianusd.app.feature.price.domain.usecase.ObservePriceUseCaseImpl
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PriceModule {

    @Provides
    @Singleton
    fun providePriceUsdtRealtimeDataSource(firebaseDatabase: FirebaseDatabase) =
        PriceUsdtRealtimeDataSource(firebaseDatabase = firebaseDatabase)

    @Provides
    @Singleton
    fun providePriceUsdFirestoreDataSource(firebaseFirestore: FirebaseFirestore) =
        PriceUsdFirestoreDataSource(firebaseFirestore = firebaseFirestore)

    @Provides
    @Singleton
    fun provideDailyCandlePostgrestDataSource(postgrest: Postgrest) =
        DailyCandlePostgrestDataSource(postgrest = postgrest)

    @Singleton
    @Provides
    fun providePriceRepository(
        priceUsdtRealtimeDataSource: PriceUsdtRealtimeDataSource,
        priceUsdFirestoreDataSource: PriceUsdFirestoreDataSource
    ): PriceRepository = PriceRepositoryImpl(
        priceUsdtRealtimeDataSource = priceUsdtRealtimeDataSource,
        priceUsdFirestoreDataSource = priceUsdFirestoreDataSource
    )

    @Singleton
    @Provides
    fun provideDailyCandleRepository(
        dailyCandlePostgrestDataSource: DailyCandlePostgrestDataSource
    ): DailyCandleRepository = DailyCandleRepositoryImpl(
        dailyCandlePostgrestDataSource = dailyCandlePostgrestDataSource
    )

    @Singleton
    @Provides
    fun provideObservePriceUseCase(
        priceRepository: PriceRepository
    ): ObservePriceUseCase = ObservePriceUseCaseImpl(
        priceRepository = priceRepository
    )

    @Singleton
    @Provides
    fun provideObservePriceRangeUseCase(
        priceRepository: PriceRepository
    ): ObservePriceRangeUseCase = ObservePriceRangeUseCaseImpl(
        priceRepository = priceRepository
    )

    @Singleton
    @Provides
    fun provideGetLatestCandlesUseCase(
        dailyCandleRepository: DailyCandleRepository
    ): GetLatestCandlesUseCase = GetLatestCandlesUseCaseImpl(
        dailyCandleRepository = dailyCandleRepository
    )

}