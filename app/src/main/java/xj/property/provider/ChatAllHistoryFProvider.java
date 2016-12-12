package xj.property.provider;

import android.content.Context;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.Path;
import xj.property.XjApplication;
import xj.property.beans.ResultGroupInfo;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.widget.GroupHeaderHelper;

/**
 * 作者：asia on 2015/11/12 20:50
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 刷新群列表图片：
 */
public class ChatAllHistoryFProvider {

    private Context mContext;

    public ChatAllHistoryFProvider(Context context){
        this.mContext = context;
    }

    interface getOrderInfoService {
        @GET("/api/v1/communities/{communityId}/groups/{groupId}/members")
        void getOrderInfo(@Path("communityId") int communityId, @Path("groupId") String groupId, Callback<ResultGroupInfo> cb);
    }

    public void getGroupInfo(final String groupId) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        getOrderInfoService service = restAdapter.create(getOrderInfoService.class);
        Callback<ResultGroupInfo> callback = new Callback<ResultGroupInfo>() {
            @Override
            public void success(ResultGroupInfo bean, retrofit.client.Response response) {
                if("yes".equals(bean.status)&&bean.info.size()>1)
                    GroupHeaderHelper.createGroupId(bean.info, groupId, XjApplication.getInstance());
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        };
        service.getOrderInfo(PreferencesUtil.getCommityId(mContext), groupId, callback);
    }
}
