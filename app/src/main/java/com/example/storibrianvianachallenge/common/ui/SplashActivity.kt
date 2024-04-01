package com.example.storibrianvianachallenge.common.ui

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.storibrianvianachallenge.databinding.ActivitySplashBinding
import com.example.storibrianvianachallenge.main.ui.MainActivity


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        initLottieAnimation()
    }

    private fun initLottieAnimation() {
        initLottieTransition()
        binding.animationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                initMainActivity()
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })
    }

    private fun initLottieTransition() {
        val appearAnimation = AlphaAnimation(0.0f, 1.0f)
        appearAnimation.duration = 1000

        appearAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
                binding.animationView.isVisible = true
            }

            override fun onAnimationEnd(p0: Animation?) {}
            override fun onAnimationRepeat(p0: Animation?) {}
        })
        binding.animationView.startAnimation(appearAnimation)
    }

    private fun initMainActivity() {
        val intent = Intent(this, MainActivity::class.java).apply {  }
        startActivity(intent)
        finish()
    }
}