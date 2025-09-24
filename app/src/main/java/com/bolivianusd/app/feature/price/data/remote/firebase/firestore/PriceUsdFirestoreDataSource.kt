package com.bolivianusd.app.feature.price.data.remote.firebase.firestore

import com.bolivianusd.app.shared.data.exception.FirestoreDataException
import com.bolivianusd.app.feature.price.data.mappers.toPrice
import com.bolivianusd.app.feature.price.data.remote.firebase.dto.PriceFirestoreDto
import com.bolivianusd.app.feature.price.domain.model.Price
import com.bolivianusd.app.shared.domain.model.TradeType
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class PriceUsdFirestoreDataSource @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) {

    fun observePrice(tradeType: TradeType): Flow<Price> = callbackFlow {
        val documentPath = when (tradeType) {
            TradeType.BUY -> DOCUMENT_PATH_BUY
            TradeType.SELL -> DOCUMENT_PATH_SELL
        }
        var listenerRegistration: ListenerRegistration? = null
        try {
            listenerRegistration = firebaseFirestore.collection(COLLECTION_PATH_PRICE_USD)
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

    companion object {
        private const val COLLECTION_PATH_PRICE_USD = "price_usd"
        private const val DOCUMENT_PATH_BUY = "buy"
        private const val DOCUMENT_PATH_SELL = "sell"
    }

}