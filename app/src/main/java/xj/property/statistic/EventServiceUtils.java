package xj.property.statistic;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import retrofit.RetrofitError;
import xj.property.beans.UserInfoDetailBean;
import xj.property.utils.CommonUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;

/**
 * Created by Administrator on 2016/1/11.
 * <p/>
 * 采集用户信息的整体策略:
 * ===========================================================
 * 当前采集信息包含: 1. 用户在首页模块的点击事件.2. 退出每一个模块事件.
 * 采集策略:
 * 1. 每次进入应用后,在登陆的状态下,进行上传当前时间之前的所有event 记录
 * 2. 进入每一个模块的点击事件, 进行存,传, 传后的删除/更新状态操作.
 * 3. 退出每一个模块的点击事件. 进行存,传, 传后的删除/更新状态操作.
 * 4. 应用退出时, 上传所有的event 记录
 *
 * 2016/03/22
 *
 * 为防止频繁启动请求线程，限制策略
 * 1, 读取上一次请求结束的时间
 * 2，进行判断如果当前时间与之大于30s则允许发出请求线程
 * 3，小于该时间，将状态改为async，下次上传
 */
public class EventServiceUtils {

    ////event 记录的类型.
    public static final String EventType_click = "click";
    public static final String EventType_exit = "exit";
    public static final String EventType_all = "all";

    //// event记录的状态.
    public static final String EventType_sync = "sync";
    public static final String EventType_async = "async";

    public static final String TAG = "EventServiceUtils";
    /**
     * 用户信息采集  http://statistics.ixiaojian.com
     */
    public static final String STATISTICS_BASEURL = Config.NET_BASE_STATISTICS;

    public  static final String STAT_forclick = STATISTICS_BASEURL + "/service/clicks";
    private static final long DELYED_SECONDS_FOR_CLICK = 10;/// 默认延迟时间
    private static final long DELYED_SECONDS_FOR_EXIT = 5;/// 默认延迟时间

    private final int THREAD_NUM = 2;
    private final int communityId;
    private String emobId;

    private UserInfoDetailBean userInfoBean;

    private Context context;

    private ScheduledExecutorService executorService;

    public EventServiceUtils(Context context) {
        this.context = context;
        this.executorService = Executors.newScheduledThreadPool(THREAD_NUM);

        this.userInfoBean = PreferencesUtil.getLoginInfo(context);
        if (userInfoBean != null) {
            //// 登陆状态
            this.emobId = userInfoBean.getEmobId();
            this.communityId = (int) userInfoBean.getCommunityId();
        } else {
            /// 未登录状态
            this.emobId = PreferencesUtil.getTourist(context);
            this.communityId = PreferencesUtil.getCommityId(context);
        }
    }
    public ScheduledExecutorService getExecuteService() {
        if (executorService == null || executorService.isTerminated() || executorService.isShutdown()) {
            executorService = Executors.newScheduledThreadPool(THREAD_NUM);
        }
        return executorService;
    }

