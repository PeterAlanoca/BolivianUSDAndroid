package com.bolivianusd.app.shared.di

import android.content.Context
import androidx.room.Room
import com.bolivianusd.app.core.managers.NetworkManager
import com.bolivianusd.app.shared.data.local.room.AppDatabase
import com.bolivianusd.app.shared.data.local.room.PriceRoomDataSource
import com.bolivianusd.app.shared.data.local.room.dao.PriceDao
import com.bolivianusd.app.shared.domain.usecase.GetPricePollingUseCase
import com.bolivianusd.app.shared.domain.usecase.GetPricePollingUseCaseImpl
import com.bolivianusd.app.shared.domain.usecase.GetPriceRangePollingUseCase
import com.bolivianusd.app.shared.domain.usecase.GetPriceRangePollingUseCaseImpl
import com.bolivianusd.app.shared.data.remote.firebase.firestore.PriceUsdFirestoreDataSource
import com.bolivianusd.app.shared.data.remote.firebase.realtime.PriceUsdtRealtimeDataSource
import com.bolivianusd.app.shared.data.repository.PriceRepositoryImpl
import com.bolivianusd.app.shared.domain.repository.PriceRepository
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.MemoryCacheSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providePriceDao(appDatabase: AppDatabase): PriceDao = appDatabase.priceDao()

    @Provides
    @Singleton
    fun provideNetworkManager(@ApplicationContext context: Context) = NetworkManager(context)

    @Provides
    @Singleton
    fun provideFirebaseDatabase() = FirebaseDatabase.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        val settings = FirebaseFirestoreSettings.Builder()
            .setLocalCacheSettings(MemoryCacheSettings.newBuilder().build())
            .build()

        return FirebaseFirestore.getInstance().apply {
            firestoreSettings = settings
        }
    }

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = "https://lerpekuagrjuizzdxomj.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImxlcnBla3VhZ3JqdWl6emR4b21qIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTc5NTQ0MzUsImV4cCI6MjA3MzUzMDQzNX0.9vPwdjuIPf09Z5mwblVX1fkIc0-IYhQmUTqacSN5F0s"
        ) {
            install(Postgrest)
        }
    }

    @Provides
    @Singleton
    fun providePostgrest(client: SupabaseClient) = client.postgrest

    @Provides
    @Singleton
    fun providePriceRoomDataSource(
        priceDao: PriceDao
    ) = PriceRoomDataSource(
        priceDao = priceDao
    )

    @Provides
    @Singleton
    fun providePriceUsdtRealtimeDataSource(
        firebaseDatabase: FirebaseDatabase,
        networkManager: NetworkManager
    ) = PriceUsdtRealtimeDataSource(
        firebaseDatabase = firebaseDatabase,
        networkManager = networkManager
    )

    @Provides
    @Singleton
    fun providePriceUsdFirestoreDataSource(
        firebaseFirestore: FirebaseFirestore,
        networkManager: NetworkManager
    ) = PriceUsdFirestoreDataSource(
        firebaseFirestore = firebaseFirestore,
        networkManager = networkManager
    )

    @Singleton
    @Provides
    fun providePriceRepository(
        priceUsdtRealtimeDataSource: PriceUsdtRealtimeDataSource,
        priceUsdFirestoreDataSource: PriceUsdFirestoreDataSource,
        priceRoomDataSource: PriceRoomDataSource
    ): PriceRepository = PriceRepositoryImpl(
        priceUsdtRealtimeDataSource = priceUsdtRealtimeDataSource,
        priceUsdFirestoreDataSource = priceUsdFirestoreDataSource,
        priceRoomDataSource = priceRoomDataSource
    )

    @Singleton
    @Provides
    fun provideGetPricePollingUseCase(
        priceRepository: PriceRepository
    ): GetPricePollingUseCase = GetPricePollingUseCaseImpl(
        priceRepository = priceRepository
    )

    @Singleton
    @Provides
    fun provideGetPriceRangePollingUseCase(
        priceRepository: PriceRepository
    ): GetPriceRangePollingUseCase = GetPriceRangePollingUseCaseImpl(
        priceRepository = priceRepository
    )

}
