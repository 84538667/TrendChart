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
	
	//构造各种对象，
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
		
		//实例化各种控件
		Button loginButton = (Button)findViewById(R.id.submit);
		Button delPwdButton = (Button)findViewById(R.id.del_password);
		
		progBar = (ProgressBar)findViewById(R.id.prog_bar);
		progBar.setVisibility(View.INVISIBLE);
		
		storePassword = (CheckBox)findViewById(R.id.store_password);
		
		userEditText = (EditText)findViewById(R.id.userEditText);
		//将最近一次使用的账号保存
		userEditText.setText(getUsername(ScoreInfLoginActivity.this));
		
		pwdEditText = (EditText)findViewById(R.id.pwdEditText);
		//如果账号有密码保存，直接添上就行了
		pwdEditText.setText(getPassword(ScoreInfLoginActivity.this, userEditText.getText().toString()));
		
		//登录按钮点击事件
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
							//获取题目信息
							allInf = GetAllInf.getAllInf(username, pwd);
							//告诉handler一声
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
		
		//为了自动填充保存的密码
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
                //当输入数字满10位时，大概学号都是10位吧。。不是我就见识短浅了
                //不是10位请自行输入密码吧。。。。。
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
		
		//这个就是用来判断用户是不是想要保存密码的。。。
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
		
		//删除当前账号已保存的密码
		delPwdButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				username = userEditText.getText().toString();
				//将保存的密码置空
				updatePwd(ScoreInfLoginActivity.this, username, null);
			}
			
		});
	}
	
	//上面告诉你获取过成绩信息了，你就该干活了
	private Handler loginHandler = new Handler(){
		public void handleMessage(Message message){
			if(allInf != null){
				//保存密码
				updateSharedUsername(ScoreInfLoginActivity.this, username);
				if(isStore)
					updatePwd(ScoreInfLoginActivity.this, username, pwd);
				
				//intent跳转activity顺便把分数给它传过去
				Intent intent=new Intent();
				intent.setClass(ScoreInfLoginActivity.this, ChartActivity.class);
				intent.putExtra("aInf",allInf);
				startActivity(intent);
				progBar.setVisibility(View.INVISIBLE);
				//这个就是个简单的滑动翻页效果，，不加一样的。
				overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left); 
			}
			else{
				//帐号密码错了就提醒一下
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
	
	//更新账号，就一句话，不用多说了吧，保存在sharedpreference中，简单快捷
	public void updateSharedUsername(Context context , String username){
		SharedPreferences sharedInf = 
				context.getSharedPreferences(ScoreListConst.TWT_USERNAME,
						 Context.MODE_PRIVATE);
        sharedInf.edit().putString("username", username).commit();
        
	}

	//更新密码，就一句话，不用多说了吧，保存在sharedpreference中，简单快捷
	public void updatePwd(Context context, String username, String pwd){
		SharedPreferences sharedInf = 
				context.getSharedPreferences(username,
						 Context.MODE_PRIVATE);
        sharedInf.edit().putString("pwd", pwd).commit();
	}
	

	//获取用户名，搞到最后一次登录的用户名
	public  String getUsername( Context context ){
		SharedPreferences sharedInf = 
				context.getSharedPreferences(ScoreListConst.TWT_USERNAME,
						 Context.MODE_PRIVATE);
        String username = sharedInf.getString( "username", "");  
        
		return username;
	}
	
	//获取你输入账号的密码，大功告成，谢了1个小时的注释。。。
	public  String getPassword( Context context,String username){
		SharedPreferences sharedInf = 
				context.getSharedPreferences(username,
						 Context.MODE_PRIVATE);
        String pwd = sharedInf.getString( "pwd", "");  
        
		return pwd;
	}
}
