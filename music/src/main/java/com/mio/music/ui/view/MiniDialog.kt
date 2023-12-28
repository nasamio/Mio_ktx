import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.WindowManager
import com.mio.music.R

class MiniDialog(context: Context) : Dialog(context) {
    init {
        setContentView(R.layout.layout_mini)

        // 获取 Dialog 的 Window
        val window = window

        // 设置 Dialog 在底部显示
        window?.setGravity(Gravity.BOTTOM)

        // 设置动画效果（可选）
//        window?.attributes?.windowAnimations = R.anim.mini_show

        // 设置 Dialog 的宽度和高度（可根据需要调整）
        val layoutParams = window?.attributes
        layoutParams?.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams?.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = layoutParams
    }
}
