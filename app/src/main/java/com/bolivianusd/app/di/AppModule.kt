package com.bolivianusd.app.di

import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseDatabase() = FirebaseDatabase.getInstance()
    /*val firebaseDatabase = FirebaseDatabase.getInstance().apply {
        // Habilita la persistencia de disco (necesario para funcionamiento offline)
        setPersistenceEnabled(true)

        // Configuración adicional recomendada
        setLogLevel(Logger.Level.DEBUG) // Para ver logs de operaciones

        // Establece el tamaño de la caché (opcional)
        // Por defecto es 10MB, puedes aumentarlo si necesitas más datos offline
        database.setPersistenceCacheSizeBytes(20 * 1024 * 1024) // 20MB
    }*/
}
