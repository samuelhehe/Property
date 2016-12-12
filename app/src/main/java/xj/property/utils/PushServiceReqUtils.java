package xj.property.utils;

import android.content.Context;
import android.util.Log;

import com.activeandroid.query.Select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.XjApplication;
import xj.property.beans.UnreadBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.cache.XJNotify;
import xj.property.event.NewNotifyEvent;
import xj.property.event.NewPushEvent;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.PushUtil;

public class PushServiceReqUtils {

    interface UnreadService {
//        @GET("/api/v1/communities/{communityId}/pushmessage/{emobid}/unread")
//        void getInfo(@Path("communityId") long communityId, @Path("emobid") String emobid, @QueryMap Map<String, String> options, Callback<UnreadBean> cb);
//        @GET("/api/v1/communities/{communityId}/pushmessage/{emobid}/unread")
//        /api/v3/home/messages?emobId={用户环信ID}&time={上次读取的时间}

        @GET("/api/v3/home/messages")
        void getInfo(@QueryMap Map<String, String> options, Callback<CommonRespBean<List<UnreadBean>>> cb);
    }

    public   void getUnreadInfo(final Context context) {

        String emobid;
        UserInfoDetailBean bean = PreferencesUtil.getLoginInfo(context);
        if (bean == null) {
            emobid = PreferencesUtil.getTourist(context);
        } else {
            emobid = bean.getEmobId();
        }
        HashMap<String, String> opt = new HashMap<>();
        int homeLastPullReqTime = PreferencesUtil.getHomeLastPullReqTime(context);
        if (homeLastPullReqTime <= 0) {
            homeLastPullReqTime = (int) (System.currentTimeMillis() / 1000L);
        }
        int lastpulltime = (int) (System.currentTimeMillis() / 1000L);
        PreferencesUtil.setHomeLastPullReqTime(context, lastpulltime);
        opt.put("time", "" + homeLastPullReqTime);
        opt.put("emobId", "" + emobid);
//        emobId={用户环信ID}&time={上次读取的时间}

        UnreadService service = RetrofitFactory.getInstance().create(context, opt, UnreadService.class);
        Callback<CommonRespBean<List<UnreadBean>>> callback = new Callback<CommonRespBean<List<UnreadBean>>>() {
            @Override
            public void success(CommonRespBean<List<UnreadBean>> bean, retrofit.client.Response response) {
                if (bean != null && "yes".equals(bean.getStatus())) {
                    processingUnreadInfo(context,bean.getData());
                } else {
                    Log.d("getUnreadInfo", " no or bean==null ");
                }
            }
            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        };
        service.getInfo(  opt, callback);
    }

    private   void processingUnreadInfo(Context context,List<UnreadBean> bean) {
        for (UnreadBean info : bean) {
            int cmd_code = info.getCMD_CODE();
            switch (cmd_code) {
                case 100:
                case 101:
                case 102:
                case 103:
                case 104:
                case 105:
                case 106:
                case 107: // 帮主
                    String emobid = "-1";
                    if (PreferencesUtil.getLogin(XjApplication.getInstance()))
                        emobid = PreferencesUtil.getLoginInfo(XjApplication.getInstance()).getEmobId();
//                        else
//                            emobid = "-1";
                    XJNotify notifydb = new Select().from(XJNotify.class).where("messageId = ?", info.getMessageId()).executeSingle();
                    /// 从db中查询消息
                    if (notifydb != null) {
                        Log.d("XJNotify", "Main processingUnreadInfo repeat return notifydb !=null  info.getMessageId()  " + info.getMessageId());

                        return;
                    } else {
                        Log.d("XJNotify", "Main processingUnreadInfo repeat return notifydb ==null  info.getMessageId()  " + info.getMessageId());

                    }
                    XJNotify notify = new XJNotify(emobid == null ? "-1" : emobid, cmd_code, info.getTitle(), info.getContent(), info.getTimestamp(), false, "no");
                    notify.save();
                    EventBus.getDefault().post(new NewNotifyEvent(notify, true));
                    break;


//
//                    String emobid2 = "-1";
//                    if (PreferencesUtil.getLogin(XjApplication.getInstance()))
//                        emobid2 = PreferencesUtil.getLoginInfo(XjApplication.getInstance()).getEmobId();
////                        else
////                            emobid = "-1";
//                    XJNotify notify2 = new XJNotify(emobid2 == null ? "-1" : emobid2, cmd_code, bean.getInfo().get(i).getTitle(),
//                            bean.getInfo().get(i).getContent(), bean.getInfo().get(i).getTimestamp(), false, "no");
//                    notify2.save();
//                    EventBus.getDefault().post(new NewNotifyEvent(notify2, true));

//                    break;


                case 110:
//                    int count = PreferencesUtil.getUnReadCount(XjApplication.getInstance()) + 1;
//                    PreferencesUtil.saveUnReadCount(XjApplication.getInstance(), count);


                    EventBus.getDefault().post(new NewPushEvent(cmd_code));
                    break;
                case -1:
                    break;
                case 121://评论
                case 122://赞
//                    int circleCount = PreferencesUtil.getUnReadCircleCount(XjApplication.getInstance()) + 1;
//                    PreferencesUtil.saveUnReadCircleCount(XjApplication.getInstance(), circleCount);  2015/12/02
                    EventBus.getDefault().post(new NewPushEvent(cmd_code));
                    break;

                case 131://邻居帮评论
                case 132://邻居帮赞 // 没有使用

//                    int unreadCooperationCount = PreferencesUtil.getCooperationIndexCount(XjApplication.getInstance()) + 1;
//                    PreferencesUtil.saveCooperationIndexCount(XjApplication.getInstance(), unreadCooperationCount);
                    EventBus.getDefault().post(new NewPushEvent(cmd_code));

                    break;

                case 141:
                case 142:
                case 143:
                case 144:

//                    1、帮主投票里，有人给我投票了     XXX对你投了一票-->election-->CMD_CODE 141
//
//                    2、我发起的投票有人投了         XXX参与了你的投票-->vote-->CMD_CODE 142
//
//                    3、有人给我发起的投票评论了     XXX对你的投票发表了评论-->comment-->CMD_CODE 143
//
//                    4、有人回复了我对某投票的评论。   XXX回复了你的投票评论 -->reply-->CMD_CODE 144

//                    int unreadVoteCount = PreferencesUtil.getVoteIndexCount(XjApplication.getInstance()) + 1;
//                    PreferencesUtil.saveVoteIndexCount(XjApplication.getInstance(), unreadVoteCount);  2015/12/02

                    EventBus.getDefault().post(new NewPushEvent(cmd_code));
                    break;
            }

            PushUtil.callBack4Service(context,info.getMessageId(), info.getEmobId());

        }
    }



}
