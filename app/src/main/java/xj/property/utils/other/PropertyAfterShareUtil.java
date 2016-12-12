package xj.property.utils.other;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Path;
import xj.property.R;
import xj.property.activity.LifeCircle.FriendZoneIndexActivity;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.netbasebean.PropertyAfterShareReqBean;
import xj.property.utils.ToastUtils;

/**
 * 2016/03/02
 * 物业缴费完毕，分享部分
 */
public class PropertyAfterShareUtil {


    public interface ShareStatusListener {


        public void sharedSuccess();

        public void sharedFailure();

    }

    /**
     * 分享获得帮帮币的对话框
     *
     * @param context
     * @param orderId 内部订单交易号
     * @param lifecontent 分享内容
     * @param photoes 分享的图片信息
     * @param coinCount 分享获得的帮帮币数量
     * @param shareStatusListener 结果状态监听
     */
    public static void showPropertyPayAfterShareDialog(final Context context,
                                                       final String orderId,
                                                       final String lifecontent,
                                                       final String photoes,
                                                       final int coinCount,
                                                       final ShareStatusListener shareStatusListener) {

        final PropertyAfterShareReqBean reqBean  =new PropertyAfterShareReqBean();
        reqBean.setCommunityId(PreferencesUtil.getCommityId(context));
        reqBean.setEmobId(PreferencesUtil.getLoginInfo(context).getEmobId());
        reqBean.setLifeContent(lifecontent);
        reqBean.setPhotoes(photoes);

        final Dialog dialog = new Dialog(context, R.style.MyDialogStyle);
        dialog.setContentView(R.layout.dialog_share_lifecircle_for_propertyshare);
        if (coinCount <= 0) {
            TextView dialog_shared_title_tv = (TextView) dialog.findViewById(R.id.dialog_shared_title_tv);
            dialog_shared_title_tv.setText("缴费完成");
            TextView dialog_shared_subtitle_tv = (TextView) dialog.findViewById(R.id.dialog_shared_subtitle_tv);
            dialog_shared_subtitle_tv.setText("转发到生活圈");
            TextView dialog_shared_coincount_tv = (TextView) dialog.findViewById(R.id.dialog_shared_coincount_tv);
            dialog_shared_coincount_tv.setText("成功缴物业费");
        } else {
            TextView dialog_shared_coincount_tv = (TextView) dialog.findViewById(R.id.dialog_shared_coincount_tv);
            dialog_shared_coincount_tv.setText("将获得" + coinCount + "个帮帮币。");
        }
        //将获得1000个帮帮币。
        dialog.setCanceledOnTouchOutside(false);
        dialog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /// 分享
                doShare(context,reqBean,orderId,shareStatusListener);
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shareStatusListener != null) {
                    shareStatusListener.sharedFailure();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    interface GetPicService {
//        /api/v3/payments/{缴费订单内部交易号}/share
        @POST("/api/v3/payments/{orderId}/share")
        void doShare(@Path("orderId") String orderId, @Body PropertyAfterShareReqBean reqBean, Callback<CommonRespBean<String>> cb);
    }

    /**
     *
     * go分享
     *
     * @param context
     * @param reqBean
     * @param orderId
     * @param shareStatusListener
     */
    public static void doShare(final Context context, PropertyAfterShareReqBean reqBean, final String orderId, final ShareStatusListener shareStatusListener) {
//        mLdDialog.show();
        GetPicService service = RetrofitFactory.getInstance().create(context,reqBean,GetPicService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> commonRespBean, retrofit.client.Response response) {
                if (commonRespBean != null) {
                    if ("yes".equals(commonRespBean.getStatus())) {
                        Intent shareIntent = new Intent(context, FriendZoneIndexActivity.class);
                        context.startActivity(shareIntent);
                        if (shareStatusListener != null) {
                            shareStatusListener.sharedSuccess();
                        }
                    } else {
                        if (shareStatusListener != null) {
                            shareStatusListener.sharedFailure();
                        }
//                    ("数据请求异常");
//                        Toast.makeText(context, "分享失败：" + commonRespBean.getMessage(), Toast.LENGTH_SHORT).show();
                        ToastUtils.showToast(context,"分享失败："+commonRespBean.getMessage());
                    }
                } else {
                    if (shareStatusListener != null) {
                        shareStatusListener.sharedFailure();
                    }
                    ToastUtils.showNetErrorToast(context);
                }
//                mLdDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
//                mLdDialog.dismiss();
                ToastUtils.showNetErrorToast(context);
                if (shareStatusListener != null) {
                    shareStatusListener.sharedFailure();
                }
                error.printStackTrace();
            }
        };
        service.doShare( orderId, reqBean, callback);
    }


}
