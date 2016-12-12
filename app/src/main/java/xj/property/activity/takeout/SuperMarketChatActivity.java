package xj.property.activity.takeout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.ChatActivity;
import xj.property.activity.contactphone.PayPreActivity;
import xj.property.adapter.MarketChatAdapter;
import xj.property.adapter.MessageAdapter;
import xj.property.beans.AddCommentsRequest;
import xj.property.beans.FastGoodsModel;
import xj.property.beans.GetShareGoodsResultPicBean;
import xj.property.beans.NotEndOrdersBean;
import xj.property.beans.OrderCancelRequest;
import xj.property.beans.OrderCompleteRequest;
import xj.property.beans.OrderStatusBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.cache.OrderDetailModel;
import xj.property.cache.OrderRepair;
import xj.property.event.FastMarketEvent;
import xj.property.netbase.BaseBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.message.XJMessageHelper;
import xj.property.utils.other.AdminUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.StarOnClickListener;
import xj.property.widget.LoadingDialog;

/**
 * Created by Administrator on 2015/4/2.
 */
public class SuperMarketChatActivity extends ChatActivity {
    private UserInfoDetailBean bean;
    protected LoadingDialog mLdDialog = null;
    // private ArrayList<OrderDetailBeanList> list;
    private Handler handler, shareHandler;
    private android.app.AlertDialog complainDialog;

