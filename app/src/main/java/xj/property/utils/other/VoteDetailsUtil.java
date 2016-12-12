package xj.property.utils.other;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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
import xj.property.activity.user.UserGroupInfoActivity;
import xj.property.beans.CooperationEveReqBean;
import xj.property.beans.CooperationEveRespBean;
import xj.property.beans.PointPraiseBean;
import xj.property.beans.RPValueAllBean;
import xj.property.beans.StatusBean;
import xj.property.beans.VoteDetailEveReqBean;
import xj.property.beans.VoteDetailEveRespBean;
import xj.property.event.EvaEvent;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.image.utils.StrUtils;

/**
 * Created by Administrator on 2015/6/8.
 */

public class VoteDetailsUtil {

    //点赞
//  public static void zambia(String emobId,String To){
//
//  }


    /**
     *
     * 评论
     * @param emobIdFrom 谁评论
     * @param emobIdTo 给谁评论
     * @param handler handler回调
     */
    public static void eva(Context context, String emobIdFrom, String emobIdTo, String chatContent, int voteId,int isComment, Handler handler) {
        addEva(context, handler, new VoteDetailEveReqBean(voteId, emobIdFrom,emobIdTo, chatContent,isComment));
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
        stringBuilder.append(":" + content);
        SpannableString sp = new SpannableString(stringBuilder.toString());
//        sp.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        NoUndelineClickSpan fromSpan = new NoUndelineClickSpan();
        fromSpan.setContext(context);
        fromSpan.setEmobid(fromEmobid);
        sp.setSpan(fromSpan, 0, fromNike.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        if (toClickSpan != null)
            sp.setSpan(toClickSpan, fromNike.length() + 2, fromNike.length() + 2 + toNike.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        TextView tv = (TextView) item_evaback.findViewById(R.id.tv_zambia);
        tv.setText(sp);
        tv.getPaint().setUnderlineText(false);
        tv.setLinkTextColor(0xFF24A529);
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
            Intent intent = new Intent(context, UserGroupInfoActivity.class);
            Log.i("onion", "emobid" + emobid);
            intent.putExtra(Config.INTENT_PARMAS2, emobid);
            context.startActivity(intent);
            EvaEvent evaEvent = new EvaEvent(null, null, 0, 0);
            EventBus.getDefault().post(evaEvent);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(ds.linkColor);
            ds.setUnderlineText(false);
        }
    }


    interface EvaService {
        ///api/v1/communities/{communityId}/circles/{emobId}
//        @POST("/api/v1/communities/{communityId}/vote/reply")
//        void addEva(@Header("signature") String signature, @Body VoteDetailEveReqBean lifeEvaBean, @Path("communityId") int communityId, Callback<VoteDetailEveRespBean> cb);
//        @POST("/api/v1/communities/{communityId}/vote/reply")

//        /api/v3/votes/comment

        @POST("/api/v3/votes/comment")
        void addEva(@Body VoteDetailEveReqBean lifeEvaBean, Callback<CommonRespBean<String>> cb);
    }

    public static void addEva( Context context, final Handler handler, VoteDetailEveReqBean lifeEvaBean) {
        Log.i("onion", "lifeEvaBean" + lifeEvaBean);
        EvaService service = RetrofitFactory.getInstance().create(context,lifeEvaBean,EvaService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> bean, retrofit.client.Response response) {
//                Log.i("onion", "status" + bean.getStatus() + bean.getResultId());
                if ("yes".equals(bean.getStatus())) {
                    Message message = Message.obtain();
                    message.what = Config.TASKCOMPLETE;
//                    message.arg1 = bean.getResultId();
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
        service.addEva( lifeEvaBean, callback);
    }


}
