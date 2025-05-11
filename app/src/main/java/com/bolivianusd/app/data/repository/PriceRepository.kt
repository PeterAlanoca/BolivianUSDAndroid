package com.bolivianusd.app.data.repository

import com.bolivianusd.app.data.model.PriceBuyModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class PriceRepository {

    private val database = FirebaseDatabase.getInstance()
    private val priceRef = database.getReference("price_buy_usdt")

    fun observePriceData(onSuccess: (PriceBuyModel) -> Unit, onError: (Exception) -> Unit) {
        priceRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue()
                if (value != null) {
                    onSuccess(PriceBuyModel("ssssss"))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error.toException())
            }
        })
    }

    fun observePriceFlow(): Flow<PriceBuyModel> = callbackFlow {
        val priceRef = database.getReference("price_buy_usdt")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue()
                if (value != null) {
                    trySend(PriceBuyModel("asasas"))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException()) // También puedes emitir un error personalizado
            }
        }

        priceRef.addValueEventListener(listener)

        awaitClose {
            priceRef.removeEventListener(listener)
        }
    }
}
