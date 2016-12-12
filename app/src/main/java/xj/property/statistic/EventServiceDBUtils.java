package xj.property.statistic;

import android.text.TextUtils;
import android.util.Log;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import java.util.List;

/**
 *
 */
public class EventServiceDBUtils {

    public static String TAG ="EventServiceDBUtils";

    public static void updateEventBefore(String type, int eventTime, EventStatus status){
        Log.d(TAG,"updateEventByUUID  type: "+type +  " status "+status );
        if(TextUtils.equals(type,EventServiceUtils.EventType_all)){
            new Update(StatisticsEventModel.class).set("status = ?",status.ordinal()).where("eventTime < ?",eventTime).execute();
        }else{
            new Update(StatisticsEventModel.class).set("status = ?",status.ordinal()).where("eventTime < ? and eventType =? ",eventTime, type).execute();
        }
    }

    public static void updateEventByUUID(String type, String uuid,EventStatus status){
        Log.d(TAG,"updateEventByUUID  type: "+type + " uuid: "+ uuid + " status "+status );
        if(TextUtils.equals(type,EventServiceUtils.EventType_all)){
            new Update(StatisticsEventModel.class).set("status = ?",status.ordinal()).where("uuid = ?",uuid).execute();
        }else{
            new Update(StatisticsEventModel.class).set("status = ?",status.ordinal()).where("uuid = ? and eventType =? ",uuid, type).execute();
        }
    }
    ///  即时上传时值为sync，如果上传的是历史数据，则值为async
    enum EventStatus{
        sync,
        async
    }


    /**
     * 保存当前统计事件
     *
     * @param statisticsEventModel
     */
    public static void saveCurrEvent(StatisticsEventModel statisticsEventModel) {
        if (statisticsEventModel != null) {
            statisticsEventModel.save();
        } else {
            throw new NullPointerException("statisticsEventModel is null ! ");
        }
    }

    /**
     * 保存点击事件
     *
     * @param uuid
     * @param serviceId
     */
    public static void saveClickEvent(String emobId,int communityId, int eventTime , String uuid, String serviceId) {
        if(!TextUtils.isEmpty(emobId)&&communityId>0){
            int status = 0;
            saveCurrEvent(uuid,emobId,communityId,eventTime,EventServiceUtils.EventType_click,serviceId,status);
        }else{
            Log.e(TAG,"saveClickEvent emobId is null or communityId <=0 ");
        }
    }

    /**
     * 保存退出事件
     *
     * @param uuid
     */
    public static void saveExitEvent(String emobId,int communityId, int eventTime , String uuid, String classname) {
        if(!TextUtils.isEmpty(emobId)&&communityId>0){
            int status = 0;
            saveCurrEvent(uuid,emobId,communityId,eventTime,EventServiceUtils.EventType_exit,classname,status);
        }else{
            Log.d(TAG,"saveExitEvent emobId is null or communityId <=0  ");
        }
    }

    /**
     * @Column(name = "uuid")
     * public String  uuid;
     * @Column(name = "emobId")
     * public String emobId;
     * @Column(name = "communityId")
     * public int communityId;
     * @Column(name = "eventTime")
     * public int eventTime;
     * @Column(name = "eventType")  //// exit /click /
     * public String eventType;
     * @Column(name = "event") //// click  --> serviceid
     * public String event ;
     * @Column(name ="status")
     * public int  status ;  //// 预留字段  2016/1/11 可以记录当前记录的状态
     */
    public static void saveCurrEvent(String uuid, String emobId, int communityId, int eventTime, String eventType, String event, int status) {
        Log.d(TAG,"saveCurrEvent  uuid: "+uuid +  " emobId "+emobId+  " communityId "+communityId
                +  " eventTime "+eventTime+  " eventType "+eventType+  " event "+event+  " status "+status );
        StatisticsEventModel eventModel = new StatisticsEventModel(uuid, emobId, communityId, eventTime, eventType, event, status);
        saveCurrEvent(eventModel);
    }


    /**
     * 查询某个时间段之间的所有event记录
     *
     * @param type
     * @param eventTime
     * @return
     */
    public static List<StatisticsEventModel> queryAllEventBeforeTime(String type, int eventTime) {

        Log.d(TAG,"queryAllEventBeforeTime  type: "+type + " eventTime: "+ eventTime  );
        if(TextUtils.equals(type,EventServiceUtils.EventType_all)){
            return new Select().from(StatisticsEventModel.class).where("eventTime < ?", eventTime).execute();
        }else{
            return new Select().from(StatisticsEventModel.class).where("eventTime < ? and eventType = ? ", eventTime, type).execute();
        }
    }

    /**
     * 删除某个时间段之前的所有event记录
     *
     * @param type
     * @param eventTime
     */
    public static void deleteAllEventBeforeTime(String type, int eventTime) {

        Log.d(TAG,"deleteAllEventBeforeTime  type: "+type + " eventTime: "+ eventTime  );
        if(TextUtils.equals(type,EventServiceUtils.EventType_all)){
            new Delete().from(StatisticsEventModel.class).where("eventTime < ? ", eventTime).execute();
        }else{
            new Delete().from(StatisticsEventModel.class).where("eventTime < ? and eventType =? ", eventTime, type).execute();
        }
    }

    /**
     * 删除某一条event记录根据具体操作类型.
     * @param type
     *
     */
    public static void deleteEventByUUID(String type, String uuid) {
        Log.d(TAG,"deleteEventByUUID  type: "+type + " uuid: "+ uuid  );
        if(TextUtils.equals(type,EventServiceUtils.EventType_all)){
            new Delete().from(StatisticsEventModel.class).where("uuid < ? ", uuid).execute();
        }else{
            new Delete().from(StatisticsEventModel.class).where("uuid < ? and eventType =? ", uuid, type).execute();
        }
    }


}
