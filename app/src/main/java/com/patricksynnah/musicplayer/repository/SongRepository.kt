package com.patricksynnah.musicplayer.repository

import android.content.ContentResolver
import android.content.Context
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.patricksynnah.musicplayer.data.db.AppDatabase
import com.patricksynnah.musicplayer.data.model.Song
import kotlinx.coroutines.Dispatchers

class SongRepository(private val context: Context) {
    private val database = AppDatabase.getInstance(context)
    private val songDao = database.songDao()

    fun getAllSongs(): LiveData<List<Song>> = songDao.getAllSongs()

    fun getSongById(songId: String): LiveData<Song> = songDao.getSongById(songId)

    fun searchSongs(query: String): LiveData<List<Song>> = songDao.searchSongs(query)

    fun loadSongsFromDevice(): LiveData<List<Song>> = liveData(Dispatchers.IO) {
        val songs = mutableListOf<Song>()
        val projection = arrayOf(
            MediaStore.Audio.AudioColumns._ID,
            MediaStore.Audio.AudioColumns.TITLE,
            MediaStore.Audio.AudioColumns.ARTIST,
            MediaStore.Audio.AudioColumns.ALBUM,
            MediaStore.Audio.AudioColumns.DATA,
            MediaStore.Audio.AudioColumns.DURATION,
            MediaStore.Audio.AudioColumns.ALBUM_ID,
            MediaStore.Audio.AudioColumns.DATE_MODIFIED
        )

        val selection = MediaStore.Audio.AudioColumns.IS_MUSIC + " != 0"
        val sortOrder = MediaStore.Audio.AudioColumns.TITLE + " ASC"

        context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST)
            val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATA)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION)
            val dateModifiedColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATE_MODIFIED)

            while (cursor.moveToNext()) {
                val song = Song(
                    id = cursor.getString(idColumn),
                    title = cursor.getString(titleColumn),
                    artist = cursor.getString(artistColumn),
                    album = cursor.getString(albumColumn),
                    filePath = cursor.getString(dataColumn),
                    duration = cursor.getLong(durationColumn),
                    dateModified = cursor.getLong(dateModifiedColumn)
                )
                songs.add(song)
            }
        }

        songDao.insertSongs(songs)
        emit(songs)
    }
}
