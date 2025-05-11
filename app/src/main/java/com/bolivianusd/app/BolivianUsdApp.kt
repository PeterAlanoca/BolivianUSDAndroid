package com.bolivianusd.app

import android.app.Application
import com.google.firebase.FirebaseApp

class BolivianUsdApp : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }

}