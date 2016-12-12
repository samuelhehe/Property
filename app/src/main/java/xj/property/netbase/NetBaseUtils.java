package xj.property.netbase;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Path;
import xj.property.beans.AddGroupMessageReqBean;
import xj.property.beans.BlackListBean;
import xj.property.beans.BlackUserInfo;
import xj.property.beans.CircleNewRecord;
import xj.property.beans.LifeCircleSuperZanBean;
import xj.property.beans.UpDateApp;
import xj.property.beans.UserBonusBean;
import xj.property.beans.UserGroupBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbasebean.BlackListReqBean;
import xj.property.netbasebean.BounsCoinInfoBean;
import xj.property.netbasebean.CommunityInfoRespBean;
import xj.property.netbasebean.GeneralZanReqBean;
import xj.property.netbasebean.ModifyUserAddressReqBean;
import xj.property.netbasebean.SendSMSReqBean;
import xj.property.netbasebean.SimpleUserInfoBean;
import xj.property.utils.other.PreferencesUtil;

/**
 * Created by Administrator on 2016/2/24.
 */
public class NetBaseUtils {

    public interface NetRespListener<T> {
        public void successYes(T commonRespBean, Response response);

        public void successNo(T commonRespBean, Response response);

        public void failure(RetrofitError error);
    }

