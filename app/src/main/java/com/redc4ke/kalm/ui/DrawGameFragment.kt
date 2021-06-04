package com.redc4ke.kalm.ui

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import com.redc4ke.kalm.R
import com.redc4ke.kalm.databinding.FragmentDrawGameBinding
import com.redc4ke.kalm.ui.base.BaseFragment

class DrawGameFragment : BaseFragment<FragmentDrawGameBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDrawGameBinding
        get() = FragmentDrawGameBinding::inflate
    private lateinit var titles: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        titles = resources.getStringArray(R.array.drawgame_title)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.drawgameTitleTV.text = titles.random()
        showTitle()

    }


    private fun showTitle() {
        ObjectAnimator.ofFloat(binding.drawgameTitleCV, "translationY", 70f)
            .apply {
                interpolator = DecelerateInterpolator()
                duration = 1100
                start()
            }
    }

}