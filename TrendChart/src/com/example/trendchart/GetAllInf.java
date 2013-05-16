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
	//��ȡȫ����������̬����
	public static AllInf getAllInf( String username, String pwd){
		AllInf allInf = null;
		
		//׼���У���ʼ��arraylist�������������ʱ��ֵ
		ArrayList<NameValuePair> userpair = new ArrayList<NameValuePair>(2);  
     	userpair.add(new BasicNameValuePair(ScoreListConst.TWT_USERNAME,username));  
     	userpair.add(new BasicNameValuePair(ScoreListConst.TWT_PASSWORD,pwd));
     	
     	//��ʼ��������
     	InputStream content = null;
     	try {
     		//����http���ӣ����е�û�Ĵ���ȥ
     		HttpClient client = new DefaultHttpClient();
	     	HttpPost post = new HttpPost(ScoreListConst.TWT_SCOREINF_URL);
	     	post.setEntity(new UrlEncodedFormEntity(userpair));
			HttpResponse response = client.execute(post);
			
			//������Ӧ�õ���Ӧ�е�����
			HttpEntity entity = response.getEntity();  
 	        content = entity.getContent();
		} catch (Exception e) {
			//����״����������Ϣ����־
			Log.e(ScoreListConst.TWT_GET_INF_ERROR,"Connect to server error!"+e.toString());
			//ֱ�ӵ�½ʧ��
			return null;
		}
     	
     	//��InputReader�е�����ת��Ϊstring
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
			//����״����������Ϣ����־
			Log.e(ScoreListConst.TWT_CONVERT_ERROR,"Convert InpterStream to String error!");
		}
		
		Log.i("b",b.toString());
		
		if(b.toString().equals(ScoreListConst.TWT_GET_SCORE_ERROR_STRING))
     		return null;
		
     	//��������õ�������
		//������������������������վ�����մ��ͻ�����json
		try {
			
			//�½�json����
			JSONArray arr = new JSONArray(b.toString());
			int courseNum = 0;
			//ѧ����Ϣ
			String[] term = new String[arr.length()-1];
			//���Ŀ�ĺ�����Ϊ�˵õ�һ������ѧ�ڡ�������
			//˳����һ�¿γ�����
			for(int i = 0 ; i < arr.length() -1 ; i ++){
				JSONArray scoreArr = (JSONArray)arr.get(i);
				courseNum += scoreArr.length();
				JSONObject jsonObject= (JSONObject)scoreArr.get(0);
				term[i] = jsonObject.getString("term");
			}
			//ȫ��������Ϣ
			ScoreInf []si = new ScoreInf[courseNum];
			
			//�γ������ɣ���ǰ���õ���������
			courseNum = 0;
			
			//�����ǰ���ѧ�ڸ�һ�����
			//��������Ϣ���������
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
					//���ޱ�־
					if(retest == 1)
						_inf += 1;
					//˫ѧλ��־
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
			//������󣬷��ؾ�����
			allInf = new AllInf( _gpa, _score, si, _totalScore, term);
			
		} catch (JSONException e) {
			//��־���ת������
			Log.e(ScoreListConst.TWT_CONVERT_ERROR, "Convert Json to String error");
		} 
        
		//����һЩ���߰���Ķ���
		return allInf;
		
	}
}
