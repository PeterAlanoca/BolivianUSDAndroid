package com.bolivianusd.app.shared.data.remote.firebase.firestore

import com.bolivianusd.app.core.extensions.get
import com.bolivianusd.app.core.extensions.observeDocument
import com.bolivianusd.app.core.managers.NetworkManager
import com.bolivianusd.app.shared.data.exception.FirestoreDataException
import com.bolivianusd.app.shared.data.mapper.toPrice
import com.bolivianusd.app.shared.data.mapper.toPriceRange
import com.bolivianusd.app.shared.data.remote.firebase.dto.PriceFirestoreDto
import com.bolivianusd.app.shared.data.remote.firebase.dto.PriceRangeFirestoreDto
import com.bolivianusd.app.shared.domain.model.Price
import com.bolivianusd.app.shared.domain.model.PriceRange
import com.bolivianusd.app.shared.domain.model.TradeType
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PriceUsdFirestoreDataSource @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val networkManager: NetworkManager

) {
    fun observePrice(tradeType: TradeType): Flow<Price> = flow {
        if (!networkManager.hasInternetAccess()) {
            throw FirestoreDataException.NoConnection()
        }
        emitAll(
            flow = firebaseFirestore.observeDocument<PriceFirestoreDto, Price>(
                collectionPath = COLLECTION_PRICE_USD_PATH,
                documentPath = when (tradeType) {
                    TradeType.BUY -> DOCUMENT_PRICE_BUY_PATH
                    TradeType.SELL -> DOCUMENT_PRICE_SELL_PATH
                }
            ) { dto -> dto.toPrice() }
        )
    }

    fun observePriceRange(tradeType: TradeType): Flow<PriceRange> = flow {
        if (!networkManager.hasInternetAccess()) {
            throw FirestoreDataException.NoConnection()
        }
        emitAll(
            flow = firebaseFirestore.observeDocument<PriceRangeFirestoreDto, PriceRange>(
                collectionPath = COLLECTION_PRICE_RANGE_USD_PATH,
                documentPath = when (tradeType) {
                    TradeType.BUY -> DOCUMENT_PRICE_RANGE_BUY_PATH
                    TradeType.SELL -> DOCUMENT_PRICE_RANGE_SELL_PATH
                }
            ) { dto -> dto.toPriceRange() }
        )
    }

    fun getPrice(tradeType: TradeType): Flow<Price> = flow {
        if (!networkManager.hasInternetAccess()) {
            throw FirestoreDataException.NoConnection()
        }
        emitAll(
            flow = firebaseFirestore.get<PriceFirestoreDto, Price>(
                collectionPath = COLLECTION_PRICE_USD_PATH,
                documentPath = when (tradeType) {
                    TradeType.BUY -> DOCUMENT_PRICE_BUY_PATH
                    TradeType.SELL -> DOCUMENT_PRICE_SELL_PATH
                }
            ) { dto -> dto.toPrice() }
        )
    }

    fun getPriceRange(tradeType: TradeType): Flow<PriceRange> = flow {
        if (!networkManager.hasInternetAccess()) {
            throw FirestoreDataException.NoConnection()
        }
        emitAll(
            flow = firebaseFirestore.get<PriceRangeFirestoreDto, PriceRange>(
                collectionPath = COLLECTION_PRICE_RANGE_USD_PATH,
                documentPath = when (tradeType) {
                    TradeType.BUY -> DOCUMENT_PRICE_RANGE_BUY_PATH
                    TradeType.SELL -> DOCUMENT_PRICE_RANGE_SELL_PATH
                }
            ) { dto -> dto.toPriceRange() }
        )
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