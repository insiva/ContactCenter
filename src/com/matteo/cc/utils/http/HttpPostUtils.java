package com.matteo.cc.utils.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import android.text.TextUtils;

public class HttpPostUtils {
	public static final String ENCODE="utf-8";
	
	private URL mUrl;
	private String mContent;
	private HttpURLConnection mConnection;
	private String mResult;
	
	public HttpPostUtils(String url,String content){
		try {
			this.mUrl=new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		this.mContent=content;
	}
	
	public String fetch(){
		try {
			this.mConnection=(HttpURLConnection) this.mUrl.openConnection();
			this.mConnection.setDoInput(true);
			this.mConnection.setDoOutput(true);
			this.mConnection.setRequestMethod("POST");
			this.mConnection.setUseCaches(false);
			this.mConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			this.mConnection.setRequestProperty("Charset", ENCODE);
			this.mConnection.connect();
			if(!TextUtils.isEmpty(this.mContent)){
				DataOutputStream dop=new DataOutputStream(this.mConnection.getOutputStream());
				dop.writeBytes(URLEncoder.encode(this.mContent, ENCODE));
				dop.flush();
				dop.close();
			}
			InputStreamReader in = new InputStreamReader(
					this.mConnection.getInputStream());
			this.readStream(in);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			this.mConnection.disconnect();
		}
		return this.mResult;
	}
	
	private void readStream(InputStreamReader in) throws IOException{
		String readLine=null;
		BufferedReader reader=new BufferedReader(in);
		StringBuilder sb=new StringBuilder();
		while((readLine=reader.readLine())!=null){
			sb.append(readLine);
		}
		this.mResult=sb.toString();
	}
}
