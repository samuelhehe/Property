package xj.property.utils.other;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

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
import xj.property.activity.HXBaseActivity.ChatActivity;
import xj.property.activity.HXBaseActivity.MainActivity;
import xj.property.beans.AdminBean;
import xj.property.beans.AdminRecoderBean;
import xj.property.beans.ComplainRequest;
import xj.property.beans.ComplainRequestV3;
import xj.property.beans.UserFeedBackRespBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.ToastUtils;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.message.XJContactHelper;

/**
 * Created by Administrator on 2015/4/9
 */
public class AdminUtils {
    interface getAdminService {
        ///api/v1/communities/{communityId}/admin/staffs?role={role}&status={status}&sort={sort}
        @GET("/api/v1/communities/{communityId}/admin/staffs")
        void getAdminInfo(@Path("communityId") long communityId, @QueryMap HashMap<String, String> option, Callback<AdminBean> cb);
    }

    /**
     *  客服:
     *  bangbang(用户反馈),
     *  wuye(物业客服),
     *  shop(店家客服),
     *  投诉
     *  shoptousu(快店投诉) ,
     *  weixiutousu(维修投诉)
     *
     * 401:客户端物业客服
     402：店家端客服
     403：客户端投诉
     404：用户反馈
     *
     *
     */
    public static void askAdminCallBack(final Context context, final String servanttype, final String content) {

        getCallbackService service = RetrofitFactory.getInstance().create(context,getCallbackService.class);
        Callback<CommonRespBean<UserFeedBackRespBean>> callback = new Callback<CommonRespBean<UserFeedBackRespBean>>() {
            @Override
            public void success(CommonRespBean<UserFeedBackRespBean> bean, retrofit.client.Response response) {
                if (bean == null || bean.getData() == null) {
                    Toast.makeText(context, "获取信息失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!"yes".equals(bean.getStatus())) {
                    Toast.makeText(context, bean.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intentPush = new Intent();
                intentPush.setClass(context, ChatActivity.class);
                if (bean == null || bean.getData() == null) {
                    Toast.makeText(context, "获取物业客服信息失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                intentPush.putExtra("userId", bean.getData().getEmobId());//tz
                String path = "drawable://" + context.getResources().getIdentifier("service", "drawable", "xj.property");
                intentPush.putExtra(Config.EXPKey_avatar, path);

                if(TextUtils.equals(servanttype,Config.SERVANT_TYPE_WUYE)){
                    intentPush.putExtra(Config.EXPKey_nickname, "物业客服");
                }else if(TextUtils.equals(servanttype,Config.SERVANT_TYPE_WEIXIUTOUSU)){
                    intentPush.putExtra(Config.EXPKey_nickname, "维修投诉");
                }else if(TextUtils.equals(servanttype,Config.SERVANT_TYPE_SHOPTOUSU)){
                    intentPush.putExtra(Config.EXPKey_nickname, "帮帮投诉");
                }
                intentPush.putExtra(Config.ComplainContent, content);
                intentPush.putExtra(Config.SERVANT_TYPE, servanttype);
                XJContactHelper.saveContact(bean.getData().getEmobId(), bean.getData().getNickname(), path, servanttype);
                boolean flag = true;
                //判断当前时间
                MainActivity.startTime = bean.getData().getStartTime();
                MainActivity.endTime = bean.getData().getEndTime();
                try {
                    flag = StrUtils.isUnServiceTime(MainActivity.startTime, MainActivity.endTime);
                } catch (Exception e) {
                    Log.e("onion", e.toString());
                }
                intentPush.putExtra(Config.InServiceTime, flag);
                intentPush.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
//                doRecoder(bean.get().getEmobId(), PreferencesUtil.getLoginInfo(context).getEmobId(), PreferencesUtil.getCommityId(context));

                context.startActivity(intentPush);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(context, context.getString(R.string.netError), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        };
//        HashMap<String, String> map = new HashMap<>();
//        map.put("q", servanttype);
        service.getCallbackInfo(PreferencesUtil.getCommityId(context), servanttype, callback);
    }


    public static void askAdmin(final Context context, String status, final String content, final String sort) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        getAdminService service = restAdapter.create(getAdminService.class);
        Callback<AdminBean> callback = new Callback<AdminBean>() {
            @Override
            public void success(AdminBean bean, retrofit.client.Response response) {
                if (!"yes".equals(bean.getStatus())) {
                    Toast.makeText(context, bean.getMessage(), Toast.LENGTH_LONG).show();
                }
                Intent intentPush = new Intent();
                intentPush.setClass(context, ChatActivity.class);
                if (bean == null || bean.getInfo() == null) {
                    Toast.makeText(context, "获取客服信息失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                intentPush.putExtra("userId", bean.info.getEmobId());//tz
                if (sort == null) {
                    intentPush.putExtra(Config.EXPKey_nickname, "物业客服");
                } else {
                    intentPush.putExtra(Config.EXPKey_nickname, (sort.equals("2") ? "帮帮投诉" : "物业投诉"));
                }
                String path = "drawable://" + context.getResources().getIdentifier("service", "drawable", "xj.property");
                intentPush.putExtra(Config.EXPKey_avatar, path);
                boolean flag = true;
                //判断当前时间
                MainActivity.startTime = bean.getInfo().getStartTime();
                MainActivity.endTime = bean.getInfo().getEndTime();
                try {
                    flag = StrUtils.isUnServiceTime(MainActivity.startTime, MainActivity.endTime);
                } catch (Exception e) {
                    Log.e("onion", e.toString());
                }
                intentPush.putExtra(Config.InServiceTime, flag);
                if (content != null) {

                    /// 快店投诉/// 403: 客户端投诉
                    intentPush.putExtra(Config.ComplainContent, content);
                    intentPush.putExtra(Config.EXPKey_CMD_CODE, 403);
                    XJContactHelper.saveContact(bean.info.getEmobId(), bean.getInfo().getNickname(), path, "403");
                } else {

                    /// 物业客服 /// 401: 客户端物业客服
                    intentPush.putExtra(Config.EXPKey_CMD_CODE, 401);
                    XJContactHelper.saveContact(bean.info.getEmobId(), bean.getInfo().getNickname(), path, "401");
                }
                intentPush.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);

//                doRecoder(bean.info.getEmobId(), PreferencesUtil.getLoginInfo(context).getEmobId(), PreferencesUtil.getCommityId(context));
                context.startActivity(intentPush);

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(context, context.getString(R.string.netError), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        };
        HashMap<String, String> map = new HashMap<>();
        map.put("role", "user");
        map.put("status", status);
        if (sort != null){
            map.put("sort", sort);
        }
        service.getAdminInfo(PreferencesUtil.getCommityId(context), map, callback);
    }

    interface ComplainService {
        ///api/v1/communities/{communityId}/
//        @POST("/api/v1/communities/{communityId}/users/{emobIdUser}/complaints")
//        void toCompalin(@Header("signature") String signature, @Path("communityId") long communityId, @Path("emobIdUser") String emobIdUser, @Body ComplainRequest bean, Callback<Object> cb);

        // /api/v3/complaints
        @POST("/api/v3/complaints")
        void toCompalinV3(@Body ComplainRequestV3 bean, Callback<CommonRespBean<Integer>> cb);
    }


    /**
     *
     * 快店投诉
     *
     * @param context
     * @param userEmobid
     * @param shopEmobid
     * @param content
     * @param orderId
     */
    public static void doComplainForShop(final Context context,  String userEmobid, String shopEmobid, final String content,  final String orderId){
        doComplain(context,  userEmobid, shopEmobid, content, Config.SERVANT_TYPE_SHOPTOUSU, orderId);
    }

    /**
     * 维修投诉
     * @param context
     * @param userEmobid
     * @param shopEmobid
     * @param content
     */
    public static void doComplainForRepair(final Context context,  String userEmobid, String shopEmobid, final String content){
        doComplain(context,  userEmobid, shopEmobid, content, Config.SERVANT_TYPE_WEIXIUTOUSU, null);
    }



    public static void doComplain(final Context context, String userEmobid, String shopEmobid, final String content, final String servantType , final String orderId) {
        ComplainRequestV3 complainRequestV3 = new ComplainRequestV3();
        complainRequestV3.setCommunityId(PreferencesUtil.getCommityId(context));
        complainRequestV3.setEmobIdFrom(userEmobid);
        complainRequestV3.setEmobIdTo(shopEmobid);
        complainRequestV3.setDetail(content);
        complainRequestV3.setType(servantType);
        if(!TextUtils.isEmpty(orderId)){
            complainRequestV3.setOrderId(orderId);
        }
        ComplainService service = RetrofitFactory.getInstance().create(context,complainRequestV3,ComplainService.class);
        Callback<CommonRespBean<Integer>> callback = new Callback<CommonRespBean<Integer>>() {
            @Override
            public void success(CommonRespBean<Integer> bean, retrofit.client.Response response) {
                try {
                    if ("yes".equals(bean.getStatus())) {
                        ToastUtils.showToast(context, "投诉已受理");

//                        askAdmin(context, "complaints", content, servantType);
                        askAdminCallBack(context,servantType,content);
                    } else {
                        ToastUtils.showToast(context,"投诉未成功:"+bean.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        };
        service.toCompalinV3(complainRequestV3,callback);
    }

//    interface AdminRecoderService {
//        ///api/v1/communities/{communityId}/
//        @POST("/api/v1/communities/{communityId}/messageList")
//        void forReocder(@Header("signature") String signature, @Path("communityId") long communityId, @Body AdminRecoderBean bean, Callback<AdminBean> cb);
//    }
//
//    public static void doRecoder(String adminId, String userId, long communityId) {
//        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
//        AdminRecoderService service = restAdapter.create(AdminRecoderService.class);
//        Callback<AdminBean> callback = new Callback<AdminBean>() {
//            @Override
//            public void success(AdminBean bean, retrofit.client.Response response) {
//
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                error.printStackTrace();
//            }
//        };
//        AdminRecoderBean bean = new AdminRecoderBean();
//        bean.emobIdFrom = userId;
//        bean.emobIdTo = adminId;
//        service.forReocder(StrUtils.string2md5(Config.BANGBANG_TAG + StrUtils.dataHeader(bean)), communityId, bean, callback);
//    }


    interface getCallbackService {
        ///api/v1/communities/{communityId}/admin/staffs?role={role}&status={status}&sort={sort}

        //        @GET("/api/v1/communities/{communityId}/userFeedBack")  2016/1/11
//        bangbang(用户反馈),wuye(物业客服),shop(店家客服),tousu(投诉客服)
//        @GET("/api/v1/communities/summary/{communityId}/servant")
//        void getCallbackInfo(@Path("communityId") long communityId, @QueryMap HashMap<String, String> option, Callback<UserFeedBackRespBean> cb);
//        @GET("/api/v1/communities/summary/{communityId}/servant")

//        /api/v3/communities/{小区ID}/servants/{客服类型}

        @GET("/api/v3/communities/{communityId}/servants/{servanttype}")
        void getCallbackInfo(@Path("communityId") int communityId, @Path("servanttype") String servanttype, Callback<CommonRespBean<UserFeedBackRespBean>> cb);
    }

    /**
        用户反馈
     */
    public static void askCallback(final Context context, String servanttype) {
        getCallbackService service = RetrofitFactory.getInstance().create(context,getCallbackService.class);
        Callback<CommonRespBean<UserFeedBackRespBean>> callback = new Callback<CommonRespBean<UserFeedBackRespBean>>() {
            @Override
            public void success(CommonRespBean<UserFeedBackRespBean> bean, retrofit.client.Response response) {
                if (bean == null || bean.getData() == null) {
                    Toast.makeText(context, "获取信息失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!"yes".equals(bean.getStatus())) {
                    Toast.makeText(context, bean.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intentPush = new Intent();
                intentPush.setClass(context, ChatActivity.class);
                intentPush.putExtra("userId", bean.getData().getEmobId());//tz
                intentPush.putExtra(Config.EXPKey_nickname, bean.getData().getNickname());

                String path = bean.getData().getAvatar();
                intentPush.putExtra(Config.EXPKey_avatar, path);
                //判断当前时间
                intentPush.putExtra(Config.InServiceTime, false);
                //// 帮帮
                intentPush.putExtra(Config.SERVANT_TYPE,Config.SERVANT_TYPE_BANGBANG);
                XJContactHelper.saveContact(bean.getData().getEmobId(), bean.getData().getNickname(), path, Config.SERVANT_TYPE_BANGBANG);
                intentPush.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);

//                doRecoder(bean.getData().getEmobId(), PreferencesUtil.getLoginInfo(context).getEmobId(), PreferencesUtil.getCommityId(context));
                context.startActivity(intentPush);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(context, context.getString(R.string.netError), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        };

        service.getCallbackInfo(PreferencesUtil.getCommityId(context), servanttype, callback);
    }
}
