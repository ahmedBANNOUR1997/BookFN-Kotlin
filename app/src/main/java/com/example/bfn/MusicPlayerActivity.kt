package com.example.bfn

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ImageButton
import android.widget.SeekBar
import kotlinx.coroutines.delay

class MusicPlayerActivity(private var handler: Handler) : AppCompatActivity() {

    lateinit var runnable : Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_musicplayer)

        val seekbar = findViewById<SeekBar>(R.id.seekbar)
        val play_btn = findViewById<ImageButton>(R.id.play_btn)

        val mediaplayer = MediaPlayer.create(this, R.raw.saunders)

        seekbar.progress = 0

        seekbar.max = mediaplayer.duration

        play_btn.setOnClickListener {
            if(!mediaplayer.isPlaying){
                mediaplayer.start()
                play_btn.setImageResource(R.drawable.ic_baseline_pause_24)
            } else {
                play_btn.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            }
        }
        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, pos: Int, changed: Boolean) {
               if(changed)
               {
                   mediaplayer.seekTo(pos)
               }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                TODO("Not yet implemented")
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                TODO("Not yet implemented")
            }

        })
        runnable = Runnable {
            seekbar.progress = mediaplayer.currentPosition
            handler.postDelayed(runnable, 1000)
        }

        handler.postDelayed(runnable, 1000)
        mediaplayer.setOnCompletionListener {
            play_btn.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            seekbar.progress = 0
        }
    }
}