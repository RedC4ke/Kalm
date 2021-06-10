package com.redc4ke.kalm.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.redc4ke.kalm.R
import com.redc4ke.kalm.databinding.FragmentLandingBinding
import com.redc4ke.kalm.ui.base.BaseFragment
import kotlin.math.hypot

class LandingFragment : BaseFragment<FragmentLandingBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLandingBinding
        get() = FragmentLandingBinding::inflate
    private val destinationList = arrayListOf(
        R.id.action_WordGame
    )

    override fun onStart() {
        super.onStart()

        binding.landingPlayIV.setOnClickListener {
            buttonHide()
            findNavController().navigate(destinationList.random())
        }
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