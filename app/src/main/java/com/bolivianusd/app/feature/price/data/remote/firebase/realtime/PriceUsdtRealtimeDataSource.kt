package com.bolivianusd.app.feature.price.data.remote.firebase.realtime

import com.bolivianusd.app.shared.data.exception.RealtimeDatabaseException
import com.bolivianusd.app.feature.price.data.remote.firebase.dto.PriceRealtimeDto
import com.bolivianusd.app.feature.price.data.mapper.toPrice
import com.bolivianusd.app.feature.price.data.mapper.toPriceRange
import com.bolivianusd.app.feature.price.data.remote.firebase.dto.PriceRangeRealtimeDto
import com.bolivianusd.app.feature.price.domain.model.Price
import com.bolivianusd.app.feature.price.domain.model.PriceRange
import com.bolivianusd.app.shared.domain.model.TradeType
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
            TradeType.BUY -> PRICE_BUY_PATH
            TradeType.SELL -> PRICE_SELL_PATH
        }
        val reference = firebaseDatabase.getReference(path)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dto = snapshot.getValue(PriceRealtimeDto::class.java)
                if (dto == null) {
                    close(RealtimeDatabaseException.NullOrInvalidData())
                } else {
                    trySend(dto.toPrice())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                close(RealtimeDatabaseException.Cancelled(error.toException()))
            }
        }
        reference.addValueEventListener(listener)
        awaitClose { reference.removeEventListener(listener) }
    }

    fun observePriceRange(tradeType: TradeType): Flow<PriceRange> = callbackFlow {
        val path = when (tradeType) {
            TradeType.BUY -> PRICE_RANGE_BUY_PATH
            TradeType.SELL -> PRICE_RANGE_SELL_PATH
        }
        val reference = firebaseDatabase.getReference(path)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dto = snapshot.getValue(PriceRangeRealtimeDto::class.java)
                if (dto == null) {
                    close(RealtimeDatabaseException.NullOrInvalidData())
                } else {
                    trySend(dto.toPriceRange())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                close(RealtimeDatabaseException.Cancelled(error.toException()))
            }
        }
        reference.addValueEventListener(listener)
        awaitClose { reference.removeEventListener(listener) }
    }

    companion object {
        private const val PRICE_BUY_PATH = "price_buy_usdt"
        private const val PRICE_SELL_PATH = "price_sell_usdt"
        private const val PRICE_RANGE_BUY_PATH = "price_range_buy_usdt"
        private const val PRICE_RANGE_SELL_PATH = "price_range_sell_usdt"
    }

}

