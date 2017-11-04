package me.michd.drumroll

import android.content.Context
import android.media.AudioManager
import android.media.AudioManager.OnAudioFocusChangeListener
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity() : AppCompatActivity(), View.OnClickListener, OnAudioFocusChangeListener, MediaPlayer.OnCompletionListener {
    private var _mp : MediaPlayer? = null
    private var _playButton : Button? = null
    private var _stopButton : Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _playButton = findViewById(R.id.playButton)
        _stopButton = findViewById(R.id.stopButton)

        _playButton?.setOnClickListener(this)
        _stopButton?.setOnClickListener(this)
        _stopButton?.isEnabled = false

        _mp = MediaPlayer.create(applicationContext, R.raw.drumroll)
        _mp?.setOnCompletionListener(this)
    }

    override fun onClick(clickedView: View?) {
        if (clickedView == null) return

        when(clickedView.id) {
            R.id.playButton -> play()
            R.id.stopButton -> stop()
        }
    }

    override fun onAudioFocusChange(focusChange: Int) {
        when(focusChange) {
            AudioManager.AUDIOFOCUS_LOSS,
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> stop()
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {
        if (_mp != mp) return // Now why on earth would that ever happen?
        _mp?.seekTo(0)
        _stopButton?.isEnabled = false
        _playButton?.isEnabled = true
    }

    private fun play() {
        var audioManager : AudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        if (audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            return
        }

        _mp?.start()
        _playButton?.isEnabled = false
        _stopButton?.isEnabled = true
    }

    private fun stop() {
        _mp?.pause()
        _mp?.seekTo(0)
        _stopButton?.isEnabled = false
        _playButton?.isEnabled = true
    }
}
