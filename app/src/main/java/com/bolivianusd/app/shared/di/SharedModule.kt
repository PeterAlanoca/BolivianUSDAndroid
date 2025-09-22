package com.bolivianusd.app.shared.di

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    fun provideFirebaseDatabase() = FirebaseDatabase.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore() = FirebaseFirestore.getInstance()

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

}
