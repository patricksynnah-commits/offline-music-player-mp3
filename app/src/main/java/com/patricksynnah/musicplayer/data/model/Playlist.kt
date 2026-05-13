package com.patricksynnah.musicplayer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class Playlist(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "playlist_songs")
data class PlaylistSongCrossRef(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val playlistId: Int,
    val songId: String,
    val position: Int
)
