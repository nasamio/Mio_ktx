package com.mio.music.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.mio.base.Tag.TAG
import com.mio.base.setClickListener
import com.mio.music.R
import com.mio.music.data.Song
import com.mio.music.databinding.ItemSongListBinding
import com.mio.music.manager.MusicPlayer

class RvSongListAdapter :
    BaseQuickAdapter<Song, BaseDataBindingHolder<ItemSongListBinding>>(R.layout.item_song_list) {
    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseDataBindingHolder<ItemSongListBinding>, item: Song) {
        holder.dataBinding?.apply {
            tvIndex.text = (holder.adapterPosition + 1).toString()
            tvName.text = item.name
            tvArtist.text = item.ar?.get(0)?.name

            root.setClickListener {
                if (MusicPlayer.currentMusic()?.id == item.id) {
                    return@setClickListener
                }
                MusicPlayer.playMusic(item)
            }
        }
    }
}