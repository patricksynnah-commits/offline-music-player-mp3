package com.patricksynnah.musicplayer.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.patricksynnah.musicplayer.data.model.Playlist
import com.patricksynnah.musicplayer.data.model.PlaylistSongCrossRef
import com.patricksynnah.musicplayer.data.model.Song

@Database(
    entities = [Song::class, Playlist::class, PlaylistSongCrossRef::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun playlistDao(): PlaylistDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "music_player_db"
                ).build().also { INSTANCE = it }
            }
    }
}
