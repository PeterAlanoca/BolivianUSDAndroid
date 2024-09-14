package com.bolivianusd.app.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bolivianusd.app.R
import com.bolivianusd.app.core.exit
import com.bolivianusd.app.core.onBackPressed
import com.bolivianusd.app.core.replaceFragment
import com.bolivianusd.app.databinding.ActivityMainBinding
import com.bolivianusd.app.ui.price.PriceFragment


class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        onBackPressed { exit() }


        val fragment = PriceFragment.newInstance()
        replaceFragment(fragment, R.id.frameLayout, PriceFragment.TAG)

    }

}