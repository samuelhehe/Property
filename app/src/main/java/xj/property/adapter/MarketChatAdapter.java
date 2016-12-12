package xj.property.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.easemob.chat.EMMessage;
import com.google.gson.Gson;

import org.json.JSONObject;

import de.greenrobot.event.EventBus;
import xj.property.R;
import xj.property.beans.FastGoodsModel;
import xj.property.beans.OrderDetailBeanList;
import xj.property.cache.OrderDetailModel;
import xj.property.event.FastMarketEvent;
import xj.property.utils.message.XJMessageHelper;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.StarOnClickListener;

/**
 * Created by Administrator on 2015/3/30.
 */
public class MarketChatAdapter extends MessageAdapter {
    private int black, yellow, white;

    public MarketChatAdapter(Context context, String username, int chatType) {
        super(context, username, chatType);
        black = context.getResources().getColor(R.color.textblack);
        yellow = context.getResources().getColor(R.color.chat_waiteva_text);
        white = context.getResources().getColor(R.color.white);
    }

    @Override
    protected View forChild(EMMessage message, int position) {
        if (message.getType() != EMMessage.Type.TXT) return null;
        return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_extremarket, null) : inflater.inflate(
                R.layout.row_sent_extmarket, null);
    }

    @Override
    protected void forChildHolder(ViewHolder holder, View convertView) {
        holder.btn_order_operation = (Button) convertView.findViewById(R.id.btn_order_operation);
        holder.tv_exp_content = (TextView) convertView.findViewById(R.id.tv_exp_content);
        holder.tv_expmsg = (TextView) convertView.findViewById(R.id.tv_msg);
        holder.tv_serial = (TextView) convertView.findViewById(R.id.tv_serial);
        holder.ll_btns = (LinearLayout) convertView.findViewById(R.id.ll_btns);
        holder.ll_exp = (LinearLayout) convertView.findViewById(R.id.ll_exp);
        holder.rl_tv_chatcontent_withExt = (RelativeLayout) convertView.findViewById(R.id.rl_tv_chatcontent_withExt);
        holder.ll_star = (LinearLayout) convertView.findViewById(R.id.ll_eva);
//        holder.ll_ext_goods = (LinearLayout) convertView.findViewById(R.id.ll_ext_goods);
//        holder.tv_ext_goods = (TextView) convertView.findViewById(R.id.tv_ext_goods);
        holder.tv_ext_goods_count = (TextView) convertView.findViewById(R.id.tv_count);
        holder.tv_ext_goods_name = (TextView) convertView.findViewById(R.id.tv_name);
        holder.tv_ext_goods_price = (TextView) convertView.findViewById(R.id.tv_price);
        holder.tv_total_goods_price = (TextView) convertView.findViewById(R.id.tv_total_price);
        holder.ll_item_ext_goods = (LinearLayout) convertView.findViewById(R.id.ll_item_ext_goods);
        holder.ll_total_ext_goods = (LinearLayout) convertView.findViewById(R.id.ll_total_ext_goods);
        holder.iv_shop_car = (ImageView) convertView.findViewById(R.id.iv_shop_car);
        holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
        holder.view_line = (View) convertView.findViewById(R.id.view_line);
        holder.tv_market_line = (TextView) convertView.findViewById(R.id.tv_market_line);
        holder.ll_ext_goods = (LinearLayout) convertView.findViewById(R.id.ll_ext_goods);
    }

    //    XJBaseAdapter goodsadapter=new XJBaseAdapter(context,R.layout.item_goods,new ArrayList<OrderDetailBeanList>(),new String[]{"serviceName","count","price"});
    class ItemHolder {
        public TextView tvName, tvCount, tvPrice;

        ItemHolder(View v) {
            tvName = (TextView) v.findViewById(R.id.tv_name);
            tvCount = (TextView) v.findViewById(R.id.tv_count);
            tvPrice = (TextView) v.findViewById(R.id.tv_price);
            v.setTag(this);
        }
    }

    @Override
    protected void handleTextMessageWithExt(final EMMessage message, ViewHolder holder, final int position, int CMD_CODE) {
        //cmd detail in json
        String cmdDetail;
        //content to show
        Log.i("onion", "handleTextMessageWithExt CMD_CODE " + CMD_CODE);
        String content = "";
        final String serial = message.getStringAttribute(Config.EXPKey_serial, "");

        Log.i("onion", "handleTextMessageWithExt serial " + serial);

        switch (CMD_CODE) {
            case 200://发送请求
                holder.rl_tv_chatcontent_withExt.setVisibility(View.VISIBLE);
                holder.ll_exp.setVisibility(View.VISIBLE);
                holder.ll_btns.setVisibility(View.VISIBLE);
                holder.tv_expmsg.setVisibility(View.GONE);

                holder.tv_serial.setText("订单号:" + serial);
                holder.iv_shop_car.setVisibility(View.GONE);
                holder.tv_num.setVisibility(View.VISIBLE);
//                holder.ll_ext_goods.setVisibility(View.VISIBLE);
                String json = message.getStringAttribute(Config.EXPKey_CMD_DETAIL, "{}");
                FastGoodsModel fastGoodsModel = new Gson().fromJson(json, FastGoodsModel.class);
//                TextPaint paint=holder.tv_ext_goods_name.getPaint();
                final OrderDetailModel orderDetailModel = new Select().from(OrderDetailModel.class).where("serial = ?", message.getStringAttribute(Config.EXPKey_serial, "")).executeSingle();
//                if (orderDetailModel != null) {
                if (fastGoodsModel != null && fastGoodsModel.orderDetailBeanList != null && fastGoodsModel.getOrderDetailBeanList().size() != 0) {
                    Log.i("debbug", "我得看下fastGoodsModel的值  " + json);
//                        holder.ll_item_ext_goods.setVisibility(View.VISIBLE);
//                        holder.ll_total_ext_goods.setVisibility(View.VISIBLE);

//                        holder.tv_ext_goods_name.setText(orderDetailModel.getOder_detail_servicename());
//                        holder.tv_ext_goods_count.setText(orderDetailModel.getOder_detail_count());
//                        holder.tv_ext_goods_price.setText(orderDetailModel.getOder_detail_price());
                    holder.tv_total_goods_price.setText("￥" + fastGoodsModel.getTotalPrice());
//                        holder.lv_ext_goods.setAdapter(goodsadapter);
//                        goodsadapter.changeData(fastGoodsModel.orderDetailBeanList);
//                        fastGoodsModel.orderDetailBeanList.add(new OrderDetailBeanList(0,"共计",fastGoodsModel.totalPrice,0));
                    for (int i = 0; i < fastGoodsModel.orderDetailBeanList.size(); i++) {//添加内容
                        OrderDetailBeanList orderDetailBeanList = fastGoodsModel.orderDetailBeanList.get(i);
                        LinearLayout llgoodsingle = null;
                        ItemHolder itemHolder = null;
                        if (i < holder.ll_ext_goods.getChildCount()) {//足够时,继续用
                            llgoodsingle = (LinearLayout) holder.ll_ext_goods.getChildAt(i);
                            llgoodsingle.setVisibility(View.VISIBLE);
                            itemHolder = (ItemHolder) llgoodsingle.getTag();
                        } else { //view不足时，inflate
                            llgoodsingle = (LinearLayout) View.inflate(context, R.layout.item_goods, null);
                            holder.ll_ext_goods.addView(llgoodsingle);
                            itemHolder = new ItemHolder(llgoodsingle);
                        }
//                            if(i==fastGoodsModel.orderDetailBeanList.size()-1){
//                                        itemHolder.tvName.setTextColor(yellow);
//                                itemHolder.tvName.setText(orderDetailBeanList.getServiceName());
//                                itemHolder.tvCount.setText("");
//                                itemHolder.tvPrice.setTextColor(yellow);
//                                itemHolder.tvPrice.setText("￥"+orderDetailBeanList.getPrice());
//                            }else{
                        itemHolder.tvName.setTextColor(black);
                        itemHolder.tvName.setText(orderDetailBeanList.getServiceName());
                        itemHolder.tvCount.setTextColor(black);
                        itemHolder.tvCount.setText("X" + orderDetailBeanList.getCount());
                        itemHolder.tvPrice.setTextColor(black);
                        itemHolder.tvPrice.setText("￥" + orderDetailBeanList.getPrice());
//                            }

                    }
                    if (holder.ll_ext_goods.getChildCount() > fastGoodsModel.orderDetailBeanList.size()) {//删除多余的
                        for (int i = fastGoodsModel.orderDetailBeanList.size(); i < holder.ll_ext_goods.getChildCount(); i++)
                            holder.ll_ext_goods.getChildAt(i).setVisibility(View.GONE);
                    }

                    holder.tv_num.setText(fastGoodsModel.getTotalCount() + "");
                    holder.tv_exp_content.setVisibility(View.GONE);

                }
                holder.btn_order_operation.setText("取消订单");
                holder.ll_star.setVisibility(View.GONE);
                if (!updateColcor4Click(message.getIntAttribute("clickable", 0), holder)) {
                    holder.btn_order_operation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            v.setClickable(false);
                            //refresh message state
                            XJMessageHelper.updateMessageStateWithMsg(message);
                            //send 203 message
                            try {
                                JSONObject js = new JSONObject();
                                js.put(Config.EXPKey_serial, serial);
                                js.put(Config.EXPKey_msgId, message.getMsgId());
                                EventBus.getDefault().post(new FastMarketEvent("业主取消请求", js, position, 1, 210));
                            } catch (Exception e) {
                                Log.e("onion", e.toString());
                            }
                        }
                    });
                    //    XJMessageHelper.saveMessage2DB(message.getMsgId(), serial, 300);
                }
                //change the related 200 message state, the relationship is bind via attr "msgId"
                // updateMesageStateWithId(message.getStringAttribute("msgId",""));
                holder.tv_serial.setTextColor(context.getResources().getColor(R.color.chat_btn_green));
                holder.tv_serial.setBackgroundColor(context.getResources().getColor(R.color.market_bg));
                holder.tv_market_line.setVisibility(View.VISIBLE);
