package com.example.storibrianvianachallenge.common.ui.dialog

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.storibrianvianachallenge.R
import com.example.storibrianvianachallenge.databinding.FragmentOnSuccessBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnSuccessDialog : DialogFragment() {
    private var _binding: FragmentOnSuccessBinding? = null
    private val binding get() = _binding!!
    private val args: OnSuccessDialogArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        initUI()
    }

    private fun initUI() {
        showLottieAnimation()
        initNavigation()
    }

    private fun showLottieAnimation() {
        initLottieTransition()
        binding.animationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {}

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })
    }

    private fun initLottieTransition() {
        val appearAnimation = AlphaAnimation(0.0f, 1.0f)
        appearAnimation.duration = 1000

        appearAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
                binding.apply {
                    animationView.isVisible = true
                    tvSubTitle.text = args.message
                }
            }

            override fun onAnimationEnd(p0: Animation?) {}
            override fun onAnimationRepeat(p0: Animation?) {}
        })
        binding.animationView.startAnimation(appearAnimation)
    }

    private fun initNavigation() {
        binding.tvDismiss.setOnClickListener {
            findNavController().navigate(R.id.action_onSuccessFragment_to_loginFragment)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnSuccessBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}