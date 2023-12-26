package com.mio.music.ui

import DialogHelper
import androidx.lifecycle.lifecycleScope
import com.lxj.xpopup.XPopup
import com.mio.base.BaseFragment
import com.mio.base.setClickListener
import com.mio.base.toast
import com.mio.music.R
import com.mio.music.databinding.FragmentMainBinding
import com.mio.music.helper.KtorHelper
import com.mio.music.helper.UserHelper
import com.mio.music.ui.view.LoginView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {
    //    private val adapter: BaseQuickAdapter<Music, BaseDataBindingHolder<ItemMusicMainBinding>> by lazy {
//        object :
//            BaseQuickAdapter<Music, BaseDataBindingHolder<ItemMusicMainBinding>>(R.layout.item_music_main) {
//            @SuppressLint("ResourceAsColor")
//            override fun convert(holder: BaseDataBindingHolder<ItemMusicMainBinding>, item: Music) {
//                holder.dataBinding?.apply {
//                    music = item
//                    imgIc.setImageResource(item.icRes)
//                    root.setOnPressListener {
//                        val textColor =
//                            mContext.resources.getColor(
//                                if (it) com.mio.base.R.color.white
//                                else com.mio.base.R.color.black_80
//                            )
//                        lifecycleScope.launch(Dispatchers.Main) {
//                            delay(100)
//                            tvTitle.setTextColor(textColor)
//                            tvContent.setTextColor(textColor)
//                        }
//                    }
//
//                    root.setOnClickListener {
//                        MusicManager.apply {
//                            if (isPlaying()) {
//                                if (currentMusicIndex == holder.adapterPosition) {
//
//                                } else {
//                                    playMusic(holder.adapterPosition)
//                                }
//                            } else {
//                                playMusic(holder.adapterPosition)
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    override fun initView() {
//        mDataBinding.root.setBackgroundColor(Color.parseColor("#2C2730"))
//        mDataBinding.rvMain.apply {
//            this.adapter = this@MainFragment.adapter
//            layoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
//        }
//    }
//
//    override fun initObserver() {
//    }
//
//    @SuppressLint("SdCardPath")
//    override fun initData() {
//        // 从本地加载音乐
//        AndPermission.with(mContext)
//            .runtime()
//            .permission(
//                Permission.READ_EXTERNAL_STORAGE,
//                Permission.WRITE_EXTERNAL_STORAGE,
//            ).onGranted {
//                lifecycleScope.launch {
//                    MusicScanHelper.scanLocalMusic(mContext) {
//                        addMusic(it)
//                    }
//                }
//            }
//            .start()
//    }
//
//    private fun addMusic(music: Music) {
//        Log.d(TAG, "initData: scan music: $music")
//        lifecycleScope.launch(Dispatchers.Main) {
//            // 设置一个默认的icon
//            music.icRes = when (music.id.toInt() % 6) {
//                1 -> R.drawable.ic_1
//                2 -> R.drawable.ic_2
//                3 -> R.drawable.ic_3
//                4 -> R.drawable.ic_4
//                5 -> R.drawable.ic_5
//                else -> R.drawable.ic_6
//            }
//
//            adapter.addData(music)
//            MusicManager.addMusic(mutableListOf(music))
//        }
//    }
    override fun initView() {
        // 检查是否登录
        if (UserHelper.isLogin.get()) {

        } else {
            // 登录弹窗
            val loginView = XPopup.Builder(mContext)
                .asCustom(LoginView(mContext))
                    as LoginView
            loginView.show()
            loginView.postDelayed({
                loginView.mDataBinding.apply {
                    btnSend.setClickListener {
                        lifecycleScope.launch {
                            val response = KtorHelper.sendCaptcha(num = etPhone.text.toString())
                            withContext(Dispatchers.Main) {
                                if (response.code == 200) {
                                    mContext.toast("验证码发送成功")
                                } else {
                                    mContext.toast("验证码发送失败")
                                }
                            }
                        }
                    }
                    btnLogin.setClickListener {
                        lifecycleScope.launch {
                            val response = KtorHelper.loginCellPhone(
                                phone = etPhone.text.toString(),
                                captcha = etCode.text.toString()
                            )
                            withContext(Dispatchers.Main) {
                                if (response.code == 200) {
                                    mContext.toast("登录成功")

                                    UserHelper.apply {
                                        isLogin.set(true)
                                        account = response.account
                                        token = response.token
                                        profile = response.profile
                                        bindings = response.bindings
                                        cookie = response.cookie
                                    }

                                    loginView.dismiss()
                                } else {
                                    mContext.toast("登录失败")
                                }
                            }
                        }
                    }
                }
            }, 1000)
        }
    }

    override fun initObserver() {
    }

    override fun initData() {

    }

}