    public String generateUUID() {
        return "android" + UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 点-->存-->传
     * 记录当前点击事件.
     * @param uuid
     */
    public void postClickEvent(String uuid, String serviceId) {

        Log.d(TAG, "postClickEvent uuid : " + uuid + " serviceId : " + serviceId);

        if (getExecuteService() != null && !getExecuteService().isTerminated()) {
             getExecuteService().submit(new EventClickThread(uuid, serviceId));
        }
    }

    /**
     * 退-->存-->传
     * 记录当前退出事件.
     *
     * @param uuid
     * @param classname
     */
    public void postExitEvent(String uuid, String classname) {
        Log.d(TAG, "postExitEvent uuid : " + uuid + " classname : " + classname);
        if (getExecuteService() != null && !getExecuteService().isTerminated()) {
            getExecuteService().submit(new EventExitThread(uuid, classname));
        }
    }

    public void postScheduleClickEvent(String uuid, String serviceId){
        Log.d(TAG, "postScheduleClickEvent uuid : " + uuid + " serviceId : " + serviceId);

        if (getExecuteService() != null && !getExecuteService().isTerminated()) {
//            getExecuteService().submit(new EventClickThread(uuid, serviceId));
            getExecuteService().schedule(new EventClickThread(uuid,serviceId), DELYED_SECONDS_FOR_CLICK, TimeUnit.SECONDS);
        }
    }

    public void postScheduleExitEvent(String uuid, String classname){
        Log.d(TAG, "postScheduleExitEvent uuid : " + uuid + " classname : " + classname);

        if (getExecuteService() != null && !getExecuteService().isTerminated()) {
//            getExecuteService().submit(new EventClickThread(uuid, serviceId));
            getExecuteService().schedule(new EventExitThread(uuid,classname), DELYED_SECONDS_FOR_EXIT, TimeUnit.SECONDS);
        }
    }


    /**
     * 传送在此之前所有没有上传完成的event list.
     */
    public void postBeforeTimeEvent() {
        Log.d(TAG, "postBeforeTimeEvent ");
        getExecuteService().submit(new EventBeforeTimePostThread());
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    class EventBeforeTimePostThread implements Callable {
        @Override
        public Object call() throws Exception {

            final int time = (int) (System.currentTimeMillis() / 1000);
            List<StatisticsEventModel> eventModels = EventServiceDBUtils.queryAllEventBeforeTime(EventType_all, time);
            Log.d(TAG, "EventBeforeTimePostThread eventModels size : " + eventModels != null ? "" + eventModels.size() : "" + eventModels);
            if (eventModels != null && eventModels.size() > 0) {
                EventServiceReqBean eventServiceReqBean = new EventServiceReqBean();
                List<EventServiceBean> reqBeans = new ArrayList<>();
                for (StatisticsEventModel model : eventModels) {
                    Log.d(TAG, "EventBeforeTimePostThread model : " + model);

                    EventServiceBean reqBean = new EventServiceBean();
                    if (model != null) {
                        if (TextUtils.equals(model.getEventType(), EventType_click)) {
                            reqBean.setEmobId(model.getEmobId());
                            reqBean.setUuid(model.getUuid());
                            reqBean.setCommunityId(model.getCommunityId());
                            reqBean.setClickTime(model.getEventTime());
                            reqBean.setClickTarget(model.getEvent());
                            if (model.getStatus() == EventServiceDBUtils.EventStatus.async.ordinal()) {
                                reqBean.setDataType(EventType_async);
                            } else if (model.getStatus() == EventServiceDBUtils.EventStatus.sync.ordinal()) {
                                reqBean.setDataType(EventType_sync);
                            }
                        } else if (TextUtils.equals(model.getEventType(), EventType_exit)) {
                            reqBean.setUuid(model.getUuid());
                            reqBean.setExitTime(model.getEventTime());
                            if (model.getStatus() == EventServiceDBUtils.EventStatus.async.ordinal()) {
                                reqBean.setDataType(EventType_async);
                            } else if (model.getStatus() == EventServiceDBUtils.EventStatus.sync.ordinal()) {
                                reqBean.setDataType(EventType_sync);
                            }
                        }
                        reqBeans.add(reqBean);
                    }
                }
                eventServiceReqBean.setData(reqBeans);
                EventServiceStatistics.eventStatisticService(eventServiceReqBean, new EventServiceStatistics.PostResultListener() {
                    @Override
                    public void onPostSuccess(StatisticRespBean statisticRespBean, retrofit.client.Response response) {
                        if (statisticRespBean != null && TextUtils.equals("yes", statisticRespBean.getStatus())) {
                            /// 删除在此之前所有上传成功的EventList
                            EventServiceDBUtils.deleteAllEventBeforeTime(EventType_all, time);
                        }
                    }

                    @Override
                    public void onPostFailure(RetrofitError error) {
                        error.printStackTrace();
                    }
                });
            } else {
                Log.e(TAG, "EventBeforeTimePostThread eventModels  is null ");
            }
            return null;
        }
    }


    class EventClickThread implements Callable {

        private String serviceId;
        private String uuid;

        public EventClickThread(String uuid, String serviceId) {
            this.uuid = uuid;
            this.serviceId = serviceId;
        }

        @Override
        public Object call() throws Exception {
            if (TextUtils.isEmpty(emobId) || communityId <= 0) {
                Log.e(TAG, "EventClickThread emobId " + emobId + " communityId : " + communityId);
                return null;
            }
            int time = (int) (System.currentTimeMillis() / 1000);
            //// 存
            EventServiceDBUtils.saveClickEvent(emobId, communityId, time, uuid, serviceId);
            if(!CommonUtils.isNetWorkConnected(getContext())){
                /// 没有上传,则将该条点击event 状态修改为async
                EventServiceDBUtils.updateEventByUUID(EventType_click, uuid, EventServiceDBUtils.EventStatus.async);
                return null;
            }

            //// 传
            EventServiceReqBean eventServiceReqBean = new EventServiceReqBean();
            List<EventServiceBean> reqBeans = new ArrayList<>();
            EventServiceBean reqBean = new EventServiceBean();
            reqBean.setEmobId(userInfoBean.getEmobId());
            reqBean.setUuid(uuid);
            reqBean.setCommunityId((int) userInfoBean.getCommunityId());
            reqBean.setClickTime(time);
            reqBean.setClickTarget(serviceId);
            reqBean.setDataType(EventType_sync);
            reqBeans.add(reqBean);
            if (eventServiceReqBean.getData() != null) {
                eventServiceReqBean.getData().clear();
            }
            eventServiceReqBean.setData(reqBeans);
            EventServiceStatistics.eventStatisticService(eventServiceReqBean, new EventServiceStatistics.PostResultListener() {
                @Override
                public void onPostSuccess(StatisticRespBean statisticRespBean, retrofit.client.Response response) {
                    if (statisticRespBean != null && TextUtils.equals("yes", statisticRespBean.getStatus())) {
                        EventServiceDBUtils.deleteEventByUUID(EventType_click, uuid);
                    } else {
                        /// 没有上传成功,则将该条点击event 状态修改为async
                        EventServiceDBUtils.updateEventByUUID(EventType_click, uuid, EventServiceDBUtils.EventStatus.async);
                    }
                }

                @Override
                public void onPostFailure(RetrofitError error) {
                    error.printStackTrace();
                    /// 没有上传成功,则将该条点击event 状态修改为async
                    EventServiceDBUtils.updateEventByUUID(EventType_click, uuid, EventServiceDBUtils.EventStatus.async);
                }
            });
            return null;
        }
    }

    class EventExitThread implements Callable {
        private String classname;
        private String uuid;

        public EventExitThread(String uuid, String classname) {
            this.uuid = uuid;
            this.classname = classname;
        }

        @Override
        public Object call() throws Exception {

            if (TextUtils.isEmpty(emobId) || communityId <= 0) {
                Log.e(TAG, "EventExitThread emobId " + emobId + " communityId : " + communityId);
                return null;
            }

            int time = (int) (System.currentTimeMillis() / 1000);
            /// 退出的className没有使用... 2016/1/13
            EventServiceDBUtils.saveExitEvent(emobId, communityId, time, uuid, classname);

            if(!CommonUtils.isNetWorkConnected(getContext())){
                /// 没有上传,则将该条点击event 状态修改为async
                EventServiceDBUtils.updateEventByUUID(EventType_exit, uuid, EventServiceDBUtils.EventStatus.async);
                return null;
            }

            EventServiceReqBean eventServiceReqBean = new EventServiceReqBean();
            List<EventServiceBean> reqBeans = new ArrayList<>();
            EventServiceBean reqBean = new EventServiceBean();
            reqBean.setUuid(uuid);
            reqBean.setDataType(EventType_sync);
            reqBean.setExitTime(time);
            reqBeans.add(reqBean);
            if (eventServiceReqBean.getData() != null) {
                eventServiceReqBean.getData().clear();
            }
            eventServiceReqBean.setData(reqBeans);
            EventServiceStatistics.eventStatisticService(eventServiceReqBean, new EventServiceStatistics.PostResultListener() {
                @Override
                public void onPostSuccess(StatisticRespBean statisticRespBean, retrofit.client.Response response) {
                    if (statisticRespBean != null && TextUtils.equals("yes", statisticRespBean.getStatus())) {
                        EventServiceDBUtils.deleteEventByUUID(EventType_exit, uuid);
                    } else {
                        /// 没有上传成功,则将该条点击event 状态修改为async
                        EventServiceDBUtils.updateEventByUUID(EventType_exit, uuid, EventServiceDBUtils.EventStatus.async);
                    }
                }

                @Override
                public void onPostFailure(RetrofitError error) {
                    error.printStackTrace();
                    /// 没有上传成功,则将该条点击event 状态修改为async
                    EventServiceDBUtils.updateEventByUUID(EventType_exit, uuid, EventServiceDBUtils.EventStatus.async);
                }
            });

            return null;
        }
    }


}
