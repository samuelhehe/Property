package xj.property.activity.repair;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;

import org.json.JSONObject;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import xj.property.R;
import xj.property.activity.HXBaseActivity.ChatActivity;
import xj.property.adapter.MessageAdapter;
import xj.property.adapter.RepairChatAdapter;
import xj.property.beans.AddCommentsRequest;
import xj.property.beans.AddCommentsRequestV3;
import xj.property.beans.OrderCancelRequest;
import xj.property.beans.OrderStatusBean;
import xj.property.beans.OrdersBeanRequest;
import xj.property.beans.OrdersBeanRequestV3;
import xj.property.beans.SerialIdBean;
import xj.property.beans.ShopInfoBean;
import xj.property.beans.ShopInfoResult;
import xj.property.beans.UserInfoDetailBean;
import xj.property.event.RepairButtonEvent;
import xj.property.netbase.BaseBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.message.XJMessageHelper;
import xj.property.utils.other.AdminUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.StarOnClickListener;
import xj.property.widget.LoadingDialog;

/**
 * Created by Administrator on 2015/3/16.
 */
public class RepairChatActivity extends ChatActivity {
    private UserInfoDetailBean bean;
    protected LoadingDialog mLdDialog = null;
    private android.app.AlertDialog complainDialog;
    private int shopId;

    @Override
    protected boolean forChild() {
        InitDialog();
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_service_chat);
//        communityId = getIntent().getStringExtra("userId");
        shopId = getIntent().getIntExtra("shopId", 0);
        findViewById(R.id.tv_repair_app).setOnClickListener(this);
        findViewById(R.id.tv_repair_photo).setOnClickListener(this);
        findViewById(R.id.tv_repair_sheet).setOnClickListener(this);
        findViewById(R.id.tv_Complaint).setOnClickListener(this);
        findViewById(R.id.container_remove).setVisibility(View.GONE);
        bean = PreferencesUtil.getLoginInfo(this);
        return true;
    }

    protected void InitDialog() {
        mLdDialog = new LoadingDialog(this);
        mLdDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                dialog.cancel();
                return false;
            }
        });
        View rootView = View.inflate(this, R.layout.dialog_complain, null);
        complainDialog = new android.app.AlertDialog.Builder(this).setView(rootView).create();
        final EditText editText = (EditText) rootView.findViewById(R.id.et_complain);
        rootView.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complainDialog.dismiss();
            }
        });
        rootView.findViewById(R.id.btn_complain).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        complainDialog.dismiss();
                        String content = editText.getText().toString();
                        if (TextUtils.isEmpty(content)) {
                            Toast.makeText(RepairChatActivity.this, "投诉内容不能为空", Toast.LENGTH_LONG).show();
                            return;
                        }
