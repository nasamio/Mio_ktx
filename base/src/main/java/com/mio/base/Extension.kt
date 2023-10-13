package com.mio.base

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction.TRANSIT_NONE

/**
 * 采用replace方式切换fragment 会销毁之前的fragment
 * 如果 transition 和 animatorEnter/animatorOut 都设置了的话 以后者为主
 */
fun AppCompatActivity.replaceFragment(
    containerId: Int, fragment: Fragment,
//    fta: FragmentTransitionAnimator = FragmentTransitionAnimator.INVALID, 拓展函数上下文就是activity,无法做到引用自选资源
    transition: Int = TRANSIT_NONE,
    animatorEnter: Int = 0,
    animatorOut: Int = 0,
) {
    supportFragmentManager.beginTransaction()
        .setTransition(transition)
        .setCustomAnimations(animatorEnter, animatorOut) // 必须在replace之前才生效
        .replace(containerId, fragment)
        .addToBackStack(null)
        .commit()
}