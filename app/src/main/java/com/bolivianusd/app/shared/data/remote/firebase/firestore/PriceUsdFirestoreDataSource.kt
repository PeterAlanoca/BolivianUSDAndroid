package com.bolivianusd.app.shared.data.remote.firebase.firestore

import com.bolivianusd.app.shared.data.exception.FirestoreDataException
import com.bolivianusd.app.shared.data.mapper.toPrice
import com.bolivianusd.app.shared.data.mapper.toPriceRange
import com.bolivianusd.app.shared.data.remote.firebase.dto.PriceFirestoreDto
import com.bolivianusd.app.shared.data.remote.firebase.dto.PriceRangeFirestoreDto
import com.bolivianusd.app.shared.domain.model.Price
import com.bolivianusd.app.shared.domain.model.PriceRange
import com.bolivianusd.app.shared.domain.model.TradeType
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PriceUsdFirestoreDataSource @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) {

    fun observePrice(tradeType: TradeType): Flow<Price> = callbackFlow {
        val documentPath = when (tradeType) {
            TradeType.BUY -> DOCUMENT_PRICE_BUY_PATH
            TradeType.SELL -> DOCUMENT_PRICE_SELL_PATH
        }
        var listenerRegistration: ListenerRegistration? = null
        try {
            listenerRegistration = firebaseFirestore.collection(COLLECTION_PRICE_USD_PATH)
                .document(documentPath)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                         val dto = snapshot.toObject(PriceFirestoreDto::class.java)
                         dto?.let {
                             trySend(it.toPrice())
                         } ?: run {
                             close(FirestoreDataException.NullOrInvalidData())
                         }
                    } else {
                        close(FirestoreDataException.DocumentNotFound(documentPath))
                    }
                }
        } catch (e: Exception) {
            close(e)
        }
        awaitClose {
            listenerRegistration?.remove()
        }
    }

    fun observePriceRange(tradeType: TradeType): Flow<PriceRange> = callbackFlow {
        val documentPath = when (tradeType) {
            TradeType.BUY -> DOCUMENT_PRICE_RANGE_BUY_PATH
            TradeType.SELL -> DOCUMENT_PRICE_RANGE_SELL_PATH
        }
        var listenerRegistration: ListenerRegistration? = null
        try {
            listenerRegistration = firebaseFirestore.collection(COLLECTION_PRICE_RANGE_USD_PATH)
                .document(documentPath)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        val dto = snapshot.toObject(PriceRangeFirestoreDto::class.java)
                        dto?.let {
                            trySend(it.toPriceRange())
                        } ?: run {
                            close(FirestoreDataException.NullOrInvalidData())
                        }
                    } else {
                        close(FirestoreDataException.DocumentNotFound(documentPath))
                    }
                }
        } catch (e: Exception) {
            close(e)
        }
        awaitClose {
            listenerRegistration?.remove()
        }
    }

    fun getPriceRange(tradeType: TradeType): Flow<PriceRange> = flow {
        val documentPath = when (tradeType) {
            TradeType.BUY -> DOCUMENT_PRICE_RANGE_BUY_PATH
            TradeType.SELL -> DOCUMENT_PRICE_RANGE_SELL_PATH
        }
        try {
            val snapshot = firebaseFirestore.collection(COLLECTION_PRICE_RANGE_USD_PATH)
                .document(documentPath)
                .get()
                .await()

            if (snapshot.exists()) {
                val dto = snapshot.toObject(PriceRangeFirestoreDto::class.java)
                    ?: throw FirestoreDataException.NullOrInvalidData()
                emit(dto.toPriceRange())
            } else {
                throw FirestoreDataException.DocumentNotFound(documentPath)
            }
        } catch (e: Exception) {
            throw FirestoreDataException.DocumentNotFound(documentPath)
        }
    }

    companion object {
        private const val COLLECTION_PRICE_USD_PATH = "price_usd"
        private const val DOCUMENT_PRICE_BUY_PATH = "buy"
        private const val DOCUMENT_PRICE_SELL_PATH = "sell"
        private const val COLLECTION_PRICE_RANGE_USD_PATH = "price_range_usd"
        private const val DOCUMENT_PRICE_RANGE_BUY_PATH = "buy"
        private const val DOCUMENT_PRICE_RANGE_SELL_PATH = "sell"
    }

}