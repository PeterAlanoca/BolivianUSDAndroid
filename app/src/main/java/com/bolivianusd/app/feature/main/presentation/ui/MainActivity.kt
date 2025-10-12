package com.bolivianusd.app.feature.main.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
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
        setupFragments()
        setupBottomNavigationView()
    }

    private fun setupToolbar() = with(binding) {
        setSupportActionBar(toolbar)
    }

    private fun setupBottomNavigationView() = with(binding) {
        //bottomNavigationView.isVisible = false
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.actionPrice -> showFragment(priceFragment)
                R.id.actionCalculator -> showFragment(calculatorFragment)
                //R.id.actionNews -> goToNews()
            }
            true
        }
        bottomNavigationView.selectedItemId = R.id.actionPrice
    }


    private fun showFragment(fragment: Fragment) {
        // Si ya está visible, no hacemos nada
        if (fragment == activeFragment) return

        supportFragmentManager.beginTransaction()
            .hide(activeFragment!!) // ocultamos el anterior
            .show(fragment)          // mostramos el nuevo
            .commitAllowingStateLoss()

        activeFragment = fragment // actualizamos el fragment activo
    }

    val priceFragment = PriceFragment.newInstance()

    private fun goToPrice() {
        pushFragment(
            fragment = priceFragment,
            containerViewId = R.id.frameLayout,
            tag = PriceFragment.TAG
        )
    }

    val calculatorFragment = CalculatorFragment.newInstance()

    private var activeFragment: Fragment? = null


    private fun setupFragments() {
        supportFragmentManager.beginTransaction()
            .add(R.id.frameLayout, calculatorFragment, CalculatorFragment.TAG)
            .hide(calculatorFragment)
            .add(R.id.frameLayout, priceFragment, PriceFragment.TAG)
            .commit()

        activeFragment = priceFragment
    }


    private fun goToCalculator() {
        pushFragment(
            fragment = calculatorFragment,
            containerViewId = R.id.frameLayout,
            tag = CalculatorFragment.TAG
        )
    }

    private fun goToNews() {
        val fragment = NewsFragment.newInstance()
        pushFragment(fragment, R.id.frameLayout, NewsFragment.TAG)
    }

}