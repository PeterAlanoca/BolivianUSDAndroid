package com.bolivianusd.app.feature.price.data.repository.datasource

import android.content.Context
import com.bolivianusd.app.feature.price.data.repository.PriceRepository
import com.bolivianusd.app.feature.price.domain.model.ChartData
import com.bolivianusd.app.feature.price.domain.model.Price
import com.bolivianusd.app.feature.price.domain.model.RangePrice
import com.bolivianusd.app.feature.price.data.repository.mapper.toChartData
import com.bolivianusd.app.feature.price.data.repository.mapper.toPrice
import com.bolivianusd.app.feature.price.data.repository.mapper.toRangePrice
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class PriceRepositoryImpl @Inject constructor(
    private val context: Context,
    private val priceDataSource: PriceDataSource
) : PriceRepository {

    override fun getPriceBuy(): Flow<Price> = callbackFlow {
        priceDataSource.getPriceBuy(
            onSuccess = { priceModel ->
                val price = priceModel.toPrice()
                trySend(price).isSuccess
            },
            onError = { error ->
                close(error)
            }
        )
        awaitClose { }
    }

    override fun getPriceSell(): Flow<Price> = callbackFlow {
        priceDataSource.getPriceSell(
            onSuccess = { priceModel ->
                val price = priceModel.toPrice()
                trySend(price).isSuccess
            },
            onError = { error ->
                close(error)
            }
        )
        awaitClose { }
    }

    override fun getChartPriceBuy(): Flow<ChartData> = callbackFlow {
        priceDataSource.getChartPriceBuy(
            onSuccess = { chartDataModel ->
                val chartData = chartDataModel.toChartData(context)
                trySend(chartData).isSuccess
            },
            onError = { error ->
                close(error)
            }
        )
        awaitClose { }
    }

    override fun getChartPriceSell(): Flow<ChartData> = callbackFlow {
        priceDataSource.getChartPriceSell(
            onSuccess = { chartDataModel ->
                val chartData = chartDataModel.toChartData(context)
                trySend(chartData).isSuccess
            },
            onError = { error ->
                close(error)
            }
        )
        awaitClose { }
    }

    override fun getRangePriceBuy(): Flow<RangePrice> = callbackFlow {
        priceDataSource.getRangePriceBuy(
            onSuccess = { rangePriceModel ->
                val rangePrice = rangePriceModel.toRangePrice()
                trySend(rangePrice).isSuccess
            },
            onError = { error ->
                close(error)
            }
        )
        awaitClose { }
    }

    override fun getRangePriceSell(): Flow<RangePrice> = callbackFlow {
        priceDataSource.getRangePriceSell(
            onSuccess = { rangePriceModel ->
                val rangePrice = rangePriceModel.toRangePrice()
                trySend(rangePrice).isSuccess
            },
            onError = { error ->
                close(error)
            }
        )
        awaitClose { }
    }

}