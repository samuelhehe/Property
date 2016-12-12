package xj.property.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.adapter.UserBonusAdapter;
import xj.property.beans.RPValueAllBean;
import xj.property.beans.UserBonusAdapterBean;
import xj.property.beans.UserBonusBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;

/**
 * Created by n on 2015/6/8.
 */
public class UserBonusUtils {

   /* private static ArrayList<UserBonusBean.Info> canused=new ArrayList<UserBonusBean.Info>();//可用的

    public static void getCanusedCount(Context context){
        getUserBonusList(context);
    }

    *//**
     * 获取ActList部分
     *//*
    interface GetUserBonusService {
        @GET("/api/v1/communities/{communityId}/bonuses/users/{emobId}")
        void getUserBonusList(@Path("communityId") long communityId, @Path("emobId") String emobId, Callback<UserBonusBean> cb);
    }

    public static void getUserBonusList(final Context context) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE2).build();
        GetUserBonusService service = restAdapter.create(GetUserBonusService.class);
        Callback<UserBonusBean> callback = new Callback<UserBonusBean>() {
            @Override
            public void success(UserBonusBean bean, retrofit.client.Response response) {
                if (null==bean&&!"yes".equals(bean.getStatus())){
                    return;
                }
                fillData( bean.getInfo());
                PreferencesUtil.saveBangBangQuanCount(context,canused.size()+"");
            }

            @Override
            public void failure(RetrofitError error) {
            }
        };
       service.getUserBonusList(PreferencesUtil.getCommityId(context), PreferencesUtil.getLoginInfo(context).getEmobId(),callback);
    }

    private static  void fillData(List<UserBonusBean.Info> list) {
        for (UserBonusBean.Info info:list){
            if ("unused".equals(info.getBonusStatus().trim())){
                if (Long.parseLong(info.getExpireTime().trim())*1000>System.currentTimeMillis()){
                    canused.add(info);
                }
            }
        }
    }


    interface RPValueService {
        @GET("/api/v1/communities/{communityId}/circles/{emobId}/praises")
        void getRPValue(@Path("communityId") int communityId, @Path("emobId") String emobId, @QueryMap Map<String, String> map, Callback<RPValueAllBean> cb);
    }

    public static void getRPValue(final Context context ) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        RPValueService service = restAdapter.create(RPValueService.class);
        Callback<RPValueAllBean> callback = new Callback<RPValueAllBean>() {
            @Override
            public void success(RPValueAllBean bean, retrofit.client.Response response) {
                if ("yes".equals(bean.getStatus())) {
                    PreferencesUtil.saveRp(context,bean.getInfo().getCharacterValues(),bean.getInfo().getCharacterPercent()+"");
                } else {
                }
            }

            @Override
            public void failure(RetrofitError error) {
            }
        };
        Map<String, String> option = new HashMap<>();
        option.put("pageNum", "1");
        option.put("pageSize", "10");
        service.getRPValue(PreferencesUtil.getCommityId(context), PreferencesUtil.getLoginInfo(context).getEmobId(), option, callback);
    }
*/

}
