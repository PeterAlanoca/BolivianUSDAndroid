package com.bolivianusd.app.shared.di

import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedModule {

    @Provides
    @Singleton
    fun provideFirebaseDatabase() = FirebaseDatabase.getInstance()

    /*@Provides
    @Singleton
    fun provideFirebaseDatabase() = FirebaseDatabase.getInstance().apply {
        // Habilita la persistencia de disco (necesario para funcionamiento offline)
        setPersistenceEnabled(true)

        // Establece el tamaño de la caché (opcional)
        // Por defecto es 10MB, puedes aumentarlo si necesitas más datos offline
        setPersistenceCacheSizeBytes(20 * 1024 * 1024) // 20MB
    }*/
}