//                refresh();

                //set avatar
                handleAvatar(message, holder, position, message.getIntAttribute("isShowAvatar", 1));

                break;
            case 201://店家接受订单
                holder.rl_tv_chatcontent_withExt.setSelected(false);
                holder.tv_expmsg.setVisibility(View.GONE);
                holder.ll_exp.setVisibility(View.VISIBLE);
                holder.ll_btns.setVisibility(View.VISIBLE);
                holder.rl_tv_chatcontent_withExt.setVisibility(View.VISIBLE);
                content = "店家已接受订单，请支付。";
                holder.tv_exp_content.setText(content);
                holder.tv_serial.setText("订单号:" + serial);
                holder.btn_order_operation.setText("立即支付");
                if (!updateColcor4ReceiveClick(message.getIntAttribute("clickable", 0), holder)) {
                    try {
                        holder.btn_order_operation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                XJMessageHelper.updateMessageStateWithMsg(message);
                                //变色处理在支付后
                                //send 203 message
                                try {
                                    JSONObject js = new JSONObject();
                                    js.put(Config.EXPKey_serial, serial);
                                    js.put(Config.EXPKey_msgId, message.getMsgId());
                                    //通知对方支付成功
                                    EventBus.getDefault().post(new FastMarketEvent("请支付", js, position, 1, 203));
                                } catch (Exception e) {
                                    Log.e("onion", e.toString());
                                }
                            }
                        });
                        //更改对应200按钮的颜色

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                handleAvatar(message, holder, position, 0);
                break;
            case 202://店家拒绝订单
                holder.rl_tv_chatcontent_withExt.setVisibility(View.GONE);
                holder.tv_expmsg.setVisibility(View.VISIBLE);
                String reason = message.getStringAttribute(Config.EXPKey_REASON, "");
                //  XJMessageHelper.updateMesageStateWithId(message.getStringAttribute(Config.EXPKey_msgId, ""), EMChatManager.getInstance().getConversation(message.getFrom()));
                //    XJMessageHelper.updateMessageStateWithMsg(message);
                holder.tv_expmsg.setText("对方拒绝了订单" + serial + ",原因" + reason);
                handleAvatar(message, holder, position, 0);
                break;
            case 203://在线支付成功，货到付款
                holder.rl_tv_chatcontent_withExt.setVisibility(View.GONE);
                holder.tv_expmsg.setVisibility(View.VISIBLE);
                content = "您选择货到付款，店家正在火速配送。";
                try {
                    JSONObject js = new JSONObject(message.getStringAttribute(Config.EXPKey_CMD_DETAIL, ""));
                    int i = js.getInt("payMethod");
                    if (i == 0) content = "支付成功，店家正在火速配送。";
                } catch (Exception e) {
                    Log.e("onion", e.toString());
                }
                holder.tv_expmsg.setText("订单号:" + serial + content);
                handleAvatar(message, holder, position, message.getIntAttribute("isShowAvatar", 0));

         /*        holder.tv_expmsg.setVisibility(View.GONE);
                holder.ll_exp.setVisibility(View.VISIBLE);
                holder.ll_btns.setVisibility(View.VISIBLE);
                holder.rl_tv_chatcontent_withExt.setVisibility(View.VISIBLE);
                holder.tv_exp_content.setVisibility(View.VISIBLE);
                content = "您选择货到付款，店家正在火速配送。" ;
                try {
                    JSONObject js = new JSONObject(message.getStringAttribute(Config.EXPKey_CMD_DETAIL, ""));
                    int i=js.getInt("payMethod");
                    if(i==0) content = "支付成功，店家正在火速配送。" ;
                }catch (Exception e){
                    Log.e("onion",e.toString());
                }

                holder.iv_shop_car.setVisibility(View.GONE);
                holder.tv_num.setVisibility(View.GONE);
                holder.tv_exp_content.setText(content);
                holder.tv_serial.setText("订单号:" + serial);
                holder.ll_star.setVisibility(View.GONE);
                holder.ll_total_ext_goods.setVisibility(View.GONE);
                holder.ll_item_ext_goods.setVisibility(View.GONE);
//                holder.btn_order_operation.setText("确认收货");
                //TODO 2.0
                holder.btn_order_operation.setVisibility(View.GONE);
               if (!updateColcor4Click(message.getIntAttribute("clickable", 0),  holder)) {

                        holder.btn_order_operation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                v.setClickable(false);
//                                XJMessageHelper.updateMessageStateWithMsg(message);
                                //变色在评论后处理
                                //send 203 message
                                try {
                                    JSONObject js = new JSONObject();
                                    js.put(Config.EXPKey_serial, serial);
                                    js.put(Config.EXPKey_msgId, message.getMsgId());
                                    //通知对方支付成功
                                    EventBus.getDefault().post(new FastMarketEvent("确认收货", js, position, 1, 204));
                                    XJMessageHelper.updateMessageStateWithMsg(message);
                                } catch (Exception e) {
                                    Log.e("onion", e.toString());
                                }


                            }
                        });
                }
                if (holder.btn_order_operation.isClickable()==true){
                    holder.tv_serial.setTextColor(context.getResources().getColor(R.color.chat_btn_green));
                    holder.tv_serial.setBackgroundColor(context.getResources().getColor(R.color.white));

                    holder.tv_exp_content.setTextColor(context.getResources().getColor(R.color.market_giveup_top_bg));
                    holder.tv_exp_content.setBackgroundColor(context.getResources().getColor(R.color.white));
                }else {
                    holder.tv_serial.setTextColor(context.getResources().getColor(R.color.white));
                    holder.tv_serial.setBackgroundColor(context.getResources().getColor(R.color.market_giveup_top_bg));
                    holder.tv_exp_content.setTextColor(white);
                    holder.tv_exp_content.setBackgroundColor(context.getResources().getColor(R.color.market_giveup_top_bg));
                }
                handleAvatar(message, holder, position, 1);*/
                break;
            case 204://确认收货
                holder.rl_tv_chatcontent_withExt.setVisibility(View.VISIBLE);
                holder.ll_btns.setVisibility(View.VISIBLE);
                holder.ll_exp.setVisibility(View.VISIBLE);
                holder.tv_expmsg.setVisibility(View.GONE);
                holder.tv_serial.setText("订单号:" + serial);
                content = "服务结束!请对此次服务做出评价";
                holder.tv_exp_content.setVisibility(View.VISIBLE);
                holder.tv_exp_content.setText(content);
                holder.ll_item_ext_goods.setVisibility(View.GONE);
                holder.ll_total_ext_goods.setVisibility(View.GONE);
                holder.iv_shop_car.setVisibility(View.GONE);
                holder.tv_num.setVisibility(View.GONE);
                holder.ll_star.setVisibility(View.GONE);
                holder.btn_order_operation.setText("去评价");
                if (!updateColcor4Click(message.getIntAttribute("clickable", 0), holder)) {
                    holder.btn_order_operation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //send 203 message
                            try {
                                JSONObject js = new JSONObject();
                                js.put(Config.EXPKey_serial, serial);
                                js.put(Config.EXPKey_msgId, message.getMsgId());
                                Log.i("onion", "msgId before" + message.getMsgId());
                                EventBus.getDefault().post(new FastMarketEvent("请评价", js, position, 1, 205));

                            } catch (Exception e) {
                                Log.e("onion", e.toString());
                            }
                        }
                    });
                }
                if (holder.btn_order_operation.isClickable() == true) {
                    holder.tv_serial.setTextColor(context.getResources().getColor(R.color.chat_btn_green));
                    holder.tv_serial.setBackgroundColor(context.getResources().getColor(R.color.white));

                    holder.tv_exp_content.setTextColor(context.getResources().getColor(R.color.market_giveup_top_bg));
                    holder.tv_exp_content.setBackgroundColor(context.getResources().getColor(R.color.white));
                } else {
                    holder.tv_serial.setTextColor(context.getResources().getColor(R.color.white));
                    holder.tv_serial.setBackgroundColor(context.getResources().getColor(R.color.market_giveup_top_bg));
                    holder.tv_exp_content.setTextColor(white);
                    holder.tv_exp_content.setBackgroundColor(context.getResources().getColor(R.color.market_giveup_top_bg));
                }

                //refresh();
                //set avatar
                handleAvatar(message, holder, position, 1);

                break;
            case 205:
                holder.rl_tv_chatcontent_withExt.setVisibility(View.VISIBLE);
                holder.ll_exp.setVisibility(View.VISIBLE);
                holder.ll_btns.setVisibility(View.GONE);
                holder.tv_expmsg.setVisibility(View.GONE);
                holder.ll_star.setVisibility(View.VISIBLE);
                holder.iv_shop_car.setVisibility(View.GONE);
                holder.tv_num.setVisibility(View.GONE);
                holder.ll_item_ext_goods.setVisibility(View.GONE);
                holder.ll_total_ext_goods.setVisibility(View.GONE);
                holder.tv_exp_content.setVisibility(View.VISIBLE);
                try {
                    JSONObject jsonObject = new JSONObject(message.getStringAttribute(Config.EXPKey_CMD_DETAIL, ""));
                    holder.tv_serial.setText("订单号" + message.getStringAttribute(Config.EXPKey_serial, ""));
                    // holder.tv_exp_content.setText("服务质量" + jsonObject.getString(Config.EXPKey_EVA));
                    holder.tv_exp_content.setText(jsonObject.getString(Config.EXPKey_EVA));
                    StarOnClickListener listener = StarOnClickListener.getInstance(holder.ll_star);
                    listener.changeBigStar(jsonObject.getInt(Config.EXPKey_Star));

                } catch (Exception e) {
                    e.printStackTrace();
                }

                holder.tv_serial.setTextColor(context.getResources().getColor(R.color.white));
                holder.tv_serial.setBackgroundColor(context.getResources().getColor(R.color.chat_btn_green));
                holder.tv_exp_content.setTextColor(context.getResources().getColor(R.color.market_giveup_top_bg));
                holder.tv_exp_content.setBackgroundColor(context.getResources().getColor(R.color.market_bg));
                holder.tv_expmsg.setVisibility(View.GONE);
                handleAvatar(message, holder, position, 1);
                break;
            case 210:
                holder.rl_tv_chatcontent_withExt.setVisibility(View.GONE);
                holder.tv_expmsg.setVisibility(View.VISIBLE);
                holder.tv_expmsg.setText("您取消了订单" + message.getStringAttribute(Config.EXPKey_serial, ""));
                handleAvatar(message, holder, position, message.getIntAttribute("isShowAvatar", 0));
                break;
            case 2072:


                Log.e("cmd_code:---->>>", "2072");