    //    private  int ServiceNameWidth;
    @Override
    protected boolean forChild() {
        InitDialog();
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat);
        bean = PreferencesUtil.getLoginInfo(this);
        //  Intent it=getIntent();
        // list = (ArrayList<OrderDetailBeanList>)it .getSerializableExtra(Config.INTENT_PARMAS1);
//        ServiceNameWidth=    getResources().getDimensionPixelOffset(R.dimen.item_goods_servicename);
        findViewById(R.id.tv_Complaint).setOnClickListener(this);
        findViewById(R.id.tv_Complaint).setVisibility(View.VISIBLE);
        // if(list!=null&&list.size()>0)
        //  getSerialId(list);

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getNotEndOrders();
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
        rootView.findViewById(R.id.btn_complain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complainDialog.dismiss();
                String content = editText.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(SuperMarketChatActivity.this, "投诉内容不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
//                AdminUtils.askAdmin(SuperMarketChatActivity.this, "complaints", content,"2");
                AdminUtils.doComplainForShop(SuperMarketChatActivity.this, bean.getEmobId(), toChatUsername, content, Config.SERVANT_TYPE_SHOPTOUSU);
            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.tv_Complaint)//投诉
            complainDialog.show();
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

    private void saveMessage2DB(String msgId, String serial) {
        OrderRepair or = new OrderRepair(msgId, serial);
        or.save();
    }

    private void updateCancleMessage(String serial) {
        List<OrderRepair> list = getMessage2DB(serial);
        for (int i = 0; i < list.size(); i++) {
            updateMesageStateWithId(list.get(i).msg_id);
        }
    }

    private List<OrderRepair> getMessage2DB(String serial) {
        return new Select()
                .from(OrderRepair.class).where("serial = ?", serial)
                .execute();
    }

    private void updateMesageStateWithId(String msgId) {
        EMMessage message = conversation.getMessage(msgId);
        message.setAttribute("clickable", 0);
        EMChatManager.getInstance().updateMessageBody(message);
    }


    // This method will be called when a MessageEvent is posted
    public void onEvent(FastMarketEvent event) {
        // Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show();
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
        TextMessageBody txtBody = null;
        switch (CMD_CODE) {
          /*  case 200:
                txtBody   = new TextMessageBody("[订单]已下单，等待店家确认");
                message.addBody(txtBody);
                // 添加ext属性
                message.setAttribute("avatar", bean.getAvatar());
                message.setAttribute("nickname", bean.getNickname());
                message.setAttribute("CMD_CODE", CMD_CODE);
                message.setAttribute("CMD_DETAIL", jsonObject.optString(Config.EXPKey_CMD_DETAIL));
                message.setAttribute("serial", jsonObject.optString(Config.EXPKey_serial));
                message.setAttribute("clickable", 1);
                message.setAttribute("isShowAvatar", 1);
                message.setAttribute("msgId", message.getMsgId());
                //to username or groupid
                message.setReceipt(toChatUsername);
                //add message to conversation
                conversation.addMessage(message);
                saveMessage2DB(message.getMsgId(), jsonObject.optString(Config.EXPKey_serial));
                // 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法
                adapter.refresh();
                listView.setSelection(listView.getCount() - 1);
//    mEditTextContent.setText("");
                setResult(RESULT_OK);
                break;*/
            case 203:  /// 去支付
                txtBody = new TextMessageBody("[订单]已支付，等待店家送货");
                message.addBody(txtBody);
                //TODO  支付，回调中变色

                message.setAttribute("avatar", bean.getAvatar());
                message.setAttribute("nickname", bean.getNickname());
                message.setAttribute("CMD_CODE", CMD_CODE);
                message.setAttribute(Config.EXPKey_VERSION, "2");
                message.setAttribute(Config.EXPKey_ADDRESS, bean.getUserFloor() + bean.getUserUnit() + bean.getRoom());
                final FastGoodsModel fastGoodsModel = new FastGoodsModel();
                fastGoodsModel.emobIdShop = toChatUsername;
                fastGoodsModel.sort = 1;
                fastGoodsModel.emobIdUser = SuperMarketChatActivity.this.bean.getEmobId();
                final OrderDetailModel orderDetailModel = new Select().from(OrderDetailModel.class).where("serial = ?", jsonObject.optString(Config.EXPKey_serial)).executeSingle();
                if (orderDetailModel == null) {
                    Toast.makeText(this, "本地数据库异常", Toast.LENGTH_LONG).show();
                    return;
                }
                fastGoodsModel.totalPrice = Double.parseDouble(orderDetailModel.getTotal_price()) + "";
                fastGoodsModel.totalCount = orderDetailModel.getTotal_count();
                handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {

                        setPayMethod(jsonObject.optString(Config.EXPKey_serial), msg.what == PayCodeOnline);
                        try {
                            JSONArray array = new JSONArray(orderDetailModel.getOrderDetailBeanList());
                            orderDetailModel.isOnline = msg.what == PayCodeOnline;
                            orderDetailModel.save();
                            JSONObject json = new JSONObject(new Gson().toJson(fastGoodsModel));
                            json.put(Config.EXPKey_nickname, bean.getNickname());
                            json.put("orderDetailBeanList", array);
                            json.put(Config.EXPKey_ADDRESS, bean.getUserFloor() + bean.getUserUnit() + bean.getRoom());
                            json.put(Config.EXPKey_BONUS, msg.arg1);
                            json.put("buyer", bean.getNickname());
                            json.put("emobIdShop", toChatUsername);
                            json.put("emobIdUser", bean.getUsername());
                            json.put(Config.EXPKey_PayMethod, msg.what);
                            json.put(Config.EXPKey_totalPrice, orderDetailModel.getTotal_price());
                            Log.i("onion", "json" + json.toString());
                            message.setAttribute(Config.EXPKey_CMD_DETAIL, json.toString());
                        } catch (Exception e) {
                            Log.e("onion", e.toString());
                        }
                        message.setAttribute("serial", jsonObject.optString(Config.EXPKey_serial));
                        message.setAttribute("clickable", 1);
                        message.setAttribute("isShowAvatar", 0);
                        message.setAttribute("msgId", message.getMsgId());
                        updateMesageStateWithId(jsonObject.optString(Config.EXPKey_msgId));
                        Log.i("onion", jsonObject.optString(Config.EXPKey_msgId));
                        //to username or groupid
                        message.setReceipt(toChatUsername);
                        //add message to conversation
                        conversation.addMessage(message);
                        // saveMessage2DB(message.getMsgId(), jsonObject.optString(Config.EXPKey_serial));
                        // 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法
                        adapter.refresh();
                        listView.setSelection(listView.getCount() - 1);
                        setResult(RESULT_OK);
                    }
                };
                Intent intent = new Intent(this, PayPreActivity.class);
                intent.putExtra(Config.PAYBODY, "body");
                intent.putExtra(Config.EXPKey_serial, jsonObject.optString(Config.EXPKey_serial));
                intent.putExtra("emobIdShop", toChatUsername);
                startActivityForResult(intent, PayCodeOnline);

                break;
            case 204://确认收货

                showToast("确认收货");

                txtBody = new TextMessageBody("[订单]已确认收货...");
                message.addBody(txtBody);
                String serial = jsonObject.optString(Config.EXPKey_serial);
                //   saveMessage2DB(message.getMsgId(),serial);
                //根据订单查到203的收获方式，及价格
                OrderDetailModel orderDetailModel204 = new Select().from(OrderDetailModel.class).where("serial = ?", serial).executeSingle();
                if (orderDetailModel204 == null) {
                    Toast.makeText(this, "本地数据库异常", Toast.LENGTH_LONG).show();
                    return;
                }
                changeOrderStatus(serial, orderDetailModel204.isOnline, orderDetailModel204.total_price, message);
                message.setAttribute(Config.EXPKey_ADDRESS, bean.getUserFloor() + bean.getUserUnit() + bean.getRoom() + "");
                break;
            case 205: /// 请评价
                showToast("[订单]已评价");

                txtBody = new TextMessageBody("[订单]已评价...");
                message.addBody(txtBody);
                message.setAttribute(Config.EXPKey_ADDRESS, bean.getUserFloor() + bean.getUserUnit() + bean.getRoom() + "");
                View rootView = View.inflate(this, R.layout.dialog_eva, null);
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
                        // Toast.makeText(SuperMarketChatActivity.this, "谢谢评价", Toast.LENGTH_LONG).show();

                        // 添加ext属性
                        message.setAttribute("nickname", bean.getNickname());
                        message.setAttribute("avatar", bean.getAvatar());
                        JSONObject js = new JSONObject();
                        String content = et.getText().toString();
                        if (content.length() > 100) {
                            Toast.makeText(SuperMarketChatActivity.this, "评论字数过长，请限制在100字以内。", Toast.LENGTH_LONG).show();
                            return;
                        }
                        dialog.dismiss();
                        if (content.length() == 0) {
                            content = "送货快，服务态度真好，点赞！";
                        }
                        try {
                            js.put(Config.EXPKey_EVA, content);
                            js.put(Config.EXPKey_Star, listener.starnum + "");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        message.setAttribute("CMD_CODE", 205);
                        message.setAttribute("CMD_DETAIL", js.toString());
                        message.setAttribute(Config.EXPKey_serial, jsonObject.optString(Config.EXPKey_serial));
                        message.setAttribute("clickable", 1);
                        message.setAttribute("isShowAvatar", 0);
                        message.setAttribute(Config.EXPKey_msgId, jsonObject.optString(Config.EXPKey_msgId));
                        addComment(listener.starnum, content, toChatUsername, jsonObject.optString(Config.EXPKey_msgId), message);

                    }
                });
                dialog.show();
                break;
            case 210://业主取消请求
                txtBody = new TextMessageBody("[订单]业主取消请求...");
                message.addBody(txtBody);
                message.setAttribute(Config.EXPKey_ADDRESS, bean.getUserFloor() + bean.getUserUnit() + bean.getRoom() + "");
                cancelOrderStatus(jsonObject.optString(Config.EXPKey_serial, ""), jsonObject.optString(Config.EXPKey_msgId, ""), message);
                break;

