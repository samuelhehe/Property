package xj.property.statistic;

import android.text.TextUtils;

/**
 * Created by Administrator on 2016/1/12.
 */
public class EventServiceBean {


    public EventServiceBean(){}

    public EventServiceBean(String uuid, Integer clickTime, String clickTarget, String emobId, Integer communityId, String dataType, Integer exitTime) {
        this.uuid = uuid;
        this.clickTime = clickTime;
        this.clickTarget = clickTarget;
        this.emobId = emobId;
        this.communityId = communityId;
        if(TextUtils.equals(EventServiceUtils.EventType_sync,dataType)){
            this.dataType = dataType;
        }else  if(TextUtils.equals(EventServiceUtils.EventType_async,dataType)){
            this.dataType = dataType;
        }

        this.exitTime = exitTime;
    }

    @Override
    public String toString() {
        return "EventServiceBean{" +
                "uuid='" + uuid + '\'' +
                ", clickTime=" + clickTime +
                ", clickTarget='" + clickTarget + '\'' +
                ", emobId='" + emobId + '\'' +
                ", communityId=" + communityId +
                ", dataType='" + dataType + '\'' +
                ", exitTime=" + exitTime +
                '}';
    }

    private String uuid;
    private Integer clickTime;
    private String clickTarget;
    private String emobId;
    private Integer communityId;
    private String dataType;
    private Integer exitTime;


    /*


    {
        "uuid":"{点击数据唯一标识}",
        "clickTime":{点击时间，单位秒},
        "clickTarget":"{点击目标，如果点击目标为应用，则值为app，如果点击目标为模块，则值为模块id}",
        "emobId":"{点击人的环信ID}",
        "communityId":{点击人所在小区的ID},
        "dataType":"{点击数据类型，即时上传时值为sync，如果上传的是历史数据，则值为async}"
    },{
        "uuid":"{点击数据唯一标识}",
        "exitTime":{退出时间，单位秒},
        "dataType":"{点击数据类型，即时上传时值为sync，如果上传的是历史数据，则值为async}"
    }
     */


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getClickTime() {
        return clickTime;
    }

    public void setClickTime(int clickTime) {
        this.clickTime = clickTime;
    }

    public String getClickTarget() {
        return clickTarget;
    }

    public void setClickTarget(String clickTarget) {
        this.clickTarget = clickTarget;
    }

    public String getEmobId() {
        return emobId;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public int getExitTime() {
        return exitTime;
    }

    public void setExitTime(int exitTime) {
        this.exitTime = exitTime;
    }


}