    /**
     * 获取一个新的token， 从Server端
     */
    public static void extractNewToken(Context context,final NetRespListener netRespListener) {
        ApiCommonService tokenReqService = RetrofitFactory.getInstance().create(context,ApiCommonService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> commonPostResultBean, Response response) {
                if (netRespListener != null) {
                    if (TextUtils.equals("yes", commonPostResultBean.getStatus())) {
                        netRespListener.successYes(commonPostResultBean, response);
                    } else {
                        netRespListener.successNo(commonPostResultBean, response);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (netRespListener != null) {
                    netRespListener.failure(error);
                }
            }
        };
        tokenReqService.extractToken(callback);
    }

    /**
     * 拉取用户信息
     *
     * @param communityId
     * @param emobid
     * @param netRespListener
     */
    public static void extractUserInfo(Context context, int communityId, String emobid, final NetRespListener netRespListener) {
        ApiCommonService service = RetrofitFactory.getInstance().create(context,ApiCommonService.class);
        Callback<CommonRespBean<UserInfoDetailBean>> callback = new Callback<CommonRespBean<UserInfoDetailBean>>() {
            @Override
            public void success(CommonRespBean<UserInfoDetailBean> bean, Response response) {
                if (netRespListener != null) {
                    if (bean.getData()!=null&&TextUtils.equals("yes", bean.getStatus())) {
                        netRespListener.successYes(bean, response);
                    } else {
                        netRespListener.successNo(bean, response);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (netRespListener != null) {
                    netRespListener.failure(error);
                }
                error.printStackTrace();
            }
        };
        service.extractUserInfo(communityId, emobid, callback);
    }

    /**
     * 拉取小区信息
     *
     * @param communityId
     * @param netRespListener
     */
    public static void extractCommunityInfo(Context context, int communityId, final NetRespListener netRespListener) {
        ApiCommonService service = RetrofitFactory.getInstance().create(context,ApiCommonService.class);
        Callback<CommonRespBean<CommunityInfoRespBean>> callback = new Callback<CommonRespBean<CommunityInfoRespBean>>() {
            @Override
            public void success(CommonRespBean<CommunityInfoRespBean> bean, Response response) {
                if (netRespListener != null) {
                    if (bean.getData()!=null&&TextUtils.equals("yes", bean.getStatus())) {
                        netRespListener.successYes(bean, response);
                    } else {
                        netRespListener.successNo(bean, response);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (netRespListener != null) {
                    netRespListener.failure(error);
                }
                error.printStackTrace();
            }
        };
        service.extractCommunityInfo(communityId, callback);
    }

    /**
     * 拉取用户的简单个人信息
     *
     * @param communityId
     * @param netRespListener
     */
    public static void extractSimpleUserInfo(Context context, int communityId, String emobid,final NetRespListener netRespListener) {
        ApiCommonService service = RetrofitFactory.getInstance().create(context,ApiCommonService.class);
        Callback<CommonRespBean<SimpleUserInfoBean>> callback = new Callback<CommonRespBean<SimpleUserInfoBean>>() {
            @Override
            public void success(CommonRespBean<SimpleUserInfoBean> bean, Response response) {
                if (netRespListener != null) {
                    if (bean.getData()!=null&&TextUtils.equals("yes", bean.getStatus())) {
                        netRespListener.successYes(bean, response);
                    } else {
                        netRespListener.successNo(bean, response);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (netRespListener != null) {
                    netRespListener.failure(error);
                }
                error.printStackTrace();
            }
        };
        service.extractSimpleUserInfo(communityId, emobid, callback);
    }

    /**
     * 拉取用户信息2
     *
     * @param communityId
     * @param emobid
     * @param netRespListener
     */
    public static void extractUserInfo2(Context context, int communityId, String emobid, final NetRespListener netRespListener) {
        ApiCommonService service = RetrofitFactory.getInstance().create(context,ApiCommonService.class);
        Callback<CommonRespBean<UserGroupBean>> callback = new Callback<CommonRespBean<UserGroupBean>>() {
            @Override
            public void success(CommonRespBean<UserGroupBean> bean, Response response) {
                if (netRespListener != null) {
                    if (bean.getData()!=null&&TextUtils.equals("yes", bean.getStatus())) {
                        netRespListener.successYes(bean, response);
                    } else {
                        netRespListener.successNo(bean, response);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (netRespListener != null) {
                    netRespListener.failure(error);
                }
                error.printStackTrace();
            }
        };
        service.extractUserInfo2(communityId, emobid, callback);
    }


    /**
     * 拉取帮帮币信息
     *
     * @param communityId
     * @param emobid
     * @param netRespListener
     */
    public static void extractBounsCoinInfo(Context context,int communityId, String emobid, final NetRespListener netRespListener) {
        ApiCommonService service = RetrofitFactory.getInstance().create(context,ApiCommonService.class);
        Callback<CommonRespBean<BounsCoinInfoBean>> callback = new Callback<CommonRespBean<BounsCoinInfoBean>>() {
            @Override
            public void success(CommonRespBean<BounsCoinInfoBean> bean, Response response) {
                if (netRespListener != null) {
                    if (TextUtils.equals("yes", bean.getStatus())) {
                        netRespListener.successYes(bean, response);
                    } else {
                        netRespListener.successNo(bean, response);
                    }
                }
            }
            @Override
            public void failure(RetrofitError error) {
                if (netRespListener != null) {
                    netRespListener.failure(error);
                }
                error.printStackTrace();
            }
        };
        service.extractBounsCoinInfo(communityId, emobid, callback);
    }

    /**
     *  拉取帮帮券信息
     *
     * @param context
     * @param communityId
     * @param emobid
     * @param type    {帮帮劵类型：2->通用劵，7->帮帮券}  如果拉取全部帮帮券，则传-1
     * @param cityId
     * @param netRespListener
     */
    public static void extractBounsQuanInfo(Context context,int communityId, String emobid, int type, int cityId , final NetRespListener netRespListener) {
        HashMap<String, String> queryMap = new HashMap<>();
        if(type!=-1){
            queryMap.put("type", "" + type);
        }
        queryMap.put("cityId", "" + cityId);
        ApiCommonService service = RetrofitFactory.getInstance().create(context,queryMap,ApiCommonService.class);
        Callback<CommonRespBean<List<UserBonusBean>>> callback = new Callback<CommonRespBean<List<UserBonusBean>>>() {
            @Override
            public void success(CommonRespBean<List<UserBonusBean>> bean, Response response) {
                if (netRespListener != null) {
                    if (TextUtils.equals("yes", bean.getStatus())&&bean.getData()!=null) {
                        netRespListener.successYes(bean, response);
                    } else {
                        netRespListener.successNo(bean, response);
                    }
                }
            }
            @Override
            public void failure(RetrofitError error) {
                if (netRespListener != null) {
                    netRespListener.failure(error);
                }
                error.printStackTrace();
            }
        };

//        @Path("communityId") int communityId, @Path("emobId") String emobId, @QueryMap Map<String, String> map, Callback<UserBonusBean> cb
        service.extractBounsQuanInfo(communityId, emobid, queryMap, callback);
    }

    /**
     * 拉取最新的应用更新信息
     *
     * @param communityId
     * @param emobid
     * @param netRespListener
     */
    public static void getAppUpdateInfoV3(Context context,int communityId, String emobid, final NetRespListener netRespListener) {
        ApiCommonService service = RetrofitFactory.getInstance().create(context,ApiCommonService.class);
        Callback<CommonRespBean<UpDateApp.Info>> callback = new Callback<CommonRespBean<UpDateApp.Info>>() {
            @Override
            public void success(CommonRespBean<UpDateApp.Info> bean, Response response) {
                if (netRespListener != null) {
                    if (TextUtils.equals("yes", bean.getStatus())) {
                        netRespListener.successYes(bean, response);
                    } else {
                        netRespListener.successNo(bean, response);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (netRespListener != null) {
                    netRespListener.failure(error);
                }
                error.printStackTrace();
            }
        };
        service.getAppUpdateInfoV3(communityId, emobid, callback);
    }



    /**
     * 添加一条群申请/拒绝消息
     *
     * @param netRespListener
     */
    public static void addAGroupMessage(Context context,AddGroupMessageReqBean addGroupMessageReqBean , final NetRespListener netRespListener) {
        ApiCommonService service = RetrofitFactory.getInstance().create(context,addGroupMessageReqBean,ApiCommonService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> bean, Response response) {
                if (netRespListener != null) {
                    if (TextUtils.equals("yes", bean.getStatus())) {
                        netRespListener.successYes(bean, response);
                    } else {
                        netRespListener.successNo(bean, response);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (netRespListener != null) {
                    netRespListener.failure(error);
                }
                error.printStackTrace();
            }
        };
        service.addAGroupMessage(addGroupMessageReqBean, callback);
    }



    /**
     * 将谁添加至黑名单
     *
     * @param netRespListener
     */
    public static void addUserToBlackList(Context context,BlackListReqBean addtoblreqbean , final NetRespListener netRespListener) {
        ApiCommonService service = RetrofitFactory.getInstance().create(context,addtoblreqbean,ApiCommonService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> bean, Response response) {
                if (netRespListener != null) {
                    if (TextUtils.equals("yes", bean.getStatus())) {
                        netRespListener.successYes(bean, response);
                    } else {
                        netRespListener.successNo(bean, response);
                    }
                }
            }
            @Override
            public void failure(RetrofitError error) {
                if (netRespListener != null) {
                    netRespListener.failure(error);
                }
                error.printStackTrace();
            }
        };
        service.addToBlackList(addtoblreqbean, callback);
    }

    /**
     * 将谁移出黑名单，
     *
     * @param netRespListener
     */
    public static void removeUserFromBlackList(Context context,BlackListReqBean addtoblreqbean , final NetRespListener netRespListener) {
        ApiCommonService service = RetrofitFactory.getInstance().create(context, addtoblreqbean,ApiCommonService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> bean, Response response) {
                if (netRespListener != null) {
                    if (TextUtils.equals("yes", bean.getStatus())) {
                        netRespListener.successYes(bean, response);
                    } else {
                        netRespListener.successNo(bean, response);
                    }
                }
            }
            @Override
            public void failure(RetrofitError error) {
                if (netRespListener != null) {
                    netRespListener.failure(error);
                }
                error.printStackTrace();
            }
        };
        service.removeFromBlackList(addtoblreqbean, callback);
    }


    /**
     * 根据不同黑名单类型,拉取黑名单列表
     *
     * v3 2016/03/04
     * @param querymap
     * @param netRespListener
     */
    public static  void getUserBlacklist(Context context,HashMap<String,String> querymap,final NetRespListener netRespListener ) {
        ApiCommonService service = RetrofitFactory.getInstance().create(context,querymap,ApiCommonService.class);
        Callback<CommonRespBean<List<BlackUserInfo>>>   callback = new Callback<CommonRespBean<List<BlackUserInfo>>>() {
            @Override
            public void success(CommonRespBean<List<BlackUserInfo>> bean, Response response) {
                if(netRespListener!=null){
                    if ("yes".equals(bean.getStatus())) {
                        netRespListener.successYes(bean, response);
                    }else{
                        netRespListener.successNo(bean, response);
                    }
                }
            }
            @Override
            public void failure(RetrofitError error) {
                if (netRespListener != null) {
                    netRespListener.failure(error);
                }
                error.printStackTrace();
            }
        };
        service.getUserBlacklist(querymap, callback);
    }



    /**
     * 超赞专用
     * 包含生活圈超赞和活动话题超赞
     *
     * @param netRespListener
     */
    public static void superZan(Context context,LifeCircleSuperZanBean addtoblreqbean , final NetRespListener netRespListener) {
        ApiCommonService service = RetrofitFactory.getInstance().create(context, addtoblreqbean,ApiCommonService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> bean, Response response) {
                if (netRespListener != null) {
                    if (TextUtils.equals("yes", bean.getStatus())) {
                        netRespListener.successYes(bean, response);
                    } else {
                        netRespListener.successNo(bean, response);
                    }
                }
            }
            @Override
            public void failure(RetrofitError error) {
                if (netRespListener != null) {
                    netRespListener.failure(error);
                }
                error.printStackTrace();
            }
        };
        service.superZan(addtoblreqbean, callback);
    }


    /**
     * 标识赞专用
     * 包含生活圈标识赞和活动话题标识赞
     *
     * @param netRespListener
     */
    public static void  generalZan(Context context,GeneralZanReqBean addtoblreqbean , final NetRespListener netRespListener) {
        ApiCommonService service = RetrofitFactory.getInstance().create(context, addtoblreqbean,ApiCommonService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> bean, Response response) {
                if (netRespListener != null) {
                    if (TextUtils.equals("yes", bean.getStatus())) {
                        netRespListener.successYes(bean, response);
                    } else {
                        netRespListener.successNo(bean, response);
                    }
                }
            }
            @Override
            public void failure(RetrofitError error) {
                if (netRespListener != null) {
                    netRespListener.failure(error);
                }
                error.printStackTrace();
            }
        };
        service.generalZan(addtoblreqbean, callback);
    }



    /**
     * 修改用户地址信息
     * @param netRespListener
     */
    public static void  modifyUserAddress(Context context,int communityId,  String emobid, @Body ModifyUserAddressReqBean bean, final NetRespListener netRespListener) {

        ApiCommonService service = RetrofitFactory.getInstance().create(context, bean,ApiCommonService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> bean, Response response) {
                if (netRespListener != null) {
                    if (TextUtils.equals("yes", bean.getStatus())) {
                        netRespListener.successYes(bean, response);
                    } else {
                        netRespListener.successNo(bean, response);
                    }
                }
            }
            @Override
            public void failure(RetrofitError error) {
                if (netRespListener != null) {
                    netRespListener.failure(error);
                }
                error.printStackTrace();
            }
        };
        service.modifyUserAddress(communityId,emobid,bean, callback);
    }



    /**
     *  发生活圈
     * @param netRespListener
     */
    public static void  newCircleRecord(Context context, CircleNewRecord circleNewRecord, final NetRespListener netRespListener) {
        ApiCommonService service = RetrofitFactory.getInstance().create(context, circleNewRecord,ApiCommonService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> bean, Response response) {
                if (netRespListener != null) {
                    if (TextUtils.equals("yes", bean.getStatus())) {
                        netRespListener.successYes(bean, response);
                    } else {
                        netRespListener.successNo(bean, response);
                    }
                }
            }
            @Override
            public void failure(RetrofitError error) {
                if (netRespListener != null) {
                    netRespListener.failure(error);
                }
                error.printStackTrace();
            }
        };
        service.newCircleRecord( circleNewRecord,   callback);
    }




    /**
     * 通过手机号发送短信邀请邻居加入邻居帮帮
     * v3 2016/03/21
     * @param netRespListener
     */
    public static void  sendSMSbyPhone(Context context,int communityId, String phone, SendSMSReqBean sendSMSReqBean,final NetRespListener netRespListener) {
        ApiCommonService service = RetrofitFactory.getInstance().create(context,sendSMSReqBean,ApiCommonService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> bean, Response response) {
                if (netRespListener != null) {
                    if (TextUtils.equals("yes", bean.getStatus())) {
                        netRespListener.successYes(bean, response);
                    } else {
                        netRespListener.successNo(bean, response);
                    }
                }
            }
            @Override
            public void failure(RetrofitError error) {
                if (netRespListener != null) {
                    netRespListener.failure(error);
                }
                error.printStackTrace();
            }
        };
        service.sendSmsByPhone(communityId, phone,sendSMSReqBean,callback);
    }





}
