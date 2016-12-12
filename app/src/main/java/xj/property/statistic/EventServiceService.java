package xj.property.statistic;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import xj.property.beans.StatusBean;
import xj.property.beans.VistorEvent;

/**
 * Created by Administrator on 2015/12/2.
 */
public interface EventServiceService {

    ///api/v1/events/
    @POST("/api/v1/communities/{communityId}/events/")
    void onEventService(@Header("signature") String signature, @Body VistorEvent vistorEvent, @Path("communityId") int communityId, Callback<StatusBean> callback);


    @POST("/service/clicks")
    void eventStatisticService(@Header("signature") String signature, @Body EventServiceReqBean eventServiceReqBean, Callback<StatisticRespBean> callback);

}
