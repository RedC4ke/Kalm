package com.redc4ke.kalm.ui

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
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
import com.redc4ke.kalm.ui.base.GameFragment
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size

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
                winner()
            }
        }
    }

    private fun winner() {
        binding.drawgameConfettiKV.build()
            .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
            .setDirection(0.0, 359.0)
            .setSpeed(1f, 5f)
            .setFadeOutEnabled(true)
            .setTimeToLive(2000L)
            .addShapes(Shape.Square, Shape.Circle)
            .addSizes(Size(12))
            .setPosition(
                -50f,
                binding.drawgameConfettiKV.width + 50f,
                -50f,
                -50f
            )
            .streamFor(300, 5000L)

        CompletedFragment(getString(R.string.drawgame_completed), this)
            .show(parentFragmentManager, "completed")
    }

}