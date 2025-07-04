package com.bolivianusd.app.ui.splash

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bolivianusd.app.databinding.ActivitySplashBinding
import com.bolivianusd.app.ui.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.util.Pair
import android.view.animation.AnimationUtils
import com.bolivianusd.app.R
import kotlinx.coroutines.Job

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }
    private var transitionJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) = with(binding)  {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        startTransition()
    }

    private fun startTransition() = with(binding) {
        splashLayout.visibility = View.VISIBLE
        splashLayout.startAnimation(
            AnimationUtils.loadAnimation(
                applicationContext,
                R.anim.anim_splash_fade_in
            )
        )
        transitionJob = CoroutineScope(Dispatchers.Main).launch {
            delay(SPLASH_DELAY)
            val pair: Array<Pair<View, String>> = arrayOf(
                Pair(lottieAnimationView, getString(R.string.app_splash_main_transition_name))
            )
            val intent = Intent(applicationContext, MainActivity::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(this@SplashActivity, *pair)
            startActivity(intent, options.toBundle())
        }
    }

    override fun onPause() {
        super.onPause()
        CoroutineScope(Dispatchers.Main).launch {
            delay(CLOSE_DELAY)
            finish()
        }
    }

    override fun onStop() {
        super.onStop()
        transitionJob?.cancel()
    }

    override fun onRestart() {
        super.onRestart()
        finish()
    }

    companion object {
        const val SPLASH_DELAY: Long = 2000
        const val CLOSE_DELAY: Long = 1500
    }
}