package com.patricksynnah.musicplayer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.patricksynnah.musicplayer.data.model.Song
import com.patricksynnah.musicplayer.repository.SongRepository
import kotlinx.coroutines.launch

class SongViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SongRepository(application)

    val allSongs: LiveData<List<Song>> = repository.getAllSongs()
    val loadedSongs: LiveData<List<Song>> = repository.loadSongsFromDevice()

    fun searchSongs(query: String): LiveData<List<Song>> = repository.searchSongs(query)

    fun refreshSongs() {
        viewModelScope.launch {
            repository.loadSongsFromDevice()
        }
    }
}
