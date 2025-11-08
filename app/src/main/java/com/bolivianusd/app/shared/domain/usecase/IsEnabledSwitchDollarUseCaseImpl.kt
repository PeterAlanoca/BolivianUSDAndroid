package com.bolivianusd.app.shared.domain.usecase

import com.bolivianusd.app.shared.domain.repository.PriceRepository
import javax.inject.Inject

class IsEnabledSwitchDollarUseCaseImpl @Inject constructor(
    private val priceRepository: PriceRepository
) : IsEnabledSwitchDollarUseCase {

    override fun invoke() = priceRepository.isEnabledSwitchDollar()
}
