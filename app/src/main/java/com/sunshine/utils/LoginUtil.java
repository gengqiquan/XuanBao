package com.sunshine.utils;

import android.content.Context;
import android.content.Intent;

import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.login.LoginAcitvity;

import java.lang.ref.WeakReference;


/**
 * 登录回调类
 * 
 * @author gengqiquan
 * @time 2015年5月20日13:24:16
 */
public abstract class LoginUtil {
	WeakReference<Context> reference;

	// 判断是否登录，是返回true
	public void checkLoginForCallBack(Context context) {
		// 弱引用，防止内存泄露，
		reference = new WeakReference<Context>(context);
		if (Util.checkNULL(Configure.SIGNID)) {
			Configure.CALLBACK = new ICallBack() {

				@Override
				public void postExec() {
					// 登录回调后执行登录回调前需要做的操作
					if (!Util.checkNULL(Configure.SIGNID)) {
						// 这里需要再次判断是否登录，防止用户取消登录，取消则不执行登录成功需要执行的回调操作
						loginForCallBack();
						//防止调用界面的回调方法中有传进上下文的引用导致内存溢出
						Configure.CALLBACK = null;
					}
				}
			};
			Context mContext = reference.get();
			if (mContext != null) {
				Intent intent = new Intent(context, LoginAcitvity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			}
		} else {
			// 登录状态直接执行登录回调前需要做的操作
			loginForCallBack();
		}
	}

	public void clear() {
		try {
			finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 声明一个登录成功回调的接口
	public interface ICallBack {
		// 在登录操作及信息获取完成后调用这个方法来执行登录回调需要做的操作
		void postExec();
	}

	public abstract void loginForCallBack();

}
