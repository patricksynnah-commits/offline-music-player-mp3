package com.patricksynnah.musicplayer.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import com.patricksynnah.musicplayer.MainActivity
import com.patricksynnah.musicplayer.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MusicService : Service() {

    private lateinit var mediaPlayer: MediaPlayer
    private val binder = LocalBinder()
    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())
    private var updateJob: Job? = null

    val isPlaying = MutableLiveData<Boolean>(false)
    val currentPosition = MutableLiveData<Int>(0)
    val duration = MutableLiveData<Int>(0)
    val currentSong = MutableLiveData<String>()

    inner class LocalBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
        setupMediaPlayer()
        createNotificationChannel()
        registerHeadphoneReceiver()
    }

    private fun setupMediaPlayer() {
        mediaPlayer.setOnCompletionListener {
            isPlaying.postValue(false)
            onSongCompleted()
        }
        mediaPlayer.setOnErrorListener { _, what, extra ->
            false
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Music Player",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Music playback controls"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    fun playSong(filePath: String, songTitle: String) {
        try {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }
            mediaPlayer.reset()
            mediaPlayer.setDataSource(filePath)
            mediaPlayer.prepare()
            mediaPlayer.start()
            isPlaying.postValue(true)
            currentSong.postValue(songTitle)
            duration.postValue(mediaPlayer.duration)
            startForeground(NOTIFICATION_ID, buildNotification())
            startUpdatePosition()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun pause() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            isPlaying.postValue(false)
            updateJob?.cancel()
        }
    }

    fun resume() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
            isPlaying.postValue(true)
            startUpdatePosition()
        }
    }

    fun seekTo(position: Int) {
        mediaPlayer.seekTo(position)
        currentPosition.postValue(position)
    }

    fun setVolume(volume: Int) {
        val vol = volume.coerceIn(0, 100) / 100f
        mediaPlayer.setVolume(vol, vol)
    }

    private fun startUpdatePosition() {
        updateJob?.cancel()
        updateJob = serviceScope.launch {
            while (isPlaying.value == true) {
                currentPosition.postValue(mediaPlayer.currentPosition)
                delay(1000)
            }
        }
    }

    private fun buildNotification(): NotificationCompat.Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Music Playing")
            .setContentText(currentSong.value ?: "Unknown")
            .setSmallIcon(R.drawable.ic_music_note)
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun onSongCompleted() {
        // Handle song completion
    }

    private fun registerHeadphoneReceiver() {
        val filter = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(HeadphoneReceiver(), filter, Context.RECEIVER_EXPORTED)
        } else {
            registerReceiver(HeadphoneReceiver(), filter)
        }
    }

    inner class HeadphoneReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY == intent?.action) {
                pause()
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        serviceScope.cancel()
        updateJob?.cancel()
    }

    companion object {
        private const val CHANNEL_ID = "music_player_channel"
        private const val NOTIFICATION_ID = 1
    }
}
