package com.bolivianusd.app.feature.main.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bolivianusd.app.R
import com.bolivianusd.app.core.extensions.exit
import com.bolivianusd.app.core.extensions.onBackPressed
import com.bolivianusd.app.core.extensions.pushFragment
import com.bolivianusd.app.databinding.ActivityMainBinding
import com.bolivianusd.app.feature.calculator.presentation.ui.CalculatorFragment
import com.bolivianusd.app.feature.news.presentation.ui.NewsFragment
import com.bolivianusd.app.feature.price.presentation.ui.PriceFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        bottomNavigationView.isVisible = false
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
        pushFragment(
            fragment = PriceFragment.newInstance(),
            containerViewId = R.id.frameLayout,
            tag = PriceFragment.TAG
        )
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