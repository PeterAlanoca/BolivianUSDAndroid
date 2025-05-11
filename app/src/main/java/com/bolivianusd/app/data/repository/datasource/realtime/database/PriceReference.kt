package com.bolivianusd.app.data.repository.datasource.realtime.database

import com.bolivianusd.app.data.model.PriceBuyModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class PriceReference @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) {

    fun getPriceBuy(onSuccess: (PriceBuyModel) -> Unit, onError: (Exception) -> Unit) {
        val priceRef = firebaseDatabase.getReference("price_buy_usdt")
        //priceRef.keepSynced(true)
        priceRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(PriceBuyModel::class.java)?.let {
                    onSuccess(it)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error.toException())
            }
        })
    }

}