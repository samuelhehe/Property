package xj.property.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;

import org.json.JSONObject;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.PUT;
import xj.property.R;
import xj.property.activity.repair.RepairChatActivity;
import xj.property.beans.OrdersBeanRequestV3;
import xj.property.event.RepairButtonEvent;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.netbasebean.OrderConfrimPayedReqBean;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.message.XJMessageHelper;
import xj.property.utils.other.Config;
import xj.property.utils.other.StarOnClickListener;

/**
 * Created by Administrator on 2015/3/30.
 */
public class RepairChatAdapter extends MessageAdapter {

    private int green, between;

    public RepairChatAdapter(Context context, String username, int chatType) {
        super(context, username, chatType);
        green = context.getResources().getColor(R.color.chat_btn_green);
        between = context.getResources().getColor(R.color.chat_btn_between);
    }

    @Override
    protected View forChild(EMMessage message, int position) {
        if (message.getType() != EMMessage.Type.TXT) return null;
//        if(message.getIntAttribute(Config.EXPKey_CMD_CODE,0)==0)return null;
        switch (message.getIntAttribute(Config.EXPKey_CMD_CODE, 0)) {
            case 305:
                return null;
            case 306:
                return null;
        }
        return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_extrepair, null) : inflater.inflate(
                R.layout.row_sent_extrepair, null);
    }

    @Override
    protected void forChildHolder(ViewHolder holder, View convertView) {
        holder.btn_order_operation = (Button) convertView.findViewById(R.id.btn_order_operation);
        holder.btn_accept = (Button) convertView.findViewById(R.id.btn_accept);
        holder.tv_exp_content = (TextView) convertView.findViewById(R.id.tv_exp_content);
        holder.tv_expmsg = (TextView) convertView.findViewById(R.id.tv_msg);
        holder.tv_serial = (TextView) convertView.findViewById(R.id.tv_serial);
        holder.ll_btns = (LinearLayout) convertView.findViewById(R.id.ll_btns);
        holder.ll_exp = (LinearLayout) convertView.findViewById(R.id.ll_exp);
        holder.rl_tv_chatcontent_withExt = (RelativeLayout) convertView.findViewById(R.id.rl_tv_chatcontent_withExt);
        holder.ll_star = (LinearLayout) convertView.findViewById(R.id.ll_eva);
        holder.tv_line = (TextView) convertView.findViewById(R.id.tv_line);
    }

    @Override
    protected void handleTextMessageWithExt(final EMMessage message, ViewHolder holder, final int position, int CMD_CODE) {
        //cmd detail in json
        String cmdDetail;
        //content to show
        String content = "";
        final String serial = message.getStringAttribute(Config.EXPKey_serial, "");
        switch (CMD_CODE) {
            case 300://发送请求
                holder.rl_tv_chatcontent_withExt.setVisibility(View.VISIBLE);
                holder.rl_tv_chatcontent_withExt.setSelected(false);
                holder.ll_exp.setVisibility(View.VISIBLE);
                holder.ll_btns.setVisibility(View.VISIBLE);
                holder.tv_expmsg.setVisibility(View.GONE);
                holder.tv_serial.setText("订单号:" + serial);
                content = "订单已发出，请等待师傅确认";
                holder.tv_exp_content.setText(content);
                holder.btn_order_operation.setText("取消订单");
                holder.btn_accept.setVisibility(View.GONE);
                holder.ll_star.setVisibility(View.GONE);
                if (!updateColcor4Click2(message.getIntAttribute("clickable", 0), holder)) {
                    holder.btn_order_operation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //refresh message state
                            XJMessageHelper.updateMessageStateWithMsg(message);
                            //send 203 message
                            try {
                                JSONObject js = new JSONObject();
                                js.put(Config.EXPKey_serial, serial);
                                js.put(Config.EXPKey_msgId, message.getMsgId());
                                EventBus.getDefault().post(new RepairButtonEvent("业主取消请求", js, position, 1, 310));
                            } catch (Exception e) {
                                Log.e("onion", e.toString());
                            }
                        }
                    });
                    //XJMessageHelper.saveMessage2DB(message.getMsgId(), serial,300);
                }
                //change the related 200 message state, the relationship is bind via attr "msgId"
                // updateMesageStateWithId(message.getStringAttribute("msgId",""));
