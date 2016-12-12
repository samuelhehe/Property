package xj.property.utils.other;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.XjApplication;
import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
import xj.property.activity.user.UserGroupInfoActivity;
import xj.property.beans.LifeEvaBean;
import xj.property.beans.PointPraiseBean;
import xj.property.beans.RPValueAllBean;
import xj.property.beans.StatusBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.event.EvaEvent;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.SmileUtils;
import xj.property.utils.image.utils.StrUtils;

/**
 * Created by Administrator on 2015/6/8.
 */

public class FriendZoneUtil {

    //点赞
//  public static void zambia(String emobId,String To){
//
//  }
    interface PraiseService {
//        @POST("/api/v1/communities/{communityId}/circles/{emobId}/praise")
//        void postPraise(@Header("signature") String signature, @Body PointPraiseBean pointPraiseBean,
//                        @Path("communityId") long communityId, @Path("emobId") String emobId, Callback<StatusBean> cb);

//        @POST("/api/v1/communities/{communityId}/circles/{emobId}/praise")

        @POST("/api/v3/lifePraises")  /// v3 2016/03/04  生活圈点赞, 评论点赞
        void postPraise(@Body PointPraiseBean pointPraiseBean, Callback<CommonRespBean<String>> cb);
    }

    public static void zambia(final String emobIdTo, int lifeCircleId, int status, Context context, final Handler handler) {
        zambia(emobIdTo, lifeCircleId, 0, status, context, handler);
    }
    public static void zambia(final String emobIdTo, int lifeCircleId, int lifeCircleDetailId, int status, Context context, final Handler handler) {

        PointPraiseBean pointPraiseBean = new PointPraiseBean();
        pointPraiseBean.setEmobIdTo(emobIdTo);
        pointPraiseBean.setLifeCircleId(lifeCircleId);
        pointPraiseBean.setCommunityId(PreferencesUtil.getCommityId(context));
        pointPraiseBean.setEmobIdFrom(PreferencesUtil.getLoginInfo(context).getEmobId());
        if(status==1){
            pointPraiseBean.setPraiseType(PointPraiseBean.praise_type_circle);
        }else if(status==2){
            pointPraiseBean.setPraiseType(PointPraiseBean.praise_type_comment);
        }
        Log.i("onion", "pointPraiseBean" + pointPraiseBean);

        PraiseService service = RetrofitFactory.getInstance().create(context,pointPraiseBean,PraiseService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> bean, retrofit.client.Response response) {
                if ("yes".equals(bean.getStatus())) {
                    handler.sendEmptyMessage(Config.TASKCOMPLETE);
                } else {
//                    handler.sendEmptyMessage(Config.TASKERROR);
                    Message message = new Message();
                    message.what = Config.TASKERROR;
                    message.obj = bean.getMessage();
                    handler.sendMessage(message);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                handler.sendEmptyMessage(Config.NETERROR);
                error.printStackTrace();
            }
        };
        service.postPraise(pointPraiseBean,callback);
    }

    //评论
    public static void eva(Context context,int  communityId, String emobIdTo, String emobIdFrom,String detailContent, int lifeCircleId, Handler handler) {
        addEva(context,handler, new LifeEvaBean(communityId,emobIdTo,emobIdFrom, lifeCircleId,detailContent));
    }

    public static void initEva(Context context, String fromNike, String fromEmobid, String toNike, String toEmobid, String content, int num, LinearLayout item_evaback) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(fromNike);
        NoUndelineClickSpan toClickSpan = null;

        if (toEmobid != null && toNike != null) {
            stringBuilder.append("回复");
            stringBuilder.append(toNike);
            toClickSpan = new NoUndelineClickSpan();
            toClickSpan.setContext(context);
            toClickSpan.setEmobid(toEmobid);
        }

        stringBuilder.append(":\t" + content);

//        SpannableString sp = new SpannableString(StrUtils.convert2DBC(stringBuilder.toString()));
        SpannableString sp = new SpannableString(stringBuilder.toString());

//        sp.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        NoUndelineClickSpan fromSpan = new NoUndelineClickSpan();
        fromSpan.setContext(context);
        fromSpan.setEmobid(fromEmobid);

