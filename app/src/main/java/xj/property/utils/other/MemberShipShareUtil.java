package xj.property.utils.other;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import xj.property.activity.membership.ActivityShareMShipCard2LifeCircle;
import xj.property.activity.welfare.ShareWelfare2LifeCircle;
import xj.property.beans.MSPCardBean;
import xj.property.beans.MspShare2LifeCircleBean;
import xj.property.beans.WelfareSharePhotoBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.message.XJContactHelper;
import xj.property.utils.message.XJMessageHelper;

/**
 * Created by che on 2015/9/18.
 */
public class MemberShipShareUtil {


    public static void showWelfareShareDialog(final Context context, final String shopVipcardId , final float discountPrice , final String shopemobid){
        final Dialog dialog = new Dialog(context, R.style.MyDialogStyle);
        dialog.setContentView(R.layout.dialog_share_lifecircle_for_mspcard);
        dialog.setCanceledOnTouchOutside(false);
        dialog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWelfarePhotos(context, shopVipcardId, discountPrice , shopemobid);
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
//        @GET("/api/v1/shopVipcards/{shopVipcardId}/share")

//        @GET("/api/v1/communities/{communityId}/users/{emobId}/share/{beanId}/{dataId}")
//        void getActList(@Path("communityId") String communityId,@Path("emobId") String emobId, @Path("beanId") String beanId,@Path("dataId") String shopVipcardId,  Callback<MspShare2LifeCircleBean> cb);
//        @GET("/api/v1/communities/{communityId}/users/{emobId}/share/{beanId}/{dataId}")


//        /api/v3/nearbyVipcards/{mspcardId}/share

        @GET("/api/v3/nearbyVipcards/{dataId}/share")
        void getActList(@Path("dataId") String shopVipcardId, Callback<CommonRespBean<List<String>>> cb);

    }



    public static void getWelfarePhotos(final Context context, final String shopVipcardId, final float discountPrice , final String shopemobid){
//        mLdDialog.show();
        GetPicService service = RetrofitFactory.getInstance().create(context,GetPicService.class);
        Callback<CommonRespBean<List<String>>> callback = new Callback<CommonRespBean<List<String>>>() {
            @Override
            public void success(CommonRespBean<List<String>> bean, retrofit.client.Response response) {
                if(bean != null){
                    if("yes".equals(bean.getStatus())){

                        Intent shareIntent = new Intent(context,ActivityShareMShipCard2LifeCircle.class);

                        shareIntent.putExtra("shopVipcardId",shopVipcardId);

                        String [] pics = new String[bean.getData().size()];

                        String[] strings = bean.getData().toArray(pics);

                        shareIntent.putExtra("photos",strings);

                        shareIntent.putExtra("discountPrice",discountPrice);

                        shareIntent.putExtra("shopemobid",shopemobid);

                        context.startActivity(shareIntent);

                    }else {
//                    ("数据请求异常");
                        Toast.makeText(context,""+bean.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(context,"数据请求异常",Toast.LENGTH_SHORT).show();
                }

//                mLdDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
//                mLdDialog.dismiss();
                Toast.makeText(context,"网络异常",Toast.LENGTH_SHORT).show();
                error.printStackTrace();
//                showNetErrorToast();
            }
        };
        service.getActList(shopVipcardId, callback);
    }

    /**
     *
     *
     * @param context
     * @param from
     * @param title
     * @param code
     * @param welfareId
     * @param image
     */
    public static void create600Message(final Context context, final String from, final String title, final String code, final String welfareId, final String image){
                        new Thread(new Runnable() {
                    @Override
                    public void run() {
                            final EMMessage message = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
                            TextMessageBody txtBody = new TextMessageBody("抢到福利");
                            message.addBody(txtBody);
                            message.setFrom(from);
                            message.setTo(PreferencesUtil.getLoginInfo(context).getEmobId());
                            message.setAttribute("avatar", "drawable://"+R.drawable.fuli_avatar);
                            message.setAttribute("nickname", "福利");
                            message.setAttribute("CMD_CODE", 600);
                            message.setAttribute("clickable", 1);
                            message.setAttribute("isShowAvatar", 1);
                            message.setAttribute("title", title);
                            message.setAttribute("code", code);
                            message.setAttribute(Config.EXPKey_SORT, "19");
                            message.setAttribute("welfareId",welfareId);
                            message.setAttribute("image",image);
//                        EMChatManager.getInstance().saveMessage(message,true);
                            EMChatManager.getInstance().importMessage(message, true);
                            XJContactHelper.saveContact(message);
                            XJMessageHelper.saveMessage2DB(message.getMsgId(), message.getStringAttribute("welfareId", ""), 600);
                    }
                }).start();
    }

}
