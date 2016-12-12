package xj.property.statistic;

/**
 * Created by Administrator on 2015/4/1.
 */

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

@Table(name = "statistics_event_model")
public class StatisticsEventModel extends Model implements Serializable {

    //// 默认的构造函数必须添加!!!
    public StatisticsEventModel(){}


    /// 2015/12/4 去重id 标识 添加字段
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public int getEventTime() {
        return eventTime;
    }

    public void setEventTime(int eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column(name = "uuid")
    public String uuid;

    @Override
    public String toString() {
        return "StatisticsEventModel{" +
                "uuid='" + uuid + '\'' +
                ", emobId='" + emobId + '\'' +
                ", communityId=" + communityId +
                ", eventTime=" + eventTime +
                ", eventType='" + eventType + '\'' +
                ", event='" + event + '\'' +
                ", status=" + status +
                '}';
    }

    @Column(name = "emobId")
    public String emobId;

    @Column(name = "communityId")
    public int communityId;

    @Column(name = "eventTime")
    public int eventTime;  //// eventTime  include click , exit

    @Column(name = "eventType")  //// exit /click /
    public String eventType;

    @Column(name = "event") //// click  --> serviceid / exit --->className
    public String event;

    @Column(name = "status")
    public int status;  //// 即时上传时值为sync，如果上传的是历史数据，则值为async

    public StatisticsEventModel(String uuid, String emobId, int communityId, int eventTime, String eventType, String event, int status) {
        this.uuid = uuid;
        this.emobId = emobId;
        this.communityId = communityId;
        this.eventTime = eventTime;
        this.eventType = eventType;
        this.event = event;
        this.status = status;
    }

}
