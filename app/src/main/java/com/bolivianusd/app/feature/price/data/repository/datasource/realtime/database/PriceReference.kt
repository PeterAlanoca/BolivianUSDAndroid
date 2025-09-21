package com.bolivianusd.app.feature.price.data.repository.datasource.realtime.database

import com.bolivianusd.app.feature.price.data.model.ChartDataModel
import com.bolivianusd.app.feature.price.data.model.PriceModel
import com.bolivianusd.app.feature.price.data.model.RangePriceModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class PriceReference @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) {

    fun getPriceBuy(onSuccess: (PriceModel) -> Unit, onError: (Exception) -> Unit) {
        val reference = firebaseDatabase.getReference(KEY_PRICE_BUY_USDT)
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
        val reference = firebaseDatabase.getReference(KEY_PRICE_SELL_USDT)
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

    fun getChartPriceBuy(onSuccess: (ChartDataModel) -> Unit, onError: (Exception) -> Unit) {
        val reference = firebaseDatabase.getReference(KEY_CHART_PRICE_BUY_USDT)
        //reference.keepSynced(true)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(ChartDataModel::class.java)?.let {
                    onSuccess(it)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error.toException())
            }
        })
    }

    fun getChartPriceSell(onSuccess: (ChartDataModel) -> Unit, onError: (Exception) -> Unit) {
        val reference = firebaseDatabase.getReference(KEY_CHART_PRICE_SELL_USDT)
        //reference.keepSynced(true)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(ChartDataModel::class.java)?.let {
                    onSuccess(it)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error.toException())
            }
        })
    }

    fun getRangePriceBuy(onSuccess: (RangePriceModel) -> Unit, onError: (Exception) -> Unit) {
        val reference = firebaseDatabase.getReference(KEY_RANGE_PRICE_BUY_USDT)
        //reference.keepSynced(true)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(RangePriceModel::class.java)?.let {
                    onSuccess(it)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error.toException())
            }
        })
    }

    fun getRangePriceSell(onSuccess: (RangePriceModel) -> Unit, onError: (Exception) -> Unit) {
        val reference = firebaseDatabase.getReference(KEY_RANGE_PRICE_SELL_USDT)
        //reference.keepSynced(true)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(RangePriceModel::class.java)?.let {
                    onSuccess(it)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error.toException())
            }
        })
    }

    companion object {
        private const val KEY_PRICE_BUY_USDT = "price_buy_usdt"
        private const val KEY_PRICE_SELL_USDT = "price_sell_usdt"
        private const val KEY_CHART_PRICE_BUY_USDT = "chart_price_buy_usdt"
        private const val KEY_CHART_PRICE_SELL_USDT = "chart_price_sell_usdt"
        private const val KEY_RANGE_PRICE_BUY_USDT = "range_price_buy_usdt"
        private const val KEY_RANGE_PRICE_SELL_USDT = "range_price_sell_usdt"
    }

}