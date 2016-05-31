package com.matteo.cc.utils.view;

import java.lang.reflect.Field;

import android.app.Activity;
import android.view.View;

public class ViewUtils {

	
	public static void inject(Activity activity){
		ViewUtils.injectObject(activity, new ViewFinder(activity));
	}
	
	public static void inject(View view){
		ViewUtils.injectObject(view, new ViewFinder(view));
	}
	
	public static void inject(Object handler,View view){
		ViewUtils.injectObject(handler, new ViewFinder(view));
	}
	
	private static void injectObject(Object handler,ViewFinder finder){
		//handler.getClass().get
		Class<?> handlerClass=handler.getClass();
		Field[] fields=handlerClass.getDeclaredFields();

		for (Field field : fields) {
			ViewInject viewInject=field.getAnnotation(ViewInject.class);
			if(viewInject!=null){
				View view=finder.findViewById(viewInject.value());
				//
				try {
					//setMethod.invoke(field,handler,view);
					field.setAccessible(true);
					field.set(handler, view);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		}
	}
}
