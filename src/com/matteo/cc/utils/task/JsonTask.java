package com.matteo.cc.utils.task;

import org.json.JSONException;
import org.json.JSONObject;

import com.matteo.cc.utils.http.HttpUtils;

public class JsonTask extends AbsAsyncTask{
	public static final String JSON_TAG_STATUS="status";
	public static final String JSON_TAG_DATA="data";
	public static final String JSON_TAG_INFO="info";
	public static final int STATUS_FAILED=-1;
	
	public enum HttpMethod{GET,POST};
	
	protected String mUrl;
	protected String mPostContent;
	protected String mResult;
	protected HttpMethod mHttpMethod = HttpMethod.GET;
	protected JSONObject mJsonResult;
	
	public int mStatus;
	public String mInfo;
	public JSONObject mData;
	
	@Override
	protected void doInBackground() {
		if(this.mHttpMethod==HttpMethod.GET){
			this.mResult=HttpUtils.get(this.mUrl);
		}else{
			this.mResult=HttpUtils.post(this.mUrl, this.mPostContent);
		}
		try {
			this.mJsonResult=new JSONObject(this.mResult);
			this.mStatus=this.mJsonResult.optInt(JSON_TAG_STATUS, STATUS_FAILED);
			this.mInfo=this.mJsonResult.optString(JSON_TAG_INFO, null);
			this.mData=this.mJsonResult.optJSONObject(JSON_TAG_DATA);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
