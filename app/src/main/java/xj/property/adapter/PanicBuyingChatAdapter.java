package xj.property.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easemob.chat.EMMessage;

import xj.property.R;
import xj.property.activity.surrounding.MyPanicBuyingActivity;
import xj.property.utils.other.Config;

/**
 * Created by che on 2015/8/17.
 */
public class PanicBuyingChatAdapter extends MessageAdapter {
    public PanicBuyingChatAdapter(Context context, String username, int chatType) {
        super(context, username, chatType);
    }


    @Override
    protected View forChild(EMMessage message, int position) {
        if (message.getType() != EMMessage.Type.TXT) return null;
        return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_panic_buying, null) : inflater.inflate(
                R.layout.row_sent_panic_buying, null);
    }

    @Override
    protected void forChildHolder(ViewHolder holder, View convertView) {
        holder.ll_detial_buying = (LinearLayout) convertView.findViewById(R.id.ll_detial_buying);
        holder.tv_detail_title = (TextView) convertView.findViewById(R.id.tv_detail_title);
        holder.tv_detail = (TextView) convertView.findViewById(R.id.tv_detail);
        holder.tv_used_code_title = (TextView) convertView.findViewById(R.id.tv_used_code_title);
        holder.tv = (TextView) convertView.findViewById(R.id.percentage);
        holder.tv_outlets_code = (TextView) convertView.findViewById(R.id.tv_outlets_code);
        holder.tv_used_code_code = (TextView) convertView.findViewById(R.id.tv_used_code_code);
        holder.ll_code_end = (LinearLayout) convertView.findViewById(R.id.ll_code_end);
    }

    //    XJBaseAdapter goodsadapter=new XJBaseAdapter(context,R.layout.item_goods,new ArrayList<OrderDetailBeanList>(),new String[]{"serviceName","count","price"});
    class ItemHolder{
        public TextView tvName,tvCount,tvPrice;
        ItemHolder(View v){
            tvName=(TextView)v.findViewById(R.id.tv_name);
            tvCount=(TextView)v.findViewById(R.id.tv_count);
            tvPrice=(TextView)v.findViewById(R.id.tv_price);
            v.setTag(this);
        }
    }
    @Override
    protected void handleTextMessageWithExt(final EMMessage message, ViewHolder holder, final int position, int CMD_CODE) {
        //cmd detail in json
        String cmdDetail;
        //content to show
        Log.i("onion", "handleTextMessageWithExt" + CMD_CODE);
        final String content = "";
        final String serial = message.getStringAttribute(Config.EXPKey_serial, "");
        switch (CMD_CODE) {

            case 500:
                handleAvatar(message, holder, position, 1);
                holder.head_iv.setClickable(false);
                holder.ll_detial_buying.setVisibility(View.VISIBLE);
                holder.tv_detail.setVisibility(View.VISIBLE);
                holder.ll_code_end.setVisibility(View.GONE);
                holder.tv_detail_title.setText(""+message.getStringAttribute("title",""));
                holder.tv_outlets_code.setText("抢购码："+message.getStringAttribute("code",""));
                if(message.getIntAttribute("clickable",0) == 1){
                    holder.tv_outlets_code.setSelected(true);
                    holder.tv_detail.setSelected(true);
                }else {
                    holder.tv_outlets_code.setSelected(false);
                    holder.tv_detail.setSelected(false);
                }
                holder.tv_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, MyPanicBuyingActivity.class);
                        intent.putExtra("code",message.getStringAttribute("code",""));
                        context.startActivity(intent);
                    }
                });
                break;
            case 501:
                handleAvatar(message, holder, position, 1);
                holder.head_iv.setClickable(false);
                holder.ll_detial_buying.setVisibility(View.GONE);
                holder.tv_detail.setVisibility(View.GONE);
                holder.ll_code_end.setVisibility(View.VISIBLE);

                holder.tv_used_code_title.setText(""+message.getStringAttribute("title",""));
                holder.tv_used_code_code.setText("抢购码："+message.getStringAttribute("code",""));

                break;

//            default:
//                holder.rl_tv_chatcontent_withExt.setVisibility(View.GONE);
//                holder.tv_expmsg.setVisibility(View.VISIBLE);
//                holder.tv_expmsg.setText("您的版本过低,无法显示该消息。请升级客户端。");
//                handleAvatar(message, holder, position, message.getIntAttribute("isShowAvatar", 0));
//                break;
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

//    protected void handleAvatar(EMMessage message, ViewHolder holder, final int position, int isShowAvatar) {
//        if (isShowAvatar == 1) {
//            holder.head_iv.setVisibility(View.VISIBLE);
//            String avatar = "";
//            if (message.direct == EMMessage.Direct.RECEIVE) {
//                User c = XJContactHelper.selectContact(message.getFrom());
//                if (c != null)
//                    avatar = c.avatar;
//                else  avatar=message.getStringAttribute(Config.EXPKey_avatar,"");
//            } else {
//                avatar = PreferencesUtil.getLoginInfo(context).getAvatar();
//            }
//            //get attribute
//            // 设置内容 mark
//            ImageLoader.getInstance().displayImage(avatar, holder.head_iv, UserUtils.options);
//        } else {
//            holder.head_iv.setVisibility(View.INVISIBLE);
//        }
//
//    }

}
