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
			//请求数据
			HttpPost httpRequest  = new HttpPost(SERVLET_URL_BIND_DEVICE_INFO);
			//创建参数  
			List<NameValuePair> params=new ArrayList<NameValuePair>(); 
			params.add(new BasicNameValuePair("employeeCode", params_obj[0]));
			params.add(new BasicNameValuePair("userId", params_obj[1]));
			params.add(new BasicNameValuePair("channelId", params_obj[2]));
			
			try
			{
				//对提交数据进行编码
				httpRequest.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
				HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
				
				Log.e(TAG, "getStatusCode = " + httpResponse.getStatusLine().getStatusCode());
				//获取响应服务器的数据
				if (httpResponse.getStatusLine().getStatusCode() == 200) 
				{
//					//利用字节数组流和包装的绑定数据
//					byte[] data =new byte[2048];
//					//先把从服务端来的数据转化成字节数组
//					data =EntityUtils.toByteArray((HttpEntity)httpResponse.getEntity());  
//					//再创建字节数组输入流对象   
//					ByteArrayInputStream bais = new ByteArrayInputStream(data);  
//					//绑定字节流和数据包装流   
//					DataInputStream dis = new DataInputStream(bais);  
//					//将字节数组中的数据还原成原来的各种数据类型，代码如下：  
//					result=new String(dis.readUTF());
					
					InputStream inputStream = httpResponse.getEntity().getContent();
					result = readInputToString(inputStream);
					Log.e("服务器返回信息:", result);
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
