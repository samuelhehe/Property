package xj.property.activity.contactphone;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.Path;
import xj.property.R;
import xj.property.activity.HXBaseActivity.ChatActivity;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.HXBaseActivity.MainActivity;
import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
import xj.property.activity.takeout.MarektHelper;
import xj.property.activity.takeout.SuperMarketChatActivity;
import xj.property.activity.user.FixUserAddressConfrimDialog;
import xj.property.adapter.CarExplandableAdapter;
import xj.property.beans.ContactPhoneListBean;
import xj.property.beans.FastGoodsModel;
import xj.property.beans.FastShopCatBean;
import xj.property.beans.FastShopDetailListBean;
import xj.property.beans.OrderDetailBeanList;
import xj.property.beans.ShopContactBean;
import xj.property.beans.ShopInfoBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.cache.FastShopCatModel;
import xj.property.cache.OrderDetailModel;
import xj.property.cache.OrderRepair;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.message.XJContactHelper;
import xj.property.utils.message.XJMessageHelper;
import xj.property.utils.other.Arith;
import xj.property.utils.other.Config;
import xj.property.utils.other.FastShopCarDBUtil;
import xj.property.utils.other.PreferencesUtil;


/**
 * 购物车
 * v3 2016/03/17
 */
public class FastShopCarActivity extends HXBaseActivity implements  FixUserAddressConfrimDialog.onChangeAddressStatusListener {
    /**
     * logger
     */
    /**
     * widgets activities page
     */
    private ExpandableListView lv_car;
    private TextView tv_sum_goods_num;
    private TextView tv_sum_price_num;
    private TextView tv_testwidth;
    private TextView tv_confirm;
    private LinearLayout bt_confirm;
    private ArrayList<FastShopDetailListBean.PagerItemBean> listbean;
    private UserInfoDetailBean userbean;
    private int ServiceNameWidth;
    private List<FastShopCatModel> fastShopCatModels;
    private CarExplandableAdapter adapter;
    private Handler handler;
    private  int totalCount=0;
    private LinearLayout ll_nomessage, ll_errorpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fastshop_cat);
        initData();//// 查询本地所有的商品列表
        userbean = PreferencesUtil.getLoginInfo(FastShopCarActivity.this);
        initTitle(null, "购物车", R.drawable.shop_trash_delete);
        initView();
        getFastShopCatDataList();
        refresh();
        ServiceNameWidth = getResources().getDimensionPixelOffset(R.dimen.item_goods_servicename);
        lv_car.requestFocusFromTouch();
        //取消expandListview默认图标
        lv_car.setGroupIndicator(null);
        adapter = new CarExplandableAdapter(FastShopCarActivity.this, group, userbean.getEmobId());
        lv_car.setAdapter(adapter);

        //设置expandListview默认展开
        for (int k = 0; k < adapter.getGroupCount(); k++) {
            lv_car.expandGroup(k);
        }
        //设置expandListview点击不关闭
        lv_car.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;//不关闭
            }
        });

        handler = new Handler() {
            @Override
            public void handleMessage(final Message msg) {
                bt_confirm.setClickable(true);
                switch (msg.what) {
                    case Config.TASKERROR:
                        if (msg.obj != null)
                            showToast("下单失败："+ msg.obj);
                        else
                            showNetErrorToast();
                        mLdDialog.dismiss();
                        break;
                    case Config.TASKCOMPLETE:
                        mLdDialog.dismiss();
                        break;
                    case Config.SINGLETASKCOMPLETE:

                        //构建环信消息 创建订单
                        Object[] objects = (Object[]) msg.obj;

                        FastGoodsModel fastGoodsModel = (FastGoodsModel) objects[1];

                        OrderDetailModel orderDetailModel = new OrderDetailModel();
                        orderDetailModel.serial = (String) objects[2];
                        //TODO list 2 array
                        orderDetailModel.orderDetailBeanList = fastGoodsModel.getOrderDetailBeanList().toString();

                        orderDetailModel.total_count = fastGoodsModel.totalCount;
                        orderDetailModel.total_price = fastGoodsModel.totalPrice + "";

                        StringBuilder count = new StringBuilder();
                        StringBuilder name = new StringBuilder();
                        StringBuilder price = new StringBuilder();

                        TextPaint paint = tv_testwidth.getPaint();
                        for (int i = 0; i < fastGoodsModel.orderDetailBeanList.size(); i++) {
                            OrderDetailBeanList orderDetailBeanList = fastGoodsModel.orderDetailBeanList.get(i);
                            name.append(StrUtils.str2SingleGoods(orderDetailBeanList.getServiceName(), paint, ServiceNameWidth));
                            count.append("X" + orderDetailBeanList.getCount() + "\n");
                            price.append("￥" + orderDetailBeanList.getPrice() + "\n");
                        }
                        orderDetailModel.oder_detail_price = price.substring(0, price.length() - 1);
                        orderDetailModel.oder_detail_count = count.substring(0, count.length() - 1);
                        orderDetailModel.oder_detail_servicename = name.substring(0, name.length() - 1);

                        orderDetailModel.save();


                        final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
                        TextMessageBody txtBody = new TextMessageBody("[订单]已下单，等待店家确认");
                        message.addBody(txtBody);
                        // 添加ext属性
                        message.setAttribute("avatar", userbean.getAvatar());
                        message.setAttribute("nickname", userbean.getNickname());
                        ////
                        ContactPhoneListBean fastShopIndexInfo = PreferencesUtil.getFastShopIndexInfo(getmContext());
                        XJContactHelper.saveContact(fastGoodsModel.emobIdShop, fastShopIndexInfo.getShopName(), fastShopIndexInfo.getLogo(), "2");
                        message.setAttribute("CMD_CODE", 200);
                        message.setAttribute("CMD_DETAIL", new Gson().toJsonTree(fastGoodsModel).toString());
                        message.setAttribute(Config.EXPKey_serial, orderDetailModel.serial);
                        message.setAttribute("clickable", 1);
                        message.setAttribute("isShowAvatar", 1);
                        message.setAttribute(Config.EXPKey_ADDRESS, userbean.getUserFloor() + userbean.getUserUnit() + userbean.getRoom());
                        message.setAttribute("msgId", message.getMsgId());
                        //to username or groupid
                        message.setReceipt(fastGoodsModel.emobIdShop);
                        //add message to conversation
                        //// 使用维修部分的Model
                        saveMessage2DB(message.getMsgId(), msg.arg1 + "");

                        EMChatManager.getInstance().sendMessage(message, new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                XJMessageHelper.saveMessage2DB(message.getMsgId(), message.getStringAttribute(Config.EXPKey_serial, ""), 200);
                                Log.i("onion", "message" + message.getMsgId());
                            }

                            @Override
                            public void onError(int i, String s) {
                                Log.i("onion", "onError" + s);
//                                bt_confirm.setClickable(true);
//                                totalCount--;
//                                Toast.makeText(FastShopCarActivity.this, "订单" + msg.arg1 + "失败：" + s, Toast.LENGTH_LONG).show();
//                                if (totalCount <= 0) {
//                                    mLdDialog.dismiss();
//                                }
                            }

                            @Override
                            public void onProgress(int i, String s) {

                            }
                        });

                        totalCount--;
                        FastShopCarDBUtil.deleteByServiceId(userbean.getEmobId(), fastShopIndexInfo.getEmobId());

                        if (totalCount <= 0) {
                            mLdDialog.dismiss();
                            Intent intent = new Intent(FastShopCarActivity.this, MainActivity.class);
                            MainActivity.index = 1;
                            startActivity(intent);
                            finish();
                        }
                }
            }
        };
    }

    private void saveMessage2DB(String msgId, String serial) {
        OrderRepair or = new OrderRepair(msgId, serial);
        or.save();
    }

    private void initData() {
        userbean = PreferencesUtil.getLoginInfo(getmContext());
        fastShopCatModels = FastShopCarDBUtil.getAll(userbean.getEmobId());
//        for(FastShopCatModel temp:fastShopCatModels){
//            if(temp.getServiceId()==323)
//                System.out.println(temp.getServiceName()+" ServiceId: "+temp.getServiceId() +" state"+temp.getState());
//        }
    }


    private ArrayList<FastShopCatBean> group = new ArrayList<FastShopCatBean>();//组列表
    private CarExplandableAdapter catExplandableAdapter;


    private void getFastShopCatDataList() {
        group.clear();
        //整理父类标题， 原来设计为不同店铺使用的。
        ArrayList<FastShopCatBean> list = new ArrayList<FastShopCatBean>();
//        for (int i = 0; i < fastShopCatModels.size(); i++) {
        for (FastShopCatModel fastShopCatModel : fastShopCatModels) {
            FastShopCatBean catBean = new FastShopCatBean();
            catBean.setShopId(fastShopCatModel.shopId);
            catBean.setShopName(fastShopCatModel.shopName);
            catBean.setPrice(FastShopCarDBUtil.getSumPriceByShopId(fastShopCatModel.shopId, userbean.getEmobId()));
            if (list.size() == 0) {
                list.add(catBean);
            } else {
                Set<Integer> temp = new HashSet<Integer>();
                for (int j = 0; j < list.size(); j++) {
                    temp.add(list.get(j).getShopId());
                }
                if (!temp.contains(fastShopCatModel.shopId)) {
                    list.add(catBean);
                }
            }
        }
        //// 查询每一个店铺下的所有订单
        for (FastShopCatBean catbean : list) {
            addInfo(catbean, FastShopCarDBUtil.findByShopId(catbean.getShopId(), userbean.getEmobId()));
        }
    }


    private void addInfo(FastShopCatBean catBean, ArrayList<FastShopCatModel> itemBean) {
        catBean.setChildList(itemBean);
        group.add(catBean);
    }


    private void initView() {
        ll_nomessage = (LinearLayout) findViewById(R.id.ll_nomessage);
        ll_errorpage = (LinearLayout) findViewById(R.id.ll_errorpage);
        lv_car = (ExpandableListView) findViewById(R.id.lv_car);
        tv_testwidth = (TextView) findViewById(R.id.tv_testwidth);
        tv_sum_goods_num = (TextView) findViewById(R.id.tv_sum_goods_num);
        tv_sum_price_num = (TextView) findViewById(R.id.tv_sum_price_num);
        bt_confirm = (LinearLayout) findViewById(R.id.bt_confirm);
        tv_confirm = (TextView) findViewById(R.id.tv_confirm);
        bt_confirm.setClickable(true);
        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FastShopCarDBUtil.deleteByState(userbean.getEmobId());
                group.clear();

                initData();

                getFastShopCatDataList();

                totalCount = group.size();


                if (PreferencesUtil.isFirstAddress(FastShopCarActivity.this)) {
                    fFixUserAddressConfrimDialog = new FixUserAddressConfrimDialog(FastShopCarActivity.this, FastShopCarActivity.this);
                    fFixUserAddressConfrimDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            /// 说明第一次输入地址没有成功, 设置为第一次输入地址信息
                            final SharedPreferences spf = getSharedPreferences(PreferencesUtil.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
                            spf.edit().putBoolean(PreferencesUtil.FIRST_ADRESS, true).commit();

                        }
                    });
                    fFixUserAddressConfrimDialog.show();
                }else{ /// 非首次下单,对于没有地址信息的显示地址对话框

                    if (PreferencesUtil.getLogin(FastShopCarActivity.this)) {
                        userbean = PreferencesUtil.getLoginInfo(FastShopCarActivity.this);
                        //// 显示地址对话框
                        if (TextUtils.isEmpty(userbean.getRoom()) || TextUtils.isEmpty(userbean.getUserFloor())) {
                            fFixUserAddressConfrimDialog = new FixUserAddressConfrimDialog(FastShopCarActivity.this, FastShopCarActivity.this);
                            fFixUserAddressConfrimDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    bt_confirm.setClickable(true);
                                    mLdDialog.dismiss();

                                }
                            });
                            fFixUserAddressConfrimDialog.show();
                            return;
                        }else{
                            bt_confirm.setClickable(false);
                            mLdDialog.show();
                            MarektHelper.getSerialId(FastShopCarActivity.this, userbean.getEmobId(), group, handler);
                        }
                    } else {
                        Intent intent = new Intent(FastShopCarActivity.this, RegisterLoginActivity.class);
                        startActivity(intent);
                        return;
                    }
                }

