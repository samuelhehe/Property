package xj.property.netbase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.beans.AddGroupMessageReqBean;
import xj.property.beans.BlackListBean;
import xj.property.beans.BlackUserInfo;
import xj.property.beans.CircleNewRecord;
import xj.property.beans.LifeCircleSuperZanBean;
import xj.property.beans.PostShareLifeCircleBean;
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

/**
 * Created by Administrator on 2016/2/25.
 * 公共接口提取类
 */
public interface ApiCommonService {

    /**
     * 获取上传图片的token
     *
     * @param cb
     */
    @GET("/api/v3/qiniu/token")
    void extractToken(Callback<CommonRespBean<String>> cb);

    /**
     * 拉取用户的个人信息
     *
     * @param communityId
     * @param emobid
     * @param cb
     */
    @GET("/api/v3/communities/{communityId}/users/{emobid}")
    void extractUserInfo(@Path("communityId") int communityId, @Path("emobid") String emobid, Callback<CommonRespBean<UserInfoDetailBean>> cb);



    /**
     * 拉取用户的简单个人信息
     *
     * @param communityId
     * @param emobid
     * @param cb
     */
    @GET("/api/v3/communities/{communityId}/users/{emobid}/simple")
    void extractSimpleUserInfo(@Path("communityId") int communityId, @Path("emobid") String emobid, Callback<CommonRespBean<SimpleUserInfoBean>> cb);


    /**
     * 拉取用户的个人信息
     *
     * @param communityId
     * @param cb
     */
    @GET("/api/v3/communities/{communityId}")
    void extractCommunityInfo(@Path("communityId") int communityId, Callback<CommonRespBean<CommunityInfoRespBean>> cb);



    /**
     * 拉取用户的个人信息2
     *
     * @param communityId
     * @param emobid
     * @param cb
     */
    @GET("/api/v3/communities/{communityId}/users/{emobid}")
    void extractUserInfo2(@Path("communityId") int communityId, @Path("emobid") String emobid, Callback<CommonRespBean<UserGroupBean>> cb);


    /**
     * 拉取用户的帮帮币信息
     *
     * @param communityId
     * @param emobid
     * @param cb
     */
    @GET("/api/v3/communities/{communityId}/users/{emobid}/bonuscoinInfo")
    void extractBounsCoinInfo(@Path("communityId") int communityId, @Path("emobid") String emobid, Callback<CommonRespBean<BounsCoinInfoBean>> cb);



    /**
     * 拉取用户的帮帮券信息
     *
     * @param communityId
     * @param cb
     */
    @GET("/api/v3/communities/{communityId}/users/{emobId}/bonus")
    void extractBounsQuanInfo(@Path("communityId") int communityId, @Path("emobId") String emobId, @QueryMap Map<String, String> map, Callback<CommonRespBean<List<UserBonusBean>>> cb);


    /**
     * 拉取应用的最新信息
     *
     * @param communityId
     * @param emobId
     * @param callback
     */
    @GET("/api/v3/communities/{communityId}/users/{emobId}/client")
    void getAppUpdateInfoV3(@Path("communityId") int communityId, @Path("emobId") String emobId, Callback<CommonRespBean<UpDateApp.Info>> callback);


    /**
     * 添加一条群组消息
     *
     * @param qt
     * @param cb
     */
    @POST("/api/v3/activities/messages")
    void addAGroupMessage(@Body AddGroupMessageReqBean qt, Callback<CommonRespBean<String>> cb);


    /**
     * 将谁添加至黑名单
      {
     "communityId": {小区ID},
     "emobIdFrom": "{拉黑方环信ID}",
     "emobIdTo": "{被拉黑方环信ID}",
     "type": "{黑名单类型：activity->聊天黑名单，circle->生活圈黑名单}"
     }
     * @param qt
     * @param cb
     */
    @POST("/api/v3/blacklists")
    void addToBlackList(@Body BlackListReqBean qt, Callback<CommonRespBean<String>> cb);

    /**
     * 将谁移除黑名单
     *{
     "emobIdFrom": "{拉黑方环信ID}",
     "emobIdTo": "{被拉黑方环信ID}",
     "type": "{黑名单类型：activity->聊天黑名单，circle->生活圈黑名单}"
     }
     * @param qt
     * @param cb
     */
    @PUT("/api/v3/blacklists")
    void removeFromBlackList(@Body BlackListReqBean qt, Callback<CommonRespBean<String>> cb);


    /**
     * 拉取黑名单,根据不同黑名单类型
     *
     * @param querymap
     * @param cb
     */
    @GET("/api/v3/blacklists")  //v3 2016/03/04
    void getUserBlacklist(@QueryMap HashMap<String, String> querymap, Callback<CommonRespBean<List<BlackUserInfo>>> cb);


    /**
     * 超赞专用
     * 包含生活圈超赞和活动话题超赞
     * @param bean
     * @param cb
     */
    @POST("/api/v3/lifePraises/superPraise")
    void superZan(@Body LifeCircleSuperZanBean bean, Callback<CommonRespBean<String>> cb);


    /**
     * 标识赞专用
     * 包含生活圈标识赞和活动话题标识赞
     * @param bean
     * @param cb
     */
    @POST("/api/v3/lifePraises/bzPraise")
    void generalZan(@Body GeneralZanReqBean bean, Callback<CommonRespBean<String>> cb);



    /**
     * 修改用户地址信息
     *
     * @param bean
     * @param cb
     */
    @PUT("/api/v3/communities/{communityId}/users/{emobId}/address")
    void modifyUserAddress(@Path("communityId") int communityId, @Path("emobId") String emobid, @Body ModifyUserAddressReqBean bean, Callback<CommonRespBean<String>> cb);


    /**
     * 发生活圈
     *
     * 发生活圈 type 不传，
     *
     * 帮主竞选拉票部分 使用type=23 ，
     *
     *{生活圈类型：2->快店分享，19->福利分享，20->会员卡分享，23->帮主竞选拉票}
     *
     * @param circleNewRecord
     * @param cb
     */
    @POST("/api/v3/lifeCircles")
    void newCircleRecord(@Body CircleNewRecord circleNewRecord, Callback<CommonRespBean<String>> cb);



    /**
     * 发送短信邀请邻居加入邻居帮帮
     *
     * @param cb
     */
    @POST("/api/v3/communities/{communityId}/users/{phone}/invite")
    void sendSmsByPhone(@Path("communityId") int communityId, @Path("phone") String phone, @Body SendSMSReqBean sendSMSReqBean, Callback<CommonRespBean<String>> cb);



}
