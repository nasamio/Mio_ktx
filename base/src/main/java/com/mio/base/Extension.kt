package com.mio.base

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Rect
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction.TRANSIT_NONE
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
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
fun Context.playTogether(vararg animators: ObjectAnimator) {
    val animatorSet = AnimatorSet()
    animatorSet.playTogether(*animators)
    animatorSet.start()
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