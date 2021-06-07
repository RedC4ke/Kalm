package com.redc4ke.kalm.ui

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import com.redc4ke.kalm.R
import com.redc4ke.kalm.databinding.FragmentFindGameBinding
import com.redc4ke.kalm.ui.base.GameFragment

class FindGameFragment : GameFragment<FragmentFindGameBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFindGameBinding
        get() = FragmentFindGameBinding::inflate
    override val directions: Array<Int>
        get() = arrayOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.findgameTitleTV.text = getString(R.string.findgame_title)

        showTitle()
    }

    private fun showTitle() {
        ObjectAnimator.ofFloat(binding.findgameTitleCV, "translationY", 70f)
            .apply {
                interpolator = DecelerateInterpolator()
                duration = 1100
                start()
            }
        ObjectAnimator.ofFloat(binding.findgameTitleCV, "translationY", -340f)
            .apply {
                duration = 500
                startDelay = 5000
                start()
            }
    }

}