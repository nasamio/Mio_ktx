package com.mio.base.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.mio.base.R;

public class ArcImageView extends AppCompatImageView {
    /*
     *弧形高度
     */
    private int mArcHeight = 100;

    public int getmArcHeight() {
        return mArcHeight;
    }

    public void setmArcHeight(int mArcHeight) {
        this.mArcHeight = mArcHeight;

        invalidate();
    }

    public ArcImageView(Context context) {
        this(context, null);
    }

    public ArcImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // 加载arc_height属性
        mArcHeight = context.obtainStyledAttributes(attrs, com.mio.base.R.styleable.ArcImageView)
                .getDimensionPixelSize(R.styleable.ArcImageView_arcHeight, mArcHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(0, getHeight() - mArcHeight);
        path.quadTo(getWidth() / 2, getHeight() + mArcHeight, getWidth(), getHeight() - mArcHeight);
        path.lineTo(getWidth(), 0);
        path.close();
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}