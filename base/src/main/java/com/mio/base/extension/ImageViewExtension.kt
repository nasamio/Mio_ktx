package com.mio.base.extension

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.mio.base.utils.ImageLoader
import jp.wasabeef.glide.transformations.BlurTransformation
import java.security.MessageDigest

/**
 * @author: mio
 * @date: 2024/04/09
 * @param url:图片路径
 * @description: 图片加载
 */
fun ImageView.load(url: String) {
    Glide.with(this)
        .load(url)
        .apply(ImageLoader.option)
        .into(this)
}

fun ImageView.load(res: Int) {
    Glide.with(this)
        .load(res)
        .apply(ImageLoader.option)
        .into(this)
}

fun ImageView.loadCircle(url: String) {
    Glide.with(this)
        .load(url)
        .circleCrop()
        .apply(ImageLoader.option)
        .into(this)
}

fun ImageView.loadRound(url: String, radius: Float) {
    Glide.with(this)
        .load(url)
        .apply(ImageLoader.option)
        .transform(RoundBitmap(radius))
        .into(this)
}

fun ImageView.loadBlur(url: String, radius: Int = 0) {
    Glide.with(this)
        .load(url)
        .apply(ImageLoader.option)
        .transform(BlurTransformation(radius))
        .into(this)
}

class RoundBitmap(val radius: Float = 0f) : BitmapTransformation() {
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {

    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val bitmap = TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight)
        return roundCrop(pool, bitmap)
    }

    private fun roundCrop(pool: BitmapPool, bitmap: Bitmap): Bitmap {
        val result =
            pool.get(bitmap.width, bitmap.height, bitmap.getConfig() ?: Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        val paint = Paint().apply {
            shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            isAntiAlias = true
        }
        val rectF = RectF(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat())
        canvas.drawRoundRect(rectF, radius, radius, paint)
        return result
    }
}

