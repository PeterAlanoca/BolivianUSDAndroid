package com.bolivianusd.app.shared.data.remote.firebase.config

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import javax.inject.Inject

class PriceConfigDataSource @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig
) {
    fun isEnabledSwitchDollar() = remoteConfig.getBoolean(KEY_ENABLED_SWITCH_DOLLAR)

    fun getDelayPolling() = remoteConfig.getLong(KEY_DELAY_POLLING)

    companion object {
        private const val KEY_ENABLED_SWITCH_DOLLAR = "enabled_switch_dollar"
        private const val KEY_DELAY_POLLING = "delay_polling"
    }
}