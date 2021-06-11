package com.redc4ke.kalm.ui

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaDataSource
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.AttributeSet
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import com.redc4ke.kalm.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.security.KeyStore

class MainActivity : AppCompatActivity() {

    lateinit var mediaPlayer: MediaPlayer
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val prefs = getPreferences(Context.MODE_PRIVATE)
        val appLaunches = prefs.getInt("appLaunches", 0)
        prefs.edit {
            putInt("appLaunches", appLaunches + 1)
            putInt("tasksThisRun", 0)
        }

        val playlist = listOf(
            R.raw.buddy,
            R.raw.cute,
            R.raw.smile,
            R.raw.ukulele
        )
        var i = 0
        playMusic(i, playlist).invokeOnCompletion {
            mediaPlayer.setOnCompletionListener {
                if (i==3) i = 0
                else i++
                playMusic(i, playlist)
            }
        }

    }

    override fun onResume() {
        super.onResume()

        val isMusicEnabled = viewModel.isMusicEnabled().value ?: true
        if (!mediaPlayer.isPlaying && isMusicEnabled) {
            mediaPlayer.start()
        }

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                )

    }

    override fun onPause() {
        mediaPlayer.pause()

        super.onPause()
    }

    private fun playMusic(i: Int, playlist: List<Int>) = runBlocking {
        launch {
            val attrs = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build()

            mediaPlayer = MediaPlayer.create(applicationContext, playlist[i])
            mediaPlayer.setVolume(0.2f, 0.2f)
            mediaPlayer.setAudioAttributes(attrs)
            mediaPlayer.start()
        }
    }

}