//                AdminUtils.askAdmin(RepairChatActivity.this, "complaints", content,"5");
                        AdminUtils.doComplainForRepair(RepairChatActivity.this, bean.getEmobId(), toChatUsername, content);
                    }
                });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_repair_app://提出申请
                view.setClickable(false);
                if (null != XJMessageHelper.orderSelect(toChatUsername)) {
                    Toast.makeText(this, "您与师傅有未完成的订单", Toast.LENGTH_SHORT).show();
                    view.setClickable(true);
                    return;
                }
                getShopInfo();
                break;
            case R.id.tv_repair_photo://拍照
                selectPicFromCamera();
                break;
            case R.id.tv_repair_sheet://获取列表
                startActivity(new Intent(this, ValueSheetActivity.class));
                //getSheet();
                break;
            case R.id.tv_Complaint://投诉
                complainDialog.show();
                break;
        }
    }

    int requestFixAddress = 456;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestFixAddress) {
            bean = PreferencesUtil.getLoginInfo(this);
            getSerialId();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    // This method will be called when a MessageEvent is posted
    public void onEvent(RepairButtonEvent event) {
        Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show();
        if (event.json != null)
            sendTextWithExt(event.json, event.CMD_CODE);
    }

    protected void sendTextWithExt(final JSONObject jsonObject, int CMD_CODE) {
//        super.sendTextWithExt(content, CMD_CODE);
        Log.i("onion", "SendTextWithExt" + jsonObject.optString(Config.EXPKey_serial) + CMD_CODE);
        final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
        // 如果是群聊，设置chattype,默认是单聊
        if (chatType == CHATTYPE_GROUP)
            message.setChatType(EMMessage.ChatType.GroupChat);
//        TextMessageBody txtBody = new TextMessageBody(jsonObject.optString(Config.EXPKey_serial));
        TextMessageBody txtBody = null;

        switch (CMD_CODE) {
            case 300:
                // 添加ext属性
                txtBody = new TextMessageBody("[订单]服务预约中...");
                message.addBody(txtBody);
                message.setAttribute("avatar", bean.getAvatar());
                message.setAttribute("nickname", bean.getNickname());
                message.setAttribute(Config.EXPKey_ADDRESS, bean.getUserFloor() + bean.getUserUnit() + bean.getRoom() + "");
                message.setAttribute("CMD_CODE", CMD_CODE);
                message.setAttribute("CMD_DETAIL", "{}");
                message.setAttribute("serial", jsonObject.optString(Config.EXPKey_serial));
                message.setAttribute("clickable", 1);
                message.setAttribute("isShowAvatar", 1);
                message.setAttribute("msgId", message.getMsgId());
                //to username or groupid
                message.setReceipt(toChatUsername);
                //add message to conversation
                conversation.addMessage(message);
                // XJMessageHelper.saveMessage2DB(message.getMsgId(), jsonObject.optString(Config.EXPKey_serial), 300);
                // 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法
                adapter.refresh();
                listView.setSelection(listView.getCount() - 1);

                setResult(RESULT_OK);
                break;

            case 304:
                // 添加ext属性
                txtBody = new TextMessageBody("[订单]双方已确认费用...");
                message.addBody(txtBody);
                message.setAttribute(Config.EXPKey_ADDRESS, bean.getUserFloor() + bean.getUserUnit() + bean.getRoom() + "");
                message.setAttribute("avatar", bean.getAvatar());
                message.setAttribute("nickname", bean.getNickname());
                message.setAttribute("CMD_CODE", CMD_CODE);
                Log.i("onion", jsonObject.toString());
                JSONObject js = new JSONObject();
                try {
                    js.put(Config.EXPKey_totalPrice, jsonObject.optString(Config.EXPKey_totalPrice));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                message.setAttribute("CMD_DETAIL", js.toString());
                message.setAttribute("serial", jsonObject.optString(Config.EXPKey_serial));
                message.setAttribute("clickable", 1);
                message.setAttribute("isShowAvatar", 0);
                message.setAttribute("msgId", message.getMsgId());
                //     XJMessageHelper.saveMessage2DB(message.getMsgId(),jsonObject.optString(Config.EXPKey_serial),304);
                //to username or groupid
                message.setReceipt(toChatUsername);
                //add message to conversation
                conversation.addMessage(message);

                // 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法
                adapter.refresh();
                listView.setSelection(listView.getCount() - 1);
//    mEditTextContent.setText("");

                setResult(RESULT_OK);
                break;
            case 308:
                txtBody = new TextMessageBody("[订单]订单已完成，已做出评价...");
                message.addBody(txtBody);
                XJMessageHelper.orderFail(toChatUsername);
                message.setAttribute(Config.EXPKey_ADDRESS, bean.getUserFloor() + bean.getUserUnit() + bean.getRoom() + "");
                View rootView = View.inflate(this, R.layout.dialog_eva_repair, null);
                final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(this).setView(rootView).create();
                final EditText et = (EditText) rootView.findViewById(R.id.et_eva);
                TextView btn_thanks = (TextView) rootView.findViewById(R.id.btn_thanks);
                TextView btn_eva = (TextView) rootView.findViewById(R.id.btn_eva);
                final StarOnClickListener listener = StarOnClickListener.getInstance((LinearLayout) rootView.findViewById(R.id.ll_eva));
                listener.setStarOnClick();
                btn_thanks.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btn_eva.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //  Toast.makeText(RepairChatActivity.this, "谢谢评价", Toast.LENGTH_LONG).show();

                        // 添加ext属性
                        message.setAttribute("nickname", bean.getNickname());
                        message.setAttribute("avatar", bean.getAvatar());
                        JSONObject js = new JSONObject();
                        String content = et.getText().toString();
                        if (content.length() > 100) {
                            Toast.makeText(RepairChatActivity.this, "评论字数过长，请限制在100字以内。", Toast.LENGTH_LONG).show();
                            return;
                        }
                        dialog.dismiss();
                        if (content.length() == 0) {
                            content = "维修师傅技术很好，服务满意!";
                        }
                        try {
                            js.put(Config.EXPKey_EVA, content);
                            js.put(Config.EXPKey_Star, listener.starnum + "");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        message.setAttribute("CMD_CODE", 308);
                        message.setAttribute("CMD_DETAIL", js.toString());
                        message.setAttribute(Config.EXPKey_serial, jsonObject.optString(Config.EXPKey_serial));
                        message.setAttribute("clickable", 1);
                        message.setAttribute("isShowAvatar", 0);
                        message.setAttribute(Config.EXPKey_msgId, message.getMsgId());
                        addComment(listener.starnum, content, toChatUsername, jsonObject.optString(Config.EXPKey_msgId), message, jsonObject.optString(Config.EXPKey_serial));

                    }
                });
                dialog.show();
                break;

            case 310://业主取消请求
                // 添加ext属性
                txtBody = new TextMessageBody("[订单]订单已取消...");
                message.addBody(txtBody);
                message.setAttribute(Config.EXPKey_ADDRESS, bean.getUserFloor() + bean.getUserUnit() + bean.getRoom() + "");
                message.setAttribute("avatar", bean.getAvatar());
                message.setAttribute("nickname", bean.getNickname());
                message.setAttribute("CMD_CODE", CMD_CODE);
                message.setAttribute("CMD_DETAIL", "{}");
                message.setAttribute(Config.EXPKey_serial, jsonObject.optString(Config.EXPKey_serial, ""));
//                message.setAttribute("clickable", 1);
//                message.setAttribute("isShowAvatar", 1);
//                message.setAttribute("msgId", message.getMsgId());
                //to username or groupid
                message.setReceipt(toChatUsername);
                findViewById(R.id.tv_repair_app).setClickable(true);
                //add message to conversation
                conversation.addMessage(message);
                // 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法

                //
//    mEditTextContent.setText("");
                cancelOrderStatus(jsonObject.optString(Config.EXPKey_serial, ""), jsonObject.optString(Config.EXPKey_msgId, ""));

                break;
            case 312://拒绝费用
                txtBody = new TextMessageBody("[订单]您拒绝费用，请等待师傅再次发起费用...");
                message.addBody(txtBody);
                message.setAttribute("avatar", bean.getAvatar());
                message.setAttribute("nickname", bean.getNickname());
                message.setAttribute(Config.EXPKey_ADDRESS, bean.getUserFloor() + bean.getUserUnit() + bean.getRoom() + "");
                message.setAttribute("CMD_CODE", CMD_CODE);
                message.setAttribute("CMD_DETAIL", "{}");
                message.setAttribute(Config.EXPKey_clickable, 1);
                message.setAttribute("serial", jsonObject.optString(Config.EXPKey_serial, ""));
                message.setAttribute(Config.EXPKey_msgId, message.getMsgId());
//                message.setAttribute("clickable", 1);
//                message.setAttribute("isShowAvatar", 1);
//                message.setAttribute("msgId", message.getMsgId());
                //to username or groupid
                message.setReceipt(toChatUsername);

                //add message to conversation
                conversation.addMessage(message);

                // 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法
                adapter.refresh();
                // listView.setSelection(listView.getCount() - 1);
//    mEditTextContent.setText("");

                setResult(RESULT_OK);
                break;

            default:
                super.sendText(jsonObject.optString(Config.EXPKey_serial));
                break;
        }
    }


    /**
     * 获取shopInfo部分
     */
    interface ShopInfoService {
        //       http://114.215.105.202:8080/api/v1/communities/{communityId}/shops/content/{emobId}
//        @GET("/api/v1/communities/{communityId}/shops/content/{emobId}")
//        void getShopInfo(@Path("communityId") long communityId, @Path("emobId") String emobId, Callback<ShopInfoResult> cb);

        @GET("/api/v3/repairs/shops/{shopId}")
        void getShopInfoV3(@Path("shopId") int shopId, Callback<CommonRespBean<ShopInfoBean>> cb);

    }

    private void getShopInfo() {


        getSerialId();
//        mLdDialog.show();
//        ShopInfoService service=RetrofitFactory.getInstance().create(getApplicationContext(),ShopInfoService.class);
//        Callback<CommonRespBean<ShopInfoBean>> callback = new Callback<CommonRespBean<ShopInfoBean>>() {
//            @Override
//            public void success(CommonRespBean<ShopInfoBean> bean, retrofit.client.Response response) {
//                mLdDialog.dismiss();
//                findViewById(R.id.tv_repair_app).setClickable(true);
//                if (bean == null || !"yes".equals(bean.getStatus())) {
//                    Toast.makeText(RepairChatActivity.this, "服务器错误" + bean, Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                ShopInfoBean contact = bean.getData();
//                if (contact != null && !"normal".equals(contact.getStatus())) {
//                    Toast.makeText(RepairChatActivity.this, "不营业!", Toast.LENGTH_LONG).show();
//                    findViewById(R.id.tv_repair_app).setClickable(true);
//                    XJMessageHelper.orderFail(toChatUsername);
//                    return;
//                } else {
//                    if(PreferencesUtil.isFirstAddress(RepairChatActivity.this)) {
//                        FistAddressDialog.showFistAddressDialog(RepairChatActivity.this, requestFixAddress,new FistAddressDialog.CancelCallBack() {
//                            @Override
//                            public void onCancel() {
//                                getSerialId();
//                            }
//                        });
//                        final SharedPreferences spf = getSharedPreferences(
//                                PreferencesUtil.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
//                        spf.edit().putBoolean(PreferencesUtil.FIRST_ADRESS, false).commit();
//                    } else{
//                        getSerialId();
//                    }
//                }
////                XJContactHelper.saveContact(contact.emobId, contact.getShopName(), contact.getLogo());
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                mLdDialog.dismiss();
//                showNetErrorToast();
//                error.printStackTrace();
//                findViewById(R.id.tv_repair_app).setClickable(true);
//                XJMessageHelper.orderFail(toChatUsername);
//            }
//        };
//
//        service.getShopInfoV3(shopId, callback);
    }

    /**
     * 获取Order部分
     */
    interface OrderIdService {
        //  /api/v1/communities/{communityId}/users/{emobIdUser}/orders
//        @POST("/api/v1/communities/{communityId}/users/{emobIdUser}/orders")
//        void getOrderId(@Header("signature")String signature,@Body OrdersBeanRequest ob, @Path("communityId") long communityId,
//                        @Path("emobIdUser") String emobIdUser, Callback<SerialIdBean> cb);

        //  /api/v3/repairs/orders

        @POST("/api/v3/repairs/orders")
        void addOrderIdV3(@Body OrdersBeanRequestV3 ordersBeanRequestV3, Callback<CommonRespBean<String>> cb);

    }

    private void getSerialId() {
        mLdDialog.show();

        OrdersBeanRequestV3 ordersBeanRequestV3 = new OrdersBeanRequestV3();
        ordersBeanRequestV3.setCommunityId(bean.getCommunityId());
        ordersBeanRequestV3.setEmobIdUser(bean.getEmobId());
        ordersBeanRequestV3.setEmobIdShop(toChatUsername);
        OrderIdService service = RetrofitFactory.getInstance().create(getApplicationContext(), ordersBeanRequestV3, OrderIdService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> bean, retrofit.client.Response response) {
                mLdDialog.dismiss();
                if (bean != null && "yes".equals(bean.getStatus())) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        XJMessageHelper.orderBegin(toChatUsername);
                        jsonObject.put(Config.EXPKey_serial, bean.getData());
                    } catch (Exception e) {

                    }
                    sendTextWithExt(jsonObject, 300);
                } else {
                    XJMessageHelper.orderFail(toChatUsername);
                    if (bean != null) {
                        Toast.makeText(RepairChatActivity.this, "添加订单失败" + bean.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RepairChatActivity.this, "添加订单失败", Toast.LENGTH_SHORT).show();
                    }
//                    XJMessageHelper.orderFail(toChatUsername);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                showNetErrorToast();
                findViewById(R.id.tv_repair_app).setClickable(true);
                XJMessageHelper.orderFail(toChatUsername);
                error.printStackTrace();
            }
        };
