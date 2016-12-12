package xj.property.utils.other;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.beans.BaseBean;
import xj.property.beans.BlackRequest;
import xj.property.beans.QuaryToken;
import xj.property.beans.StatusBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.NetBaseUtils;
import xj.property.netbasebean.BlackListReqBean;
import xj.property.utils.image.utils.StrUtils;

/**
 * Created by Administrator on 2015/5/26.
 */
public class BlackListHelper {

    /**
     * 把某人从通讯录黑名单移除
     * {
     * //                "emobIdFrom": "{拉黑方环信ID}",
     * //                "emobIdTo": "{被拉黑方环信ID}",
     * //                "type": "{黑名单类型：activity->聊天黑名单，circle->生活圈黑名单}"
     * //        }
     * v3 2016/03/04
     * @param context
     * @param emobIdTo
     * @param handler
     *
     */
    public static void removeBlack(final Context context, final String emobIdTo, final Handler handler) {
//        {
//                "emobIdFrom": "{拉黑方环信ID}",
//                "emobIdTo": "{被拉黑方环信ID}",
//                "type": "{黑名单类型：activity->聊天黑名单，circle->生活圈黑名单}"
//        }
        BlackListReqBean blackListReqBean = new BlackListReqBean();
        blackListReqBean.setEmobIdTo(emobIdTo);
        blackListReqBean.setEmobIdFrom(PreferencesUtil.getLoginInfo(context).getEmobId());
        blackListReqBean.setType(BlackListReqBean.type_activity);
        NetBaseUtils.removeUserFromBlackList(context,blackListReqBean, new NetBaseUtils.NetRespListener<CommonRespBean<String>>() {
            @Override
            public void successYes(CommonRespBean<String> commonRespBean, Response response) {
                handler.sendEmptyMessage(Config.REMOVEBLACKLIST);
            }

            @Override
            public void successNo(CommonRespBean<String> commonRespBean, Response response) {
                handler.sendEmptyMessage(Config.TASKERROR);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("onion",error.toString());
                handler.sendEmptyMessage(Config.NETERROR);
            }
        });
    }


    /**
     * 添加某人到通讯录黑名单
     * <p/>
     * v3 2016/03/04
     */
    public static void addBlack(final Context context,final String emobid,final Handler handler) {
        BlackListReqBean blackListReqBean = new BlackListReqBean();
        blackListReqBean.setEmobIdTo(emobid);
        blackListReqBean.setEmobIdFrom(PreferencesUtil.getLoginInfo(context).getEmobId());
        blackListReqBean.setCommunityId(PreferencesUtil.getLoginInfo(context).getCommunityId());
        blackListReqBean.setType(BlackListReqBean.type_activity);
        NetBaseUtils.addUserToBlackList(context,blackListReqBean, new NetBaseUtils.NetRespListener<CommonRespBean<String>>() {
            @Override
            public void successYes(CommonRespBean<String> commonRespBean, Response response) {
                handler.sendEmptyMessage(Config.ADDBLACKLIST);
            }

            @Override
            public void successNo(CommonRespBean<String> commonRespBean, Response response) {
                handler.sendEmptyMessage(Config.TASKERROR);
            }

            @Override
            public void failure(RetrofitError error) {
                handler.sendEmptyMessage(Config.NETERROR);
            }
        });
    }

}
