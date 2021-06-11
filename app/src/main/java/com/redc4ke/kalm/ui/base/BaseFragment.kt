package com.redc4ke.kalm.ui.base

import android.content.Context
import android.graphics.Color
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.transition.MaterialFadeThrough
import com.redc4ke.kalm.R
import com.redc4ke.kalm.ui.CompletedFragment
import com.redc4ke.kalm.ui.MainActivity
import com.redc4ke.kalm.ui.MainViewModel
import nl.dionsegijn.konfetti.KonfettiView
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size

abstract class BaseFragment<VB: ViewBinding>: Fragment() {

    private var _binding: VB? = null
    protected val binding
        get() = _binding!!
    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)

        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
        reenterTransition = MaterialFadeThrough()
        returnTransition = MaterialFadeThrough()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

abstract class GameFragment<VB: ViewBinding> : BaseFragment<VB>() {

    abstract val directions: Array<Int>
    abstract val reloadDirection: Int
    private val attrs = AudioAttributes.Builder()
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .setUsage(AudioAttributes.USAGE_GAME)
        .build()
    private val soundPool = SoundPool.Builder()
        .setAudioAttributes(attrs)
        .build()
    private var winSound = 0
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        winSound = soundPool.load(context, R.raw.ez_win, 1)
        viewModel = ViewModelProvider(activity as MainActivity)[MainViewModel::class.java]
    }

    fun winner(string: String, confetti: KonfettiView) {
        confetti.build()
            .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
            .setDirection(0.0, 359.0)
            .setSpeed(1f, 5f)
            .setFadeOutEnabled(true)
            .setTimeToLive(2000L)
            .addShapes(Shape.Square, Shape.Circle)
            .addSizes(Size(12))
            .setPosition(
                -50f,
                confetti.width + 50f,
                -50f,
                -50f
            )
            .streamFor(300, 5000L)

        val prefs = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val tasksThisRun = prefs.getInt("tasksThisRun", 0)
        val tasksOverall = prefs.getInt("tasksOverall", 0)
        prefs.edit {
            putInt("tasksThisRun", tasksThisRun + 1)
            putInt("tasksOverall", tasksOverall + 1)
        }

        CompletedFragment(string, this)
            .show(parentFragmentManager, "completed")

        if (viewModel.isMusicEnabled().value != false) {
            soundPool.play(winSound, 1.0f, 1.0f, 1, 0, 1f)
        }
    }

    fun reload(view: View) {
        view.setOnClickListener {
            this.findNavController().navigate(directions.random())
        }
    }

}