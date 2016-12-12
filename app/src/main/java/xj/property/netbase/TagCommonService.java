package xj.property.netbase;

import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.beans.MyTagsRespBean;
import xj.property.beans.SysDefaultTagsV3Bean;
import xj.property.beans.TagsA2BAddReqBean;
import xj.property.beans.TagsA2BDelReqBean;
import xj.property.beans.TagsA2BRespBean;
import xj.property.beans.TagsWhoTagMeRespV3Bean;

/**
 * Created by asia on 2016/3/4.
 * 公共接口提取类 标签接口
 */
public interface TagCommonService {

    /**
     * 获取用户的标签信息
     * @param cb
     */
    @GET("/api/v3/labels/details")
    void getUserTagsV3(@QueryMap Map<String, String> map, Callback<CommonRespBean<List<MyTagsRespBean.InfoEntity>>> cb);

    /**
     * 获取用户默认可添加标签
     * @param map
     *  option.put("communityId", userInfoDetailBean.getCommunityId()+"");
     *  option.put("time", "" + PreferencesUtil.getLastReqTagsTime(getmContext()));
     *
     * @param cb
     */
    @GET("/api/v3/labels/defaults")
    void getSysDefaultTagsV3(@QueryMap Map<String, String> map, Callback<CommonRespBean<SysDefaultTagsV3Bean>> cb);

    /**
     * 为其他用户贴标签
     * @param qt
     * @param cb
     */
    @POST("/api/v3/labels")
    void getTagsA2BAddV3(@Body TagsA2BAddReqBean qt, Callback<CommonRespBean<String>> cb);

    /**
     * 获取某用户某标签的历史详细信息
     * @param map
     * {labelContent} 标签
     * emobIdTo 被贴此标签的用户的环信ID
     * page 页码
     * limit 页面大小
     * @param cb
     */
    @GET("/api/v3/labels/{labelContent}/history")
    void getLabelsHistory(@Path("labelContent") String labelContent, @QueryMap Map<String, String> map, Callback<CommonRespBean<TagsWhoTagMeRespV3Bean>> cb);

    /**
     * 删除标签
     * @param qt    未完成
     * quaryToken.setMethod("DELETE");
       quaryToken.setEmobIdTo(emobiIdTo);
       quaryToken.setLabelContent(labelContent);
     * @param cb
     */
    @DELETE("/api/v3/labels")
    void getTagsA2BDelV3(@Body TagsA2BDelReqBean qt, Callback<CommonRespBean<String>> cb);

    /**
     * 根据上次访问的时间返回该时间之后最新的一条贴标签记录  未完成
     * @param map
     * @param cb
     */
    @DELETE("/api/v3/labels/last")
    void getTagsA2BLastV3(@QueryMap Map<String, String> map, Callback<CommonRespBean<String>> cb);

    /**
     * 获取用户A贴给用户B的标签
     * @param map
     * @param cb
     * emobIdFrom 贴标签的人
     * emobIdTo 被贴标签的人
     */
    @GET("/api/v3/labels")
    void getTagsA2BTags(@QueryMap Map<String, String> map, Callback<CommonRespBean<List<String>>> cb);

}
