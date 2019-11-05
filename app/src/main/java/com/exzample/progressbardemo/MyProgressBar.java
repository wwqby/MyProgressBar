package com.exzample.progressbardemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.List;

/**
 * /*@Description
 * /*created by wwq on 2019/11/4
 * /*@company zhongyiqiankun
 */
public class MyProgressBar extends View {

    private static final String TAG = "MyProgressBar";

    private CountDownTimer mTimer;
    private List<Integer> mTimeList;

//    控件宽度
    private float maxWidth;
//    控件高度
    private float mHeight;
    private Paint mPaint;

    private float mLength=0;
//    总计运动时间
    private int totalTime;
//    数组指针
    private int index=0;
//    每个指针剩余的时间
    private long leftTime;
//    已经经过的指针时间
    private long lastTime;
//    绘画间隔
    private int interval=17;
//    回调监听
    private CallBack mCallBack;


    public MyProgressBar(Context context) {
        this(context,null);

    }





    public MyProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);

    }

    public MyProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }




    public List<Integer> getTimeList() {
        return mTimeList;
    }

    public void setTimeList(List<Integer> timeList) {
        mTimeList = timeList;
        for(int i=0;i<mTimeList.size();i++){
            if (i%2==0){
                totalTime+=mTimeList.get(i);
                Log.i(TAG, "setTimeList: totalTime="+totalTime);
            }
        }
        initTimer(mTimeList.get(index));
    }

    private void initTimer(long million){
        leftTime=million;
        mTimer=new CountDownTimer(million,interval) {


            @Override
            public void onTick(long l) {
                Log.i(TAG, "onTick: l="+l);
                leftTime=l;
                if (mCallBack!=null){
                    mCallBack.updateTime(l);
                }
                Log.i(TAG, "onTick: leftTime="+leftTime);
//                float indexLength=maxWidth/(float) (totalTime*1000/interval);
//                Log.i(TAG, "onTick: indexLength="+indexLength);
                if (index%2==0){
                    mLength=(float) (lastTime+mTimeList.get(index).longValue()*1000-l)/totalTime/1000*maxWidth;
                    Log.i(TAG, "onTick: mlength="+mLength);
                    invalidate();
                }
            }

            @Override
            public void onFinish() {
                Log.i(TAG, "onFinish: mlength="+mLength);
                if (mTimer!=null){
                    mTimer.cancel();
                }
                mTimer=null;
//                启动下一个timer
                if (index%2==0&&index<mTimeList.size()&&index>=0){
                    lastTime+=mTimeList.get(index)*1000;
                }
                index++;
                Log.i(TAG, "onFinish: index="+index);
                if (index<mTimeList.size()){
                    initTimer(mTimeList.get(index));
                    mTimer.start();
                }
                if (mCallBack!=null){
                    mCallBack.updateTime(0);
                }
            }
        };
    }
    private void initTimer(final int indexTime) {
        leftTime=indexTime*1000;
        initTimer(leftTime);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        maxWidth=MeasureSpec.getSize(widthMeasureSpec);
        Log.i(TAG, "onMeasure: maxWidth="+maxWidth);
        mHeight=MeasureSpec.getSize(heightMeasureSpec);
        Log.i(TAG, "onMeasure: mHeight="+mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i(TAG, "onDraw: ");
        if (mPaint==null){
            mPaint=new Paint();
//            设置画笔颜色
            mPaint.setColor(Color.RED);
//            设置透明度
            mPaint.setAlpha(1000);
//            设置画笔端口形状
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            //设置线段宽度
            mPaint.setStrokeWidth(mHeight);
        }


        float rounder=mHeight/2;
        if (mLength>0&&mLength<maxWidth){
            canvas.drawLine(0+rounder,rounder,mLength-rounder,rounder,mPaint);
        }else if (mLength>=maxWidth){
            canvas.drawLine(0+rounder,rounder,maxWidth-rounder,rounder,mPaint);
        }
    }

    public void start(){
        if (mTimer!=null){
            mTimer.start();
        }else if (index>=0&&index<mTimeList.size()){
            initTimer(leftTime);
            mTimer.start();
        }
    }

    public void stop(){
        if (mTimer!=null){
            mTimer.cancel();
            mTimer=null;
        }
    }

    public void reset(){
        if (mTimer!=null){
            mTimer.cancel();
            mTimer=null;
        }
        index=0;
        leftTime=mTimeList.get(index)*1000;
        lastTime=0;
        initTimer(leftTime);
        mLength=0;
        invalidate();
    }

    public void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }

    public  interface CallBack{
         void updateTime(long time);
    }
}
