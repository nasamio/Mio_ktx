package com.mio.base

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.MenuInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction.TRANSIT_NONE
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.mio.base.Tag.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.Date


/**
 * 采用replace方式切换fragment 会销毁之前的fragment
 * 如果 transition 和 animatorEnter/animatorOut 都设置了的话 以后者为主
 */
fun AppCompatActivity.replaceFragment(
    containerId: Int, fragment: Fragment,
//    fta: FragmentTransitionAnimator = FragmentTransitionAnimator.INVALID, 拓展函数上下文就是activity,无法做到引用自选资源
    transition: Int = TRANSIT_NONE,
    animatorEnter: Int = 0, // 可以尝试使用：fragment_enter_from_left.xml
    animatorOut: Int = 0,
) {
    supportFragmentManager.beginTransaction()
        .setTransition(transition)
        .setCustomAnimations(animatorEnter, animatorOut) // 必须在replace之前才生效
        .replace(containerId, fragment)
        .addToBackStack(null)
        .commit()
}

/**
 * dp = 值 * 屏幕密度
 */
val Int.dp: Int
    get() {
        val density = Resources.getSystem().displayMetrics.density
        return (this * density).toInt()
    }

/**
 * px 就是值
 */
val Int.px: Int
    get() {
        return this
    }


fun String.md5(): String {
    val bytes = this.toByteArray(Charset.defaultCharset())
    val md5Digest = MessageDigest.getInstance("MD5")
    val md5Bytes = md5Digest.digest(bytes)

    // 转换为十六进制字符串
    val hexChars = CharArray(md5Bytes.size * 2)
    for (i in md5Bytes.indices) {
        val v = md5Bytes[i].toInt() and 0xFF
        hexChars[i * 2] = "0123456789ABCDEF"[v shr 4]
        hexChars[i * 2 + 1] = "0123456789ABCDEF"[v and 0x0F]
    }

    return String(hexChars)
}

// 创建一个扩展函数，用于Activity之间的跳转
inline fun <reified T : Any> Context.startActivity() {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
}

fun AppCompatActivity.toFragment(
    containerId: Int,
    fragment: Fragment,
    animatorEnter: Int = 0, // 可以尝试使用：fragment_enter_from_left.xml
    animatorOut: Int = 0,
) {
    supportFragmentManager
        .beginTransaction()
        .setCustomAnimations(animatorEnter, animatorOut) // 必须在replace之前才生效
        .replace(containerId, fragment)
        .show(fragment)
        .addToBackStack(null)
        .commit()
}

fun AppCompatActivity.toFragment(
    containerId: Int,
    currentFragment: Fragment?,
    fragment: Fragment,
    animatorEnter: Int = 0, // 可以尝试使用：fragment_enter_from_left.xml
    animatorOut: Int = 0,
    shouldDestroy: (Fragment) -> Boolean = { true }, // 这个参数表示，调用者可以根据自己的需要，判断页面是否需要销毁；返回true表示每次会重新创建，false表示状态会一直保存
) {
    val transaction = supportFragmentManager
        .beginTransaction()
        .setCustomAnimations(animatorEnter, animatorOut) // 必须在replace之前才生效


    currentFragment?.let {
        if (shouldDestroy(currentFragment)) {
            transaction.remove(currentFragment)
        } else {
            transaction.hide(currentFragment)
        }
    }

    // 添加新的 Fragment，如果不存在则替换
    if (!fragment.isAdded) {
        transaction.add(containerId, fragment)
    } else {
        transaction.show(fragment)
    }

    transaction.addToBackStack(null).commit()
}

/**
 * dp 转 px
 */
fun dp2px(dp: Float): Float = dp * Resources.getSystem().displayMetrics.density

fun Float.dp() = dp2px(this)

fun Int.dp() = dp2px(this.toFloat()).toInt()

/**
 * sp 转 px
 */
fun sp2px(sp: Float): Float = sp * Resources.getSystem().displayMetrics.density

fun Float.sp() = sp2px(this)

fun Int.sp() = sp2px(this.toFloat()).toInt()


