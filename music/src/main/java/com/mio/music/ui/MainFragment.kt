package com.mio.music.ui

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mio.base.BaseFragment
import com.mio.music.R
import com.mio.music.databinding.FragmentMainBinding

class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {
    override fun initView() {
        mDataBinding.root.setBackgroundColor(Color.parseColor("#2C2730"))
    }

    override fun initObserver() {
    }

    override fun initData() {
    }

}