package xj.property.utils.other;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.Path;
import xj.property.R;
import xj.property.activity.welfare.ShareWelfare2LifeCircle;
import xj.property.beans.WelfareSharePhotoBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.message.XJContactHelper;
import xj.property.utils.message.XJMessageHelper;

/**
 * Created by che on 2015/9/18.
 */
public class WelfareUtil {

    public static void showWelfareShareDialog(final Context context, final String welfareId) {
        final Dialog dialog = new Dialog(context, R.style.MyDialogStyle);
        dialog.setContentView(R.layout.dialog_share_lifecircle_for_welfare);
        dialog.setCanceledOnTouchOutside(false);
        dialog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWelfarePhotos(context, welfareId);
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    interface GetPicService {
//        @GET("/api/v1/communities/{communityId}/welfares/{serial}/share/imgs")
//        void getActList(@Path("communityId") int communityId, @Path("serial") String serial, Callback<WelfareSharePhotoBean> cb);
//        @GET("/api/v1/communities/{communityId}/welfares/{serial}/share/imgs")
       ///api/v3/welfares/{福利ID}/share
        @GET("/api/v3/welfares/{welfareId}/share")
        void getActList(@Path("welfareId") String welfareId, Callback<CommonRespBean<List<String>>> cb);
    }

    public static void getWelfarePhotos(final Context context, final String welfareId) {
        GetPicService service = RetrofitFactory.getInstance().create(context,GetPicService.class);
        Callback<CommonRespBean<List<String>>> callback = new Callback<CommonRespBean<List<String>>>() {
            @Override
            public void success(CommonRespBean<List<String>> bean, retrofit.client.Response response) {
                if (bean != null) {
                    if ("yes".equals(bean.getStatus())) {
                        Intent shareIntent = new Intent(context, ShareWelfare2LifeCircle.class);
                        shareIntent.putExtra("orderId", welfareId);

                        List<String> picList = bean.getData();


                        String[] arrs = new String[picList.size()];
                        String[] pics = picList.toArray(arrs);
                        shareIntent.putExtra("photos", pics);
                        context.startActivity(shareIntent);

                    } else {
//                    ("数据请求异常");
                        Toast.makeText(context, "" + bean.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "数据请求异常", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        };
        service.getActList( welfareId, callback);
    }


    //          WelfareUtil.create600Message(this, emobId, info.getTitle(), orderid, "" + info.getWelfareId(), info.getPoster());

    public static void create600Message(final Context context, final String from, final String title, final String code, final String welfareId, final String image) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final EMMessage message = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
                TextMessageBody txtBody = new TextMessageBody("抢到福利");
                message.addBody(txtBody);
                message.setFrom(from);
                message.setTo(PreferencesUtil.getLoginInfo(context).getEmobId());
                message.setAttribute("avatar", "drawable://" + R.drawable.fuli_avatar);
                message.setAttribute("nickname", "福利");
                message.setAttribute("CMD_CODE", 600);
                message.setAttribute("clickable", 1);
                message.setAttribute("isShowAvatar", 1);
                message.setAttribute("title", title);
                message.setAttribute("code", code);
                message.setAttribute(Config.EXPKey_SORT, "19");
                message.setAttribute("welfareId", welfareId);
                message.setAttribute("image", image);
                message.setUnread(true);  /// 2015/12/4  设置消息未读状态
//                        EMChatManager.getInstance().ssaveMessage(message,true);
                EMChatManager.getInstance().importMessage(message, true);  /// true 有提醒, false 没有提醒
                XJContactHelper.saveContact(message);
                XJMessageHelper.saveMessage2DB(message.getMsgId(), message.getStringAttribute("welfareId", ""), 600);
                int unreadMsgsCount = EMChatManager.getInstance().getUnreadMsgsCount();
                Log.d("unreadMsgCountTotal ", "unreadMsgsCount " + unreadMsgsCount);
                if(unreadMsgsCount>0){
                    PreferencesUtil.saveWelfareSuccessMsgCount(context,0);
                }else{
                    /// 默认抢购成功消息数
                    PreferencesUtil.saveWelfareSuccessMsgCount(context,1);
                }

            }
        }).start();
    }

}
