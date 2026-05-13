package com.patricksynnah.musicplayer.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import com.patricksynnah.musicplayer.service.MusicService

class MusicBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (AudioManager.ACTION_AUDIO_BECOMING_NOISY == intent?.action) {
            // Pause music when headphones are disconnected
            val musicServiceIntent = Intent(context, MusicService::class.java)
            context?.stopService(musicServiceIntent)
        }
    }
}
