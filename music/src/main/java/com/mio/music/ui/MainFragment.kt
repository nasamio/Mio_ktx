package com.mio.music.ui

import MusicManager
import android.annotation.SuppressLint
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.mio.base.BaseFragment
import com.mio.base.Tag.TAG
import com.mio.music.R
import com.mio.music.data.Music
import com.mio.music.databinding.FragmentMainBinding
import com.mio.music.databinding.ItemMusicMainBinding
import com.mio.music.helper.MusicScanHelper
import com.mio.music.setOnPressListener
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {
    private val adapter: BaseQuickAdapter<Music, BaseDataBindingHolder<ItemMusicMainBinding>> by lazy {
        object :
            BaseQuickAdapter<Music, BaseDataBindingHolder<ItemMusicMainBinding>>(R.layout.item_music_main) {
            @SuppressLint("ResourceAsColor")
            override fun convert(holder: BaseDataBindingHolder<ItemMusicMainBinding>, item: Music) {
                holder.dataBinding?.apply {
                    music = item
                    imgIc.setImageResource(item.icRes)
                    root.setOnPressListener {
                        val textColor =
                            mContext.resources.getColor(
                                if (it) com.mio.base.R.color.white
                                else com.mio.base.R.color.black_80
                            )
                        lifecycleScope.launch(Dispatchers.Main) {
                            delay(100)
                            tvTitle.setTextColor(textColor)
                            tvContent.setTextColor(textColor)
                        }
                    }

                    root.setOnClickListener {
                        MusicManager.apply {
                            if (isPlaying()) {
                                if (currentMusicIndex == holder.adapterPosition) {

                                } else {
                                    playMusic(holder.adapterPosition)
                                }
                            } else {
                                playMusic(holder.adapterPosition)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun initView() {
        mDataBinding.root.setBackgroundColor(Color.parseColor("#2C2730"))
        mDataBinding.rvMain.apply {
            this.adapter = this@MainFragment.adapter
            layoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
        }
    }

    override fun initObserver() {
    }

    @SuppressLint("SdCardPath")
    override fun initData() {
        AndPermission.with(mContext)
            .runtime()
            .permission(
                Permission.READ_EXTERNAL_STORAGE,
                Permission.WRITE_EXTERNAL_STORAGE,
            ).onGranted {
                lifecycleScope.launch {
                    MusicScanHelper.scanLocalMusic(mContext) {
                        addMusic(it)
                    }
                }
            }
            .start()
    }

    private fun addMusic(music: Music) {
        Log.d(TAG, "initData: scan music: $music")
        lifecycleScope.launch(Dispatchers.Main) {
            // 设置一个默认的icon
            music.icRes = when (music.id.toInt() % 6) {
                1 -> R.drawable.ic_1
                2 -> R.drawable.ic_2
                3 -> R.drawable.ic_3
                4 -> R.drawable.ic_4
                5 -> R.drawable.ic_5
                else -> R.drawable.ic_6
            }

            adapter.addData(music)
            MusicManager.addMusic(mutableListOf(music))
        }
    }

}