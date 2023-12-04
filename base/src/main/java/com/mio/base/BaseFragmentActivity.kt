package com.mio.base

import android.util.Log
import androidx.databinding.ObservableInt
import com.mio.base.Tag.TAG
import com.mio.base.databinding.ActivityFragmentBinding
import com.mio.base.view.BottomTab

abstract class BaseFragmentActivity
    (
    val fragments: MutableList<BaseFragment<*>>,
    val menu: Int,
) : BaseActivity<ActivityFragmentBinding>(R.layout.activity_fragment) {
    private val checkPos: ObservableInt by lazy {
        ObservableInt(-1)
    }
    private var currentFragment: BaseFragment<*>? = null

    override fun initView() {
        mDataBinding.bt.setCheckedChangeListener(object : BottomTab.OnCheckChangeListener {
            override fun onChange(pos: Int) {
                checkPos.set(pos)
            }
        })
        mDataBinding.bt.menuId = this.menu

        try {
            checkPos.addChangeCallback {
                toFragment(R.id.fl_con, currentFragment, fragments[it])
                currentFragment = fragments[it]
            }
        } catch (IllegalStateException: Exception) {
            Log.e(TAG, "initView: ", IllegalStateException)
        }

        checkPos.set(0)

        initV()
    }

    abstract fun initV()
}