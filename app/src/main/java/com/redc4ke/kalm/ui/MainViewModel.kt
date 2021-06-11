package com.redc4ke.kalm.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val isMusicEnabled: MutableLiveData<Boolean> = MutableLiveData<Boolean>().also {
        it.value = true
    }

    fun isMusicEnabled(): MutableLiveData<Boolean> {
        return isMusicEnabled
    }

    fun change(activity: MainActivity) {
        isMusicEnabled.apply { value = !value!! }
        if (activity.mediaPlayer.isPlaying) {
            activity.mediaPlayer.pause()
        } else {
            activity.mediaPlayer.start()
        }
    }
}