// 默认的动画时间
const val ANIMATION_DURATION: Long = 500


/**
 * view的平移动画
 */
@SuppressLint("RtlHardcoded")
fun View.translation(
    direction: Int,
    length: Float,
    duration: Long = ANIMATION_DURATION
): ObjectAnimator {
    val propertyName = when (direction) {
        Gravity.LEFT -> "translationX"
        Gravity.RIGHT -> "translationX"
        Gravity.TOP -> "translationY"
        Gravity.BOTTOM -> "translationY"
        else -> ""
    }

    var len = when (direction) {
        Gravity.TOP -> -length
        Gravity.LEFT -> -length
        else -> length
    }

    val current =
        if (direction == Gravity.LEFT || direction == Gravity.RIGHT) translationX
        else translationY
    return ObjectAnimator
        .ofFloat(this, propertyName, current, len)
        .setDuration(duration)
}

fun View.rotate(angle: Float, duration: Long = ANIMATION_DURATION): ObjectAnimator {
    return ObjectAnimator.ofFloat(this, "rotation", rotation, angle).setDuration(duration)
}

fun View.alpha(toAlpha: Float, duration: Long = ANIMATION_DURATION): ObjectAnimator {
    return ObjectAnimator.ofFloat(this, "alpha", alpha, toAlpha).setDuration(duration)
}

/**
 * 传入多个动画对象 一起运行
 */
fun Context.playTogether(
    vararg animators: ObjectAnimator,
    startListener: () -> Unit = {},
    endListener: () -> Unit = {}
) {
    val animatorSet = AnimatorSet()
    animatorSet.playTogether(*animators)
    animatorSet.start()
    animatorSet.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator) {
            startListener()
        }

        override fun onAnimationEnd(animation: Animator) {
            endListener()
        }
    })
}

/**
 * 设置子item的间距 上下左右等间距
 */
fun RecyclerView.setItemMargin(margin: Int) {
    addItemDecoration(object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            itemPosition: Int,
            parent: RecyclerView
        ) {
            outRect.left = margin
            outRect.right = margin
            outRect.top = margin
            adapter?.let {
                if (itemPosition == it.itemCount - 1) {
                    outRect.bottom = margin
                }
            }
        }
    })
}

fun Context.toast(text: String) {
    shortToast(text)
}


fun Context.shortToast(text: String) {
    GlobalScope.launch(Dispatchers.Main) {
        Toast.makeText(this@shortToast, text, Toast.LENGTH_SHORT).show()
    }
}

fun Context.longToast(text: String) {
    GlobalScope.launch(Dispatchers.Main) {
        Toast.makeText(this@longToast, text, Toast.LENGTH_LONG).show()
    }
}

/**
 * 获取屏幕宽度
 */
fun Context.getScreenWidth() = resources.displayMetrics.widthPixels

/**
 * 根据时间戳获取时间
 */
@SuppressLint("SimpleDateFormat")
fun Long.getDateHHmm() = SimpleDateFormat("HH:mm").format(Date(this))!!

fun ViewPager.addOnPageSelectListener(listener: (Int) -> Unit) {
    addOnPageChangeListener(object : OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            listener(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
        }

    })
}

/**
 * 设置vp的内部元素的出现样式
 */
fun ViewGroup.layoutAnimationFrom(animId: Int, duration: Long = 400) {
    val animation = AnimationUtils.loadAnimation(context, animId)
    animation.duration = duration
    layoutAnimation = LayoutAnimationController(animation)
}

@SuppressLint("RestrictedApi")
fun Context.parseMenu(menuId: Int): MenuBuilder {
    val menuBuilder = MenuBuilder(this)
    MenuInflater(this).inflate(
        menuId, menuBuilder
    )
    return menuBuilder
}

fun View.size(width: Int, height: Int) {
    val lp = layoutParams
    lp.apply {
        this.width = width
        this.height = height
    }
    layoutParams = lp
}

