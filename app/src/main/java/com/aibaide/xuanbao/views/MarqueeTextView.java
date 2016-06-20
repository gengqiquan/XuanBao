package com.aibaide.xuanbao.views;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author gengqiquan:
 * @version 创建时间：2016-4-18 下午4:51:26
 * 类说明
 */
	public class MarqueeTextView extends TextView {

		 public MarqueeTextView(Context context) {
		  super(context);
		 }
		 
		 public MarqueeTextView(Context context, AttributeSet attrs) {
		  super(context, attrs);
		 }

		 public MarqueeTextView(Context context, AttributeSet attrs,
		   int defStyle) {
		  super(context, attrs, defStyle);
		 }
		 
		 @Override
		 public boolean isFocused() {
		  return true;
		 }
		 @Override  
		    protected void onFocusChanged(boolean focused, int direction,  
		            Rect previouslyFocusedRect) {  
		        if (focused) {  
		            super.onFocusChanged(focused, direction, previouslyFocusedRect);  
		        }  
		    }  
		  
		    @Override  
		    public void onWindowFocusChanged(boolean focused) {  
		        if (focused) {  
		            super.onWindowFocusChanged(focused);  
		        }  
		    }  
}
