package xj.property.utils.other;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.query.Select;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.PUT;
import retrofit.http.Path;
import xj.property.XjApplication;
import xj.property.beans.PushCallBack;
import xj.property.beans.PushMessageBean;
import xj.property.beans.StatusBean;
import xj.property.cache.PushEventModel;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.image.utils.StrUtils;

/**
 * Created by Administrator on 2015/6/18.
 * v3 2016/03/18
 */
public class PushUtil {
    //该发通知return true,不该返回  false
    public static synchronized boolean onPushComing( Context context, JSONObject jsonObject, String stauts) {
        String emobid = "";
        if (PreferencesUtil.getLogin(XjApplication.getInstance())) {
            emobid = PreferencesUtil.getLoginInfo(XjApplication.getInstance()).getEmobId();
        } else
            emobid = PreferencesUtil.getTourist(XjApplication.getInstance());

        PushEventModel pushEventModel = new Select().from(PushEventModel.class).where("messageId = ? and emobid = ?", jsonObject.optString("messageId"), emobid).executeSingle();
        if (pushEventModel == null) {
            pushEventModel = new PushEventModel();
            pushEventModel.messageId = jsonObject.optLong("messageId");
            pushEventModel.cmd_code = jsonObject.optInt(Config.EXPKey_CMD_CODE);
            pushEventModel.title = jsonObject.optString("title");
            pushEventModel.content = jsonObject.optString("content");
            pushEventModel.timestemp = jsonObject.optInt("timestamp");
            pushEventModel.stauts = stauts;
            pushEventModel.emobid = emobid;
            pushEventModel.save();
            callBack4Service(context,pushEventModel.messageId, emobid);
            return true;
        } else {
            callBack4Service(context,pushEventModel.messageId, emobid);
            return false;
        }
    }

    interface PushCallBackService {
//        @PUT("/api/v1/communities/{communityId}/pushmessage/{emobId}")
//        void pushCallBack(@Header("signature") String signature, @Body PushCallBack pushCallBack,@Path("communityId") int communityId, @Path("emobId") String emobId, Callback<StatusBean> cb);
//        @PUT("/api/v1/communities/{communityId}/pushmessage/{emobId}")
//

        //        /api/v3/home/messages
        @PUT("/api/v3/home/messages")
        void pushCallBack(@Body PushCallBack pushCallBack, Callback<CommonRespBean<String>> cb);
    }


    public static void callBack4Service(Context context, final long messageId, final String emobid) {
        if (XjApplication.getInstance().callBackIds.contains(messageId)) return;
        else {
            XjApplication.getInstance().callBackIds.add(messageId);
            Log.i("onion", messageId + "emobid" + emobid);
            PushCallBack pushCallBack = new PushCallBack();
            pushCallBack.setMessageId(messageId);
            pushCallBack.setEmobId(emobid);
            PushCallBackService service = RetrofitFactory.getInstance().create(context, pushCallBack, PushCallBackService.class);
            Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
                @Override
                public void success(CommonRespBean<String> bean, retrofit.client.Response response) {
                    XjApplication.getInstance().callBackIds.remove(messageId);
                    if (bean != null) {
                        if ("yes".equals(bean.getStatus())) {
                            Log.i("onion", "推送回调成功");
                        } else {
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    XjApplication.getInstance().callBackIds.remove(messageId);
                    error.printStackTrace();
                }
            };

            service.pushCallBack(pushCallBack, callback);
        }
    }

    interface PushMessageService {
        ///api/v1/communities/{communityId}/admin/staffs?role={role}&status={status}&sort={sort}
        @PUT("/api/v1/communities/{communityId}/pushmessage/up/{emobId}")
        void isActive(@Header("signature") String signature, @Body PushMessageBean pushMessageBean, @Path("communityId") int communityId, @Path("emobId") String emobId, Callback<StatusBean> cb);
    }


    public static void isActive(final Context context) {
//        try{Thread.sleep(10*1000);}catch (Exception e){}
        Log.i("onion", "联网回调m");
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        PushMessageService service = restAdapter.create(PushMessageService.class);
        Callback<StatusBean> callback = new Callback<StatusBean>() {
            @Override
            public void success(StatusBean bean, retrofit.client.Response response) {
                if (bean != null) {
                    Log.i("onion", "联网回调m" + bean.status);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        String emobid = "";
        if (PreferencesUtil.getLogin(XjApplication.getInstance())) {
            emobid = PreferencesUtil.getLoginInfo(XjApplication.getInstance()).getEmobId();
        } else {
            emobid = PreferencesUtil.getTourist(XjApplication.getInstance());
        }
//       HashMap<String,Integer> option=new HashMap<>();
//        option.put("time",PreferencesUtil.isFirstUpdate(XjApplication.getInstance(),UserUtils.getVersion(XjApplication.getInstance())));
        PushMessageBean pushMessageBean = new PushMessageBean();
        pushMessageBean.setMethod("PUT");
        pushMessageBean.time = PreferencesUtil.isFirstUpdate(XjApplication.getInstance(), UserUtils.getVersion(XjApplication.getInstance()));
        service.isActive(StrUtils.string2md5(Config.BANGBANG_TAG + StrUtils.dataHeader(pushMessageBean)), pushMessageBean, PreferencesUtil.getCommityId(XjApplication.getInstance()), emobid, callback);
    }


}