//Toast.makeText(context,"2072", Toast.LENGTH_LONG).show();
                if ("yes".equals(PreferencesUtil.getShowBonuscoin(context))) {

//Toast.makeText(context,"getShowBonuscoin yes", Toast.LENGTH_LONG).show();

                    Log.e("getShowBonuscoin:---->>>", "yes");

                    Log.i("debbug", "分享的帮帮币=" + message.getIntAttribute(Config.EXPKey_bonuscoinCount, 0));
                    Log.i("debbug", "分享的帮帮币+=" + message.getIntAttribute(Config.EXPKey_shareBonuscoinCount, 0));

                    if (message.getIntAttribute(Config.EXPKey_bonuscoinCount, 0) < 0 && message.getIntAttribute(Config.EXPKey_shareBonuscoinCount, 0) < 0) {
                        holder.rl_tv_chatcontent_withExt.setVisibility(View.GONE);
                        holder.tv_expmsg.setVisibility(View.VISIBLE);
                        holder.tv_expmsg.setText("订单:" + message.getStringAttribute(Config.EXPKey_serial, "") + "交易完成");
                    } else {
                        holder.rl_tv_chatcontent_withExt.setVisibility(View.VISIBLE);
                        holder.tv_expmsg.setVisibility(View.GONE);
                        holder.tv_expmsg.setText("订单:" + message.getStringAttribute(Config.EXPKey_serial, "") + "交易完成");
                        holder.ll_btns.setVisibility(View.VISIBLE);
                        holder.ll_exp.setVisibility(View.VISIBLE);
                        holder.tv_exp_content.setText("店家已结单，您共获得" + message.getIntAttribute(Config.EXPKey_bonuscoinCount, 0) + "个帮币");
                        holder.tv_serial.setText("订单号" + message.getStringAttribute(Config.EXPKey_serial, ""));
                        if (message.getIntAttribute(Config.EXPKey_shareBonuscoinCount, 0) == 0) {
                            holder.btn_order_operation.setText("分享到生活圈");
                        } else {
                            holder.btn_order_operation.setText("分享到生活圈 帮币+" + message.getIntAttribute(Config.EXPKey_shareBonuscoinCount, 0));
                        }
                        if (!updateColcor4ReceiveClick(message.getIntAttribute("clickable", 0), holder)) {
                            try {
                                holder.btn_order_operation.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
//                                XJMessageHelper.updateMessageStateWithMsg(message);
                                        //变色处理在分享后
                                        //send 2072 message
                                        try {
                                            JSONObject js = new JSONObject();
                                            js.put(Config.EXPKey_serial, serial);
                                            js.put(Config.EXPKey_msgId, message.getMsgId());
                                            //通知对方支付成功
                                            EventBus.getDefault().post(new FastMarketEvent("分享", js, position, 1, 2072));
                                        } catch (Exception e) {
                                            Log.e("onion", e.toString());
                                        }
                                    }
                                });
                                //更改对应200按钮的颜色

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                } else {
//Toast.makeText(context,"getShowBonuscoin no", Toast.LENGTH_LONG).show();

                    Log.e("cmd_code:---->>>", "getShowBonuscoin no");

                    holder.rl_tv_chatcontent_withExt.setVisibility(View.GONE);
                    holder.tv_expmsg.setVisibility(View.VISIBLE);
                    holder.tv_expmsg.setText("订单:" + message.getStringAttribute(Config.EXPKey_serial, "") + "交易完成");
//Toast.makeText(context,"订单:" + message.getStringAttribute(Config.EXPKey_serial, "") + "交易完成", Toast.LENGTH_LONG).show();
                    Log.e("cmd_code:---->>>", "订单:" + message.getStringAttribute(Config.EXPKey_serial, "") + "交易完成");

                }

                handleAvatar(message, holder, position, message.getIntAttribute("isShowAvatar", 0));
                break;
            default:
                holder.rl_tv_chatcontent_withExt.setVisibility(View.GONE);
                holder.tv_expmsg.setVisibility(View.VISIBLE);
                holder.tv_expmsg.setText("您的版本过低,无法显示该消息。请升级客户端。");
                handleAvatar(message, holder, position, message.getIntAttribute("isShowAvatar", 0));
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
            holder.rl_tv_chatcontent_withExt.setSelected(true);
            holder.tv_market_line.setVisibility(View.GONE);
            holder.btn_order_operation.setClickable(false);
            holder.btn_order_operation.setTextColor(context.getResources().getColor(R.color.market_giveup_text_bg));
            holder.btn_order_operation.setBackgroundColor(context.getResources().getColor(R.color.market_giveup_bottom_bg));


        } else {
            holder.tv_market_line.setVisibility(View.VISIBLE);
            holder.btn_order_operation.setClickable(true);
            holder.btn_order_operation.setTextColor(context.getResources().getColor(R.color.white));
            holder.btn_order_operation.setBackgroundColor(context.getResources().getColor(R.color.chat_btn_green));
        }
        return clickable == 0;
    }

    protected boolean updateColcor4ReceiveClick(int clickable, ViewHolder holder) {
        if (clickable == 0) {
            holder.view_line.setVisibility(View.GONE);
            holder.btn_order_operation.setClickable(false);
            holder.btn_order_operation.setTextColor(context.getResources().getColor(R.color.market_giveup_top_bg));
//            holder.btn_order_operation.setTextColor(context.getResources().getColor(R.color.gray_normal));
            holder.btn_order_operation.setBackgroundColor(context.getResources().getColor(R.color.market_giveup_bottom_bg));
            holder.tv_serial.setTextColor(context.getResources().getColor(R.color.white));
            holder.tv_serial.setBackgroundColor(context.getResources().getColor(R.color.market_giveup_top_bg));
            holder.tv_exp_content.setTextColor(context.getResources().getColor(R.color.white));
            holder.tv_exp_content.setBackgroundColor(context.getResources().getColor(R.color.market_giveup_top_bg));
        } else {
            holder.view_line.setVisibility(View.VISIBLE);
            holder.btn_order_operation.setClickable(true);
            holder.btn_order_operation.setTextColor(context.getResources().getColor(R.color.white));
            holder.btn_order_operation.setBackgroundColor(context.getResources().getColor(R.color.chat_btn_green));

            holder.tv_serial.setTextColor(context.getResources().getColor(R.color.chat_btn_green));
            holder.tv_serial.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.tv_exp_content.setTextColor(context.getResources().getColor(R.color.gray_normal));
            holder.tv_exp_content.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
        return clickable == 0;
    }

    private View createGoodsTv(OrderDetailBeanList orderDetailBeanList) {
        TextView tv = (TextView) View.inflate(context, R.layout.textview_goods, null);

        String content = new StringBuilder().append(orderDetailBeanList.getServiceName()).append("\t").append(orderDetailBeanList.getCount()).append("\t").append("$").append(orderDetailBeanList.getPrice()).toString();
        tv.setText(content);
        return tv;
    }
}
