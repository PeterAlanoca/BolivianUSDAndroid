package com.bolivianusd.app.feature.price.di

import com.bolivianusd.app.core.managers.NetworkManager
import com.bolivianusd.app.feature.price.data.local.room.DailyCandleRoomDataSource
import com.bolivianusd.app.feature.price.data.local.room.dao.DailyCandleDao
import com.bolivianusd.app.feature.price.data.remote.supabase.postgrest.DailyCandlePostgrestDataSource
import com.bolivianusd.app.feature.price.data.repository.DailyCandleRepositoryImpl
import com.bolivianusd.app.feature.price.domain.repository.DailyCandleRepository
import com.bolivianusd.app.feature.price.domain.usecase.GetLatestCandlesUseCase
import com.bolivianusd.app.feature.price.domain.usecase.GetLatestCandlesUseCaseImpl
import com.bolivianusd.app.feature.price.domain.usecase.ObservePriceRangeUseCase
import com.bolivianusd.app.feature.price.domain.usecase.ObservePriceRangeUseCaseImpl
import com.bolivianusd.app.feature.price.domain.usecase.ObservePriceUseCase
import com.bolivianusd.app.feature.price.domain.usecase.ObservePriceUseCaseImpl
import com.bolivianusd.app.shared.data.local.room.AppDatabase
import com.bolivianusd.app.shared.domain.repository.PriceRepository
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
    fun provideDailyCandleDao(
        appDatabase: AppDatabase
    ): DailyCandleDao = appDatabase.dailyCandleDao()

    @Provides
    @Singleton
    fun provideDailyCandleRoomDataSource(
        dailyCandleDao: DailyCandleDao
    ) = DailyCandleRoomDataSource(
        dailyCandleDao = dailyCandleDao
    )

    @Provides
    @Singleton
    fun provideDailyCandlePostgrestDataSource(postgrest: Postgrest, networkManager: NetworkManager) =
        DailyCandlePostgrestDataSource(
            postgrest = postgrest,
            networkManager = networkManager
        )

    @Singleton
    @Provides
    fun provideDailyCandleRepository(
        dailyCandlePostgrestDataSource: DailyCandlePostgrestDataSource,
        dailyCandleRoomDataSource: DailyCandleRoomDataSource
    ): DailyCandleRepository = DailyCandleRepositoryImpl(
        dailyCandlePostgrestDataSource = dailyCandlePostgrestDataSource,
        dailyCandleRoomDataSource = dailyCandleRoomDataSource
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