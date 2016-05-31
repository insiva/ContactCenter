package com.matteo.cc.utils.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpGetUtils {
	private URL mUrl;
	private HttpURLConnection mConnection;
	private String mResult;

	public HttpGetUtils(String url) {
		try {
			this.mUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public String fetch() {
		try {
			this.mConnection = (HttpURLConnection) this.mUrl.openConnection();
			InputStreamReader in = new InputStreamReader(
					this.mConnection.getInputStream());
			this.readStream(in);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
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
