package com.dooioo.androidservlet;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity
{
	
	public static final String TAG ="MainActivity";
	public static final String SERVLET_URL_BIND_DEVICE_INFO ="http://myclients.duapp.com/mysqlbind/deviceinfo";
	private ProgressDialog progressDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		progressDialog = new ProgressDialog(this);
		
		new AT().execute("93468", "617943248495163973", "3688022906336843531");
	}
	
	class AT extends AsyncTask<String, Object, Object>
	{
		
		String result = "";
		
		@Override
		protected void onPreExecute()
		{
			progressDialog.show();
		}
		
		@Override
		protected Object doInBackground(String... params_obj) 
		{
			Log.e(TAG, "params_obj[0] = " + params_obj[0]);
			Log.e(TAG, "params_obj[1] = " + params_obj[1]);
			Log.e(TAG, "params_obj[2] = " + params_obj[2]);
			//��������
			HttpPost httpRequest  = new HttpPost(SERVLET_URL_BIND_DEVICE_INFO);
			//��������  
			List<NameValuePair> params=new ArrayList<NameValuePair>(); 
			params.add(new BasicNameValuePair("employeeCode", params_obj[0]));
			params.add(new BasicNameValuePair("userId", params_obj[1]));
			params.add(new BasicNameValuePair("channelId", params_obj[2]));
			
			try
			{
				//���ύ���ݽ��б���
				httpRequest.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
				HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
				
				Log.e(TAG, "getStatusCode = " + httpResponse.getStatusLine().getStatusCode());
				//��ȡ��Ӧ������������
				if (httpResponse.getStatusLine().getStatusCode() == 200) 
				{
//					//�����ֽ��������Ͱ�װ�İ�����
//					byte[] data =new byte[2048];
//					//�ȰѴӷ������������ת�����ֽ�����
//					data =EntityUtils.toByteArray((HttpEntity)httpResponse.getEntity());  
//					//�ٴ����ֽ���������������   
//					ByteArrayInputStream bais = new ByteArrayInputStream(data);  
//					//���ֽ��������ݰ�װ��   
//					DataInputStream dis = new DataInputStream(bais);  
//					//���ֽ������е����ݻ�ԭ��ԭ���ĸ����������ͣ��������£�  
//					result=new String(dis.readUTF());
					
					InputStream inputStream = httpResponse.getEntity().getContent();
					result = readInputToString(inputStream);
					Log.e("������������Ϣ:", result);
				}
			} 
			catch(Exception e)
			{  
				e.printStackTrace();  
			} 
			
			return result;
		}
		
		@Override
		protected void onPostExecute(Object result) 
		{
			progressDialog.cancel();
		}
		
	} 
	
	public static String readInputToString(InputStream is) throws Exception
	{
		BufferedReader bd = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		char[] cbuf = new char[1024];
		int ch = -1;
		int _count = 0;
		while ((ch = bd.read(cbuf)) >= 0)
		{
			sb.append(cbuf, 0, ch);
			_count += ch;
			Log.e(TAG, "--> downloading _count = " + _count);
		}
		bd.close();
		return sb.toString();
	}
}
