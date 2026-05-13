package com.patricksynnah.musicplayer.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.patricksynnah.musicplayer.data.model.Playlist
import com.patricksynnah.musicplayer.databinding.ItemPlaylistBinding

class PlaylistAdapter(
    private val onPlaylistClick: (Playlist) -> Unit,
    private val onPlaylistDelete: (Playlist) -> Unit = {}
) : ListAdapter<Playlist, PlaylistAdapter.PlaylistViewHolder>(PlaylistDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding = ItemPlaylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistViewHolder(binding, onPlaylistClick, onPlaylistDelete)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PlaylistViewHolder(
        private val binding: ItemPlaylistBinding,
        private val onPlaylistClick: (Playlist) -> Unit,
        private val onPlaylistDelete: (Playlist) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(playlist: Playlist) {
            binding.apply {
                nameTextView.text = playlist.name
                descriptionTextView.text = playlist.description ?: "No description"
                root.setOnClickListener { onPlaylistClick(playlist) }
                deleteButton.setOnClickListener { onPlaylistDelete(playlist) }
            }
        }
    }

    class PlaylistDiffCallback : DiffUtil.ItemCallback<Playlist>() {
        override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist) = oldItem == newItem
    }
}
