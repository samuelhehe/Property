package xj.property.netbase;

import android.content.Context;
import android.text.TextUtils;

import com.activeandroid.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.QueryMap;
import xj.property.beans.MyTagsRespBean;
import xj.property.beans.SysDefaultTagsV3Bean;
import xj.property.beans.TagsA2BAddReqBean;
import xj.property.beans.TagsA2BDelReqBean;
import xj.property.beans.TagsA2BRespBean;
import xj.property.beans.TagsWhoTagMeRespV3Bean;
import xj.property.utils.ToastUtils;
import xj.property.utils.other.PreferencesUtil;

/**
 * 作者：asia on 2016/3/4 13:05
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class NetBaseTagUtils {

    public interface NetRespListener<T> {
        public void success(T commonRespBean, Response response);

        public void failure(RetrofitError error);
    }

    /**
     * 获取用户默认可添加标签
     * @param query
     * @param netRespListener
     */
    public static void getSysDefaultTags( final String fromemobId,final Context context ,HashMap<String, String> query,final NetRespListener netRespListener) {
        TagCommonService tagCommonService = RetrofitFactory.getInstance().create(context,query,TagCommonService.class);
        Callback<CommonRespBean<SysDefaultTagsV3Bean>> callback = new Callback<CommonRespBean<SysDefaultTagsV3Bean>>() {
            @Override
            public void success(CommonRespBean<SysDefaultTagsV3Bean> tagsbean, Response response) {
                if (tagsbean != null) {
                    List<String> systemDefaultTags;
                    if (TextUtils.equals("yes", tagsbean.getStatus())) {
                        systemDefaultTags = tagsbean.getData().getList();
                        if (systemDefaultTags == null || systemDefaultTags.isEmpty() || systemDefaultTags.size() < 1) {
                        } else {
                            PreferencesUtil.saveNewSysTags(context, fromemobId, systemDefaultTags);
                        }
                        PreferencesUtil.setLastReqTagsTime(context, 0);
                        android.util.Log.d("getSystemDefaultTags  ", "systemDefaulttags  " + systemDefaultTags);
                    }
                    android.util.Log.d("getSystemDefaultTags  ", "getSysDefaultTags  stauts  =" + (tagsbean!=null?tagsbean.getStatus():"no"));
                    netRespListener.success(tagsbean, response);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (netRespListener != null) {
                    Log.e("error"+error.toString());
                    netRespListener.failure(error);
                }
            }
        };
        tagCommonService.getSysDefaultTagsV3(query, callback);
    }

    /**
     * 获取用户的标签信息
     * @param emobid
     * @param netRespListener
     */
    public static void getUserTagsInfo(Context context,final String emobid,final NetRespListener netRespListener) {
        HashMap<String, String> query = new HashMap<>();
        query.put("emobIdTo", emobid + "");
        TagCommonService tagCommonService = RetrofitFactory.getInstance().create(context,query,TagCommonService.class);
        Callback<CommonRespBean<List<MyTagsRespBean.InfoEntity>>> callback = new Callback<CommonRespBean<List<MyTagsRespBean.InfoEntity>>>() {
            @Override
            public void success(CommonRespBean<List<MyTagsRespBean.InfoEntity>> commonPostResultBean, Response response) {
                netRespListener.success(commonPostResultBean, response);
                android.util.Log.d("getUserTagsInfo  ", "getUserTagsInfo == "+(commonPostResultBean!=null?commonPostResultBean.getStatus():"null"));
            }

            @Override
            public void failure(RetrofitError error) {
                if (netRespListener != null) {
                    Log.e("error"+error.toString());
                    netRespListener.failure(error);
                }
            }
        };
        tagCommonService.getUserTagsV3(query, callback);
    }


    /**
     * 为其他用户贴标签
     * @param netRespListener
     */
    public static void getTagsA2BAdd(final Context context,List<String> choicedLables, String fromemobId, String toEmobId, final NetRespListener netRespListener) {
        TagsA2BAddReqBean quaryToken = new TagsA2BAddReqBean();
        quaryToken.setLabelList(getcurrentA2BTagsList(choicedLables, fromemobId, toEmobId));

        TagCommonService tagCommonService = RetrofitFactory.getInstance().create(context,quaryToken,TagCommonService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> respBean, Response response) {
                if (respBean != null && TextUtils.equals("yes", respBean.getStatus())) {
                    ToastUtils.showToast(context, "添加标签成功");
                    PreferencesUtil.setRefreshSame(context, true);
                } else if (respBean != null && TextUtils.equals("no", respBean.getStatus())) {
                    ToastUtils.showToast(context, respBean.getMessage());

                } else {
                    ToastUtils.showToast(context, "添加标签失败");
                }
                netRespListener.success(respBean, response);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                if (netRespListener != null) {
                    Log.e("error"+error.toString());
                    netRespListener.failure(error);
                }
            }
        };

        tagCommonService.getTagsA2BAddV3(quaryToken, callback);
    }

    /**
     * 获取某用户某标签的历史详细信息
     * @param map
     * {labelContent} 标签
     * emobIdTo 被贴此标签的用户的环信ID
     * page 页码
     * limit 页面大小
     * @param netRespListener
     */
    public static void getLabelsHistory(Context context,String labelContent, @QueryMap HashMap<String, String> map, final NetRespListener netRespListener) {
        TagCommonService tagCommonService = RetrofitFactory.getInstance().create(context,map,TagCommonService.class);
        Callback<CommonRespBean<TagsWhoTagMeRespV3Bean>> callback = new Callback<CommonRespBean<TagsWhoTagMeRespV3Bean>>() {
            @Override
            public void success(CommonRespBean<TagsWhoTagMeRespV3Bean> commonPostResultBean, Response response) {
                netRespListener.success(commonPostResultBean, response);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                if (netRespListener != null) {
                    Log.e("error"+error.toString());
                    netRespListener.failure(error);
                }
            }
        };
        tagCommonService.getLabelsHistory(labelContent, map, callback);
    }

    /**
     * 删除标签
     * @param qt
     * quaryToken.setMethod("DELETE");
     * quaryToken.setEmobIdTo(emobiIdTo);
     * quaryToken.setLabelContent(labelContent);
     * @param netRespListener
     */
    public static void getTagsA2BDelV3(Context context,TagsA2BDelReqBean qt, final NetRespListener netRespListener) {
        TagCommonService tagCommonService = RetrofitFactory.getInstance().create(context,qt,TagCommonService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> commonPostResultBean, Response response) {
                netRespListener.success(commonPostResultBean, response);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                if (netRespListener != null) {
                    Log.e("error"+error.toString());
                    netRespListener.failure(error);
                }
            }
        };
        tagCommonService.getTagsA2BDelV3(qt, callback);
    }

    /**
     * 根据上次访问的时间返回该时间之后最新的一条贴标签记录
     * @param query
     * @param netRespListener
     */
    public static void getTagsA2BLastV3(Context context,HashMap<String, String> query,final NetRespListener netRespListener) {
        TagCommonService tagCommonService = RetrofitFactory.getInstance().create(context,query,TagCommonService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> commonPostResultBean, Response response) {
                netRespListener.success(commonPostResultBean, response);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                if (netRespListener != null) {
                    Log.e("error"+error.toString());
                    netRespListener.failure(error);
                }
            }
        };
        tagCommonService.getTagsA2BLastV3(query, callback);
    }

    /**
     * 获取用户A贴给用户B的标签
     * @param query
     * @param netRespListener
     */
    public static void getTagsA2BTags(Context context,HashMap<String, String> query,final NetRespListener netRespListener) {
        TagCommonService tagCommonService = RetrofitFactory.getInstance().create(context,query,TagCommonService.class);
        Callback<CommonRespBean<List<String>>> callback = new Callback<CommonRespBean<List<String>>>() {
            @Override
            public void success(CommonRespBean<List<String>> tagsbean, Response response) {
                if (tagsbean != null) {
                    if ("no".equals(tagsbean.getStatus())) {
                        android.util.Log.d("getTagsA2BTags ", "" + tagsbean.getMessage());
                    }
                    netRespListener.success(tagsbean, response);
                } else {
                    android.util.Log.d("getTagsA2BTags ", "tagsbean is null ");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                if (netRespListener != null) {
                    Log.e("error"+error.toString());
                    netRespListener.failure(error);
                }
            }
        };
        tagCommonService.getTagsA2BTags(query, callback);
    }

    //TODO 需要用到的方法
    /**
     * 获取封装后的A2B的添加标签列表
     *
     * @param choicedLables
     * @return
     */
    private static List<TagsA2BAddReqBean.LabelListEntity> getcurrentA2BTagsList(List<String> choicedLables, String fromemobId, String toEmobId) {
        List<TagsA2BAddReqBean.LabelListEntity> labelList = new ArrayList<>();
        for (String tag : choicedLables) {
            TagsA2BAddReqBean.LabelListEntity labelListEntity = new TagsA2BAddReqBean.LabelListEntity();
            labelListEntity.setEmobIdFrom(fromemobId);
            labelListEntity.setEmobIdTo(toEmobId);
            labelListEntity.setLabelContent(tag);
            labelList.add(labelListEntity);
        }
        return labelList;
    }


}
