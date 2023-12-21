package com.mio.mio_ktx.ui

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.mio.base.BaseFragment
import com.mio.base.Tag.TAG
import com.mio.base.extension.toJson
import com.mio.base.setClickListener
import com.mio.base.toast
import com.mio.mio_ktx.R
import com.mio.mio_ktx.databinding.FragmentABinding
import com.mio.mio_ktx.ui.bean.Msg
import com.mio.mio_ktx.ui.mqtt.MqttHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * todo 改成一个即使通讯的fragment
 */
class AFragment : BaseFragment<FragmentABinding>(R.layout.fragment_a) {
    private val rvAdapter =
        object : BaseMultiItemQuickAdapter<Msg, BaseViewHolder>(mutableListOf()) {
            init {
                addItemType(0, R.layout.item_msg_mine)
                addItemType(1, R.layout.item_msg_other)
            }

            @SuppressLint("UseCompatLoadingForDrawables")
            override fun convert(helper: BaseViewHolder?, item: Msg?) {
                item?.let { msg ->
                    helper?.let {
                        it.setText(R.id.tv_content, msg.msg)

                        val icImageView = it.getView<ImageView>(R.id.iv_icon)
                        // 使用glide加载圆角图片
                        Glide.with(mContext)
                            .load(if (msg.type == 0) R.drawable.ic_zjl else R.drawable.ic_meizi)
                            .apply(RequestOptions.bitmapTransform(CircleCrop()))
                            .into(icImageView)

                        // 如果上一个和当前的一个是一个人发的 就隐藏头像
                        if (data.size > 1
                            && helper.adapterPosition > 0
                            && data[helper.adapterPosition - 1].type == msg.type
                        ) {
                            icImageView.visibility = View.GONE
                        } else
                            icImageView.visibility = View.VISIBLE

                    }
                }
            }
        }

    override fun initView() {
        showLoading()
        mDataBinding.rvMain.apply {
            adapter = rvAdapter
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(mContext)
        }

        mDataBinding.btnSend.setClickListener {
            if (!MqttHelper.isConnected) {
                mContext.toast("mqtt服务连接失败...")
            } else {
                val str = mDataBinding.et.text.toString()
                if (str.isNotEmpty()) {
                    val topic = "pc"
                    MqttHelper.publish(topic, str)
                    mDataBinding.et.text = null

                    rvAdapter.addData(
                        Msg(
                            0,
                            str,
                            "",
                            0
                        )
                    )
                    mDataBinding.rvMain.scrollToPosition(rvAdapter.itemCount - 1)

                } else {
                    mContext.toast("请输入内容...")
                }
            }
        }


    }

    override fun getLoadingLayoutId(): Int {
        return R.layout.layout_loading2
    }

    override fun initObserver() {
    }

    override fun initData() {
        val topic = "android"

        MqttHelper.apply {
            init(mContext,
                onConnected = {
                    subscribe(topic,
                        onSuccess = {
                            Log.d(TAG, "initData: 注册${topic}成功")
                        })
                    showContent()
                },
                onDisconnected = {
                    showError()
                },
                onMsgArrived = { t, msg ->
                    if (t == topic) {
                        rvAdapter.addData(
                            Msg(
                                msg.id,
                                parseMsg(String(msg.payload)),
                                "",
                                1
                            )
                        )
                        mDataBinding.rvMain.scrollToPosition(rvAdapter.itemCount - 1)
                    }
                }
            )
        }

//        rvAdapter.setNewData(
//            mutableListOf(
//                Msg(0, "我是jj", "", 0),
//                Msg(0, "我是gg", "", 1),
//                Msg(0, "我是jj", "", 0),
//                Msg(0, "我是jj", "", 0),
//                Msg(0, "我是jj", "", 0),
//                Msg(0, "我是gg", "", 1),
//                Msg(0, "我是jj", "", 0),
//                Msg(0, "我是jj", "", 0),
//                Msg(0, "我是jj", "", 0),
//                Msg(0, "我是gg", "", 1),
//                Msg(0, "我是jj", "", 0),
//                Msg(0, "我是jj", "", 0),
//            )
//        )
    }
}