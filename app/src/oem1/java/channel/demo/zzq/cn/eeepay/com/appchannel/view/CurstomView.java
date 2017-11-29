package channel.demo.zzq.cn.eeepay.com.appchannel.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 描述：练习一下自定义view
 * 作者：zhuangzeqin
 * 时间: 2017/11/23-14:41
 * 邮箱：zzq@eeepay.cn
 */
public class CurstomView extends View {
    public CurstomView(Context context) {
        super(context);
    }

    public CurstomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CurstomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //getMeasuredWidth/Height 获取View 的宽高。
        int measuredHeight = getMeasuredHeight();

        int measuredWidth = getMeasuredWidth();

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        //Canvas 画布常用的一些简单的图形会先
//        drawArc 绘制弧
//        drawBitmap 绘制位图
//        drawCircle 绘制圆形
//        drawLine 绘制线
//        drawOval 绘制椭圆
//        drawPath 绘制路径
//        drawPoint 绘制一个点
//        drawPoints 绘制多个点
//        drawRect 绘制矩形
//        drawRoundRect 绘制圆角矩形
//        drawText 绘制字符串
//        drawTextOnPath 沿着路径绘制字符串

        Paint paint = new Paint();
//        paint.setColor(Color.parseColor("#3F51B5"));
//        paint.setAlpha(75);
//        paint.setAntiAlias(false);
//        canvas.drawCircle(200,500,45,paint);
//        //设置空心Style
//        paint.setStyle(Paint.Style.STROKE);
//        //设置空心边框的宽度
//        paint.setStrokeWidth(10);
//        paint.setShadowLayer(100,200,200,Color.parseColor("#ED9246"));
//        //绘制空心圆
//        canvas.drawCircle(200,500,90,paint);
//
//        //去锯齿
//        paint.setAntiAlias(true);
//        paint.setStyle(Paint.Style.FILL);
//        //设置颜色
//        paint.setColor(getResources().getColor(android.R.color.holo_blue_light));
//        //绘制正方形
//        canvas.drawRect(200, 500, 500, 200, paint);
//
//
//        //设置空心Style
//        paint.setStyle(Paint.Style.STROKE);
//        //设置空心边框的宽度
//        paint.setStrokeWidth(20);
//        //绘制空心矩形
//        canvas.drawRect(100, 400, 600, 800, paint);
//
//
//        //去锯齿
//        paint.setAntiAlias(true);
//        //设置颜色
//        paint.setColor(getResources().getColor(android.R.color.holo_orange_dark));
//        //绘制圆角矩形
//        canvas.drawRoundRect(100, 100, 300, 300, 30, 30, paint);
//        //上面代码等同于
//        //RectF rel=new RectF(100,100,300,300);
//        //canvas.drawRoundRect(rel,30,30,paint);
//
//        //设置空心Style
//        paint.setStyle(Paint.Style.STROKE);
//        //设置空心边框的宽度
//        paint.setStrokeWidth(20);
//        //绘制空心圆角矩形
//        canvas.drawRoundRect(100, 400, 600, 800, 30, 30, paint);


        //加粗
        paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(45);
//        canvas.drawColor(Color.BLUE) ;
//        canvas.drawRect(250, 200, 30, 30, paint);
        canvas.drawText("MyView", 250, 200, paint);
//        setARGB/setColor 设置颜色
//        setAlpha 设置透明度
//        setAntiAlias 设置是否抗锯齿
//        setShader 设置画笔的填充效果
//        setShadowLayer 设置阴影
//        setStyle 设置画笔风格
//        setStrokeWidth 设置空心边框的宽度
//        setTextSize 设置绘制文本时文字的大小


    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
    }


}
