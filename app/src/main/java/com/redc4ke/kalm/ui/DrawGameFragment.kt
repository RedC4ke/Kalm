package com.redc4ke.kalm.ui

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import com.redc4ke.kalm.R
import com.redc4ke.kalm.databinding.FragmentDrawGameBinding
import com.redc4ke.kalm.ui.base.GameFragment

class DrawGameFragment() : GameFragment<FragmentDrawGameBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDrawGameBinding
        get() = FragmentDrawGameBinding::inflate
    override val directions: Array<Int>
        get() = arrayOf(
            R.id.action_DrawGame_FindGame
        )
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
        buttonsSetup()
    }


    private fun showTitle() {
        ObjectAnimator.ofFloat(binding.drawgameTitleCV, "translationY", 70f)
            .apply {
                interpolator = DecelerateInterpolator()
                duration = 1100
                start()
            }
    }

    private fun buttonsSetup() {
        with(binding) {
            drawgameEraserBT.setOnClickListener {
                drawgameCanvasCV.changePaint("eraser")
            }
            drawgameGreenBT.setOnClickListener {
                drawgameCanvasCV.changePaint("green")
            }
            drawgameBlueBT.setOnClickListener {
                drawgameCanvasCV.changePaint("blue")
            }
            drawgameBlackBT.setOnClickListener {
                drawgameCanvasCV.changePaint("black")
            }

            drawgameCompletedBT.setOnClickListener {
                winner(getString(R.string.drawgame_completed), drawgameConfettiKV)
            }
        }
    }
}