//                if (PreferencesUtil.isFirstAddress(FastShopCarActivity.this)) {
//                    FistAddressDialog.showFistAddressDialog(FastShopCarActivity.this, requestFixAddress, new FistAddressDialog.CancelCallBack() {
//                        @Override
//                        public void onCancel() {
//
//                            MarektHelper.getSerialId(FastShopCarActivity.this, userbean.getEmobId(), group, handler);
//                        }
//                    });
//
//
//                    final SharedPreferences spf = getSharedPreferences(PreferencesUtil.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
//                    spf.edit().putBoolean(PreferencesUtil.FIRST_ADRESS, false).commit();
//                } else {
//                    MarektHelper.getSerialId(FastShopCarActivity.this, userbean.getEmobId(), group, handler);
//                }
            }
        });


    }



    @Override
    public void onChangeAddressSuccess(String message) {
        userbean = PreferencesUtil.getLoginInfo(FastShopCarActivity.this);
        bt_confirm.setClickable(false);
        mLdDialog.show();

        MarektHelper.getSerialId(FastShopCarActivity.this, userbean.getEmobId(), group, handler);

        final SharedPreferences spf = getSharedPreferences(PreferencesUtil.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        spf.edit().putBoolean(PreferencesUtil.FIRST_ADRESS, false).commit();
        /// 地址修改成功, 下次不再弹出
    }

    @Override
    public void onChangeAddressFail(String message) {
        final SharedPreferences spf = getSharedPreferences(PreferencesUtil.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        spf.edit().putBoolean(PreferencesUtil.FIRST_ADRESS, true).commit();
    }

    @Override
    public void onChangeAddressCancel() {
        final SharedPreferences spf = getSharedPreferences(PreferencesUtil.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        spf.edit().putBoolean(PreferencesUtil.FIRST_ADRESS, true).commit();
    }
    private FixUserAddressConfrimDialog fFixUserAddressConfrimDialog;

    protected void onDestroy() {
        super.onDestroy();
        if (fFixUserAddressConfrimDialog != null) {
            fFixUserAddressConfrimDialog.dismiss();
        }
    }

    int requestFixAddress = 7484;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_right:
                final Dialog dialog = new Dialog(FastShopCarActivity.this, R.style.MyDialogStyle);
                dialog.setContentView(R.layout.dialog_deletecircle);
                TextView title = (TextView) dialog.findViewById(R.id.tv_title);
                title.setText("将所选商品从购物车中移除？");
                dialog.findViewById(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.findViewById(R.id.tv_relogin).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FastShopCarDBUtil.deleteByServiceId(userbean.getEmobId());
                        changeChildNum();
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
        }
    }

    public void changeChildNum() {
        initData();
        getFastShopCatDataList();
        refresh();
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestFixAddress) {
            userbean = PreferencesUtil.getLoginInfo(this);
            MarektHelper.getSerialId(FastShopCarActivity.this, userbean.getEmobId(), group, handler);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void refresh() {
        //List<FastShopCatModel> lists=FastShopCarDBUtil.getAllAndState();
        fastShopCatModels = FastShopCarDBUtil.getAllAndState(userbean.getEmobId());
        double totalprice = FastShopCarDBUtil.getAllGoodsPrice(fastShopCatModels);
        double deliverLimit = PreferencesUtil.getDeliverLimit(this);
        double _price = Arith.sub(deliverLimit, totalprice);
        if (_price > 0) {
            tv_confirm.setText("还差" + _price + "元起送");
            bt_confirm.setBackgroundColor(0xffc8c8cd);
            bt_confirm.setClickable(false);
        } else {
            tv_confirm.setText("下单");
            bt_confirm.setBackgroundColor(0xffff6634);
            bt_confirm.setClickable(true);

        }
        if (group.size() <= 0) {

            ll_nomessage.setVisibility(View.VISIBLE);
            ll_errorpage.setVisibility(View.VISIBLE);
            TextView tv_nomessage = (TextView) findViewById(R.id.tv_nomessage);
            tv_nomessage.setText("购物车空空了，快去选些商品吧！");
        } else {
            ll_nomessage.setVisibility(View.GONE);
            ll_errorpage.setVisibility(View.GONE);
        }

//        if (fastShopCatModels.size() == 0) {
//            bt_confirm.setVisibility(View.GONE);
//        } else {
//            bt_confirm.setVisibility(View.VISIBLE);
//        }
        tv_sum_goods_num.setText(FastShopCarDBUtil.getGoodsCount(fastShopCatModels) + "");
        tv_sum_price_num.setText("总价:￥" + totalprice + "");
    }
}
