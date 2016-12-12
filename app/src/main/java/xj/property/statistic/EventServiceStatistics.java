package xj.property.statistic;

import android.content.Context;
import android.util.Log;

import java.util.Date;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import xj.property.XjApplication;
import xj.property.beans.StatusBean;
import xj.property.beans.VistorEvent;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;

/**
 * Created by Administrator on 2015/12/2.
 */
public class EventServiceStatistics {

    public interface PostResultListener{
        public void onPostSuccess(StatisticRespBean statisticRespBean, retrofit.client.Response response);

        public void onPostFailure(RetrofitError error);
    }

    public static  void eventService(final Context context, final String serviceId, final String serviceName, final int count) {
        Log.i("debbug", "eventService" + serviceId + serviceName + count);
        if (count < 1) return;
        if (context == null) {
            Log.i("debbug", "eventService context == null  return " + serviceId + serviceName + count);
            return;
        }
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_AGENT).build();
//        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://192.168.1.51:9095").build();
        EventServiceService service = restAdapter.create(EventServiceService.class);
        Callback<StatusBean> callback = new Callback<StatusBean>() {
            @Override
            public void success(StatusBean object, retrofit.client.Response response) {
                if (object != null && !"true".equals(object.getStatus()))
                    eventService(context,serviceId, serviceName, count - 1);
            }

            @Override
            public void failure(RetrofitError error) {
                eventService(context,serviceId, serviceName, count - 1);
            }
        };
        VistorEvent vistorEvent = new VistorEvent();
        vistorEvent.hour = (int) (new Date().getTime() / 1000);
        vistorEvent.serviceId = serviceId;
        vistorEvent.serviceName = serviceName;
        try {
            vistorEvent.label = context.getClass().getName();
//                    .getLocalClassName();
            vistorEvent.appVersion = UserUtils.getVersion(XjApplication.getInstance().getApplicationContext());
        } catch (Exception e) {
            vistorEvent.appVersion = "";
        }

        Log.i("debbug", "getTourist=" + PreferencesUtil.getTourist(context));

        vistorEvent.setEmobId(PreferencesUtil.getLogin(context) ? PreferencesUtil.getLoginInfo(context).getEmobId() : PreferencesUtil.getTourist(context));
        vistorEvent.setUserId(PreferencesUtil.getLogin(context) ? PreferencesUtil.getUserId(context) + "" : PreferencesUtil.getTouristid(context) + "");
        // Log.i("onion","info1: "+info.toString());
        if (vistorEvent.getEmobId().trim().length() == 0) {
            vistorEvent.setUserId(PreferencesUtil.getlogoutUserId(context) + "");
            vistorEvent.setEmobId(PreferencesUtil.getlogoutEmobId(context));
        }

        //  Log.i("onion", "自定义事件采集: " + "serviceId: " + serviceId + "  " + vistorEvent.toString());

        service.onEventService(StrUtils.string2md5(Config.BANGBANG_TAG + StrUtils.dataHeader(vistorEvent)), vistorEvent, PreferencesUtil.getCommityId(context), callback);
    }

    public static  void eventStatisticService(EventServiceReqBean eventServiceReqBean, final PostResultListener postResultListener) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(EventServiceUtils.STATISTICS_BASEURL).build();
        if(Config.SHOW_lOG){
            restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        }
        EventServiceService service = restAdapter.create(EventServiceService.class);
        Callback<StatisticRespBean> callback = new Callback<StatisticRespBean>() {
            @Override
            public void success(StatisticRespBean statisticRespBean, retrofit.client.Response response) {
               if(postResultListener!=null){
                   postResultListener.onPostSuccess(statisticRespBean,response);
               }
            }

            @Override
            public void failure(RetrofitError error) {
                if(postResultListener!=null){
                    postResultListener.onPostFailure(error);
                }
            }
        };
        service.eventStatisticService(StrUtils.string2md5(Config.BANGBANG_TAG + StrUtils.dataHeader(eventServiceReqBean)), eventServiceReqBean, callback);
    }

}
