package com.redc4ke.kalm.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.redc4ke.kalm.R
import com.redc4ke.kalm.databinding.FragmentInformationBinding
import com.redc4ke.kalm.ui.base.BaseFragment

class InformationFragment : BaseFragment<FragmentInformationBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentInformationBinding
        get() = FragmentInformationBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            val linkToTextView = mapOf(
                "https://www.freepik.com/freepik" to infoAuthor1TV,
                "https://www.freepik.com/brgfx" to infoAuthor2TV,
                "https://www.freepik.com/upklyak" to infoAuthor3TV,
                "https://www.freepik.com/studiogstock" to infoAuthor4TV,
                "https://www.freepik.com/pch-vector" to infoAuthor5TV,
                "https://www.freepik.com/macrovector" to infoAuthor6TV,
                "https://www.freepik.com/lifeforstock" to infoAuthor7TV,
                "https://www.instagram.com/_cunningham/" to infoSpecialauthor1TV,
                "https://www.bensound.com/royalty-free-music" to infoMusicTV,
                "https://freesound.org/people/jobro/" to infoSoundTV
            )

            linkToTextView.forEach {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.key))
                it.value.setOnClickListener {
                    startActivity(intent)
                }
            }

            infoReturnBT.setOnClickListener {
                findNavController().navigate(R.id.landingFragment)
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

}