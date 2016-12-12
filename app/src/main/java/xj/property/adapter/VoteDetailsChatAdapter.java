package xj.property.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easemob.chat.EMMessage;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import xj.property.R;
import xj.property.activity.runfor.HistoryRunForActivity;
import xj.property.activity.runfor.RunForActivity;
import xj.property.activity.vote.VoteDetailsActivity;
import xj.property.beans.VoteChatDetailReceivedBean;
import xj.property.beans.VoteChatResultRespBean;
import xj.property.utils.SmileUtils;
import xj.property.utils.other.Config;

public class VoteDetailsChatAdapter extends MessageAdapter {
    public VoteDetailsChatAdapter(Context context, String username, int chatType) {
        super(context, username, chatType);
    }

    @Override
    protected View forChild(EMMessage message, int position) {
        if (message.getType() != EMMessage.Type.TXT) return null;
        return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_vote, null) : inflater.inflate(R.layout.row_sent_panic_buying, null);
    }

    @Override
    protected void forChildHolder(ViewHolder holder, View convertView) {


        //// 703 投票消息布局
        /// 点击详情
        holder.tv_detail = (TextView) convertView.findViewById(R.id.tv_detail);
        /// 投票人名字
        holder.provider_details_name_tv = (TextView) convertView.findViewById(R.id.provider_details_name_tv);

        /// 投票内容描述
        holder.tv_used_code_title = (TextView) convertView.findViewById(R.id.tv_used_code_title);
        /// percent
        holder.tv = (TextView) convertView.findViewById(R.id.percentage);
        /// 整个头部大布局
        holder.ll_code_end = (LinearLayout) convertView.findViewById(R.id.ll_code_end);
        /// 用户头像
        holder.provider_details_iv_avtar = (android.widget.ImageView) convertView.findViewById(R.id.provider_details_iv_avtar);
        /// 投票结果展示
        holder.vote_detail_result_llay = (LinearLayout) convertView.findViewById(R.id.vote_detail_result_llay);

        /// 帮主消息布局
        holder.ll_code_bangzhu_llay = (LinearLayout) convertView.findViewById(R.id.ll_code_bangzhu_llay);
        ////帮主获得者的三个人名
        holder.bangzhu_notice_name_llay = (LinearLayout) convertView.findViewById(R.id.bangzhu_notice_name_llay);
        ///帮主消息提示描述
        holder.bangzhu_notice_desc_tv = (TextView) convertView.findViewById(R.id.bangzhu_notice_desc_tv);
        /// 投票排名变化,类型提示
        holder.provider_details_notice_type_tv = (TextView) convertView.findViewById(R.id.provider_details_notice_type_tv);


//        LinearLayout ll_code_bangzhu_llay;
//        /// 帮主获得者的三个人名
//        LinearLayout bangzhu_notice_name_llay;
//        //// 帮主消息提示描述
//        TextView bangzhu_notice_desc_tv;


    }

    @Override
    protected void handleTextMessageWithExt(final EMMessage message, final ViewHolder holder, final int position, int CMD_CODE) {
        //cmd detail in json
        //content to show
        Log.i("onion", "handleTextMessageWithExt" + CMD_CODE);

//        final String content = "";
        final String serial = message.getStringAttribute(Config.EXPKey_serial, "");

        switch (CMD_CODE) {

            case 701: /// 帮主竞选结果
                handleAvatar(message, holder, position, 1);

                holder.head_iv.setClickable(false);

                holder.ll_code_end.setVisibility(View.GONE);

                holder.ll_code_bangzhu_llay.setVisibility(View.VISIBLE);
                /// 人名可见
                holder.bangzhu_notice_name_llay.setVisibility(View.VISIBLE);
                //// 选举结果icon 可见
                holder.ll_code_bangzhu_llay.findViewById(R.id.vote_chat_election_result_llay).setVisibility(View.VISIBLE);
                /// 号召选举icon不可见
                holder.ll_code_bangzhu_llay.findViewById(R.id.vote_chat_lead_election_llay).setVisibility(View.GONE);

                holder.bangzhu_notice_desc_tv.setText("快速点击查看他们的得分数据，点击查看清晰对比数据。");
                /// 排名变化提示 类型..
                holder.provider_details_notice_type_tv.setText("你所投票的选项排名已发生变化");
                holder.provider_details_notice_type_tv.setVisibility(View.GONE);


                TextView bangzhu_name_tv = (TextView) holder.bangzhu_notice_name_llay.findViewById(R.id.bangzhu_name_tv);
                TextView fubangzhu_name1_tv = (TextView) holder.bangzhu_notice_name_llay.findViewById(R.id.fubangzhu_name1_tv);
                TextView fubangzhu_name2_tv = (TextView) holder.bangzhu_notice_name_llay.findViewById(R.id.fubangzhu_name2_tv);

                ////投票消息
                String nickname1 =   message.getStringAttribute("nickname","");
                /// 左侧头像
                String avatar1 =   message.getStringAttribute("avatar","");

                String title1 =   message.getStringAttribute("title","");


                ((TextView) holder.ll_code_bangzhu_llay.findViewById(R.id.bangzhu_notice_title_tv)).setText(title1);


                int currentTime = (int) (System.currentTimeMillis()/1000);

                final String timestamp1 =   message.getStringAttribute("timestamp", "" + currentTime);

                String content1 =   message.getStringAttribute("content", "");

                VoteChatResultRespBean  electionResult= convertContentToElectionBean(content1);

                if(electionResult!=null){

                    String bangzhu = electionResult.getBangzhu();
                    String fubangzhu1 = electionResult.getFubangzhu1();
                    String fubangzhu2 = electionResult.getFubangzhu2();

                    bangzhu_name_tv.setText(TextUtils.isEmpty(bangzhu)?"无名氏":bangzhu);
                    fubangzhu_name1_tv.setText(TextUtils.isEmpty(fubangzhu1)?"无名氏":fubangzhu1);
                    fubangzhu_name2_tv.setText(TextUtils.isEmpty(fubangzhu2)?"无名氏":fubangzhu2);
                    bangzhu_name_tv.setVisibility(View.VISIBLE);
                    fubangzhu_name1_tv.setVisibility(View.VISIBLE);
                    fubangzhu_name2_tv.setVisibility(View.VISIBLE);
                }else{
                    bangzhu_name_tv.setVisibility(View.GONE);
                    fubangzhu_name1_tv.setVisibility(View.GONE);
                    fubangzhu_name2_tv.setVisibility(View.GONE);
                }

                /// 投票排名变化
//                String title1 =   message.getStringAttribute("title","");

//                Log.i("handleTextMessageWithExt ", "701 avatar  "+avatar1);
//                Log.i("handleTextMessageWithExt ", "701 nickname "+nickname1);
//
//                Log.i("handleTextMessageWithExt ", "701 timestamp  "+timestamp1);
//
//                Log.i("handleTextMessageWithExt ", "701 content  "+ content1);


                //// 查看详情
                holder.tv_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        /// 2018/12/1  跳转至帮主竞选结果页

                        Intent intent = new Intent(context, HistoryRunForActivity.class);
                        intent.putExtra("searchTime",timestamp1);
                        intent.setFlags(701);
                        context.startActivity(intent);

                    }
                });

                break;
            case 702: /// 号召参与帮主竞选
                handleAvatar(message, holder, position, 1);
                holder.head_iv.setClickable(false);
                holder.ll_code_end.setVisibility(View.GONE);

                holder.ll_code_bangzhu_llay.setVisibility(View.VISIBLE);
                /// 人名不可见
                holder.bangzhu_notice_name_llay.setVisibility(View.GONE);
                //// 选举结果icon 不可见
                holder.ll_code_bangzhu_llay.findViewById(R.id.vote_chat_election_result_llay).setVisibility(View.GONE);
                /// 号召选举可见
                holder.ll_code_bangzhu_llay.findViewById(R.id.vote_chat_lead_election_llay).setVisibility(View.VISIBLE);

                String title2 =   message.getStringAttribute("title","");

               ((TextView) holder.ll_code_bangzhu_llay.findViewById(R.id.bangzhu_notice_title_tv)).setText(title2);

                holder.bangzhu_notice_desc_tv.setText("下月新的帮主已经开始竞选啦，赶快去拉票或者投票吧。");

                /// 排名变化提示 类型..
                holder.provider_details_notice_type_tv.setText("你所投票的选项排名已发生变化");
                holder.provider_details_notice_type_tv.setVisibility(View.GONE);

                //// 查看详情
                holder.tv_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, RunForActivity.class);
                        intent.setFlags(702);
                        context.startActivity(intent);
                    }
                });

                break;
            case 703: /// 普通投票消息通知
                handleAvatar(message, holder, position, 1);

                holder.head_iv.setClickable(false);

