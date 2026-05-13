package com.patricksynnah.musicplayer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.patricksynnah.musicplayer.data.model.Playlist
import com.patricksynnah.musicplayer.repository.PlaylistRepository
import kotlinx.coroutines.launch

class PlaylistViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PlaylistRepository(application)

    val allPlaylists: LiveData<List<Playlist>> = repository.getAllPlaylists()

    fun getPlaylistById(playlistId: Int): LiveData<Playlist> = repository.getPlaylistById(playlistId)

    fun createPlaylist(name: String, description: String? = null) {
        viewModelScope.launch {
            val playlist = Playlist(
                name = name,
                description = description
            )
            repository.createPlaylist(playlist)
        }
    }

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            repository.deletePlaylist(playlist)
        }
    }

    fun addSongToPlaylist(playlistId: Int, songId: String, position: Int) {
        viewModelScope.launch {
            repository.addSongToPlaylist(playlistId, songId, position)
        }
    }
}
