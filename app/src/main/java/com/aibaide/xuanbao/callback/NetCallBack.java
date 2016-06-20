package com.aibaide.xuanbao.callback;


public abstract class NetCallBack<T> {
	public abstract void onFailure(Throwable t, String errorMsg, int statusCode);

	public abstract void onSuccess(T t,T bean);

	public static final NetCallBack<String> DEFAULT_RESULT_CALLBACK = new NetCallBack<String>() {
		@Override
		public void onFailure(Throwable t, String errorMsg, int statusCode) {

		}

		@Override
		public void onSuccess(String t,String str) {

		}
	};
}
