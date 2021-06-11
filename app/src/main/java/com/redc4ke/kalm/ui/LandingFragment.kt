package com.redc4ke.kalm.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.redc4ke.kalm.R
import com.redc4ke.kalm.databinding.FragmentLandingBinding
import com.redc4ke.kalm.ui.base.BaseFragment
import kotlin.math.hypot

class LandingFragment : BaseFragment<FragmentLandingBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLandingBinding
        get() = FragmentLandingBinding::inflate
    private val destinationList = arrayListOf(
        R.id.action_WordGame,
        R.id.action_DrawGame,
        R.id.action_BirdGame,
        R.id.action_FindGame
    )
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity() as MainActivity)[MainViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()

        binding.landingPlayIV.setOnClickListener {
            buttonHide()
            findNavController().navigate(destinationList.random())
        }

        binding.landingInfoBT.setOnClickListener {
            findNavController().navigate(R.id.informationFragment)
        }

        viewModel.isMusicEnabled().observe(viewLifecycleOwner, {
            if (it) binding.landingMusicBT.setImageResource(R.drawable.ic_baseline_music_note_24)
            else binding.landingMusicBT.setImageResource(R.drawable.ic_baseline_music_off_24)
        })
        binding.landingMusicBT.setOnClickListener {
            viewModel.change(requireActivity() as MainActivity)
        }

        ObjectAnimator.ofFloat(binding.landPanik, "translationX", 1500f).apply {
            duration = 800
            startDelay = 700
        }.start()
        ObjectAnimator.ofFloat(binding.landKalm, "translationX", 0f).apply {
            duration = 1000
            startDelay = 200
            interpolator = AccelerateInterpolator()
        }.start()

    }

    private fun buttonHide() {
        val button = binding.landingPlayIV

        val cx = button.width / 2
        val cy = button.height / 2

        val initialRadius = hypot(cx.toDouble(), cy.toDouble()).toFloat()
        val anim = ViewAnimationUtils.createCircularReveal(button, cx, cy, initialRadius, 0f)

        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                button.visibility = View.INVISIBLE
            }
        })

        anim.start()
    }

}