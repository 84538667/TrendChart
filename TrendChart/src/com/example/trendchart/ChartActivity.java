package com.example.trendchart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ChartActivity extends Activity{

	//保存全部分数信息
	private AllInf allInf;
	//用户需要查看的学期数
	private String term;
    //成绩折线图
	private DrawView drawView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.chart_activity_layout);
		
		//实例化获取所有信息对象
		allInf = (AllInf) this.getIntent().getSerializableExtra("aInf");

		//分数信息
		float[] gradePoint = allInf.getScore();
		//学期信息
		String[] year = allInf.getTerm();
		
		//设置总学分 绩点
		setScore(allInf.getTotalScore());
		setGPA(allInf.getGPA());
		
		//设置折线图，自己画的view 下面函数均为drawview函数
		drawView = (DrawView) findViewById(R.id.draw_view);
		drawView.setGrade(gradePoint);
		drawView.setYear(year);
		drawView.setYearNum(year.length);
		drawView.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				//更换学期，刷新成绩列表
				setListViewAdapter();
				return false;
			}
		});
		
		//刷新成绩列表
		setListViewAdapter();

	}
	
	//设置GPA函数
	private void setGPA(String gpaS){
		//实例化gpa图片对象
		ImageView []s = new ImageView[3];
		s[0]= (ImageView)findViewById(R.id.gpa_1);
		s[1]= (ImageView)findViewById(R.id.gpa_2);
		s[2]= (ImageView)findViewById(R.id.gpa_3);
		//获取gpa长度
		int j = gpaS.toCharArray().length;
		//如果多余2位，则第二位为小数点 干掉它
		if( j > 2 )
			j -= 1;
		//将gpa图片修改为分数数字效果图片
		for( int i = 0 ; i < j ; i++){
			//第一位为小数点钱， 第二位为小数点  之后为小数点后数字
			//因不需要小数点那一位，所以干掉它
			if( i < 1 )	
				setScoreImage(s[i], ""+gpaS.toCharArray()[i]);
			else
				setScoreImage(s[i], ""+gpaS.toCharArray()[i+1]);
		}
		//末尾不足则补0
		for( int i = j ; i < 3 ; i++){
				setScoreImage(s[i], "0");
		}

	}
	
	//设置分数信息，基本与设置gpa相同，不多写了 逻辑一模一样
	//学分4位 gpa3位而已
	private void setScore(float f){
		//将传入的float转为String对象
		String gpaS = ""+f;
		//如果第二位为"."则加权在小数点前只有1位，则需要补一位0在前面
		if((""+gpaS.toCharArray()[1]).equals("."))
			gpaS = "0"+gpaS;
		
		//以下逻辑同gpa添加，自行查看，懒得谢了。。。
		
		ImageView []s = new ImageView[4];
		s[0]= (ImageView)findViewById(R.id.score_1);
		s[1]= (ImageView)findViewById(R.id.score_2);
		s[2]= (ImageView)findViewById(R.id.score_3);
		s[3]= (ImageView)findViewById(R.id.score_4);
		
		int j = gpaS.toCharArray().length;
		if( j > 3 )
			j -= 1;
		for( int i = 0 ; i < j ; i++){
			
			if( i < 2 )
				setScoreImage(s[i], ""+gpaS.toCharArray()[i]);
			else 
				setScoreImage(s[i], ""+gpaS.toCharArray()[i+1]);
		}
		
		for( int i = j ; i < 4 ; i++){
				setScoreImage(s[i],"0");
		}
	}
	
	//因为在gpa和学分使用的数字为图片，所以使用此函数来设置图片
	//什么数字对应什么图图片
	private void setScoreImage(ImageView iv, String s){
		//将字符转换为数字，使用switch搞定它
		int sc = Integer.parseInt(s);
		switch(sc){
			case 0:
				iv.setImageResource(R.drawable.s_0);
				break;
			case 1:
				iv.setImageResource(R.drawable.s_1);
				break;
			case 2:
				iv.setImageResource(R.drawable.s_2);
				break;
			case 3:
				iv.setImageResource(R.drawable.s_3);
				break;
			case 4:
				iv.setImageResource(R.drawable.s_4);
				break;
			case 5:
				iv.setImageResource(R.drawable.s_5);
				break;
			case 6:
				iv.setImageResource(R.drawable.s_6);
				break;
			case 7:
				iv.setImageResource(R.drawable.s_7);
				break;
			case 8:
				iv.setImageResource(R.drawable.s_8);
				break;
			case 9:
				iv.setImageResource(R.drawable.s_9);
				break;
			default:;
		}
	}
	
	public void setListViewAdapter(){
		term = drawView.getTerm();
		
		//本来是使用simpleAdapter进行listview的设置的，后来神奇的发现不能改变某一行的颜色状态
		//之后专用自定义adapter了
//		ListView lv = (ListView)findViewById(R.id.listview);
//        SimpleAdapter adapter = new SimpleAdapter(this,
//        		getData(term),
//        		R.layout.score_list,
//        		new String[]{"text_cname","text_score","text_credit"},
//        		new int[]{R.id.text_cname,R.id.text_score,R.id.text_credit});
//        lv.setAdapter(adapter);
		
		//实力换listview对象
		ListView lv = (ListView)findViewById(R.id.listview);
		//new Adapter可以根据当前term，刷新listview的列表信息
		lv.setAdapter(new MyAdapter(this,term));
	}
	
	//将scoreInf改编成为适合listview的数据对象类型
    private List<Map<String, Object>> getData(String term) {
    	
        //将要返回的listview对象
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        
        //获取信息，你懂的
        ScoreInf[] si = allInf.getScoreInf();
        
        //循环添加实力，将所有这一学期的信息添加在map中
        //之后将map加入list中，从而适应adapter的要求
        for( int j = 0 ; j < si.length ; j++ ){
        	if( si[j].getTerm().equals( term)){
        		Map<String, Object> childdata = new HashMap<String, Object>();
        		childdata.put("text_cname", si[j].getCname());
        		childdata.put("text_credit", si[j].getCredit());
        		childdata.put("text_score", si[j].getScore());
        		childdata.put("text_inf", si[j].getTextInf());
        		data.add(childdata);
        	}
        }
        
        return data;
    }
	
    //菜单项，啥都没写，不用说了吧
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}
	
	//viewholderclass，用来在下面的class中使用
	//想知道干啥用的，请见下面代码
    public final class ViewHolder{
    	public LinearLayout linear;
        public TextView cname;
        public TextView score;
        public TextView credit;
    }
     
    //myAdapter class，用来将分数数据存在listview中
    //代替了以前写的simpleAdapter，因为是自定义的，所以可以自己设置n多信息
    public class MyAdapter extends BaseAdapter{
 
    	//对象初始化
        private LayoutInflater mInflater;
        private List<Map<String, Object>> mData ;
         
        //构造函数
        public MyAdapter(Context context, String term){
            //获取上下文信息
        	this.mInflater = LayoutInflater.from(context);
            //获取数据信息
        	mData = getData(term);
        }
        
        //因为这个adapter的适配是根据数据在list中所处的位置和listview的位置一一对应来实现的
        //所以需要在这里重写这个函数，否则会有问题
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
        	return mData.size();
        }
 
        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }
 
        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }
 
        //主要的function，添加数据就靠它了！
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			//主的view，就是listview的view，需要你自己找到
			convertView = null ;
			//viewHolder对象，上面那个class就是在这用的
            ViewHolder holder = null;
            
            if (convertView == null) {
            
            	holder=new ViewHolder();  
                //实例化对象，找到viewlist
                convertView = mInflater.inflate(R.layout.score_list, null);
                //holder的对象和list对象一一对应
                holder.linear = (LinearLayout)convertView.findViewById(R.id.linear);
                holder.cname = (TextView)convertView.findViewById(R.id.text_cname);
                holder.score = (TextView)convertView.findViewById(R.id.text_score);
                holder.credit = (TextView)convertView.findViewById(R.id.text_credit);
                convertView.setTag(holder);
                
            }else {
                holder = (ViewHolder)convertView.getTag();
            }
            
            //设置数据
            holder.cname.setText((String)mData.get(position).get("text_cname"));
            holder.score.setText((String)mData.get(position).get("text_score"));
            holder.credit.setText((String)mData.get(position).get("text_credit"));
            //根据各种乱七八糟的状态设定字体颜色
            //挂科
            if(Integer.parseInt(mData.get(position).get("text_score").toString()) < 60){
        	    holder.cname.setTextColor(Color.parseColor("#d30000"));
        	    holder.score.setTextColor(Color.parseColor("#d30000"));
        	   	holder.credit.setTextColor(Color.parseColor("#d30000"));
            }
            //重修
            else if(Integer.parseInt(mData.get(position).get("text_inf").toString()) == 1){
        	    holder.cname.setTextColor(Color.parseColor("#009d12"));
        	    holder.score.setTextColor(Color.parseColor("#009d12"));
        	    holder.credit.setTextColor(Color.parseColor("#009d12"));
        	    //holder.linear.setBackgroundColor( Color.parseColor("#00ffff") );
            }
            //双学位
            //不区分双学位和双学位重修，否则显得乱
            else if(Integer.parseInt(mData.get(position).get("text_inf").toString()) > 1){
        	    //holder.linear.setBackgroundColor( Color.parseColor("#00ffff") );
        	    holder.cname.setTextColor(Color.parseColor("#848484"));
        	    holder.score.setTextColor(Color.parseColor("#848484"));
        	    holder.credit.setTextColor(Color.parseColor("#848484"));
            }
            return convertView;
		}
         
    }
}

