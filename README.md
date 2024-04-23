## 这是用于简化开发的仓库（kotlin编写）

仅限个人及小伙伴使用，下面是各个模块的使用方式。

### 使用：

项目根目录的build.gradle中：

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

高版本的as可能在settings.gradle中。

接着需要在你的项目的build.gradle中加入依赖：

```
dependencies {
    implementation 'com.github.nasamio:Mio_ktx:Tag'
}
```

tag需要看最新的tag标签：

### base

base库封装基类库，基于data binding的，所以使用者还需要在模块的build.gradle中的android闭包中声明：

```groovy
    dataBinding {
        enabled = true
    }
```

之后点击右上角的sync now进行同步。

**base activity使用:**

```
package com.mio.mio_ktx.ui

import android.util.Log
import androidx.databinding.ViewDataBinding
import com.mio.base.BaseActivity
import com.mio.base.BaseFragment
import com.mio.base.Tag.TAG
import com.mio.base.replaceFragment
import com.mio.mio_ktx.R
import com.mio.mio_ktx.databinding.ActivityMainBinding


class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    val fragments: List<BaseFragment<out ViewDataBinding>> by lazy {
        listOf(
            AFragment(),
            BFragment(),
        )
    }

    override fun initView() {
        // 在这里进行界面的初始化
        showInitTag = true

        replaceFragment(R.id.container, AFragment())

    }

    override fun initData() {
        // 在这里进行界面数据的获取
    }

    override fun initObserver() {
        // 初始化观察者
    }

}
```

**base fragment使用:**

```
package com.mio.mio_ktx.ui

import android.util.Log
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
import com.mio.base.BaseFragment
import com.mio.base.Tag.TAG
import com.mio.base.replaceFragment
import com.mio.mio_ktx.R
import com.mio.mio_ktx.databinding.FragmentABinding

class AFragment : BaseFragment<FragmentABinding>(R.layout.fragment_a) {
    override fun initView() {
        Log.d(TAG, "initView: $this")

        mDataBinding.btn.setOnClickListener {
            val activity = activity as MainActivity
            activity.replaceFragment(
                R.id.container, activity.fragments[1],
                TRANSIT_FRAGMENT_OPEN,
                animatorEnter = R.anim.fragment_enter_from_right,
                animatorOut = R.anim.fragment_exit_to_left,
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }

    override fun initObserver() {
    }

    override fun initData() {
    }
}

```

**baseview使用：**

```
package com.mio.mio_ktx.ui

import android.content.Context
import android.util.AttributeSet
import com.mio.base.BaseView
import com.mio.mio_ktx.R
import com.mio.mio_ktx.databinding.ViewTestBinding

class TestView(context: Context?, attrs: AttributeSet?) :
    BaseView<ViewTestBinding>(context, attrs, R.layout.view_test) {
    override fun initView() {
        mDataBinding.tvCenter.text = "我是测试view"
    }


}
```

**fragment切换：**

```
 activity.replaceFragment(
                R.id.container, activity.fragments[1],
                TRANSIT_FRAGMENT_OPEN,
                animatorEnter = R.anim.fragment_enter_from_right,
                animatorOut = R.anim.fragment_exit_to_left,
            )
```
### 下面是一些测试的demo，分属在不同的模块下
