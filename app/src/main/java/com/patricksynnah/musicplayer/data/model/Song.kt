package com.patricksynnah.musicplayer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class Song(
    @PrimaryKey
    val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val filePath: String,
    val duration: Long,
    val albumArtPath: String? = null,
    val dateModified: Long
)

data class SongWithPlayCount(
    val song: Song,
    val playCount: Int = 0
)
