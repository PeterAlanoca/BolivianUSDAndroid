package com.bolivianusd.app.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bolivianusd.app.R
import com.bolivianusd.app.core.exit
import com.bolivianusd.app.core.onBackPressed
import com.bolivianusd.app.core.pushFragment
import com.bolivianusd.app.databinding.ActivityMainBinding
import com.bolivianusd.app.ui.calculator.CalculatorFragment
import com.bolivianusd.app.ui.news.NewsFragment
import com.bolivianusd.app.ui.price.PriceFragment

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        onBackPressed { exit() }
        setupToolbar()
        setupBottomNavigationView()
    }

    private fun setupToolbar() = with(binding) {
        setSupportActionBar(toolbar)
    }

    private fun setupBottomNavigationView() = with(binding) {
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.actionPrice -> goToPrice()
                R.id.actionCalculator -> goToCalculator()
                R.id.actionNews -> goToNews()
            }
            true
        }
        bottomNavigationView.selectedItemId = R.id.actionPrice
    }

    private fun goToPrice() {
        val fragment = PriceFragment.newInstance()
        pushFragment(fragment, R.id.frameLayout, PriceFragment.TAG)
    }

    private fun goToCalculator() {
        val fragment = CalculatorFragment.newInstance()
        pushFragment(fragment, R.id.frameLayout, CalculatorFragment.TAG)
    }

    private fun goToNews() {
        val fragment = NewsFragment.newInstance()
        pushFragment(fragment, R.id.frameLayout, NewsFragment.TAG)
    }

}