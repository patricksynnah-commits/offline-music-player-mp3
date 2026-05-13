package com.patricksynnah.musicplayer.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.patricksynnah.musicplayer.data.db.AppDatabase
import com.patricksynnah.musicplayer.data.model.Playlist
import com.patricksynnah.musicplayer.data.model.PlaylistSongCrossRef

class PlaylistRepository(context: Context) {
    private val database = AppDatabase.getInstance(context)
    private val playlistDao = database.playlistDao()

    fun getAllPlaylists(): LiveData<List<Playlist>> = playlistDao.getAllPlaylists()

    fun getPlaylistById(playlistId: Int): LiveData<Playlist> = playlistDao.getPlaylistById(playlistId)

    suspend fun createPlaylist(playlist: Playlist): Long = playlistDao.insertPlaylist(playlist)

    suspend fun deletePlaylist(playlist: Playlist) = playlistDao.deletePlaylist(playlist)

    suspend fun addSongToPlaylist(playlistId: Int, songId: String, position: Int) {
        val crossRef = PlaylistSongCrossRef(
            playlistId = playlistId,
            songId = songId,
            position = position
        )
        playlistDao.addSongToPlaylist(crossRef)
    }

    suspend fun removeSongFromPlaylist(crossRef: PlaylistSongCrossRef) {
        playlistDao.removeSongFromPlaylist(crossRef)
    }

    suspend fun clearPlaylist(playlistId: Int) = playlistDao.clearPlaylist(playlistId)
}
