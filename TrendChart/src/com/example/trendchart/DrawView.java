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
        /* �������� */  
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
        /* ����Paint���� */  
        Paint nPaint = new Paint();
        
        /* ����ȡ�����Ч�� */  
        nPaint.setAntiAlias(true);
        //��ɫ
        nPaint.setColor(0xfff4782f);
        //����������ϸ
        nPaint.setStrokeWidth(10);
        //������ʽ
        nPaint.setStyle(Paint.Style.STROKE);
        /* ���û�������ɫ */  
        canvas.drawColor(0x00000000);
        
        /* ���������� */  
        canvas.save();
        
        //��������Ҫ�õļ���ͼƬ���������
        Bitmap unsBitmap = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.chart_unselected));  
        Bitmap sBitmap = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.chart_selected));

        Bitmap scoreBox = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.score_bg));
        
        mGrade = new float[grade.length];

        //��߷ֺ���ͷ֣���ʼ��
        float lowGrade = 100;
        float highGrade = 0;
        
        //��ȡ���м�Ȩ�ɼ��е���߷ֺ���ͷ�
        for(int i = 0 ; i < grade.length ; i++){
        	if(grade[i] < lowGrade)
        		lowGrade = grade[i];
        	if(grade[i] > highGrade)
        		highGrade = grade[i];
        }
        
        //�����Է���������-��ͷ֣����㻭ͼ
        for(int i = 0 ; i < mGrade.length ; i++){
        	mGrade[i] = grade[i] - lowGrade; 
        }
        
        //��ȡ��Ⱦ����1�ֵĸ߶Ⱦ���
        height = this.getHeight()-10;
		//�����γɼ��������ɼ����
        if(this.yearNum>1)
			widthSpace = (this.getWidth() - 80) / (this.yearNum - 1);
		else
			widthSpace = (this.getWidth() - 80) / 2;
		
        heightSpace = (int) ((height - 80)/(highGrade - lowGrade));
        
        //Ϊÿһ����Ϣ����һ������
        mPaint = new Paint[yearNum];
        for(int i = 0 ; i < yearNum ; i++){
        	mPaint[i] = new Paint();
        	mPaint[i].setAntiAlias(true);
        	mPaint[i].setColor(Color.BLACK);
        	//������Ϊ͸��ɫ
        	mPaint[i].setAlpha(100);
        }
        
        //�趨ѡ��ѧ�ڻ���
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
        
        //���ֻ��һ��ĳɼ�������ô�ɡ�����
        //���Ͼ����ˡ�����
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
	        //��ͼ
	        for(int i = 0 ; i < yearNum ; i++){
	//        	//����
	//        	canvas.drawText(""+grade[i], 30 + widthSpace*(i), height-35-(mGrade[i]*heightSpace), mPaint[i]);
				//������ǵ�һ����ͻ���
	        	if( i != 0)
	        		canvas.drawLine(40+widthSpace*(i-1), height-40-(mGrade[i-1]*heightSpace) ,
	        				40+widthSpace*i, height-40-(mGrade[i]*heightSpace), nPaint);
	        }
	        
	        //��¼�Ƿ�Ϊ�ս���״̬
	        boolean sign = true;
	        for(int i = 0 ; i < yearNum ; i++){
	        	if(year[i].equals(term)){
	        		//������ѡ��ѧ�ڵĶ�����ʲôȦȦ�����������ݣ�����ɶô������
	        		canvas.drawBitmap(scoreBox, 40+widthSpace*(i)-scoreBox.getWidth() / 2,
	        				height-40-(mGrade[i]*heightSpace)-sBitmap.getHeight() / 6 -scoreBox.getHeight(),null);
	        		
	        		canvas.drawBitmap(sBitmap, 40+widthSpace*(i)-sBitmap.getWidth() / 2,
	        				height-40-(mGrade[i]*heightSpace)-sBitmap.getHeight() / 6, null);
	        		
	        		canvas.drawText(""+grade[i], 40+widthSpace*(i)-15,
	        				height-40-(mGrade[i]*heightSpace)+sBitmap.getHeight() / 6-scoreBox.getHeight(), mPaint[i]);
	        		sign = false;
	        		continue;
	        	}
	        	//��Ϊ�ս��룬��û��ѡ��ѧ�ڣ����Զ���ʾ����ѧ�ڵķ���
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
	        	//���
	        	canvas.drawText(""+year[i], 40 + widthSpace*(i) - 17, height-40-(mGrade[i]*heightSpace)+sBitmap.getHeight() / 2, mPaint[i]);
	        }
	        
        }
        
    	invalidate();
    }  
	
	public int getRealHeight(){
		return this.getHeight();
	}
	
	//��ȡѡ�е����
	public String getTerm(){
		for( int i = 0 ; i < yearNum ; i++ )
			if(year[i].equals(term))
				return term;
		return year[yearNum - 1];
	}
	
    // �����¼�  
	//������꣬�ͽ�ѧ����Ϊ����
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

	// ���������¼�  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        return true;  
    }  
  
    // ���������¼�  
    public boolean onKeyUp(int keyCode, KeyEvent event) {  
        return false;  
    }  
  
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {  
        return true;  
    }  
  
    //��ͣˢ�°�ˢ�°�ˢ�°���
    //��ˢ�»���ʹӽ�����ʼһֱ���䣬�������ô��
    public void run() {  
        while (!Thread.currentThread().isInterrupted()) {  
            try {  
                Thread.sleep(100);  
            } catch (InterruptedException e) {  
                Thread.currentThread().interrupt();  
            }  
            // ʹ��postInvalidate����ֱ�����߳��и��½���  
            postInvalidate();  
        }  
    }
}  