//                refresh();

                //set avatar
                handleAvatar(message, holder, position, message.getIntAttribute("isShowAvatar", 1));

                break;
            case 301://师傅接受订单
                holder.tv_expmsg.setVisibility(View.VISIBLE);
                holder.ll_exp.setVisibility(View.VISIBLE);
                holder.ll_btns.setVisibility(View.VISIBLE);
                holder.rl_tv_chatcontent_withExt.setVisibility(View.GONE);
                content = "师傅已接受订单" + message.getStringAttribute(Config.EXPKey_serial, "") + "，不可更改";
                holder.tv_expmsg.setText(content);
//                if (message.getIntAttribute(Config.EXPKey_clickable, 0) == 1) {
//                    try {
//                        XJMessageHelper.     updateMessageStateWithMsg(message);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
                handleAvatar(message, holder, position, 0);
                break;
            case 302:
                holder.rl_tv_chatcontent_withExt.setVisibility(View.GONE);
                holder.tv_expmsg.setVisibility(View.VISIBLE);
                String reason = message.getStringAttribute(Config.EXPKey_REASON, "");
                holder.tv_expmsg.setText("师傅拒绝了请求,拒绝原因" + reason);
                handleAvatar(message, holder, position, 0);
                break;
            case 303://师傅发起金额请求
                holder.rl_tv_chatcontent_withExt.setVisibility(View.VISIBLE);
                holder.ll_exp.setVisibility(View.VISIBLE);
                holder.ll_btns.setVisibility(View.VISIBLE);
                holder.tv_expmsg.setVisibility(View.GONE);
                holder.tv_serial.setText("订单号:" + serial);
                String price = "";
                final JSONObject js = new JSONObject();
                try {
                    price = new JSONObject(message.getStringAttribute(Config.EXPKey_CMD_DETAIL, "")).getString(Config.EXPKey_totalPrice);
                    js.put(Config.EXPKey_totalPrice, price);
                    js.put(Config.EXPKey_serial, serial);
                } catch (Exception e) {
                    Log.e("onion", e.toString());
                }
                final String innerprice = price;
                holder.tv_exp_content.setText("对方发起一笔费用确认。结算费用为" + price + "元");
                holder.btn_order_operation.setText("拒绝");
                holder.btn_accept.setVisibility(View.VISIBLE);
                holder.btn_accept.setText("接受");
                if (!updateColcor4Click(message.getIntAttribute("clickable", 0), holder)) {
                    holder.btn_accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //refresh message state
                            XJMessageHelper.updateMessageStateWithMsg(message);
                            EventBus.getDefault().post(new RepairButtonEvent("费用确认", js, position, 1, 304));
                            ///// 可以在这里加确认金额
                            doConfirmPayed(context, serial, innerprice);
                            refresh();
                            Log.i("onion", "js" + js.toString());

                        }
                    });
                    holder.btn_order_operation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            XJMessageHelper.updateMessageStateWithMsg(message);
                            EventBus.getDefault().post(new RepairButtonEvent("拒绝费用", js, position, 1, 312));
                            refresh();
                            //     EventBus.getDefault().post(new ButtonOnClickEvent("拒绝费用", position, 1, 304));
                        }
                    });

                }
                //change the related 200 message state, the relationship is bind via attr "msgId"
                // updateMesageStateWithId(message.getStringAttribute("msgId",""));
                handleAvatar(message, holder, position, message.getIntAttribute("isShowAvatar", 0));

                break;

            case 304:
                holder.rl_tv_chatcontent_withExt.setVisibility(View.GONE);
                holder.tv_expmsg.setVisibility(View.VISIBLE);
                try {
                    content = "双方已经确认费用为" + new JSONObject(message.getStringAttribute(Config.EXPKey_CMD_DETAIL)).optString(Config.EXPKey_totalPrice) + "元";
                    holder.tv_expmsg.setText(content);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handleAvatar(message, holder, position, 0);
                break;
            case 307:
                holder.rl_tv_chatcontent_withExt.setVisibility(View.VISIBLE);
                holder.ll_btns.setVisibility(View.VISIBLE);
                holder.ll_exp.setVisibility(View.VISIBLE);
                holder.tv_expmsg.setVisibility(View.GONE);
                holder.tv_serial.setText("订单号:" + serial);
                String name = message.getStringAttribute(Config.EXPKey_REASON, null);
                if (name != null) {
                    content = name + "已维修完成(" + StrUtils.getTime4Millions(Integer.parseInt(message.getStringAttribute("startTime", "0")) * 1000L) + "到" + StrUtils.getTime4Millions(message.getMsgTime()) + "),请对师傅做出评价。";
                } else
                    content = "已结束!请对此次服务做出评价";
                holder.tv_exp_content.setText(content);
                holder.btn_order_operation.setText("去评价");
                holder.btn_accept.setVisibility(View.GONE);
                if (!updateColcor4Click(message.getIntAttribute("clickable", 0), holder)) {
                    holder.btn_order_operation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //send 203 message
                            try {
                                JSONObject js = new JSONObject();
                                js.put(Config.EXPKey_serial, serial);
                                js.put(Config.EXPKey_msgId, message.getMsgId());
                                EventBus.getDefault().post(new RepairButtonEvent("请评价", js, position, 1, 308));
                            } catch (Exception e) {
                                Log.e("onion", e.toString());
                            }
                        }
                    });

                }
                //refresh();
                //set avatar
                handleAvatar(message, holder, position, 0);

                break;
            case 308:
                holder.rl_tv_chatcontent_withExt.setVisibility(View.VISIBLE);

                holder.rl_tv_chatcontent_withExt.setSelected(false);
                holder.tv_serial.setTextColor(Color.GRAY);
                holder.tv_exp_content.setTextColor(Color.GRAY);
                holder.tv_line.setBackgroundColor(context.getResources().getColor(R.color.item_view_line));

                holder.ll_exp.setVisibility(View.VISIBLE);
                holder.ll_btns.setVisibility(View.GONE);
                holder.ll_star.setVisibility(View.VISIBLE);
                holder.ll_star.findViewById(R.id.iv_star0).setVisibility(View.VISIBLE);
                try {
                    JSONObject jsonObject = new JSONObject(message.getStringAttribute(Config.EXPKey_CMD_DETAIL, ""));
                    holder.tv_serial.setText("订单号" + message.getStringAttribute(Config.EXPKey_serial, ""));
                    holder.tv_exp_content.setText(jsonObject.getString(Config.EXPKey_EVA));
                    StarOnClickListener listener = StarOnClickListener.getInstance(holder.ll_star);
                    listener.changeBigStar(jsonObject.getInt(Config.EXPKey_Star));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                holder.tv_expmsg.setVisibility(View.GONE);
                handleAvatar(message, holder, position, 1);
                break;
            case 310:
                holder.rl_tv_chatcontent_withExt.setVisibility(View.GONE);
                holder.tv_expmsg.setVisibility(View.VISIBLE);
                holder.tv_expmsg.setText("您取消了订单" + message.getStringAttribute(Config.EXPKey_serial, ""));
                handleAvatar(message, holder, position, message.getIntAttribute("isShowAvatar", 0));
                break;
            case 312:
                holder.rl_tv_chatcontent_withExt.setVisibility(View.GONE);
                holder.tv_expmsg.setVisibility(View.VISIBLE);
                holder.tv_expmsg.setText("用户拒绝了订单" + message.getStringAttribute(Config.EXPKey_serial, "") + "的费用审核");
                handleAvatar(message, holder, position, message.getIntAttribute("isShowAvatar", 0));
                //业主拒绝费用
                break;
            case 313:
                holder.rl_tv_chatcontent_withExt.setVisibility(View.GONE);
                holder.tv_expmsg.setVisibility(View.VISIBLE);
                holder.tv_expmsg.setText("对方终止了服务");
                handleAvatar(message, holder, position, message.getIntAttribute("isShowAvatar", 0));
                //店家终止服务
                break;
            case 319:
                holder.rl_tv_chatcontent_withExt.setVisibility(View.VISIBLE);
                holder.ll_exp.setVisibility(View.VISIBLE);
                holder.ll_btns.setVisibility(View.GONE);
                holder.tv_expmsg.setVisibility(View.GONE);
                holder.tv_serial.setText("订单号:" + serial);
                content = "此单为分包工作，请耐心等待维修师傅协调解决。(" + StrUtils.getTime4Millions(message.getMsgTime()) + ")";
                holder.tv_exp_content.setText(content);
                handleAvatar(message, holder, position, message.getIntAttribute("isShowAvatar", 0));
                break;
            default:
                break;
        }

        //state of msg sending
        if (message.direct == EMMessage.Direct.SEND) {
            switch (message.status) {
                case SUCCESS: // 发送成功
                    holder.pb.setVisibility(View.GONE);
                    holder.staus_iv.setVisibility(View.GONE);
                    break;
                case FAIL: // 发送失败
                    holder.pb.setVisibility(View.GONE);
                    holder.staus_iv.setVisibility(View.VISIBLE);
                    break;
                case INPROGRESS: // 发送中
                    holder.pb.setVisibility(View.VISIBLE);
                    holder.staus_iv.setVisibility(View.GONE);
                    break;
                default:
                    // 发送消息
                    sendMsgInBackground(message, holder);
            }
        }
    }

    protected boolean updateColcor4Click(int clickable, ViewHolder holder) {
        if (clickable == 0) {
            holder.btn_order_operation.setClickable(false);
            holder.btn_accept.setClickable(false);
            toGray(holder.btn_order_operation, holder.btn_accept);
            holder.rl_tv_chatcontent_withExt.setSelected(true);
//            holder.btn_order_operation.setTextColor(Color.GRAY);
//            holder.btn_accept.setTextColor(Color.GRAY);
//            holder.tv_serial.setTextColor(Color.WHITE);

        } else {
            ((LinearLayout) holder.btn_order_operation.getParent()).setBackgroundColor(between);
            holder.btn_order_operation.setClickable(true);
            holder.btn_accept.setClickable(true);
            holder.btn_order_operation.setTextColor(Color.WHITE);
            holder.btn_accept.setTextColor(Color.WHITE);

            holder.btn_order_operation.setBackgroundColor(green);
            holder.btn_accept.setBackgroundColor(green);

        }
        return clickable == 0;
    }

    protected boolean updateColcor4Click2(int clickable, ViewHolder holder) {
        if (clickable == 0) {
            holder.btn_order_operation.setClickable(false);
            holder.btn_accept.setClickable(false);
            toGray(holder.btn_order_operation, holder.btn_accept);
            holder.rl_tv_chatcontent_withExt.setSelected(true);
//            holder.btn_order_operation.setTextColor(Color.GRAY);
//            holder.btn_accept.setTextColor(Color.GRAY);
//            holder.tv_serial.setTextColor(Color.WHITE);
            holder.tv_serial.setTextColor(Color.WHITE);
            holder.tv_exp_content.setTextColor(Color.WHITE);
            holder.tv_line.setBackgroundColor(Color.WHITE);

        } else {
            ((LinearLayout) holder.btn_order_operation.getParent()).setBackgroundColor(between);
            holder.btn_order_operation.setClickable(true);
            holder.btn_accept.setClickable(true);
            holder.btn_order_operation.setTextColor(Color.WHITE);
            holder.btn_accept.setTextColor(Color.WHITE);

            holder.btn_order_operation.setBackgroundColor(green);
            holder.btn_accept.setBackgroundColor(green);

            holder.tv_serial.setTextColor(Color.GRAY);
            holder.tv_exp_content.setTextColor(Color.GRAY);
            holder.tv_line.setBackgroundColor(context.getResources().getColor(R.color.item_view_line));
        }
        return clickable == 0;
    }

    interface ConfirmPayedService {
        //        PUT {baseUrl}/api/v3/repairs/orders/{订单号}/confirm
        @PUT("/api/v3/repairs/orders/{orderId}/confirm")
        void confirmPayed(String orderId, @Body OrderConfrimPayedReqBean orderconfrimpayedreqbean, Callback<CommonRespBean> cb);
    }

    //// 确认金额
    protected void doConfirmPayed(Context context, String serial, String price) {
        OrderConfrimPayedReqBean orderconfrimpayedreqbean = new OrderConfrimPayedReqBean();
        orderconfrimpayedreqbean.setOrderPrice(price);
        ConfirmPayedService service = RetrofitFactory.getInstance().create(context, orderconfrimpayedreqbean, ConfirmPayedService.class);
        Callback<CommonRespBean> callback = new Callback<CommonRespBean>() {

            @Override
            public void success(CommonRespBean commonRespBean, Response response) {
                if (commonRespBean != null && "yes".equals(commonRespBean.getStatus())) {
                } else {

                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        };
        service.confirmPayed(serial, orderconfrimpayedreqbean, callback);
    }
}
