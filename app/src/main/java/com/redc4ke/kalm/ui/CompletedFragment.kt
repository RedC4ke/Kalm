package com.redc4ke.kalm.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.compose.animation.core.Spring
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.navigation.fragment.findNavController
import com.redc4ke.kalm.databinding.FragmentCompletedBinding
import com.redc4ke.kalm.ui.base.BaseDialogFragment
import com.redc4ke.kalm.ui.base.GameFragment


class CompletedFragment(private val header: String, private val gameFragment: GameFragment<*>) :
    BaseDialogFragment<FragmentCompletedBinding>() {


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCompletedBinding
        get() = FragmentCompletedBinding::inflate


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        preventBug()
        dialog!!.setCancelable(false)

        val width = resources.displayMetrics.widthPixels
        val height = resources.displayMetrics.heightPixels
        dialog!!.window!!.setLayout(width, height)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.completedBGMiddleCL.bringToFront()
        binding.comppletedBGHeadCL.bringToFront()

        binding.completedHeaderTV.text = header
        val prefs = requireActivity().getPreferences(Context.MODE_PRIVATE)
        binding.completedTasksThisRunTV.text = prefs.getInt("tasksThisRun", 0).toString()
        binding.completedTasksOverallV.text = prefs.getInt("tasksOverall", 0).toString()
        binding.completedAppLaunchesTV.text = prefs.getInt("appLaunches", 0).toString()

        binding.completedOnwardTV.setOnClickListener {

            dismiss()
            gameFragment.findNavController().navigate(gameFragment.directions.random())

        }

        animate()
    }


    private fun preventBug() {

        //Shit pasted from stack to prevent exiting immersive mode
        dialog!!.window!!
            .setFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            )
        dialog!!.window!!
            .decorView.systemUiVisibility = requireActivity().window.decorView.systemUiVisibility

        dialog!!.setOnShowListener { //Clear the not focusable flag from the window
            dialog!!.window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)

            //Update the WindowManager with the new attributes (no nicer way I know of to do this)..
            val wm = requireActivity().getSystemService(Context.WINDOW_SERVICE) as WindowManager
            wm.updateViewLayout(dialog!!.window!!.decorView, dialog!!.window!!.attributes)
        }
    }

    private fun animate() {

        val pixelsPerSecond: Float = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            5f,
            resources.displayMetrics
        )

        val middleTranslation: Float = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            60f,
            resources.displayMetrics
        )

        val bottomTranslation: Float = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            160f,
            resources.displayMetrics
        )

        binding.completedBGMiddleCL.let {
            SpringAnimation(it, DynamicAnimation.TRANSLATION_Y, middleTranslation).apply {

                setStartVelocity(pixelsPerSecond)
                spring.dampingRatio = Spring.DampingRatioMediumBouncy
                spring.stiffness = Spring.StiffnessLow
                start()
            }
        }

        binding.completedBGBottomCL.let {
            SpringAnimation(it, DynamicAnimation.TRANSLATION_Y, bottomTranslation).apply {
                setStartVelocity(pixelsPerSecond)
                spring.dampingRatio = Spring.DampingRatioMediumBouncy
                spring.stiffness = Spring.StiffnessLow
                start()
            }
        }
    }

}