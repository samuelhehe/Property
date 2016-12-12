package xj.property.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import xj.property.R;
import xj.property.activity.welfare.ActivityWelfareIndex;
import xj.property.utils.SmileUtils;
import xj.property.utils.other.Config;

/**
 * Created by che on 2015/8/17.
 */
public class WelfareChatAdapter extends MessageAdapter {
    public WelfareChatAdapter(Context context, String username, int chatType) {
        super(context, username, chatType);


    }


    @Override
    protected View forChild(EMMessage message, int position) {
        if (message.getType() != EMMessage.Type.TXT) return null;
        return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_welfare, null) : inflater.inflate(
                R.layout.row_sent_panic_buying, null);
    }

    @Override
    protected void forChildHolder(ViewHolder holder, View convertView) {
        holder.ll_detial_buying = (LinearLayout) convertView.findViewById(R.id.ll_detial_buying);
        holder.tv_detail_title = (TextView) convertView.findViewById(R.id.tv_detail_title);
        holder.tv_detail = (TextView) convertView.findViewById(R.id.tv_detail);
        holder.tv_used_code_title = (TextView) convertView.findViewById(R.id.tv_used_code_title);
        holder.tv = (TextView) convertView.findViewById(R.id.percentage);
        holder.ll_code_end = (LinearLayout) convertView.findViewById(R.id.ll_code_end);
        holder.maskImage = (xj.property.widget.MaskImage) convertView.findViewById(R.id.iv_welfare_photo);
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
    protected void handleTextMessageWithExt(final EMMessage message, final ViewHolder holder, final int position, int CMD_CODE) {
        //cmd detail in json
        String cmdDetail;
        //content to show
        Log.i("onion", "handleTextMessageWithExt" + CMD_CODE);
        final String content = "";
        final String serial = message.getStringAttribute(Config.EXPKey_serial, "");
        switch (CMD_CODE) {

            case 600:
                handleAvatar(message, holder, position, 1);
                holder.head_iv.setClickable(false);
                holder.ll_detial_buying.setVisibility(View.VISIBLE);
                holder.ll_code_end.setVisibility(View.GONE);
                holder.tv_detail_title.setText("" + message.getStringAttribute("title", ""));
                holder.tv_detail_title.setTextColor(context.getResources().getColor(R.color.buying_chat_txt_color));
                ImageLoader.getInstance().loadImage(message.getStringAttribute("image", ""),options,new ImageLoadingListener(){

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        holder.maskImage.setMaskImageView( R.drawable.default_picture,R.drawable.black_paopao);
                        Log.i("debbug", "onLoadingStarted");
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        holder.maskImage.setMaskImageView( R.drawable.default_picture,R.drawable.black_paopao);
                        Log.i("debbug", "onLoadingFailed");
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        Log.i("debbug", "onLoadingComplete");
                        holder.maskImage.setMaskImageView( loadedImage,R.drawable.black_paopao);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        Log.i("debbug", "onLoadingCancelled");
                        holder.maskImage.setMaskImageView( R.drawable.default_picture,R.drawable.black_paopao);
                    }
                });
                holder.tv_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ActivityWelfareIndex.class);
//                        intent.putExtra("welfareId",message.getStringAttribute("code",""));
                        intent.putExtra("welfareId",message.getStringAttribute("welfareId",""));
                        intent.setFlags(600);
                        context.startActivity(intent);
                    }
                });
                break;
            case 601:
                TextMessageBody txtBody = (TextMessageBody) message.getBody();
                Spannable span = SmileUtils.getSmiledText(context, txtBody.getMessage());
                handleAvatar(message, holder, position, 1);
                holder.head_iv.setClickable(false);
                holder.ll_code_end.setVisibility(View.VISIBLE);
                holder.ll_detial_buying.setVisibility(View.GONE);
                holder.tv_used_code_title.setText("福利即将发放"+message.getStringAttribute("title","")+"\n"+span);

                SpannableStringBuilder builder = new SpannableStringBuilder(holder.tv_used_code_title.getText().toString());
                ForegroundColorSpan color1 = new ForegroundColorSpan(context.getResources().getColor(R.color.color_fulijijiangfafang));
                builder.setSpan(color1, 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.tv_used_code_title.setText(builder);

                holder.tv_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ActivityWelfareIndex.class);
                        intent.putExtra("welfareId",message.getStringAttribute("welfareId",""));
                        intent.setFlags(600);
                        context.startActivity(intent);
                    }
                });
                break;
            case 602:
            case 603:
                handleAvatar(message, holder, position, 1);
                holder.head_iv.setClickable(false);
                TextMessageBody txtBody2 = (TextMessageBody) message.getBody();
                Spannable span2 = SmileUtils.getSmiledText(context, txtBody2.getMessage());
                holder.ll_detial_buying.setVisibility(View.GONE);
                holder.tv_used_code_title.setTextColor(context.getResources().getColor(R.color.buying_chat_txt_color));
                holder.tv_used_code_title.setText(""+span2);
                holder.ll_code_end.setVisibility(View.VISIBLE);
                holder.tv_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ActivityWelfareIndex.class);
                        intent.putExtra("welfareId",message.getStringAttribute("welfareId",""));
                        intent.setFlags(600);
                        context.startActivity(intent);
                    }
                });
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

    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .build();

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
