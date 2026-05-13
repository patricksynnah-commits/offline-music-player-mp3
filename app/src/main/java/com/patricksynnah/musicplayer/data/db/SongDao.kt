package com.patricksynnah.musicplayer.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.patricksynnah.musicplayer.data.model.Song

@Dao
interface SongDao {
    @Query("SELECT * FROM songs ORDER BY title ASC")
    fun getAllSongs(): LiveData<List<Song>>

    @Query("SELECT * FROM songs WHERE id = :songId")
    fun getSongById(songId: String): LiveData<Song>

    @Query("SELECT * FROM songs WHERE title LIKE '%' || :query || '%' OR artist LIKE '%' || :query || '%'")
    fun searchSongs(query: String): LiveData<List<Song>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(song: Song)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongs(songs: List<Song>)

    @Delete
    suspend fun deleteSong(song: Song)

    @Query("DELETE FROM songs")
    suspend fun deleteAllSongs()
}
