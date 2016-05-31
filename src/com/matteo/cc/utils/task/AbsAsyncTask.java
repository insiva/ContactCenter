package com.matteo.cc.utils.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.os.Message;

public abstract class AbsAsyncTask {
	private static final int MSG_TASK_COMPLETE=0x001;
	private static ExecutorService mThreadPool=Executors.newCachedThreadPool();
	
	private TaskHandler mHandler;
	
	public AbsAsyncTask(){
		this.mHandler=new TaskHandler(this);
	}
	
	protected void onPreExecute(){
		
	}
	
	protected void doInBackground() {
		
	}
	
	protected void onPostExecute() {
		
	}
	
	public void execute(){
		this.onPreExecute();
		this.doBackgroundTask();
	}
	
	private final void doBackgroundTask(){
		AbsAsyncTask.mThreadPool.execute(new Runnable() {
			
			@Override
			public void run() {
				AbsAsyncTask.this.doInBackground();
				AbsAsyncTask.this.mHandler.sendEmptyMessage(MSG_TASK_COMPLETE);
			}
		});
	}
	
	static class TaskHandler extends Handler{
		private AbsAsyncTask mTask;
		
		public TaskHandler(AbsAsyncTask task) {
			super();
			this.mTask=task;
		}
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==MSG_TASK_COMPLETE){
				this.mTask.onPostExecute();
			}
		}
	}
}