//        OrdersBeanRequest ordersBeanRequest=new OrdersBeanRequest();
//        OrderBean orderBean = new OrderBean();
//        orderBean.setEmobIdShop(toChatUsername);
//        ArrayList<OrderDetailBeanList> list = new ArrayList<OrderDetailBeanList>();
//        orderBean.setOrderDetailBeanList(list);
//        ordersBeanRequest.getOrders().add(orderBean);
//        Log.i("onion", "user" + XjApplication.getInstance().getUserName());
        service.addOrderIdV3(ordersBeanRequestV3, callback);
    }


    /**
     * 提交Order部分
     */
    /*interface OrderCompleteService {
        // /api/v1/communities/{communityId}/users/{emobIdUser}/orders/update/{orderId}
        // /api/v1/communities/{communityId}/users/{emobIdUser}/orders/{orderId}//取消订单
        @PUT("/api/v1/communities/{communityId}/users/{emobIdUser}/orders/update/{orderId}")
        void setOrderComplete(@Body OrderBean ob, @Path("communityId") long communityId, @Path("emobIdUser") String emobIdUser, Callback<OrderStatusBean> cb);
    }

    private void changeOrderStatus(String orderId) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        OrderCompleteService service = restAdapter.create(OrderCompleteService.class);
        Callback<OrderStatusBean> callback = new Callback<OrderStatusBean>() {
            @Override
            public void success(OrderStatusBean bean, retrofit.client.Response response) {
                Log.i("onion", "Status" + bean.getStatus());
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(RepairChatActivity.this, error.toString(), 1).show();

                error.printStackTrace();
            }
        };
        OrderBean orderBean = new OrderBean();
        orderBean.setEmobIdShop("1");
        ArrayList<OrderBean.OrderDetailBeanList> list = new ArrayList<OrderBean.OrderDetailBeanList>();
        orderBean.setOrderDetailBeanList(list);
        service.setOrderComplete(orderBean, 1, bean.getEmobId(), callback);
    }*/


    /**
     * 取消Order部分
     */
    interface OrderCancelService {
        // /api/v1/communities/{communityId}/users/{emobIdUser}/orders/{orderId}//取消订单
//        @PUT("/api/v1/communities/{communityId}/users/{emobIdUser}/orders/{orderId}")
//        void setOrderCancel(@Header("signature")String signature, @Body OrderCancelRequest ocr, @Path("communityId") long communityId, @Path("emobIdUser") String emobIdUser, @Path("orderId") String orderId, Callback<OrderStatusBean> cb);

        @PUT("/api/v3/repairs/orders/{orderId}/cancel")
        void setOrderCancelV3(@Path("orderId") String orderId, @Body BaseBean baseBean, Callback<CommonRespBean<String>> cb);

    }

    private void cancelOrderStatus(final String orderId, final String msgId) {
        mLdDialog.show();

        BaseBean baseBean = new BaseBean();

//        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
//        OrderCancelService service = restAdapter.create(OrderCancelService.class);
        OrderCancelService service = RetrofitFactory.getInstance().create(getApplicationContext(), baseBean, OrderCancelService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> bean, retrofit.client.Response response) {
                mLdDialog.dismiss();
                if ("yes".equals(bean.getStatus())) {
                    XJMessageHelper.saveMessage2DB(msgId, orderId, 310);
                    // Toast.makeText(RepairChatActivity.this, "取消了订单", 1).show();
                    adapter.refresh();
                    XJMessageHelper.orderFail(toChatUsername);
                    listView.setSelection(listView.getCount() - 1);
                    setResult(RESULT_OK);
                } else {
                    // Toast.makeText(RepairChatActivity.this, "取消订单失败", 1).show();
                    try {
                        EMMessage message = conversation.getMessage(msgId);
                        message.setAttribute("clickable", 1);
                        EMChatManager.getInstance().updateMessageBody(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                showNetErrorToast();
//                Toast.makeText(RepairChatActivity.this, "取消订单失败", 1).show();
                try {
                    EMMessage message = conversation.getMessage(msgId);
                    message.setAttribute("clickable", 1);
                    EMChatManager.getInstance().updateMessageBody(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        service.setOrderCancelV3(orderId, baseBean, callback);
//        service.setOrderCancel(StrUtils.string2md5(Config.BANGBANG_TAG+StrUtils.dataHeader(request)),
//                request, PreferencesUtil.getCommityId(this), bean.getEmobId(), orderId, callback);
    }

    /**
     * 添加评论
     */
    interface AddCommentsService {
        // /api/v1/communities/{communityId}/shops/{emobId}/comments//添加评论
        @POST("/api/v1/communities/{communityId}/shops/{emobId}/comments")
        void addComments(@Header("signature") String signature, @Body AddCommentsRequest acr, @Path("communityId") long communityId, @Path("emobId") String emobId, Callback<OrderStatusBean> cb);
        ///api/v3/repairs/orders/{订单号}/cancel

        @POST("/api/v3/repairs/comments")
        void addCommentsV3(@Body AddCommentsRequestV3 acr, Callback<CommonRespBean<String>> cb);


    }

    private void addComment(int scoer, String content, String emobIdFrom, final String msgId, final EMMessage message, String serialId) {
        mLdDialog.show();
        AddCommentsRequestV3 addCommentsRequestV3 = new AddCommentsRequestV3();
        addCommentsRequestV3.setContent(content);
        addCommentsRequestV3.setScore(scoer);
        addCommentsRequestV3.setEmobIdFrom(bean.getEmobId());
        addCommentsRequestV3.setEmobIdTo(emobIdFrom);
        addCommentsRequestV3.setSerial(serialId);
        AddCommentsService service = RetrofitFactory.getInstance().create(getApplicationContext(), addCommentsRequestV3, AddCommentsService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> bean, retrofit.client.Response response) {
                mLdDialog.dismiss();
                if ("yes".equals(bean.getStatus())) {
                    //   Toast.makeText(RepairChatActivity.this, "评论成功" + bean.getStatus(), 1).show();
                    XJMessageHelper.updateMesageStateWithId(msgId, conversation);
                    //to username or groupid
                    message.setReceipt(toChatUsername);

                    //add message to conversation
                    conversation.addMessage(message);

                    // 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法
                    adapter.refresh();
                    listView.setSelection(listView.getCount() - 1);
                } else {
                    Toast.makeText(RepairChatActivity.this, "" + bean.getMessage(), Toast.LENGTH_SHORT).show();
//                    try {
//                        EMMessage message = conversation.getMessage(msgId);
//                        message.setAttribute("clickable", 1);
//                        EMChatManager.getInstance().updateMessageBody(message);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
//                Toast.makeText(RepairChatActivity.this, "评论失败", 1).show();
                showNetErrorToast();
            }
        };

        service.addCommentsV3(addCommentsRequestV3, callback);
    }

    @Override
    protected MessageAdapter createMessageAdapter(ChatActivity chatActivity, String toChatUsername, int chatType) {
        return new RepairChatAdapter(chatActivity, toChatUsername, chatType);
    }

}
