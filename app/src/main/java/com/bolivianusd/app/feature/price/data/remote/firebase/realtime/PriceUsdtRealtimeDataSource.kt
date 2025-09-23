package com.bolivianusd.app.feature.price.data.remote.firebase.realtime

import com.bolivianusd.app.feature.price.data.remote.firebase.dto.PriceRealtimeDto
import com.bolivianusd.app.feature.price.data.mappers.toPrice
import com.bolivianusd.app.feature.price.domain.model.Price
import com.bolivianusd.app.shared.data.model.TradeType
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

    fun observePrice(tradeType: TradeType): Flow<Price> = callbackFlow {
        val path = when (tradeType) {
            TradeType.BUY -> PATH_BUY
            TradeType.SELL -> PATH_SELL
        }
        val reference = firebaseDatabase.getReference(path)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dto = snapshot.getValue(PriceRealtimeDto::class.java)
                dto?.let { trySend(it.toPrice()) }
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        reference.addValueEventListener(listener)
        awaitClose { reference.removeEventListener(listener) }
    }

    companion object {
        private const val PATH_BUY = "price_buy_usdt1"
        private const val PATH_SELL = "price_sell_usdt1"
    }

}

