package xj.property.ums.common;


import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import xj.property.beans.BlackListBean;
import xj.property.beans.ErrorBean;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;

/**
 * Created by Administrator on 2015/5/12.
 */
public class ErrorUpLoad {

    //上传错误
    interface UpLoadErrorService {
      //  {"appkey":"123","time":"2015-05-04 18:31:10","activity":".activity.HXBaseActivity.MainActivity","deviceId":"Meizu MX4","osVersion":"4.4.2","appVersion":"1.0.0","stacktrace":"stacktrace msg"}

        //http://115.28.73.37:9090/api/v1/communities/1/crash/
        //void upLoadError(@Body ErrorBean errorBean,@Path("communityId") int communityId , Callback<Object> callback);
        @POST("/api/v1/communities/{communityId}/crash/")
        void upLoadError(@Body ErrorBean errorBean, @Path("communityId") int communityId, Callback<Object> callback);
    }

    public static void onError(int communityId,ErrorBean errorBean) {
      new ErrorSave(errorBean).start();
        Log.i("onion","error: "+errorBean);

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_AGENT).build();
        UpLoadErrorService service = restAdapter.create(UpLoadErrorService.class);
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        Callback<Object> callback = new Callback<Object>() {
            @Override
            public void success(Object Object, Response response) {
                Log.i("onion", "错误上传成功" + Object);
            }
            @Override
            public void failure(RetrofitError error) {
                Log.i("onion", "失败" + error.toString());
            }

        };

        ErrorBean request=new ErrorBean();
        request.setActivity(errorBean.getActivity());
        request.setAppkey(errorBean.getAppkey()+"");
        request.setAppVersion(errorBean.getAppVersion());
        request.setOsVersion(errorBean.getOsVersion());
        request.setDeviceId(errorBean.getDeviceId());
        request.setTime(errorBean.getTime());
        /*request.setActivity(".activity.HXBaseActivity.MainActivity");
        request.setAppkey("123");
        request.setAppVersion("1.0.0");
        request.setOsVersion("4.4.2");
        request.setDeviceId("Meizu MX4");
        request.setTime("2015-06-03 17:56:38");
        request.setStacktrace("hahahhah");*/
        Log.i("onion","error: "+request);
        service.upLoadError(request,communityId, callback);
        Log.i("onion","方法22222222");
    }








}
