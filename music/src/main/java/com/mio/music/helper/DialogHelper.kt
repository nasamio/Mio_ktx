import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.setPadding
import com.mio.base.Tag
import com.mio.base.dp
import com.mio.base.setClickListener
import com.mio.music.helper.KtorHelper
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object DialogHelper {
    @OptIn(DelicateCoroutinesApi::class)
    fun showLoginDialog(context: Context) {
        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setTitle("验证码登录")

        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL

        val num = EditText(context)
        num.hint = "请输入手机号"
        num.setPadding(10.dp)
        layout.addView(num)

//        val password = EditText(context)
//        password.hint = "请输入密码"
//        layout.addView(password)

        val captcha = EditText(context)
        captcha.hint = "请输入验证码"
        captcha.setPadding(10.dp)
        layout.addView(captcha)

        alertDialog.setView(layout)

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "登录") { _, _ ->
            val phoneNumber = num.text.toString()
//            val password = password.text.toString()
            val captcha = captcha.text.toString()
            if (phoneNumber.isNotEmpty() /*&& password.isNotEmpty()*/) {
                // 这里可以添加你的登录逻辑
                Toast.makeText(
                    context,
                    "点了登录,手机号:$phoneNumber,验证码:$captcha",
                    Toast.LENGTH_SHORT
                ).show()
                GlobalScope.launch {
                    val response = KtorHelper.loginCellPhone(
                        phone = phoneNumber,
                        captcha = captcha
                    )
                    withContext(Dispatchers.Main) {
                        if (response.code == 200) {
                            Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "登录失败", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            } else {
                Toast.makeText(context, "手机号和密码不能为空", Toast.LENGTH_SHORT).show()
            }
        }

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "发送验证码") { _, _ ->
        }

        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setClickListener {
            GlobalScope.launch {
                val phoneNumber = num.text.toString()
                val response = KtorHelper.sendCaptcha(phoneNumber)
                Log.d(Tag.TAG, "initData: response:$response")
                withContext(Dispatchers.Main) {
                    if (response.code == 200) {
                        Toast.makeText(context, "验证码发送成功", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "验证码发送失败", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "取消") { dialog, _ ->
            dialog.dismiss()
        }

        alertDialog.show()
    }
}
