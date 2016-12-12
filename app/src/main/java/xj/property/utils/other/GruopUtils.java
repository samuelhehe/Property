package xj.property.utils.other;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.activity.HXBaseActivity.ChatActivity;
import xj.property.beans.AdminBean;
import xj.property.utils.message.XJContactHelper;

/**
 * Created by Administrator on 2015/4/9.
 */
public class GruopUtils {
    interface getAdminService {
        ///api/v1/communities/{communityId}/emobGroup/{emobGroupId}
        @GET("/api/v1/communities/{communityId}/emobGroup/{emobGroupId}")
        void getAdminInfo(@Path("communityId") int communityId, @Path("emobGroupId") HashMap<String, String> option, Callback<AdminBean> cb);
    }

    public static void askAdmin(final Context context,String status,final String content) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        getAdminService service = restAdapter.create(getAdminService.class);
        Callback<AdminBean> callback = new Callback<AdminBean>() {
            @Override
            public void success(AdminBean bean, retrofit.client.Response response) {
                Intent intentPush = new Intent();
                intentPush.setClass(context, ChatActivity.class);
                intentPush.putExtra("userId", bean.info.getEmobId());//tz
                intentPush.putExtra(Config.EXPKey_nickname, "物业客服");
                String path="drawable://" + context.getResources().getIdentifier("ic_launcher", "drawable",
                        "xj.property");
                intentPush.putExtra(Config.EXPKey_avatar,  path);
                if(content!=null){
                intentPush.putExtra(Config.ComplainContent,content);
                intentPush.putExtra(Config.EXPKey_CMD_CODE,403);
                    XJContactHelper.saveContact(bean.info.getEmobId(), "物业客服", path,"403");
                }else {
                    intentPush.putExtra(Config.EXPKey_CMD_CODE,401);
                    XJContactHelper.saveContact(bean.info.getEmobId(), "物业客服", path,"401");
                }
                intentPush.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
                context.startActivity(intentPush);

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(context, "系统出错", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        };
        HashMap<String,String> map=new HashMap<>();
        map.put("q",status);
        service.getAdminInfo(PreferencesUtil.getCommityId(context), map, callback);
    }
}
