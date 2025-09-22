package com.bolivianusd.app.feature.price.data.remote.firebase.realtime

import com.bolivianusd.app.feature.price.data.remote.firebase.dto.PriceRealtimeDto
import com.bolivianusd.app.feature.price.data.mappers.toPrice
import com.bolivianusd.app.feature.price.domain.model.Price
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class PriceUsdtRealtimeDataSource @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) {
    fun observePriceBuy(): Flow<Price> = callbackFlow {
        val ref = firebaseDatabase.getReference("price_buy_usdt")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dto = snapshot.getValue(PriceRealtimeDto::class.java)
                dto?.let { trySend(it.toPrice()) }
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        ref.addValueEventListener(listener)

        awaitClose { ref.removeEventListener(listener) }
    }

    fun observePriceSell(): Flow<Price> = callbackFlow {
        val ref = firebaseDatabase.getReference("price_sell_usdt")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dto = snapshot.getValue(PriceRealtimeDto::class.java)
                dto?.let { trySend(it.toPrice()) }
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        ref.addValueEventListener(listener)

        awaitClose { ref.removeEventListener(listener) }
    }
}

