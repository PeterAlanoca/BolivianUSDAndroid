package com.bolivianusd.app.data.repository.datasource.realtime.database

import com.bolivianusd.app.data.model.PriceModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class PriceReference @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) {

    fun getPriceBuy(onSuccess: (PriceModel) -> Unit, onError: (Exception) -> Unit) {
        val reference = firebaseDatabase.getReference(KEY_DATA_BUY_USDT)
        //reference.keepSynced(true)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(PriceModel::class.java)?.let {
                    onSuccess(it)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error.toException())
            }
        })
    }

    fun getPriceSell(onSuccess: (PriceModel) -> Unit, onError: (Exception) -> Unit) {
        val reference = firebaseDatabase.getReference(KEY_DATA_SELL_USDT)
        //reference.keepSynced(true)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(PriceModel::class.java)?.let {
                    onSuccess(it)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error.toException())
            }
        })
    }

    companion object {
        private const val KEY_DATA_BUY_USDT = "price_buy_usdt"
        private const val KEY_DATA_SELL_USDT = "price_sell_usdt"
    }

}