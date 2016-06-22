package com.aibaide.xuanbao.taste.itry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aibaide.xuanbao.R;
import com.aibaide.xuanbao.base.BaseActivity;
import com.aibaide.xuanbao.bean.AddressBean;
import com.aibaide.xuanbao.bean.GoodsBean;
import com.aibaide.xuanbao.bean.RQBean;
import com.aibaide.xuanbao.callback.NetCallBack;
import com.aibaide.xuanbao.configure.Configure;
import com.aibaide.xuanbao.configure.U;
import com.aibaide.xuanbao.image.NetImageView;
import com.aibaide.xuanbao.mine.AddressActivity;
import com.sunshine.utils.AjaxParams;
import com.sunshine.utils.JsonUtil;
import com.sunshine.utils.LoginUtil;
import com.sunshine.utils.RQ;
import com.sunshine.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

public class SendActivity extends BaseActivity {
    TextView mName, mPrice, mIntegral, mTime, mBtSure;
    TextView receAddress, receName, recePhone;
    RelativeLayout mAddress, mLoaction;
    EditText mWords;
    NetImageView mGoodsImg;
    GoodsBean mGoodsDetailBean;
    String mRAddress, mPerson, mPhone, mMsg;
    AddressBean addressBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_info);
        mTabTitleBar.setTile("邮寄信息");
        mTabTitleBar.showLeft();
        mGoodsDetailBean = (GoodsBean) getIntent().getSerializableExtra("mGoodsDetailBean");
        initviews();
        loadDefultAddress();
    }

    private void initviews() {
        mGoodsImg = (NetImageView) mContentView.findViewById(R.id.goods_img);
        mName = (TextView) mContentView.findViewById(R.id.goods_name);
        mPrice = (TextView) mContentView.findViewById(R.id.goods_price);
        mIntegral = (TextView) mContentView.findViewById(R.id.goods_integral);
        mTime = (TextView) mContentView.findViewById(R.id.goods_time);
        mBtSure = (TextView) mContentView.findViewById(R.id.sure);
        receAddress = (TextView) mContentView.findViewById(R.id.address);
        receName = (TextView) mContentView.findViewById(R.id.name);
        recePhone = (TextView) mContentView.findViewById(R.id.phone);
        mWords = (EditText) mContentView.findViewById(R.id.words);
        mAddress = (RelativeLayout) mContentView.findViewById(R.id.defult_address);
        mLoaction = (RelativeLayout) mContentView.findViewById(R.id.location);
        mName.setText(mGoodsDetailBean.getGoodsName());
        mPrice.setText("价值：" + mGoodsDetailBean.getGoodsPrice() + "元");
        //mPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mGoodsImg.LoadUrl(U.g(mGoodsDetailBean.getFilePath()), mLoader);
        mIntegral.setText(mGoodsDetailBean.getGoodsPoint() + "积分");
        if (!Util.checkNULL(mGoodsDetailBean.getLineEndTime()) && mGoodsDetailBean.getLineEndTime().length() > 10) {
            String str = mGoodsDetailBean.getLineEndTime().substring(0, 10);
            mTime.setText("有效期至" + str);
        }
        mAddress.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new LoginUtil() {

                    @Override
                    public void loginForCallBack() {
                        Intent intent = new Intent(mContext, AddressActivity.class);
                        startActivityForResult(intent, 20);
                    }
                }.checkLoginForCallBack(mContext);

            }
        });
        mLoaction.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new LoginUtil() {

                    @Override
                    public void loginForCallBack() {
                        Intent intent = new Intent(mContext, AddressActivity.class);
                        startActivityForResult(intent, 20);
                    }
                }.checkLoginForCallBack(mContext);

            }
        });

        if (Configure.IsFirstOrder) {
            mBtSure.setText("首单" + Configure.FirstOrderPrice + "元，确认支付");
        } else {
            mBtSure.setText("确认支付" + mGoodsDetailBean.getGdLineDetailsList().get(0).getPayPrice() + "元");
        }

        mBtSure.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Util.checkNULL(mRAddress) || Util.checkNULL(mPerson) || Util.checkNULL(mPhone)) {
                    showToast("请填写收货信息");
                    return;
                }
                new LoginUtil() {

                    @Override
                    public void loginForCallBack() {
                        order_sure();
                    }
                }.checkLoginForCallBack(mContext);

            }
        });
    }

    private void loadDefultAddress() {
        AjaxParams params = new AjaxParams();
        params.put("type", "9");
        params.put("memberId", "" + Configure.USERID);
        params.put("signId", "" + Configure.SIGNID);
        fh.post(U.g(U.defultAddress), params, new NetCallBack<String>() {

            @Override
            public void onFailure(Throwable t, String errorMsg, int statusCode) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onSuccess(String t, String url) {
                // TODO Auto-generated method stub
                RQBean rq = RQ.d(t);
                if (rq != null && rq.success && rq.data != null) {
                    try {
                        JSONObject obj = new JSONObject(rq.data);
                        String str = obj.getString("bma");
                        addressBean = (AddressBean) JsonUtil.fromJson(str, AddressBean.class);
                        if (addressBean != null) {
                            mRAddress = addressBean.getPostAddress();
                            mPerson = addressBean.getReceiver();
                            mPhone = addressBean.getReceiverPhone() + "";
                            mLoaction.setVisibility(View.GONE);
                            mAddress.setVisibility(View.VISIBLE);
                            receAddress.setText(mRAddress);
                            receName.setText(mPerson);
                            recePhone.setText(mPhone);
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void order_sure() {

        AjaxParams params = new AjaxParams();
        params.put("memberId", "" + Configure.USERID);
        params.put("signId", "" + Configure.SIGNID);
        params.put("onlineId", "" + mGoodsDetailBean.getLineId());
        params.put("goodsName", "" + mGoodsDetailBean.getGoodsName());
        params.put("takeType", "2");
        params.put("merchantId", mGoodsDetailBean.getGdLineDetailsList().get(0).getStoreId() + "");
        params.put("postAddress", mRAddress);
        params.put("receiver", mPerson);
        params.put("receiverPhone", mPhone);
        params.put("leaveMsg", "" + mWords.getText().toString());

        fh.post(U.g(U.addOrder), params, new NetCallBack<String>() {

            @Override
            public void onFailure(Throwable t, String errorMsg, int statusCode) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onSuccess(String t, String url) {
                // TODO Auto-generated method stub
                RQBean rq = RQ.d(t);
                if (rq != null && rq.success && rq.data != null) {
                    try {
                        JSONObject obj = new JSONObject(rq.data);
                        String orderId = obj.getString("orderId");
                        String orderNo = obj.getString("orderNo");
                        Intent intent;

                        intent = new Intent(mContext, PayActivity.class);
                        intent.putExtra("orderNo", orderNo);
                        intent.putExtra("goodsName", mGoodsDetailBean.getGoodsName());
                        intent.putExtra("price", mGoodsDetailBean.getGdLineDetailsList().get(0).getPayPrice() + "");
                        intent.putExtra("isSend", true);
                        intent.putExtra("orderId", orderId);
                        intent.putExtra("addressBean", addressBean);
                        startActivity(intent);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    showToast(rq.msg);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if (arg1 == 20 && arg2 != null) {
            addressBean = (AddressBean) arg2.getSerializableExtra("addressbean");
            if (addressBean != null) {
                mRAddress = addressBean.getPostAddress();
                mPerson = addressBean.getReceiver();
                mPhone = addressBean.getReceiverPhone() + "";
                mLoaction.setVisibility(View.GONE);
                mAddress.setVisibility(View.VISIBLE);
                receAddress.setText(mRAddress);
                receName.setText(mPerson);
                recePhone.setText(mPhone);
            }

        }
    }
}
