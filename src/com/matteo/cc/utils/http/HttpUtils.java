package com.matteo.cc.utils.http;

public class HttpUtils {
	private static HttpUtils mInstance=null;
	
	private HttpUtils(){
		
	}
	
	public String httpGet(String url){
		HttpGetUtils utils=new HttpGetUtils(url);
		return utils.fetch();
	}
	
	public String httpPost(String url,String content){
		HttpPostUtils utils=new HttpPostUtils(url, content);
		return utils.fetch();
	}
	
	private static HttpUtils getInstance(){
		if(mInstance==null){
			HttpUtils.init();
		}
		return mInstance;
	}
	
	private static synchronized void init() {
		if(mInstance==null){
			mInstance=new HttpUtils();
		}
	}
	
	public static String get(String url){
		return HttpUtils.getInstance().httpGet(url);
	}
	
	public static String post(String url,String content){
		return null;
	}
}
