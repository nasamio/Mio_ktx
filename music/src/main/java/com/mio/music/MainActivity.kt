package com.mio.music

import DialogHelper
import android.os.Bundle
import androidx.core.view.forEachIndexed
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import com.lxj.xpopup.XPopup
import com.mio.base.BaseActivity
import com.mio.base.BaseQuickFragmentVpAdapter
import com.mio.base.addChangeCallback
import com.mio.base.addOnPageSelectListener
import com.mio.base.setClickListener
import com.mio.base.toast
import com.mio.music.databinding.ActivityMainBinding
import com.mio.music.databinding.LayoutMiniPlayerBinding
import com.mio.music.helper.KtorHelper
import com.mio.music.helper.UserHelper
import com.mio.music.manager.MusicPlayer
import com.mio.music.ui.MainFragment
import com.mio.music.ui.MineFragment
import com.mio.music.ui.ToolsFragment
import com.mio.music.ui.VideoFragment
import com.mio.music.ui.view.LoginView
import com.mio.music.ui.view.MiniView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.ConnectException

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val fragmentVpAdapter: BaseQuickFragmentVpAdapter by lazy {
        BaseQuickFragmentVpAdapter(
            supportFragmentManager, mutableListOf(
                MainFragment(),
                VideoFragment(),
                ToolsFragment(),
                MineFragment(),
            )
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        // 在 Activity 的 onCreate 方法中调用
//        window.decorView.systemUiVisibility = (
//                View.SYSTEM_UI_FLAG_FULLSCREEN
//                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                )
//        actionBar?.hide()

        checkPermission()
        checkLoginState()

        super.onCreate(savedInstanceState)
    }



    /**
     * 检查权限
     */
    private fun checkPermission() {

    }

    /**
     * 检查登录状态
     */
    private fun checkLoginState() {
        // 检查是否登录
        if (UserHelper.isLogin()) {
            toast("已登录,token:${UserHelper.token}")
        } else {
            // 登录弹窗
            val loginView = XPopup.Builder(this)
                .asCustom(LoginView(this))
                    as LoginView
            loginView.show()
            loginView.postDelayed({
                loginView.mDataBinding.apply {
                    btnSend.setClickListener {
                        lifecycleScope.launch {
                            try {
                                val response = KtorHelper.sendCaptcha(num = etPhone.text.toString())
                                withContext(Dispatchers.Main) {
                                    if (response.code == 200) {
                                        toast("验证码发送成功")
                                    } else {
                                        toast("验证码发送失败")
                                    }
                                }
                            } catch (e: ConnectException) {
                                withContext(Dispatchers.Main) {
                                    toast("服务器连接失败，请检查是否服务器异常")
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
                                    toast("登录成功")

                                    UserHelper.apply {
                                        account = response.account
                                        token = response.token
                                        uid = response.profile.userId.toLong()
                                        nickname = response.profile.nickname
                                        avatarUrl = response.profile.avatarUrl
                                        backgroundUrl = response.profile.backgroundUrl
                                        profile = response.profile
                                        bindings = response.bindings
                                        cookie = response.cookie
                                    }

                                    loginView.dismiss()
                                } else {
                                    toast("登录失败")
                                }
                            }
                        }
                    }
                }
            }, 1000)
        }
    }

    override fun initView() {
        mDataBinding.vp.apply {
            this.adapter = fragmentVpAdapter
            addOnPageSelectListener {
                mDataBinding.bnv.selectedItemId = mDataBinding.bnv.menu[it].itemId
            }
        }

        mDataBinding.bnv.setOnNavigationItemSelectedListener {
            mDataBinding.bnv.menu.forEachIndexed { index, item ->
                if (it.itemId == item.itemId) {
                    mDataBinding.vp.setCurrentItem(index, true)
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    override fun initObserver() {
    }

    override fun initData() {
    }
}