        sp.setSpan(fromSpan, 0, fromNike.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        /// 设置包含tonick后的冒号颜色值  2015/12/16
        if (toClickSpan != null){
            sp.setSpan(toClickSpan, fromNike.length() + 2, fromNike.length() + 2 + toNike.length()+1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            /// 回复两个字的颜色 有人评论回复时显示
            sp.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.sys_green_theme_text_color)) ,fromNike.length(), fromNike.length()+2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        /// 回复内容文字颜色
        sp.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.sys_darker_69_theme_text_color)) , sp.length()-content.length(), sp.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

         /// 处理表情
        Spannable spanExtAll = SmileUtils.getSmiledText(context, sp);

        TextView tv = (TextView) item_evaback.findViewById(R.id.tv_zambia);
        tv.setText(spanExtAll, TextView.BufferType.SPANNABLE);
        tv.getPaint().setUnderlineText(false);
        /// fromNick tonick 颜色值设置
        tv.setLinkTextColor(context.getResources().getColor(R.color.sys_darker_66_theme_text_color));
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        tv.setFocusable(false);
//        tv.setClickable(false);
        tv.setLongClickable(false);


        TextView tvCount = (TextView) item_evaback.findViewById(R.id.tv_zambia_count);
        if (num > 0) {
            tvCount.setVisibility(View.VISIBLE);
            tvCount.setText(num + "");
        } else {
            tvCount.setVisibility(View.INVISIBLE);
        }

    }

    static class NoUndelineClickSpan extends ClickableSpan {
        Context context;
        String emobid;
        private UserInfoDetailBean userInfoDetailBean;

        public String getEmobid() {
            return emobid;
        }

        public void setEmobid(String emobid) {
            this.emobid = emobid;
        }

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View widget) {

            userInfoDetailBean = PreferencesUtil.getLoginInfo(context);
            if (userInfoDetailBean != null) {
                if(!TextUtils.equals(emobid,userInfoDetailBean.getEmobId())){
                    Intent intent = new Intent(context, UserGroupInfoActivity.class);
                    Log.i("onion", "emobid" + emobid);
                    intent.putExtra(Config.INTENT_PARMAS2, emobid);
                    context.startActivity(intent);
                }
                EvaEvent evaEvent = new EvaEvent(null, null, 0, 0);
                EventBus.getDefault().post(evaEvent);
            } else {
                Intent intent = new Intent(context, RegisterLoginActivity.class);
                context.startActivity(intent);
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(ds.linkColor);
            ds.setUnderlineText(false);
        }
    }


    interface EvaService {
        ///api/v1/communities/{communityId}/circles/{emobId}
//        @POST("/api/v1/communities/{communityId}/circles/{emobId}")
//        void addEva(@Header("signature") String signature, @Body LifeEvaBean lifeEvaBean, @Path("communityId") int communityId, @Path("emobId") String emobId, Callback<StatusBean> cb);
//
//        @POST("/api/v1/communities/{communityId}/circles/{emobId}")

        @POST("/api/v3/lifeCircleDetails")
        void addEva(@Body LifeEvaBean lifeEvaBean, Callback<CommonRespBean<Integer>> cb);

    }

    public static void addEva(Context context, final Handler handler, LifeEvaBean lifeEvaBean) {
        Log.i("onion", "lifeEvaBean" + lifeEvaBean);

        EvaService service = RetrofitFactory.getInstance().create(context,lifeEvaBean,EvaService.class);
        Callback<CommonRespBean<Integer>> callback = new Callback<CommonRespBean<Integer>>() {
            @Override
            public void success(CommonRespBean<Integer> bean, retrofit.client.Response response) {
                Log.i("onion", "status" + bean.getStatus() + bean.getData());
                if ("yes".equals(bean.getStatus())) {
                    Message message = Message.obtain();
                    message.what = Config.TASKCOMPLETE;
                    message.arg1 = bean.getData();
                    handler.sendMessage(message);
                } else {
                    handler.sendEmptyMessage(Config.TASKERROR);
                }
            }
            @Override
            public void failure(RetrofitError error) {
                handler.sendEmptyMessage(Config.NETERROR);
            }
        };
        service.addEva(lifeEvaBean,callback);
    }

}
