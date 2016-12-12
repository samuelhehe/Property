package xj.property.netbasebean;

import xj.property.netbase.BaseBean;

/**
 * Created by Administrator on 2016/3/3.
 * <p/>
 * 添加黑名单和删除黑名单公用一个bean
 * <p/>
 * <p>
 * {
 * "emobIdFrom": "{拉黑方环信ID}",
 * "emobIdTo": "{被拉黑方环信ID}",
 * "type": "{黑名单类型：activity->聊天黑名单，circle->生活圈黑名单}"
 * }
 * <p/>
 * </p>
 */
public class BlackListReqBean extends BaseBean {
    /**
     * {
     * "communityId": {小区ID},
     * "emobIdFrom": "{拉黑方环信ID}",
     * "emobIdTo": "{被拉黑方环信ID}",
     * "type": "{黑名单类型：activity->聊天黑名单，circle->生活圈黑名单}"
     * }
     * <p/>
     * <p/>
     * {
     * "communityId": 2,
     * "emobIdFrom": "d463b16dfc014466a1e441dd685ba505",
     * "emobIdTo": "fcb6adf78bef4ee4940d2af8ee7905f9",
     * "type": "circle"
     * }
     */


    //"{黑名单类型：activity->聊天黑名单，circle->生活圈黑名单}"
    public static final String type_activity = "activity";

    public static final String type_circle = "circle";

    private Integer communityId;
    private String emobIdFrom;
    private String emobIdTo;
    private String type;

    public void setEmobIdFrom(String emobIdFrom) {
        this.emobIdFrom = emobIdFrom;
    }

    public void setEmobIdTo(String emobIdTo) {
        this.emobIdTo = emobIdTo;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmobIdFrom() {
        return emobIdFrom;
    }

    public String getEmobIdTo() {
        return emobIdTo;
    }

    public String getType() {
        return type;
    }

    public Integer getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }
}
