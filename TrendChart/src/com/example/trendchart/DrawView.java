package com.example.trendchart;

import com.example.trendchart.R.drawable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class DrawView extends View implements Runnable {  

    private int yearNum = 0;
    private float []grade;
    private String []year;
    private RectF []oval;
    private String term;
	private int height;
	private int widthSpace;
	private int heightSpace;
	private float[] mGrade;
    private Paint []mPaint;
    
    public DrawView(Context context, AttributeSet attrs){
    	super(context, attrs);
    }
    
    public DrawView(Context context) {  
        super(context);  
        /* 构建对象 */  
        new Thread(this).start();  
    }
    
    public void setGrade( float[] grade){
    	this.grade = grade;
    }
    
    public void setYear( String[] year){
    	this.year = year;
    }
    
    public void setYearNum( int yearNum){
    	this.yearNum = yearNum;
    }
    
	public void onDraw(Canvas canvas) {
        super.onDraw(canvas);  
        /* 声明Paint对象 */  
        Paint nPaint = new Paint();
        
        /* 设置取消锯齿效果 */  
        nPaint.setAntiAlias(true);
        //颜色
        nPaint.setColor(0xfff4782f);
        //设置线条粗细
        nPaint.setStrokeWidth(10);
        //设置样式
        nPaint.setStyle(Paint.Style.STROKE);
        /* 设置画布的颜色 */  
        canvas.drawColor(0x00000000);
        
        /* 线锁定画布 */  
        canvas.save();
        
        //设置所需要用的几张图片，方便调用
        Bitmap unsBitmap = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.chart_unselected));  
        Bitmap sBitmap = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.chart_selected));

        Bitmap scoreBox = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.score_bg));
        
        mGrade = new float[grade.length];

        //最高分和最低分，初始化
        float lowGrade = 100;
        float highGrade = 0;
        
        //获取所有加权成绩中的最高分和最低分
        for(int i = 0 ; i < grade.length ; i++){
        	if(grade[i] < lowGrade)
        		lowGrade = grade[i];
        	if(grade[i] > highGrade)
        		highGrade = grade[i];
        }
        
        //获得相对分数（分数-最低分）方便画图
        for(int i = 0 ; i < mGrade.length ; i++){
        	mGrade[i] = grade[i] - lowGrade; 
        }
        
        //获取宽度距离和1分的高度距离
        height = this.getHeight()-10;
		//如果多次成绩，则计算成绩间隔
        if(this.yearNum>1)
			widthSpace = (this.getWidth() - 80) / (this.yearNum - 1);
		else
			widthSpace = (this.getWidth() - 80) / 2;
		
        heightSpace = (int) ((height - 80)/(highGrade - lowGrade));
        
        //为每一个信息创建一个画笔
        mPaint = new Paint[yearNum];
        for(int i = 0 ; i < yearNum ; i++){
        	mPaint[i] = new Paint();
        	mPaint[i].setAntiAlias(true);
        	mPaint[i].setColor(Color.BLACK);
        	//画笔设为透明色
        	mPaint[i].setAlpha(100);
        }
        
        //设定选定学期画笔
        for(int i = 0 ; i < yearNum ; i++){
        	if(year[i].equals(term)){
        		mPaint[i].setAlpha(255);

                mPaint[i].setTextSize(15);
        		break;
        	}
        	if(i == yearNum - 1){
        		mPaint[i].setAlpha(255);

        		mPaint[i].setTextSize(15);
        	}
        		
    	}
        
        //如果只有一年的成绩，就这么干。。。
        //画上就是了。。。
        if(	yearNum == 1 ){
    		canvas.drawBitmap(scoreBox, 40+widthSpace*(1)-scoreBox.getWidth() / 2,
    				height-40-(mGrade[0]*heightSpace)-sBitmap.getHeight() / 6 -scoreBox.getHeight(),null);
    		
    		canvas.drawBitmap(sBitmap, 40+widthSpace*(1)-sBitmap.getWidth() / 2,
    				height-40-(mGrade[0]*heightSpace)-sBitmap.getHeight() / 6, null);
    		
    		canvas.drawText(""+grade[0], 40+widthSpace*(1)-15,
    				height-40-(mGrade[0]*heightSpace)+sBitmap.getHeight() / 6-scoreBox.getHeight(), mPaint[0]);
    		canvas.drawText(""+year[0], 40 + widthSpace*(1) - 17, height-40-(mGrade[0]*heightSpace)+sBitmap.getHeight() / 2, mPaint[0]);
        }
        else if ( yearNum > 1){        
	        //画图
	        for(int i = 0 ; i < yearNum ; i++){
	//        	//分数
	//        	canvas.drawText(""+grade[i], 30 + widthSpace*(i), height-35-(mGrade[i]*heightSpace), mPaint[i]);
				//如果不是第一个点就画线
	        	if( i != 0)
	        		canvas.drawLine(40+widthSpace*(i-1), height-40-(mGrade[i-1]*heightSpace) ,
	        				40+widthSpace*i, height-40-(mGrade[i]*heightSpace), nPaint);
	        }
	        
	        //记录是否为刚进入状态
	        boolean sign = true;
	        for(int i = 0 ; i < yearNum ; i++){
	        	if(year[i].equals(term)){
	        		//画各种选定学期的东西，什么圈圈，分数，气泡，还有啥么。。。
	        		canvas.drawBitmap(scoreBox, 40+widthSpace*(i)-scoreBox.getWidth() / 2,
	        				height-40-(mGrade[i]*heightSpace)-sBitmap.getHeight() / 6 -scoreBox.getHeight(),null);
	        		
	        		canvas.drawBitmap(sBitmap, 40+widthSpace*(i)-sBitmap.getWidth() / 2,
	        				height-40-(mGrade[i]*heightSpace)-sBitmap.getHeight() / 6, null);
	        		
	        		canvas.drawText(""+grade[i], 40+widthSpace*(i)-15,
	        				height-40-(mGrade[i]*heightSpace)+sBitmap.getHeight() / 6-scoreBox.getHeight(), mPaint[i]);
	        		sign = false;
	        		continue;
	        	}
	        	//如为刚进入，还没有选择学期，则自动显示最新学期的分数
	        	if(sign && i == yearNum -1 ){
	        		canvas.drawBitmap(scoreBox, 40+widthSpace*(i)-scoreBox.getWidth() / 2,
	        				height-40-(mGrade[i]*heightSpace)-sBitmap.getHeight() / 6 -scoreBox.getHeight(),null);
	        		
	        		canvas.drawBitmap(sBitmap, 40+widthSpace*(i)-sBitmap.getWidth() / 2,
	        				height-40-(mGrade[i]*heightSpace)-sBitmap.getHeight() / 6, null);
	        		
	        		canvas.drawText(""+grade[i], 40+widthSpace*(i)-15,
	        				height-40-(mGrade[i]*heightSpace)+sBitmap.getHeight() / 6-scoreBox.getHeight(), mPaint[i]);
	        		break;
	        	}
	        	canvas.drawBitmap(unsBitmap, 40+widthSpace*(i)-unsBitmap.getWidth() / 2,
	    				height-40-(mGrade[i]*heightSpace)-unsBitmap.getHeight() / 4, null);
	    		}
	
	        for(int i = 0 ; i < yearNum ; i++){
	        	//年份
	        	canvas.drawText(""+year[i], 40 + widthSpace*(i) - 17, height-40-(mGrade[i]*heightSpace)+sBitmap.getHeight() / 2, mPaint[i]);
	        }
	        
        }
        
    	invalidate();
    }  
	
	public int getRealHeight(){
		return this.getHeight();
	}
	
	//获取选中的年份
	public String getTerm(){
		for( int i = 0 ; i < yearNum ; i++ )
			if(year[i].equals(term))
				return term;
		return year[yearNum - 1];
	}
	
    // 触笔事件  
	//点击哪年，就将学期设为哪年
    public boolean onTouchEvent(MotionEvent event) {
    	//Log.i("drawview",term);
    	for(int i = 0 ; i< yearNum ; i++)
    		if(Math.abs(event.getX()-(40+widthSpace*i)) <= 20 &&
    									Math.abs(event.getY()-(height-40-(mGrade[i]*heightSpace))) <= 10){
    			term = year[i];
    		}
    	invalidate();
		return true;
    }  

	// 按键按下事件  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        return true;  
    }  
  
    // 按键弹起事件  
    public boolean onKeyUp(int keyCode, KeyEvent event) {  
        return false;  
    }  
  
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {  
        return true;  
    }  
  
    //不停刷新啊刷新啊刷新啊，
    //不刷新画面就从近来开始一直不变，随便你怎么点
    public void run() {  
        while (!Thread.currentThread().isInterrupted()) {  
            try {  
                Thread.sleep(100);  
            } catch (InterruptedException e) {  
                Thread.currentThread().interrupt();  
            }  
            // 使用postInvalidate可以直接在线程中更新界面  
            postInvalidate();  
        }  
    }
}  