            case 2072://分享到生活圈
                showToast("分享到生活圈");
                shareHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        updateMesageStateWithId(jsonObject.optString(Config.EXPKey_msgId));
                    }
                };
                getSharePic(jsonObject.optString(Config.EXPKey_serial));
                break;

            default:
                super.sendText(jsonObject.optString(Config.EXPKey_serial));
                break;
        }
    }

    int PayCodeOnline = 0;
    int PayCodeOffline = 1;
    int shareCode = 100;

    void toPay(int serial, double price) {
        // Intent intent=new Intent(this, PayDemoActivity.class);

        Intent intent = new Intent(this, PayPreActivity.class);
        intent.putExtra(Config.PAYBODY, "body");
        intent.putExtra(Config.EXPKey_serial, serial);
        intent.putExtra(Config.EXPKey_totalPrice, price);
        startActivityForResult(intent, PayCodeOnline);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PayCodeOnline) {
            if (resultCode == RESULT_OK && handler != null) {
                // handler.sendEmptyMessage(PayCodeOnline);
                //   String address=data.getStringExtra("address");
                Message message = new Message();
                message.what = PayCodeOnline;
                message.arg1 = data.getIntExtra(Config.EXPKey_BONUS, 0);
                //      message.obj=address;
                handler.sendMessage(message);
                if ("yes".equals(PreferencesUtil.getShowBonuscoin(getApplicationContext()))) {
                    PreferencesUtil.saveNewBangBangBi(this, true);
                }
            } else if (resultCode == PayCodeOffline) {/// 货到付款
                String address = data.getStringExtra("address");
                Message message = new Message();
                message.what = PayCodeOffline;
                message.obj = address;
                handler.sendMessage(message);
                if ("yes".equals(PreferencesUtil.getShowBonuscoin(getApplicationContext()))) {
                    PreferencesUtil.saveNewBangBangBi(this, true);
                }
                //  handler.sendEmptyMessage(PayCodeOffline);
            } else {
                //Toast.makeText(this,"支付异常",Toast.LENGTH_LONG).show();

            }
        }

        if (requestCode == shareCode && resultCode == RESULT_OK) {
            showToast("分享成功");
            shareHandler.sendEmptyMessage(0);
            PreferencesUtil.saveNewBangBangBi(this, true);
        }
    }


    /**
     * 提交Order部分
     */
    interface OrderCompleteService {
        ///api/v1/communities/{communityId}/users/{emobIdUser}/orders/{orderId}
        @PUT("/api/v1/communities/{communityId}/users/{emobIdUser}/orders/{orderId}")
        void setOrderComplete(@Header("signature") String signature, @Body OrderCompleteRequest ocr, @Path("communityId") int communityId, @Path("emobIdUser") String emobIdUser, @Path("orderId") String orderId, Callback<OrderStatusBean> cb);
    }

    private void changeOrderStatus(final String orderId, boolean isOnLine, String orderPrice, final EMMessage message) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE2).build();
        OrderCompleteService service = restAdapter.create(OrderCompleteService.class);
        Callback<OrderStatusBean> callback = new Callback<OrderStatusBean>() {
            @Override
            public void success(OrderStatusBean orderStatusBean, Response response) {
                if (null == orderStatusBean || !"yes".equals(orderStatusBean.getStatus())) return;
                message.setAttribute("avatar", bean.getAvatar());
                message.setAttribute("nickname", bean.getNickname());
                message.setAttribute("serial", orderId);
                message.setAttribute("CMD_CODE", 204);
                message.setAttribute("clickable", 1);
                message.setAttribute(Config.EXPKey_ADDRESS, bean.getUserFloor() + "楼" + bean.getUserUnit() + "单元" + bean.getRoom());
                message.setAttribute("msgId", message.getMsgId());
                message.setAttribute("isShowAvatar", 0);
                message.setAttribute("CMD_DETAIL", new JSONObject().toString());
                message.setReceipt(toChatUsername);
                //add message to conversation
                conversation.addMessage(message);
                adapter.refresh();
                listView.setSelection(listView.getCount() - 1);
            }

            @Override
            public void failure(RetrofitError error) {
                showNetErrorToast();
//                Toast.makeText(SuperMarketChatActivity.this, error.toString(), 1).show();
                Log.e("onion", error.toString());
                error.printStackTrace();
            }
        };
        OrderCompleteRequest orderCompleteRequest = new OrderCompleteRequest();
        orderCompleteRequest.status = "ended";
        orderCompleteRequest.online = isOnLine ? "yes" : "no";
        orderCompleteRequest.orderPrice = orderPrice;
        orderCompleteRequest.setMethod("PUT");
        service.setOrderComplete(StrUtils.string2md5(Config.BANGBANG_TAG + StrUtils.dataHeader(orderCompleteRequest)),
                orderCompleteRequest, PreferencesUtil.getCommityId(this), bean.getEmobId(), orderId, callback);
    }

    /**
     * 修改支付状态， 修改支付方式，线上，线下
     *
     * @param orderId
     * @param isOnLine
     */
    private void setPayMethod(final String orderId, boolean isOnLine) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE2).build();
        OrderCompleteService service = restAdapter.create(OrderCompleteService.class);
        Callback<OrderStatusBean> callback = new Callback<OrderStatusBean>() {
            @Override
            public void success(OrderStatusBean orderStatusBean, Response response) {
                if (null == orderStatusBean || !"yes".equals(orderStatusBean.getStatus())) return;
            }

            @Override
            public void failure(RetrofitError error) {
                showNetErrorToast();
//                Toast.makeText(SuperMarketChatActivity.this, error.toString(), 1).show();
                Log.e("onion", error.toString());
                error.printStackTrace();
            }
        };
        OrderCompleteRequest orderCompleteRequest = new OrderCompleteRequest();
        orderCompleteRequest.online = isOnLine ? "yes" : "no";
        orderCompleteRequest.setMethod("PUT");
        orderCompleteRequest.setStatus("paid");
        service.setOrderComplete(StrUtils.string2md5(Config.BANGBANG_TAG + StrUtils.dataHeader(orderCompleteRequest)),
                orderCompleteRequest, PreferencesUtil.getCommityId(this), bean.getEmobId(), orderId, callback);
    }


    /**
     * 取消Order部分
     * v3 2016/03/17
     */
    interface OrderCancelService {
        // /api/v1/communities/{communityId}/users/{emobIdUser}/orders/{orderId}//取消订单
//        @PUT("/api/v1/communities/{communityId}/users/{emobIdUser}/orders/{orderId}")
//        void setOrderCancel(@Header("signature")String signature, @Body OrderCancelRequest ocr, @Path("communityId") int communityId, @Path("emobIdUser") String emobIdUser, @Path("orderId") String orderId, Callback<OrderStatusBean> cb);
//        @PUT("/api/v1/communities/{communityId}/users/{emobIdUser}/orders/{orderId}")


        //PUT {baseUrl}/api/v3/shopOrders/{订单号}/cancel

        @PUT("/api/v3/shopOrders/{orderId}/cancel")
        void setOrderCancel(@Path("orderId") String orderId, @Body BaseBean baseBean, Callback<CommonRespBean<String>> cb);
    }

    /**
     * 取消Order部分
     * v3 2016/03/17
     */
    private void cancelOrderStatus(final String orderId, final String msgId, final EMMessage message) {
        mLdDialog.show();
        BaseBean baseBean = new BaseBean();
        OrderCancelService service = RetrofitFactory.getInstance().create(getmContext(), baseBean, OrderCancelService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> bean, Response response) {
                mLdDialog.dismiss();
                if ("yes".equals(bean.getStatus())) {
                    //  Toast.makeText(SuperMarketChatActivity.this, "取消了订单", 1).show();
                    XJMessageHelper.saveMessage2DB(msgId, orderId, 210);
                    TextMessageBody txtBody = new TextMessageBody("[订单]订单已取消...");
                    message.addBody(txtBody);
                    // 添加ext属性
                    message.setAttribute("avatar", SuperMarketChatActivity.this.bean.getAvatar());
                    message.setAttribute("nickname", SuperMarketChatActivity.this.bean.getNickname());
                    message.setAttribute("CMD_CODE", 210);
                    message.setAttribute("CMD_DETAIL", "{}");
                    message.setAttribute("serial", orderId);
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
                } else {
                    showToast("取消订单失败：" + bean.getMessage());
                    try {
                        EMMessage message = conversation.getMessage(msgId);
                        message.setAttribute("clickable", 1);
                        EMChatManager.getInstance().updateMessageBody(message);
                        adapter.refresh();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
//                Toast.makeText(SuperMarketChatActivity.this, "取消订单失败", 1).show();
                showNetErrorToast();
                try {
                    EMMessage message = conversation.getMessage(msgId);
                    message.setAttribute("clickable", 1);
                    EMChatManager.getInstance().updateMessageBody(message);
                    adapter.refresh();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        service.setOrderCancel(orderId, baseBean, callback);
    }


    /**
     * 添加评论
     */
    interface AddCommentsService {
        // /api/v1/communities/{communityId}/shops/{emobId}/comments//添加评论

        @POST("/api/v1/communities/{communityId}/shops/{emobId}/comments")
        void addComments(@Header("signature") String signature, @Body AddCommentsRequest acr, @Path("communityId") int communityId, @Path("emobId") String emobId, Callback<OrderStatusBean> cb);
    }

    private void addComment(int scoer, String content, String emobIdFrom, final String msgId, final EMMessage message) {
        mLdDialog.show();
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        AddCommentsService service = restAdapter.create(AddCommentsService.class);
        Callback<OrderStatusBean> callback = new Callback<OrderStatusBean>() {
            @Override
            public void success(OrderStatusBean bean, Response response) {
                mLdDialog.dismiss();
                if ("yes".equals(bean.getStatus())) {
                    // Toast.makeText(SuperMarketChatActivity.this, "评论成功" + bean.getStatus(), 1).show();
                    //to username or groupid
                    message.setReceipt(toChatUsername);
                    XJMessageHelper.updateMesageStateWithId(msgId, EMChatManager.getInstance().getConversation(message.getTo()));
                    //add message to conversation
                    conversation.addMessage(message);

                    // 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法
                    adapter.refresh();
                    listView.setSelection(listView.getCount() - 1);
                } else {
                    showToast("评论失败：" + bean.getMessage());

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
                showNetErrorToast();
            }
        };
        AddCommentsRequest request = new AddCommentsRequest();
        request.emobIdFrom = bean.getEmobId();
        Log.i("onion", "toEmobId" + emobIdFrom);//d5b24fe936dfc56ee064c98b8fcacb84
        request.score = scoer;
        request.content = content;
        service.addComments(StrUtils.string2md5(Config.BANGBANG_TAG + StrUtils.dataHeader(request)),
                request, PreferencesUtil.getCommityId(this), emobIdFrom, callback);
    }

    @Override
    protected MessageAdapter createMessageAdapter(ChatActivity chatActivity, String toChatUsername, int chatType) {
        return new MarketChatAdapter(chatActivity, toChatUsername, chatType);
    }


    interface GetPicService {
//        @GET("/api/v1/communities/{communityId}/circles/{serial}/share")
//        void getActList(@Path("communityId") int communityId, @Path("serial") String serial, Callback<GetShareGoodsResultPicBean> cb);
//        @GET("/api/v1/communities/{communityId}/circles/{serial}/share")


        ////api/v3/lifeCircles/share/{快店订单号}

//        /api/v3/shopOrders/{快店订单号}/share
        @GET("/api/v3/shopOrders/{serial}/share")
            /// v3 2016/03/04 分享快店订单到生活圈
        void getActList(@Path("serial") String serial, Callback<CommonRespBean<GetShareGoodsResultPicBean>> cb);
    }

    /**
     * 获取分享到生活圈的图片
     *
     * @param orderId
     */
    private void getSharePic(final String orderId) {
        mLdDialog.show();
        GetPicService service = RetrofitFactory.getInstance().create(getmContext(), GetPicService.class);
        Callback<CommonRespBean<GetShareGoodsResultPicBean>> callback = new Callback<CommonRespBean<GetShareGoodsResultPicBean>>() {
            @Override
            public void success(CommonRespBean<GetShareGoodsResultPicBean> bean, Response response) {
                if ("yes".equals(bean.getStatus())) {
                    Intent shareIntent = new Intent(SuperMarketChatActivity.this, ShareGoodsLifeCircleActivity.class);
                    shareIntent.putExtra("orderId", orderId);
                    shareIntent.putExtra("photos", bean.getData().getList());
                    shareIntent.putExtra("shopid", toChatUsername);
                    startActivityForResult(shareIntent, shareCode);
                } else {
                    showToast("数据请求异常");
                }
                mLdDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                error.printStackTrace();
                showNetErrorToast();
            }
        };
        service.getActList(orderId, callback);
    }


    interface GetNotEndOrdersService {
        @GET("/api/v1/communities/{communityId}/users/{emobIdUser}/orders/notEndOrders")
        void notEndOrders(@Path("communityId") int communityId, @Path("emobIdUser") String emobId, @QueryMap Map<String, String> map, Callback<NotEndOrdersBean> cb);
    }


    /**
     * 拉取未完成的订单
     */
    private void getNotEndOrders() {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        GetNotEndOrdersService service = restAdapter.create(GetNotEndOrdersService.class);
        Callback<NotEndOrdersBean> callback = new Callback<NotEndOrdersBean>() {
            @Override
            public void success(final NotEndOrdersBean notEndOrdersBean, Response response) {
                final List<EMMessage> msgs = new ArrayList<EMMessage>();
                if ("yes".equals(notEndOrdersBean.getStatus())) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < notEndOrdersBean.getInfo().size(); i++) {

                                if ("new".equals(notEndOrdersBean.getInfo().get(i).getStatus()) &&
                                        XJMessageHelper.getOrderModel(notEndOrdersBean.getInfo().get(i).getSerial(), 200) == null) {
                                    Log.i("debbug", "创建了一个新的msg");
                                    EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
                                    TextMessageBody txtBody = new TextMessageBody("[订单]已下单，等待店家确认");
                                    // 设置消息body
                                    message.addBody(txtBody);
                                    message.setFrom(bean.getEmobId());
                                    message.setTo(toChatUsername);
                                    message.setAttribute("shopType", "2");
                                    message.setAttribute("avatar", bean.getAvatar());
                                    message.setAttribute("nickname", bean.getNickname());
                                    message.setAttribute("CMD_CODE", 200);
                                    message.setAttribute("CMD_DETAIL", new Gson().toJsonTree(createFastGoodsModel(notEndOrdersBean.getInfo().get(i))).toString());
                                    message.setAttribute(Config.EXPKey_serial, notEndOrdersBean.getInfo().get(i).getSerial());
                                    message.setAttribute("clickable", 1);
                                    message.setAttribute("isShowAvatar", 1);
                                    message.setAttribute(Config.EXPKey_ADDRESS, bean.getUserFloor() + bean.getUserUnit() + bean.getRoom() + "");
                                    message.setMsgTime(System.currentTimeMillis());
//                                message.setAttribute("msgId", message.getMsgId());
                                    msgs.add(message);
                                    EMChatManager.getInstance().importMessage(message, false);
                                    conversation.addMessage(message);
                                    XJMessageHelper.saveMessage2DB(message.getMsgId(), message.getStringAttribute(Config.EXPKey_serial, ""), 200);
                                    XJMessageHelper.operatNewMessage(getmContext(), message);
//                                    XJMessageHelper.operatNewMessage(message);
//                            listView.setAdapter(adapter);
//                            adapter.refresh();
//                            listView.setSelection(listView.getCount() - 1);
//                            setResult(RESULT_OK);
                                } else if ("ongoing".equals(notEndOrdersBean.getInfo().get(i).getStatus()) &&
                                        XJMessageHelper.getOrderModel(notEndOrdersBean.getInfo().get(i).getSerial(), 201) == null) {
//                                    FastGoodsModel fastGoodsModel = createFastGoodsModel(notEndOrdersBean.getInfo().get(i));
//                                    fastGoodsModel.emobIdShop = SharedPreferenceUtils.getEmobId(SupermarketChatActivity.this);
//                                    fastGoodsModel.sort = 1;
//                                    fastGoodsModel.setEmobIdUser(toChatUsername);
//                                    fastGoodsModel.setPayMethod(1);
//                                    fastGoodsModel.setNickname(nickname);
//                                    fastGoodsModel.setAddress(userAddress);

                                    EMMessage message = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
                                    TextMessageBody txtBody = new TextMessageBody("已接单，请选择付款方式");
                                    // 设置消息body
                                    message.addBody(txtBody);
                                    message.setFrom(toChatUsername);
                                    message.setTo(PreferencesUtil.getLoginInfo(SuperMarketChatActivity.this).getEmobId());
                                    message.setAttribute("avatar", PreferencesUtil.getLoginInfo(SuperMarketChatActivity.this).getAvatar());
                                    message.setAttribute("nickname", PreferencesUtil.getLoginInfo(SuperMarketChatActivity.this).getNickname());
                                    message.setAttribute("CMD_CODE", 201);
                                    message.setAttribute("CMD_DETAIL", new Gson().toJsonTree(createFastGoodsModel(notEndOrdersBean.getInfo().get(i))).toString());
                                    message.setAttribute(Config.EXPKey_serial, notEndOrdersBean.getInfo().get(i).getSerial());
                                    message.setAttribute("clickable", 1);
                                    message.setAttribute("isShowAvatar", 1);
                                    message.setAttribute("shopType", "2");
                                    message.setAttribute(Config.EXPKey_ADDRESS, bean.getUserFloor() + bean.getUserUnit() + bean.getRoom() + "");
                                    message.setMsgTime(System.currentTimeMillis());
//                                    try {
//
//                                        JSONObject json = new JSONObject(new Gson().toJson(fastGoodsModel));
//                                        message.setAttribute(Config.EXPKey_CMD_DETAIL, json.toString());
//                                    } catch (Exception e) {
//                                        Log.e("onion", e.toString());
//                                    }
//                                message.setAttribute("msgId", message.getMsgId());
                                    msgs.add(message);
                                    EMChatManager.getInstance().importMessage(message, false);
                                    conversation.addMessage(message);
                                    XJMessageHelper.operatNewMessage(getmContext(), message);
                                    XJMessageHelper.saveMessage2DB(message.getMsgId(), message.getStringAttribute(Config.EXPKey_serial, ""), 201);
                                }

                            }
//                    EMChatManager.getInstance().
//                    onConversationInit();
//                    onListViewCreation();
//                    EMChatManager.getInstance().importMessages(msgs);
                            refreshUIWithNewMessage();
                        }
                    }).start();

//                    listView.setAdapter(adapter);
//                    adapter.refresh();
//                    listView.setSelection(listView.getCount() - 1);
//                    EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
//                    TextMessageBody txtBody = new TextMessageBody("交易已完成");
//                    // 设置消息body
//                    message.addBody(txtBody);
//                    message.setReceipt(toChatUsername);
//                    EMChatManager.getInstance().importMessage(message,true);
//                    listView.setAdapter(adapter);
//
//                    adapter.refresh();
//                    adapter.notifyDataSetChanged();
//                    listView.setSelection(listView.getCount() - 1);
//                    setResult(RESULT_OK);
                } else {

                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("error", "error" + error.toString());
            }
        };
        Map<String, String> map = new HashMap<String, String>();
        map.put("q", toChatUsername);
        Log.i("debbug", "看一下tochatusername=" + toChatUsername);
        service.notEndOrders(PreferencesUtil.getCommityId(this), bean.getEmobId(), map, callback);
    }

    private FastGoodsModel createFastGoodsModel(NotEndOrdersBean.InfoEntity info) {
        FastGoodsModel fastGoodsModel = new FastGoodsModel();
        OrderDetailModel orderDetailModel = new OrderDetailModel();
        int totalCount = 0;
        String totalPrice;
        double doubleTotalPrice = 0;
        for (int i = 0; i < info.getOrderDetailBeanList().size(); i++) {
            totalCount = totalCount + info.getOrderDetailBeanList().get(i).getCount();
            doubleTotalPrice = doubleTotalPrice + Double.parseDouble(info.getOrderDetailBeanList().get(i).getPrice()) * info.getOrderDetailBeanList().get(i).getCount();
        }
        totalPrice = "" + doubleTotalPrice;
        fastGoodsModel.setTotalCount(totalCount);
        fastGoodsModel.totalPrice = totalPrice;
        fastGoodsModel.setOrderDetailBeanList(info.getOrderDetailBeanList());

        return fastGoodsModel;
    }
}
