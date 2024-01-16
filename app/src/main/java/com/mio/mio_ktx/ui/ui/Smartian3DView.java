package com.mio.mio_ktx.ui.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * 3d环绕例子 实测可用
 * https://juejin.cn/post/7310112330663690267#heading-14
 */
public class Smartian3DView extends View {

    private TextPaint mCommonPaint;
    private DisplayMetrics mDM;


    public Smartian3DView(Context context) {
        this(context, null);
    }

    public Smartian3DView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        mDM = getResources().getDisplayMetrics();
        //否则提供给外部纹理绘制
        mCommonPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        mCommonPaint.setAntiAlias(true);
        mCommonPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mCommonPaint.setStrokeCap(Paint.Cap.ROUND);
        mCommonPaint.setFilterBitmap(true);
        mCommonPaint.setDither(true);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode != MeasureSpec.EXACTLY) {
            widthSize = mDM.widthPixels / 2;
        }
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (heightMode != MeasureSpec.EXACTLY) {
            heightSize = widthSize / 2;
        }

        setMeasuredDimension(widthSize, heightSize);

    }


    double xr = Math.toRadians(5);  //绕x轴旋转
    double yr = 0;  //绕y轴旋转;
    double zr = 0;

    private List<Point> pointList = new ArrayList<>();

    private Random random = new Random();

    private Shader shader = null;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        if (width < 1) return;
        int height = getHeight();
        if (height < 1) return;

        float radius = Math.min(width, height) / 3f;
        int save = canvas.save();
        canvas.translate(width / 2f, height / 2f);


        if (pointList.isEmpty()) {

            int max = 20;

            for (int i = 0; i < max; i++) {

                //均匀排列
                double v = -1.0 + (2.0 * i - 1.0) / max;
                if (v < -1.0) {
                    v = 1.0f;
                }
                float delta = (float) Math.acos(v);
                float alpha = (float) (Math.sqrt(max * Math.PI) * delta);

                //随机排列
                //  float alpha = random.nextFloat() * 360;
                //   float delta = random.nextFloat() * 180;  //靠正面

                Point point = new Point();

                point.x = (float) (radius * Math.cos(alpha) * Math.sin(delta));
                point.y = (float) (radius * Math.sin(alpha) * Math.sin(delta));
                point.z = (float) (radius * Math.cos(delta));
                point.color = argb(random.nextFloat(), random.nextFloat(), random.nextFloat());
                pointList.add(point);
            }
        }


        for (int i = 0; i < pointList.size(); i++) {

            Point point = pointList.get(i);
            float x = point.x;
            float y = point.y;
            float z = point.z;

            //绕X轴旋转，乘以X轴的旋转矩阵
            float rx1 = x;
            float ry1 = (float) (y * Math.cos(xr) + z * -Math.sin(xr));
            float rz1 = (float) (y * Math.sin(xr) + z * Math.cos(xr));

            // 绕Y轴旋转,乘以Y轴的旋转矩阵
            float rx2 = (float) (rx1 * Math.cos(yr) + rz1 * Math.sin(yr));
            float ry2 = ry1;
            float rz2 = (float) (rx1 * -Math.sin(yr) + rz1 * Math.cos(yr));

            // 绕Z轴旋转,乘以Z轴的旋转矩阵
            float rx3 = (float) (rx2 * Math.cos(zr) + ry2 * -Math.sin(zr));
            float ry3 = (float) (rx2 * Math.sin(zr) + ry2 * Math.cos(zr));
            float rz3 = rz2;


            point.x = rx3;
            point.y = ry3;
            point.z = rz3;

            // 透视除法，z轴向内的方向
            float scale = (2 * radius) / ((2 * radius) + rz3);
            point.scale = scale;

        }

        //排序，先画背面的，再画正面的
        Collections.sort(pointList, comparator);

        for (int i = 0; i < pointList.size(); i++) {
            Point point = pointList.get(i);
            mCommonPaint.setColor(point.color);
            if (point.scale > 1) {
                mCommonPaint.setAlpha(255);
            } else {
                mCommonPaint.setAlpha((int) (point.scale * 255));
            }
            if (point.z > 0) {
                canvas.drawCircle(point.x * point.scale, point.y * point.scale, 5 + 25 * point.scale, mCommonPaint);
                continue;
            }
            break;
        }
        mCommonPaint.setAlpha(255);
        if (shader == null) {
            shader = new RadialGradient(0, 0, radius,
                    new int[]{0xffec7733, 0x77f9922, 0x11000000},
                    new float[]{0.2f, 0.7f, 0.9f},
                    Shader.TileMode.CLAMP);
        }
        mCommonPaint.setShader(shader);
        mCommonPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(0, 0, radius, mCommonPaint);
        mCommonPaint.setShader(null);

        //绘制大于
        for (int i = pointList.size() - 1; i >= 0; i--) {
            Point point = pointList.get(i);
            mCommonPaint.setColor(point.color);
            if (point.scale > 1) {
                mCommonPaint.setAlpha(255);
            } else {
                mCommonPaint.setAlpha((int) (point.scale * 255));
            }
            if (point.z <= 0) {
                canvas.drawCircle(point.x * point.scale, point.y * point.scale, 5 + 25 * point.scale, mCommonPaint);
            } else {
                break;
            }
        }
        canvas.restoreToCount(save);
        postInvalidateDelayed(32);

    }

    Comparator comparator = new Comparator<Point>() {
        @Override
        public int compare(Point left, Point right) {
            if (left.z - right.z < 0) {
                return 1;
            }
            if (left.z == right.z) {
                return 0;
            }
            return -1;
        }
    };

    static class Point {
        private int color;
        private float x;
        private float y;
        private float z;

        private float scale = 1f;
    }


    public static int argb(float red, float green, float blue) {
        return ((int) (1 * 255.0f + 0.5f) << 24) |
                ((int) (red * 255.0f + 0.5f) << 16) |
                ((int) (green * 255.0f + 0.5f) << 8) |
                (int) (blue * 255.0f + 0.5f);
    }

}
