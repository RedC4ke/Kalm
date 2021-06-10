package com.redc4ke.kalm.ui

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Color
import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.navigation.fragment.findNavController
import com.redc4ke.kalm.R
import com.redc4ke.kalm.databinding.FragmentBirdGameBinding
import com.redc4ke.kalm.ui.base.GameFragment
import nl.dionsegijn.konfetti.KonfettiView
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size

class BirdGameFragment : GameFragment<FragmentBirdGameBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentBirdGameBinding
        get() = FragmentBirdGameBinding::inflate
    private var draggedView: View? = null
    private var draggedColor: String? = null
    private var score = 0
    override val directions = arrayOf(
        R.id.action_BirdGame_DrawGame,
        R.id.action_BirdGame_FindGame,
        R.id.action_BirdGame_WordGame
    )

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Show the title banner
        showTitle()

        reload(binding.reloadbtCV)

        val birbs = arrayListOf(
            Bird(binding.birdgameBirdRedIV, "Red"),
            Bird(binding.birdgameBirdBlueIV, "Blue"),
            Bird(binding.birdgameBirdYellowIV, "Yellow")
        )
        val nests = arrayListOf(
            Nest(binding.birdgameNestRedIV, "Red"),
            Nest(binding.birdgameNestBlueIV, "Blue"),
            Nest(binding.birdgameNestYellowIV, "Yellow")
        )
        val fakenests = arrayListOf(
            binding.birdgameFakeNestRedIV,
            binding.birdgameFakeNestBlueIV,
            binding.birdgameFakeNestYellowIV
        )

        birbs.forEach {
            it.view.setOnTouchListener { v, _ ->

                val clipItem = ClipData.Item(it.color)
                val clipData = ClipData(
                    "Bird",
                    arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                    clipItem
                )

                val dragShadow = View.DragShadowBuilder(v)

                v.startDragAndDrop(
                    clipData,
                    dragShadow,
                    null,
                    0
                )

                draggedView = v
                draggedColor = it.color
                v.visibility = View.INVISIBLE

                true
            }
        }

        nests.forEach {
            it.view.setOnDragListener { _, event ->

                when (event.action) {

                    DragEvent.ACTION_DRAG_STARTED -> {
                        if (event.clipDescription.label == "Bird") {

                            fakenests.forEach { fakenest ->
                                fakenest.visibility = View.INVISIBLE
                            }

                            true
                        } else {
                            false
                        }
                    }

                    DragEvent.ACTION_DROP -> {
                        val color = event.clipData.getItemAt(0).text
                        if (color == it.color) {
                            score += 1

                            if (score == 3) {
                                winner(getString(R.string.birdgame_completed),
                                    binding.birdGameConfetti)
                            }

                            true
                        } else {
                            false
                        }

                    }

                    DragEvent.ACTION_DRAG_ENDED -> {
                        fakenests.forEach { fakenest ->
                            fakenest.visibility = View.VISIBLE
                        }
                        if (!event.result) {
                            draggedView?.visibility = View.VISIBLE
                        } else {
                            when (draggedColor) {
                                "Red" -> binding.birdgameFakeBirdRedIV.visibility =
                                    View.VISIBLE
                                "Blue" -> binding.birdgameFakeBirdBlueIV.visibility =
                                    View.VISIBLE
                                "Yellow" -> binding.birdgameFakeBirdYellowIV.visibility =
                                    View.VISIBLE
                            }
                        }

                        draggedColor = null
                        draggedView = null

                        false
                    }
                    else -> false
                }
            }
        }
    }

    private fun showTitle() {
        ObjectAnimator.ofFloat(binding.birdgameTitleCV, "translationY", 70f)
            .apply {
                interpolator = DecelerateInterpolator()
                duration = 1100
                start()
            }
    }

    private data class Bird(val view: View, val color: String)
    private data class Nest(val view: View, val color: String)

}