fun ObservableBoolean.addChangeCallback(
    autoCallbackOnce: Boolean = false,/*是否自动回调一次*/
    callback: (Boolean) -> Unit,
) {
    this.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            sender?.let {
                callback((sender as ObservableBoolean).get())
            }
        }
    })
    if (autoCallbackOnce) {
        callback(this.get())
    }
}

fun ObservableInt.addChangeCallback(
    autoCallbackOnce: Boolean = false,
    callback: (Int) -> Unit
) {
    this.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            sender?.let {
                callback((sender as ObservableInt).get())
            }
        }
    })
    if (autoCallbackOnce) {
        callback(this.get())
    }
}

fun View.scaleXx(toScale: Float, duration: Long = ANIMATION_DURATION): ObjectAnimator {
    return ObjectAnimator.ofFloat(this, "scaleX", scaleX, toScale).setDuration(duration)
}

fun View.scaleYy(toScale: Float, duration: Long = ANIMATION_DURATION): ObjectAnimator {
    return ObjectAnimator.ofFloat(this, "scaleY", scaleY, toScale).setDuration(duration)
}

fun View.bgColor(
    endColor: Int,
    startColor: Int = 0,
    duration: Long = ANIMATION_DURATION
): ObjectAnimator {
    val colorAnimator = ObjectAnimator.ofArgb(
        this, "backgroundColor",
        startColor, endColor
    )
    colorAnimator.duration = duration
    return colorAnimator
}

fun TextView.textColor(
    endColor: Int,
    duration: Long = ANIMATION_DURATION
): ObjectAnimator {
    val colorAnimator = ObjectAnimator.ofArgb(this, "textColor", endColor)
    colorAnimator.duration = duration
    return colorAnimator
}

fun View.topMargin(margin: Int) {
    margin(top = margin)
}

fun View.margin(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
    layoutParams?.let {
        val lp = if (it is ConstraintLayout.LayoutParams) {
            ConstraintLayout.LayoutParams(it)
        } else {
            ViewGroup.MarginLayoutParams(it)
        }
        lp.apply {
            this.leftMargin = left
            this.topMargin = top
            this.rightMargin = right
            this.bottomMargin = bottom
        }
        layoutParams = lp
    }
}

fun RecyclerView.setGridRvItemDecoration(margin: Int, onlyInner: Boolean = false) {
    addItemDecoration(object :
        RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            itemPosition: Int,
            parent: RecyclerView
        ) {
            super.getItemOffsets(outRect, itemPosition, parent)
            val spanCount =
                (parent.layoutManager as GridLayoutManager).spanCount
            val xInt = itemPosition % spanCount
            val yInt = itemPosition / spanCount
            if (onlyInner) {
                if (xInt != 0) {
                    outRect.left = margin
                }
                if (yInt != 0) {
                    outRect.top = margin
                }
            } else {
                // 实现每一行之间的间距一致
                if (itemPosition >= spanCount) {
                    outRect.bottom = margin
                } else {
                    outRect.top = margin
                    outRect.bottom = margin
                }
            }
        }
    })
}

/**
 * 防重复点击
 */
fun View.setClickListener(duration: Long = 300, listener: OnClickListener) {
    setOnClickListener(object : OnClickListener {
        var lastClickTime = 0L
        override fun onClick(v: View?) {
            val currentTimeMillis = System.currentTimeMillis()
            if (currentTimeMillis - lastClickTime > duration) {
                lastClickTime = currentTimeMillis
                listener.onClick(v)
            }
        }
    })
}

fun View.setAnimationClickListener(
    scale: Float = 1.1f,
    durationL: Long = 300,
    listener: OnClickListener
) {
    setOnClickListener {
        context.playTogether(
            this.scaleXx(scale, durationL / 2),
            this.scaleYy(scale, durationL / 2),
            this.alpha(0.9f, durationL / 2),
            endListener = {
                context.playTogether(
                    this.scaleXx(1f, durationL / 2),
                    this.scaleYy(1f, durationL / 2),
                    this.alpha(1f, durationL / 2),
                    endListener = {
                        listener.onClick(this)
                    }
                )
            }
        )
    }
}


fun ViewGroup.defaultAnimation() {
    layoutAnimationFrom(R.anim.default_show)
}