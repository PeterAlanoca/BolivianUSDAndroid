package com.bolivianusd.app.feature.price.data.remote.firebase.firestore

import com.bolivianusd.app.feature.price.data.mappers.toPrice
import com.bolivianusd.app.feature.price.data.remote.firebase.dto.PriceFirestoreDto
import com.bolivianusd.app.feature.price.domain.model.Price
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class PriceUsdFirestoreDataSource @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) {

    fun observePriceBuy(): Flow<Price> = callbackFlow {
        var listenerRegistration: ListenerRegistration? = null
        try {
            listenerRegistration = firebaseFirestore.collection("price_usd")
                .document("buy")
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
                             close(IllegalStateException("Firestore data is null or invalid"))
                         }
                    } else {
                        close(IllegalStateException("Document does not exist: "))
                    }
                }
        } catch (e: Exception) {
            close(e)
        }
        awaitClose {
            listenerRegistration?.remove()
        }
    }

}