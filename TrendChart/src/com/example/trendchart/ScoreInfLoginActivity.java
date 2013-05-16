package com.example.trendchart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ScoreInfLoginActivity extends Activity
						implements OnGestureListener, OnTouchListener{
	
	//������ֶ���
	EditText userEditText;
	EditText pwdEditText;
	String username;
	String pwd;
	CheckBox storePassword;
	ProgressBar progBar;
	private AllInf allInf;
	boolean isStore = false;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		setContentView(R.layout.score_inf_login_layout);
		
		//ʵ�������ֿؼ�
		Button loginButton = (Button)findViewById(R.id.submit);
		Button delPwdButton = (Button)findViewById(R.id.del_password);
		
		progBar = (ProgressBar)findViewById(R.id.prog_bar);
		progBar.setVisibility(View.INVISIBLE);
		
		storePassword = (CheckBox)findViewById(R.id.store_password);
		
		userEditText = (EditText)findViewById(R.id.userEditText);
		//�����һ��ʹ�õ��˺ű���
		userEditText.setText(getUsername(ScoreInfLoginActivity.this));
		
		pwdEditText = (EditText)findViewById(R.id.pwdEditText);
		//����˺������뱣�棬ֱ�����Ͼ�����
		pwdEditText.setText(getPassword(ScoreInfLoginActivity.this, userEditText.getText().toString()));
		
		//��¼��ť����¼�
		loginButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				progBar.setVisibility(View.VISIBLE);
				ConnectivityManager conM = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo info = conM.getActiveNetworkInfo();
				if( info != null && info.isAvailable() ){
					username = userEditText.getText().toString();
					pwd =  pwdEditText.getText().toString();
					
					new Thread(new Runnable() {
						@Override
						public void run() {
							//��ȡ��Ŀ��Ϣ
							allInf = GetAllInf.getAllInf(username, pwd);
							//����handlerһ��
							loginHandler.sendMessage(new Message());
						}
					}).start();
				}
				else{
					progBar.setVisibility(View.INVISIBLE);
					Toast.makeText(ScoreInfLoginActivity.this, ScoreListConst.TWT_CONNECT_UNABLE, 500).show();
				}
				
			}
		});
		
		//Ϊ���Զ���䱣�������
		userEditText.addTextChangedListener(new TextWatcher(){
            private CharSequence temp;              
            private int selectionStart ;              
            private int selectionEnd ;
            
            //textChange listener
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				selectionStart = userEditText.getSelectionStart();
                selectionEnd = userEditText.getSelectionEnd();
                //������������10λʱ�����ѧ�Ŷ���10λ�ɡ��������Ҿͼ�ʶ��ǳ��
                //����10λ��������������ɡ���������
                if (temp.length() == 10){
                    pwdEditText.setText(getPassword(ScoreInfLoginActivity.this, userEditText.getText().toString()));
                } 
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				temp = s;
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}
		});
		
		//������������ж��û��ǲ�����Ҫ��������ġ�����
		storePassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					isStore = true;
				} else {
					isStore = false;
				}
			}
		});
		
		//ɾ����ǰ�˺��ѱ��������
		delPwdButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				username = userEditText.getText().toString();
				//������������ÿ�
				updatePwd(ScoreInfLoginActivity.this, username, null);
			}
			
		});
	}
	
	//����������ȡ���ɼ���Ϣ�ˣ���͸øɻ���
	private Handler loginHandler = new Handler(){
		public void handleMessage(Message message){
			if(allInf != null){
				//��������
				updateSharedUsername(ScoreInfLoginActivity.this, username);
				if(isStore)
					updatePwd(ScoreInfLoginActivity.this, username, pwd);
				
				//intent��תactivity˳��ѷ�����������ȥ
				Intent intent=new Intent();
				intent.setClass(ScoreInfLoginActivity.this, ChartActivity.class);
				intent.putExtra("aInf",allInf);
				startActivity(intent);
				progBar.setVisibility(View.INVISIBLE);
				//������Ǹ��򵥵Ļ�����ҳЧ����������һ���ġ�
				overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left); 
			}
			else{
				//�ʺ�������˾�����һ��
				Toast.makeText(ScoreInfLoginActivity.this, ScoreListConst.TWT_TJU_LOGIN_ERROR, 500).show();
			}
		}
	};
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	
	//�����˺ţ���һ�仰�����ö�˵�˰ɣ�������sharedpreference�У��򵥿��
	public void updateSharedUsername(Context context , String username){
		SharedPreferences sharedInf = 
				context.getSharedPreferences(ScoreListConst.TWT_USERNAME,
						 Context.MODE_PRIVATE);
        sharedInf.edit().putString("username", username).commit();
        
	}

	//�������룬��һ�仰�����ö�˵�˰ɣ�������sharedpreference�У��򵥿��
	public void updatePwd(Context context, String username, String pwd){
		SharedPreferences sharedInf = 
				context.getSharedPreferences(username,
						 Context.MODE_PRIVATE);
        sharedInf.edit().putString("pwd", pwd).commit();
	}
	

	//��ȡ�û������㵽���һ�ε�¼���û���
	public  String getUsername( Context context ){
		SharedPreferences sharedInf = 
				context.getSharedPreferences(ScoreListConst.TWT_USERNAME,
						 Context.MODE_PRIVATE);
        String username = sharedInf.getString( "username", "");  
        
		return username;
	}
	
	//��ȡ�������˺ŵ����룬�󹦸�ɣ�л��1��Сʱ��ע�͡�����
	public  String getPassword( Context context,String username){
		SharedPreferences sharedInf = 
				context.getSharedPreferences(username,
						 Context.MODE_PRIVATE);
        String pwd = sharedInf.getString( "pwd", "");  
        
		return pwd;
	}
}
