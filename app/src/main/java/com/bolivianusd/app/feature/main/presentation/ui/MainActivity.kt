package com.bolivianusd.app.feature.main.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bolivianusd.app.R
import com.bolivianusd.app.core.extensions.exit
import com.bolivianusd.app.core.extensions.onBackPressed
import com.bolivianusd.app.databinding.ActivityMainBinding
import com.bolivianusd.app.feature.calculator.presentation.ui.CalculatorFragment
import com.bolivianusd.app.feature.price.presentation.ui.PriceFragment
import dagger.hilt.android.AndroidEntryPoint
import com.bolivianusd.app.core.extensions.captureAsBitmap
import com.bolivianusd.app.core.util.MIME_TYPE_IMAGE_PNG

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val priceFragment = PriceFragment.newInstance()

    private val calculatorFragment = CalculatorFragment.newInstance()

    private var activeFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        onBackPressed { exit() }
        setupToolbar()
        setupBottomNavigationView()
    }

    private fun setupToolbar() = with(binding) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setupBottomNavigationView() = with(binding) {
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.actionPrice -> showFragment(priceFragment, PriceFragment.TAG)
                R.id.actionCalculator -> showFragment(calculatorFragment, CalculatorFragment.TAG)
            }
            true
        }
        bottomNavigationView.selectedItemId = R.id.actionPrice
    }


    private fun showFragment(fragment: Fragment, tag: String) {
        val current = activeFragment
        val transaction = supportFragmentManager.beginTransaction()
        if (current != null) transaction.hide(current)
        if (!fragment.isAdded) {
            transaction.add(R.id.frameLayout, fragment, tag)
        } else {
            transaction.show(fragment)
        }
        transaction.commit()
        activeFragment = fragment
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.actionShare -> {
                shared()
                true
            }
            R.id.actionOff -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun shared() = with(binding) {
        frameLayout.captureAsBitmap(
            context = applicationContext
        )?.let { uri ->
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = MIME_TYPE_IMAGE_PNG
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(Intent.createChooser(intent, getString(R.string.shared)))
        }
    }

}
