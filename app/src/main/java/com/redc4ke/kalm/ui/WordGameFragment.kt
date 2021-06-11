package com.redc4ke.kalm.ui

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.children
import com.redc4ke.kalm.R
import com.redc4ke.kalm.databinding.FragmentWordGameBinding
import com.redc4ke.kalm.ui.base.GameFragment
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size

class WordGameFragment : GameFragment<FragmentWordGameBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentWordGameBinding
        get() = FragmentWordGameBinding::inflate
    override val directions = arrayOf(
        R.id.action_WordGame_FindGame,
        R.id.action_WordGame_DrawGame,
        R.id.action_WordGame_BirdGame
    )
    override val reloadDirection: Int
        get() = R.id.action_wordGameFragment_self
    private var draggedView: View? = null
    private var score = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val word = resources.getStringArray(R.array.wordgame_words).random().toCharArray()

        with(binding) {
            reload(reloadbtCV)

            val allLeaves = arrayOf(leaf1FL, leaf2FL, leaf3FL, leaf4FL, leaf5FL, leaf6FL, leaf7FL)
            val allEmptyLeaves = wordgameEmptyLeavesLL.children.toList()
            allLeaves.shuffle()
            val currentLeaves = allLeaves.take(word.size)
            val currentEmptyLeaves = allEmptyLeaves.take(word.size)
            var currentEmptyLeaf: View? = null

            for (i in (word.indices)) {
                val leaf = currentLeaves[i]
                val emptyLeaf = currentEmptyLeaves[i]
                leaf.visibility = View.VISIBLE
                emptyLeaf.visibility = View.VISIBLE
                val character = word[i].toString()
                (leaf.getChildAt(1) as TextView).text = character

                leaf.setOnTouchListener { v, _ ->
                    val clipItem = ClipData.Item(character)
                    val clipData = ClipData(
                        "Leaf",
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
                    v.visibility = View.INVISIBLE

                    true
                }

                emptyLeaf.setOnDragListener() { v, event ->
                    when (event.action) {
                        DragEvent.ACTION_DRAG_STARTED -> {
                            true
                        }
                        DragEvent.ACTION_DROP -> {
                            val item = event.clipData.getItemAt(0).text
                            if (item == character) {
                                draggedView?.setOnTouchListener { _, _ ->  true}
                                score += 1
                                currentEmptyLeaf = v
                                if (score == word.size) winner(
                                    getString(R.string.wordgame_completed),
                                    wordgameConfettiKV
                                )

                                true
                            } else {
                                false
                            }
                        }
                        DragEvent.ACTION_DRAG_ENDED -> {
                            if (!event.result) {
                                draggedView?.visibility = View.VISIBLE
                            } else {
                                val location = IntArray(2)
                                wordgameEmptyLeavesLL.getLocationInWindow(location)
                                draggedView?.x =
                                    location[0].toFloat() + (currentEmptyLeaf?.x ?: 0f)
                                draggedView?.y =
                                    location[1].toFloat() + (currentEmptyLeaf?.y ?: 0f)
                                draggedView?.bringToFront()

                                currentEmptyLeaf?.visibility = View.INVISIBLE
                                draggedView?.visibility = View.VISIBLE
                            }

                            draggedView = null

                            false
                        }
                        else -> false
                    }
                }
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }
}