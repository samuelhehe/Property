package xj.property.utils.other;

import android.os.Handler;
import android.os.Message;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import xj.property.XjApplication;
import xj.property.beans.Floor;
import xj.property.beans.FloorResult;
import xj.property.beans.UnitRoomBean;

/**
 * Created by Administrator on 2015/5/8.
 */
public class HomeOwnerUtil {


    //获取楼信息

    interface HomeOwnerService {
        ///api/v1/communities/{communityId}/homeOwner
        @GET("/api/v1/communities/{communityId}/homeOwner")
        void getHomelist(@Path("communityId") int communityId, Callback<FloorResult> cb);
    }

    public static  void getHomeList(final  Handler handler){
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE2).build();
        HomeOwnerService homeOwnerService = restAdapter.create(HomeOwnerService.class);
        Callback<FloorResult> callback = new Callback<FloorResult>() {
            @Override
            public void success(FloorResult floorResult, Response response) {
                if("yes".equals(floorResult.getStatus())) {
                    Message message = Message.obtain();
                    message.obj = floorResult.getInfo();
                    message.what=Config.TASKCOMPLETE;
                    handler.sendMessage(message);
                }else {
                    handler.sendEmptyMessage(Config.TASKERROR);
                }
            }

            @Override
            public void failure(RetrofitError error) {
            }
        };
        homeOwnerService.getHomelist(PreferencesUtil.getCommityId(XjApplication.getInstance()), callback);
    }

    public static String[] getFloor(List<Floor> floors){
        String[] strings=new String[floors.size()];
        for(int i=0;i<floors.size();i++){
            strings[i]=floors.get(i).getUserFloor()+"";
        }
return  strings;
    }
    public static String[] getuserUnit(Floor floor){
        ;
        String[] units =new String[floor.getList().size()];
        for(int i=0;i<floor.getList().size();i++){
            units[i]=floor.getList().get(i).getUserUnit()+"";
        }
        if (units[0].equals("0")){
            units[0]="";
        }
    return  units;
    }
    public static String[] getuserRoom(UnitRoomBean unitRoomBean){

        String[] rooms =new String[unitRoomBean.getUserRooms().size()];
        for(int i=0;i<unitRoomBean.getUserRooms().size();i++){
            rooms[i]=unitRoomBean.getUserRooms().get(i);
        }
        return  rooms;
    }
}
