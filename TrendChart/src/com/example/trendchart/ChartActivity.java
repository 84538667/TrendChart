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

	//����ȫ��������Ϣ
	private AllInf allInf;
	//�û���Ҫ�鿴��ѧ����
	private String term;
    //�ɼ�����ͼ
	private DrawView drawView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.chart_activity_layout);
		
		//ʵ������ȡ������Ϣ����
		allInf = (AllInf) this.getIntent().getSerializableExtra("aInf");

		//������Ϣ
		float[] gradePoint = allInf.getScore();
		//ѧ����Ϣ
		String[] year = allInf.getTerm();
		
		//������ѧ�� ����
		setScore(allInf.getTotalScore());
		setGPA(allInf.getGPA());
		
		//��������ͼ���Լ�����view ���溯����Ϊdrawview����
		drawView = (DrawView) findViewById(R.id.draw_view);
		drawView.setGrade(gradePoint);
		drawView.setYear(year);
		drawView.setYearNum(year.length);
		drawView.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				//����ѧ�ڣ�ˢ�³ɼ��б�
				setListViewAdapter();
				return false;
			}
		});
		
		//ˢ�³ɼ��б�
		setListViewAdapter();

	}
	
	//����GPA����
	private void setGPA(String gpaS){
		//ʵ����gpaͼƬ����
		ImageView []s = new ImageView[3];
		s[0]= (ImageView)findViewById(R.id.gpa_1);
		s[1]= (ImageView)findViewById(R.id.gpa_2);
		s[2]= (ImageView)findViewById(R.id.gpa_3);
		//��ȡgpa����
		int j = gpaS.toCharArray().length;
		//�������2λ����ڶ�λΪС���� �ɵ���
		if( j > 2 )
			j -= 1;
		//��gpaͼƬ�޸�Ϊ��������Ч��ͼƬ
		for( int i = 0 ; i < j ; i++){
			//��һλΪС����Ǯ�� �ڶ�λΪС����  ֮��ΪС���������
			//����ҪС������һλ�����Ըɵ���
			if( i < 1 )	
				setScoreImage(s[i], ""+gpaS.toCharArray()[i]);
			else
				setScoreImage(s[i], ""+gpaS.toCharArray()[i+1]);
		}
		//ĩβ������0
		for( int i = j ; i < 3 ; i++){
				setScoreImage(s[i], "0");
		}

	}
	
	//���÷�����Ϣ������������gpa��ͬ������д�� �߼�һģһ��
	//ѧ��4λ gpa3λ����
	private void setScore(float f){
		//�������floatתΪString����
		String gpaS = ""+f;
		//����ڶ�λΪ"."���Ȩ��С����ǰֻ��1λ������Ҫ��һλ0��ǰ��
		if((""+gpaS.toCharArray()[1]).equals("."))
			gpaS = "0"+gpaS;
		
		//�����߼�ͬgpa��ӣ����в鿴������л�ˡ�����
		
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
	
	//��Ϊ��gpa��ѧ��ʹ�õ�����ΪͼƬ������ʹ�ô˺���������ͼƬ
	//ʲô���ֶ�ӦʲôͼͼƬ
	private void setScoreImage(ImageView iv, String s){
		//���ַ�ת��Ϊ���֣�ʹ��switch�㶨��
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
		
		//������ʹ��simpleAdapter����listview�����õģ���������ķ��ֲ��ܸı�ĳһ�е���ɫ״̬
		//֮��ר���Զ���adapter��
//		ListView lv = (ListView)findViewById(R.id.listview);
//        SimpleAdapter adapter = new SimpleAdapter(this,
//        		getData(term),
//        		R.layout.score_list,
//        		new String[]{"text_cname","text_score","text_credit"},
//        		new int[]{R.id.text_cname,R.id.text_score,R.id.text_credit});
//        lv.setAdapter(adapter);
		
		//ʵ����listview����
		ListView lv = (ListView)findViewById(R.id.listview);
		//new Adapter���Ը��ݵ�ǰterm��ˢ��listview���б���Ϣ
		lv.setAdapter(new MyAdapter(this,term));
	}
	
	//��scoreInf�ı��Ϊ�ʺ�listview�����ݶ�������
    private List<Map<String, Object>> getData(String term) {
    	
        //��Ҫ���ص�listview����
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        
        //��ȡ��Ϣ���㶮��
        ScoreInf[] si = allInf.getScoreInf();
        
        //ѭ�����ʵ������������һѧ�ڵ���Ϣ�����map��
        //֮��map����list�У��Ӷ���Ӧadapter��Ҫ��
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
	
    //�˵��ɶ��ûд������˵�˰�
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}
	
	//viewholderclass�������������class��ʹ��
	//��֪����ɶ�õģ�����������
    public final class ViewHolder{
    	public LinearLayout linear;
        public TextView cname;
        public TextView score;
        public TextView credit;
    }
     
    //myAdapter class���������������ݴ���listview��
    //��������ǰд��simpleAdapter����Ϊ���Զ���ģ����Կ����Լ�����n����Ϣ
    public class MyAdapter extends BaseAdapter{
 
    	//�����ʼ��
        private LayoutInflater mInflater;
        private List<Map<String, Object>> mData ;
         
        //���캯��
        public MyAdapter(Context context, String term){
            //��ȡ��������Ϣ
        	this.mInflater = LayoutInflater.from(context);
            //��ȡ������Ϣ
        	mData = getData(term);
        }
        
        //��Ϊ���adapter�������Ǹ���������list��������λ�ú�listview��λ��һһ��Ӧ��ʵ�ֵ�
        //������Ҫ��������д��������������������
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
 
        //��Ҫ��function��������ݾͿ����ˣ�
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			//����view������listview��view����Ҫ���Լ��ҵ�
			convertView = null ;
			//viewHolder���������Ǹ�class���������õ�
            ViewHolder holder = null;
            
            if (convertView == null) {
            
            	holder=new ViewHolder();  
                //ʵ���������ҵ�viewlist
                convertView = mInflater.inflate(R.layout.score_list, null);
                //holder�Ķ����list����һһ��Ӧ
                holder.linear = (LinearLayout)convertView.findViewById(R.id.linear);
                holder.cname = (TextView)convertView.findViewById(R.id.text_cname);
                holder.score = (TextView)convertView.findViewById(R.id.text_score);
                holder.credit = (TextView)convertView.findViewById(R.id.text_credit);
                convertView.setTag(holder);
                
            }else {
                holder = (ViewHolder)convertView.getTag();
            }
            
            //��������
            holder.cname.setText((String)mData.get(position).get("text_cname"));
            holder.score.setText((String)mData.get(position).get("text_score"));
            holder.credit.setText((String)mData.get(position).get("text_credit"));
            //���ݸ������߰����״̬�趨������ɫ
            //�ҿ�
            if(Integer.parseInt(mData.get(position).get("text_score").toString()) < 60){
        	    holder.cname.setTextColor(Color.parseColor("#d30000"));
        	    holder.score.setTextColor(Color.parseColor("#d30000"));
        	   	holder.credit.setTextColor(Color.parseColor("#d30000"));
            }
            //����
            else if(Integer.parseInt(mData.get(position).get("text_inf").toString()) == 1){
        	    holder.cname.setTextColor(Color.parseColor("#009d12"));
        	    holder.score.setTextColor(Color.parseColor("#009d12"));
        	    holder.credit.setTextColor(Color.parseColor("#009d12"));
        	    //holder.linear.setBackgroundColor( Color.parseColor("#00ffff") );
            }
            //˫ѧλ
            //������˫ѧλ��˫ѧλ���ޣ������Ե���
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