//                TextMessageBody txtBody2 = (TextMessageBody) message.getBody();

                ////投票消息
              String nickname =   message.getStringAttribute("nickname","");
                /// 左侧头像
              String avatar =   message.getStringAttribute("avatar","");

              String timestamp =   message.getStringAttribute("timestamp","");

              String content =   message.getStringAttribute("content","");

              final VoteChatDetailReceivedBean receivedBean = convertContentStrToObj(content);

                /// 投票排名变化
//                 String title3 =   message.getStringAttribute("title","");


//                Log.i("handleTextMessageWithExt ", "703 voteOptionsList   "+receivedBean.getVoteOptionsList()+ " voteOptionsList 0 "+ receivedBean.getVoteOptionsList().get(0));
//
//                Log.i("handleTextMessageWithExt ", "703 voteId  "+receivedBean.getVoteId());
//
//                Log.i("handleTextMessageWithExt ", "703 voteTitle  "+receivedBean.getVoteTitle());
//
//                Log.i("handleTextMessageWithExt ", "703 avatar  "+avatar);
//
//                Log.i("handleTextMessageWithExt ", "703 timestamp  "+timestamp);
//
                Log.i("handleTextMessageWithExt ", "703 content  "+ content);


                Spannable span2 = SmileUtils.getSmiledText(context, receivedBean.getVoteTitle());

                holder.tv_used_code_title.setText(span2, TextView.BufferType.SPANNABLE);

                holder.provider_details_name_tv.setText(receivedBean.getNickname());

                /// 投票发起人头像
                ImageLoader.getInstance().displayImage(receivedBean.getAvatar(),holder.provider_details_iv_avtar,options);

                holder.ll_code_end.setVisibility(View.VISIBLE);

                holder.ll_code_bangzhu_llay.setVisibility(View.GONE);

                /// 排名变化提示 类型..
                holder.provider_details_notice_type_tv.setText("你所投票的选项排名已发生变化");
                holder.provider_details_notice_type_tv.setVisibility(View.VISIBLE);

                holder.tv_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, VoteDetailsActivity.class);
                        intent.putExtra("voteId", ""+receivedBean.getVoteId());
                        intent.setFlags(703);
                        context.startActivity(intent);
                    }
                });

                holder.vote_detail_result_llay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, VoteDetailsActivity.class);
                        intent.putExtra("voteId", ""+receivedBean.getVoteId());
                        intent.setFlags(703);
                        context.startActivity(intent);

                    }
                });

                loadVoteResult(holder.vote_detail_result_llay, receivedBean.getVoteOptionsList());

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

    /**
     * 解析帮主竞选投票结果
     *
     * @param content1
     * @return
     */
    private VoteChatResultRespBean convertContentToElectionBean(String content1) {
        if(TextUtils.isEmpty(content1)){
            return null;
        }else{
            VoteChatResultRespBean voteChatResultRespBean = new Gson().fromJson(content1, VoteChatResultRespBean.class);
            if(voteChatResultRespBean!=null){
                return voteChatResultRespBean;
            }else{
                return null;
            }
        }
    }

    /**
     * 加载投票结果
     *
     * @param vote_detail_result_llay
     * @param voteOptionsList
     */
    private void loadVoteResult(LinearLayout vote_detail_result_llay, List<VoteChatDetailReceivedBean.VoteOptionsListEntity> voteOptionsList) {

        LinearLayout  vote_index_item_rcontent_llay = (LinearLayout) vote_detail_result_llay.findViewById(R.id.vote_index_item_rcontent_llay);
        if(vote_index_item_rcontent_llay!=null){
            vote_index_item_rcontent_llay.removeAllViews();

            if(voteOptionsList!=null && voteOptionsList.size()>0){

                for( VoteChatDetailReceivedBean.VoteOptionsListEntity entity: voteOptionsList){
                    View vote_result=  inflater.inflate(R.layout.common_vote_detailchat_percent_layout,null);

                    String count = ""+entity.getCount();
                    if(entity.getCount()>=1000){
                        count = "999+";
                    }
                    String percentStr = entity.getStrPersent()+"% ("+ count+"票)";

                    ((TextView)vote_result.findViewById(R.id.vote_right_percent_tv)).setText(percentStr);

                    ((TextView)vote_result.findViewById(R.id.vote_result_pername_tv)).setText(entity.getVoteOptionsContent());
                    //// 添加
                    vote_index_item_rcontent_llay.addView(vote_result);
                }

            }else{
                vote_detail_result_llay.setVisibility(View.GONE);
            }
            vote_detail_result_llay.setVisibility(View.VISIBLE);
        }else {
            vote_detail_result_llay.setVisibility(View.GONE);
        }
    }

    private VoteChatDetailReceivedBean convertContentStrToObj(String content) {
        if(TextUtils.isEmpty(content)){
            return null;
        }else{
//            content = content.replaceAll("\\\"", "\"");

            VoteChatDetailReceivedBean receivedBean = new Gson().fromJson(content, VoteChatDetailReceivedBean.class);
            if(receivedBean!=null){
                return receivedBean;
            }else{
                return null;
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
