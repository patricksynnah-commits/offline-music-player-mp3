package com.patricksynnah.musicplayer.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.patricksynnah.musicplayer.data.model.Song
import com.patricksynnah.musicplayer.databinding.ItemSongBinding

class SongAdapter(
    private val onSongClick: (Song) -> Unit,
    private val onSongLongClick: (Song) -> Boolean = { false }
) : ListAdapter<Song, SongAdapter.SongViewHolder>(SongDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongViewHolder(binding, onSongClick, onSongLongClick)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SongViewHolder(
        private val binding: ItemSongBinding,
        private val onSongClick: (Song) -> Unit,
        private val onSongLongClick: (Song) -> Boolean
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song) {
            binding.apply {
                titleTextView.text = song.title
                artistTextView.text = song.artist
                durationTextView.text = formatDuration(song.duration)
                root.setOnClickListener { onSongClick(song) }
                root.setOnLongClickListener { onSongLongClick(song) }
            }
        }

        private fun formatDuration(millis: Long): String {
            val seconds = (millis / 1000) % 60
            val minutes = (millis / (1000 * 60)) % 60
            return String.format("%02d:%02d", minutes, seconds)
        }
    }

    class SongDiffCallback : DiffUtil.ItemCallback<Song>() {
        override fun areItemsTheSame(oldItem: Song, newItem: Song) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Song, newItem: Song) = oldItem == newItem
    }
}
