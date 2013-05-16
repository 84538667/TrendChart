package com.example.trendchart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


public class GetAllInf {
	//获取全部分数，静态函数
	public static AllInf getAllInf( String username, String pwd){
		AllInf allInf = null;
		
		//准备中，初始化arraylist，这个用来连接时传值
		ArrayList<NameValuePair> userpair = new ArrayList<NameValuePair>(2);  
     	userpair.add(new BasicNameValuePair(ScoreListConst.TWT_USERNAME,username));  
     	userpair.add(new BasicNameValuePair(ScoreListConst.TWT_PASSWORD,pwd));
     	
     	//初始化输入流
     	InputStream content = null;
     	try {
     		//建立http连接，将有的没的传过去
     		HttpClient client = new DefaultHttpClient();
	     	HttpPost post = new HttpPost(ScoreListConst.TWT_SCOREINF_URL);
	     	post.setEntity(new UrlEncodedFormEntity(userpair));
			HttpResponse response = client.execute(post);
			
			//解析相应得到响应中的主体
			HttpEntity entity = response.getEntity();  
 	        content = entity.getContent();
		} catch (Exception e) {
			//如果抛错，输出错误信息到日志
			Log.e(ScoreListConst.TWT_GET_INF_ERROR,"Connect to server error!"+e.toString());
			//直接登陆失败
			return null;
		}
     	
     	//将InputReader中的内容转化为string
     	StringBuilder b = null;
		try {
			BufferedReader reader = new BufferedReader( new InputStreamReader(content,"UTF-8"), 8);
			b = new StringBuilder();
	     	String tmp = null;
	     	try {
				while((tmp = reader.readLine()) != null){
					b.append(tmp);
				}
				
	     	} catch (UnsupportedEncodingException e1) {
			
	     		e1.printStackTrace();
	     	}
		} catch (IOException e) {
			//如果抛错，输出错误信息到日志
			Log.e(ScoreListConst.TWT_CONVERT_ERROR,"Convert InpterStream to String error!");
		}
		
		Log.i("b",b.toString());
		
		if(b.toString().equals(ScoreListConst.TWT_GET_SCORE_ERROR_STRING))
     		return null;
		
     	//处理请求得到的数据
		//这段请自行用浏览器打开连接网站，对照传送回来的json
		try {
			
			//新建json数组
			JSONArray arr = new JSONArray(b.toString());
			int courseNum = 0;
			//学期信息
			String[] term = new String[arr.length()-1];
			//这个目的好像，是为了得到一共几个学期。。。，
			//顺便数一下课程数量
			for(int i = 0 ; i < arr.length() -1 ; i ++){
				JSONArray scoreArr = (JSONArray)arr.get(i);
				courseNum += scoreArr.length();
				JSONObject jsonObject= (JSONObject)scoreArr.get(0);
				term[i] = jsonObject.getString("term");
			}
			//全部分数信息
			ScoreInf []si = new ScoreInf[courseNum];
			
			//课程数量吧，以前锁得到过的数量
			courseNum = 0;
			
			//这里是按照学期搞一遍解析
			//将分数信息都给搞出来
			for(int j = 0 ; j < arr.length() -1 ; j ++){
				JSONArray scoreArr = (JSONArray)arr.get(j);
				
				for(int i = 0 ; i < scoreArr.length() ; i++){
			
					JSONObject jsonObject= (JSONObject)scoreArr.get(i);
					
					String _term = jsonObject.getString("term");
					String _cname = jsonObject.getString("name");
					String _credit = jsonObject.getString("credit");
					String _score = jsonObject.getString("score");
					int type = jsonObject.getInt("type");
					int retest = jsonObject.getInt("retest");
					int _inf = 0;
					//重修标志
					if(retest == 1)
						_inf += 1;
					//双学位标志
					if(type == 1)
						_inf += 2;
				
					si[i+courseNum] = new ScoreInf(_term, _cname, _credit, _score, _inf);
				}
				courseNum += scoreArr.length();
			}
			JSONObject otherInf = (JSONObject)arr.get(arr.length()-1);
			
			String _gpa = otherInf.getString("gpa");
			float _totalScore = (float) otherInf.getDouble("totalGpa");
			JSONArray eScore = (JSONArray)otherInf.get("every");
			float[] _score = new float[eScore.length()];
			for( int i = 0 ; i < eScore.length() ; i++){
				_score[i] = (float) eScore.getDouble(i);
			}
			//构造对象，返回就行了
			allInf = new AllInf( _gpa, _score, si, _totalScore, term);
			
		} catch (JSONException e) {
			//日志输出转换错误
			Log.e(ScoreListConst.TWT_CONVERT_ERROR, "Convert Json to String error");
		} 
        
		//返回一些乱七八糟的东西
		return allInf;
		
	}
}
