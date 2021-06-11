package com.redc4ke.kalm.ui

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.doOnEnd
import com.redc4ke.kalm.R
import com.redc4ke.kalm.databinding.FragmentFindGameBinding
import com.redc4ke.kalm.ui.base.GameFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class FindGameFragment : GameFragment<FragmentFindGameBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFindGameBinding
        get() = FragmentFindGameBinding::inflate
    override val directions = arrayOf(
        R.id.action_FindGame_BirdGame,
        R.id.action_FindGame_WordGame,
        R.id.action_FindGame_DrawGame
    )
    override val reloadDirection: Int
        get() = R.id.action_findGameFragment_self
    private var score = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //MainScope().launch(Dispatchers.IO) {
        //    with(binding) {
        //        val viewToResource = mapOf(
        //            //dogs
        //            findgameCorgiIV to R.drawable.mockup_findgamedog1,
        //            findgameHuskyIV to R.drawable.mockup_findgamedog5,
        //            findgameDachshundIV to R.drawable.mockup_findgamedog3,
        //            findgameJrterrierIV to R.drawable.mockup_findgamedog2,
        //            findgamePugIV to R.drawable.mockup_findgamedog4,
        //            findgameTerrierIV to R.drawable.mockup_findgamedog6,
        //            //dog icons
        //            findgameCorgiIconIV to R.drawable.mockup_findgamecorgi,
        //            findgameHuskyIconIV to R.drawable.mockup_findgamehusky,
        //            findgameDachshundIconIV to R.drawable.mockup_findgamedachshund,
        //            findgameJrterrierIconIV to R.drawable.mockup_findgamejrterrier,
        //            findgamePugIconIV to R.drawable.mockup_findgamepug,
        //            findgameTerrierIconIV to R.drawable.mockup_findgameterrier,
        //            //trees
        //            findgameTree1 to R.drawable.mockup_findgametree1,
        //            findgameTree2 to R.drawable.mockup_findgametree2,
        //            findgameTree3 to R.drawable.mockup_findgametree3,
        //            //misc
        //            findgameBalloonsIV to R.drawable.mockup_findgamebaloons,
        //            findgameBenchIV to R.drawable.mockup_findgamebench,
        //            findgameLeavesIV to R.drawable.mockup_findgameleavespile
        //        )
//
        //        viewToResource.forEach { (t, u) ->
        //            t.setImageResource(u)
        //        }
        //    }
        //}

        with(binding) {
            findgameTitleTV.text = getString(R.string.findgame_title)

            reload(reloadbtCV)

            var isFirst = false
            val dogList = mapOf(
                "corgi" to findgameCorgiIV,
                "husky" to findgameHuskyIV,
                "dachshund" to findgameDachshundIV,
                "jrterrier" to findgameJrterrierIV,
                "terrier" to findgameTerrierIV,
                "pug" to findgamePugIV
            )
            val dogIconList = mapOf(
                "corgi" to findgameCorgiIconIV,
                "husky" to findgameHuskyIconIV,
                "dachshund" to findgameDachshundIconIV,
                "jrterrier" to findgameJrterrierIconIV,
                "terrier" to findgameTerrierIconIV,
                "pug" to findgamePugIconIV
            )

            levitate(findgameTerrierIV, 35f)
            levitate(findgameBalloonsIV, 35f)

            dogList.forEach { entry ->
                entry.value.setOnClickListener { v ->
                    if (v == findgameTerrierIV) {
                        ObjectAnimator
                            .ofFloat(findgameBalloonsIV, "translationY", -800f)
                            .apply {
                                duration = 500
                                interpolator = AccelerateInterpolator()
                                start()
                            }.doOnEnd {
                                findgameBalloonsIV.visibility = View.GONE
                            }
                    }
                    ObjectAnimator.ofFloat(v, "alpha", 0f).apply {
                        duration = 300
                        start()
                    }.doOnEnd {
                        v.visibility = View.GONE
                        if (!isFirst) {
                            ObjectAnimator
                                .ofFloat(findgameProgressCV, "translationX", 0f)
                                .apply {
                                    duration = 500
                                    start()
                                }
                            isFirst = !isFirst
                        }
                        ObjectAnimator
                            .ofFloat(dogIconList[entry.key], "alpha", 100f)
                            .apply {
                                duration = 400
                                start()
                            }
                    }
                    score += 1
                    if (score == 6) {
                        winner(getString(R.string.findgame_completed), findgameConfettiKV)
                    }
                }
            }
        }

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

    private fun levitate(view: View, y: Float) {
        ObjectAnimator.ofFloat(view, "translationY", y).apply {
            duration = 3500
            interpolator = AccelerateDecelerateInterpolator()
            startDelay = 150
            start()
        }.doOnEnd {
            levitate(view, -y)
        }
    }
}