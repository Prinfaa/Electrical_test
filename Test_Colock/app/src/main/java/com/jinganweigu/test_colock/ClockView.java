package com.jinganweigu.test_colock;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class ClockView extends View {

    private Paint mPaint;
    /**
     * 描边线的粗细
     */
    private int strokeWidth = 2;
    /**
     * 时钟是否在走（即是否第一次onDraw）
     */
    private boolean isRunning=false;
//
//    private Handler mHandler;
//    private Runnable clockRunnable;

    private float milsAngle= (float) 0.0;
    /**
     * 时钟圆的半径
     */
    private int radius = 300;

    private String[] clockNumbers = {"90°", "60°", "30°", "0°", "330°", "300°", "270°", "240°", "210°", "180°", "150°", "120°"};
    /**
     * 时钟上需要绘制的数字
     */
    private String num;

    /**
     * 用于测量文本的宽、高度（这里主要是来获取高度）
     */
    private Rect textBounds = new Rect();

    private Calendar cal;

    private int hour, min, second;
    private float hourAngle = 90, minAngle = 210, secAngle = 330;

    public ClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClockView(Context context) {
        super(context);
        init();
    }

    private void init() {

        mPaint = new Paint();

    }


    public void refrush(float milsangle,boolean isRunning) {

//        hourAngle = 60;//360/12是指每个数字之间的角度 黄
//        minAngle = 180;        //绿
//        secAngle = 300;        //红
        this.isRunning=isRunning;
//        this.minAngle=milsangle;
        milsAngle+=15;


    }






        @Override
        protected void onDraw (Canvas canvas){
            super.onDraw(canvas);
            initPaint();
            //绘制圆形部分
            drawClockCircle(canvas);
            //绘制刻度线
            drawClockScale(canvas);
            //绘制数字
            drawClockNumber(canvas);
            //绘制中心原点
            drawClockDot(canvas);
            //绘制三个指针
            drawClockPoint(canvas);
        }

        /**
         * 绘制三个指针
         * @param canvas
         */
        private void drawClockPoint (Canvas canvas){

            Paint Hpaint = new Paint();
            Hpaint.setAntiAlias(true);
            Hpaint.setColor(Color.YELLOW);
            Hpaint.setStrokeWidth(3);
            Hpaint.setStyle(Paint.Style.FILL);
            Paint Mpaint = new Paint();
            Mpaint.setAntiAlias(true);
            Mpaint.setStrokeWidth(3);
            Mpaint.setColor(Color.GREEN);
            Mpaint.setStyle(Paint.Style.FILL);
            Paint Spaint = new Paint();
            Spaint.setAntiAlias(true);
            Spaint.setStrokeWidth(3);
            Spaint.setColor(Color.RED);
            Spaint.setStyle(Paint.Style.FILL);


            cal = Calendar.getInstance();
            hour = cal.get(Calendar.HOUR);//Calendar.HOUR获取的是12小时制，Calendar.HOUR_OF_DAY获取的是24小时制
            min = cal.get(Calendar.MINUTE);
            second = cal.get(Calendar.SECOND);
            //计算时分秒指针各自需要偏移的角度
//		hourAngle = 90;//360/12是指每个数字之间的角度 黄
//		minAngle = 210;		//绿
//		secAngle = 330;		//红
            //下面将时、分、秒指针按照各自的偏移角度进行旋转，每次旋转前要先保存canvas的原始状态
            canvas.save();
            canvas.rotate(hourAngle, getWidth() / 2, getHeight() / 2);//坐标轴旋转hourangle度
//		canvas.drawLine(getWidth() / 2, getHeight() / 2, getWidth() / 2, getHeight() / 2 - 260, Hpaint);//时针长度设置为65
            drawAL(getWidth() / 2, getHeight() / 2, getWidth() / 2, getHeight() / 2 - 260, canvas, Hpaint);
            canvas.restore();//坐标轴 复位
            canvas.save();

            canvas.rotate(minAngle, getWidth() / 2, getHeight() / 2);
//		canvas.drawLine(getWidth() / 2, getHeight() / 2, getWidth() / 2, getHeight() / 2 - 260 , Mpaint);//分针长度设置为90
            drawAL(getWidth() / 2, getHeight() / 2, getWidth() / 2, getHeight() / 2 - 260, canvas, Mpaint);
            canvas.restore();

            canvas.save();
            canvas.rotate(secAngle, getWidth() / 2, getHeight() / 2);
//		canvas.drawLine(getWidth() / 2, getHeight() / 2, getWidth() / 2, getHeight() / 2 - 260 , Spaint);//秒针长度设置为110
            drawAL(getWidth() / 2, getHeight() / 2, getWidth() / 2, getHeight() / 2 - 260, canvas, Spaint);
            canvas.restore();

            if(isRunning){
                Paint milspaint = new Paint();
                milspaint.setAntiAlias(true);
                milspaint.setStrokeWidth(3);
                milspaint.setColor(Color.BLUE);
                milspaint.setStyle(Paint.Style.FILL);
                canvas.save();
                canvas.rotate(milsAngle, getWidth() / 2, getHeight() / 2);
//		canvas.drawLine(getWidth() / 2, getHeight() / 2, getWidth() / 2, getHeight() / 2 - 260 , Spaint);//秒针长度设置为110
                drawAL(getWidth() / 2, getHeight() / 2, getWidth() / 2, getHeight() / 2 - 200, canvas, milspaint);
                canvas.restore();

                isRunning=false;

            }

        }


        /**
         * 画箭头
         * @param sx
         * @param sy
         * @param ex
         * @param ey
         */
        public void drawAL ( int sx, int sy, int ex, int ey, Canvas myCanvas, Paint myPaint)
        {
            double H = 30; // 箭头高度
            double L = 15; // 底边的一半
            int x3 = 0;
            int y3 = 0;
            int x4 = 0;
            int y4 = 0;
            double awrad = Math.atan(L / H); // 箭头角度
            double arraow_len = Math.sqrt(L * L + H * H); // 箭头的长度
            double[] arrXY_1 = rotateVec(ex - sx, ey - sy, awrad, true, arraow_len);
            double[] arrXY_2 = rotateVec(ex - sx, ey - sy, -awrad, true, arraow_len);
            double x_3 = ex - arrXY_1[0]; // (x3,y3)是第一端点
            double y_3 = ey - arrXY_1[1];
            double x_4 = ex - arrXY_2[0]; // (x4,y4)是第二端点
            double y_4 = ey - arrXY_2[1];
            Double X3 = new Double(x_3);
            x3 = X3.intValue();
            Double Y3 = new Double(y_3);
            y3 = Y3.intValue();
            Double X4 = new Double(x_4);
            x4 = X4.intValue();
            Double Y4 = new Double(y_4);
            y4 = Y4.intValue();
            // 画线
            myCanvas.drawLine(sx, sy - 8, ex, ey, myPaint);
            Path triangle = new Path();
            triangle.moveTo(ex, ey);
            triangle.lineTo(x3, y3);
            triangle.lineTo(x4, y4);
            triangle.close();
            myCanvas.drawPath(triangle, myPaint);

        }
        // 计算
        public double[] rotateVec ( int px, int py, double ang, boolean isChLen, double newLen)
        {
            double mathstr[] = new double[2];
            // 矢量旋转函数，参数含义分别是x分量、y分量、旋转角、是否改变长度、新长度
            double vx = px * Math.cos(ang) - py * Math.sin(ang);
            double vy = px * Math.sin(ang) + py * Math.cos(ang);
            if (isChLen) {
                double d = Math.sqrt(vx * vx + vy * vy);
                vx = vx / d * newLen;
                vy = vy / d * newLen;
                mathstr[0] = vx;
                mathstr[1] = vy;
            }
            return mathstr;
        }


        /**
         * 绘制中心原点
         */
        private void drawClockDot (Canvas canvas){
            mPaint.reset();
            mPaint.setAntiAlias(true);
            mPaint.setColor(Color.RED);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, 6, mPaint);
            initPaint();
        }

        /**
         * 绘制数字（从正上方12点处开始绘制）
         * @param canvas
         */
        private void drawClockNumber (Canvas canvas){
            //先保存一下当前画布的状态，因为后面画布会进行旋转操作，而在绘制完刻度后，需要恢复画布状态
            canvas.save();
//		mPaint.setStrokeWidth(2);
            mPaint.setTextSize(25);
            //计算12点处 数字 的坐标
            int preX = getWidth() / 2;
            int preY = getHeight() / 2 - radius - strokeWidth - 10;//10为圆与数字文本之间的间距
            //x，y才是文本真正的准确坐标，需要减去文本的自身宽、高因素
            int x, y;
            //计算画布每次需要旋转的角度
            int degree = 360 / clockNumbers.length;
            for (int i = 0; i < clockNumbers.length; i++) {
                num = clockNumbers[i];
                mPaint.getTextBounds(num, 0, num.length(), textBounds);
                x = (int) (preX - mPaint.measureText(num) / 2);
                y = preY - textBounds.height();//从文本的中心点处开始绘制
                canvas.drawText(num, x, y, mPaint);
                canvas.rotate(degree, getWidth() / 2, getHeight() / 2);//以圆中心进行旋转
            }
            //绘制完后，记得把画布状态复原
            canvas.restore();
        }

        /**
         * 绘制刻度线（总共60条）
         * 从正上方，即12点处开始绘制一条直线，后面的只是旋转一下画布角度即可
         * @param canvas
         */
        private void drawClockScale (Canvas canvas){
            //先保存一下当前画布的状态，因为后面画布会进行旋转操作，而在绘制完刻度后，需要恢复画布状态
            canvas.save();
            //计算12点处刻度的开始坐标
            int startX = getWidth() / 2;
            int startY = getHeight() / 2 - radius;//y坐标即园中心点的y坐标-半径
            //计算12点处的结束坐标
            int stopX = startX;
            int stopY1 = startY + 2 * radius;//整点处的线长度为30
            int stopY2 = startY + 2 * radius;//非整点处的线长度为15
            //计算画布每次旋转的角度
//		float degree = 360 / 60;
            float degree = 360 / 12;

            for (int i = 0; i < 6; i++) {

                canvas.drawLine(startX, startY, stopX, stopY1, mPaint);//绘制整点长的刻度

                canvas.rotate(degree, getWidth() / 2, getHeight() / 2);//以圆中心进行旋转

            }


//		for(int i = 0; i < 60; i++){
//			if(i % 5 == 0)
//				canvas.drawLine(startX, startY, stopX, stopY1, mPaint);//绘制整点长的刻度
//			else
//				canvas.drawLine(startX, startY, stopX, stopY2, mPaint);//绘制非整点处短的刻度
//			canvas.rotate(degree, getWidth() / 2, getHeight() / 2);//以圆中心进行旋转
//		}
            //绘制完后，记得把画布状态复原
            canvas.restore();
        }

        /**
         * 绘制时钟的圆形部分
         * @param canvas
         */
        private void drawClockCircle (Canvas canvas){
            //获得圆的圆点坐标
            int x = getWidth() / 2;
            int y = getHeight() / 2;
            canvas.drawCircle(x, y, radius, mPaint);
            canvas.drawCircle(x, y, radius - 80, mPaint);
            canvas.drawCircle(x, y, radius - 160, mPaint);
        }

        private void initPaint () {
            mPaint.reset();
            mPaint.setColor(Color.WHITE);
            mPaint.setStyle(Paint.Style.STROKE);//设置描边
            mPaint.setStrokeWidth(strokeWidth);//设置描边线的粗细
            mPaint.setAntiAlias(true);//设置抗锯齿，使圆形更加圆滑
        }

//	private void runClock() {
//		isRunning = true;
//		mHandler.postDelayed(clockRunnable, 1000);
//	}


    }
