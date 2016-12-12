package xj.property.provider;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import xj.property.Constant;
import xj.property.R;
import xj.property.activity.share.ShareDialogActivity;
import xj.property.utils.CommonUtils;
import xj.property.utils.ToastUtils;
import xj.property.utils.other.Config;

/**
 * 作者：asia on 2015/12/29 11:08
 * ********************************************
 * 公司：北京小间科技发展有限公司
 *
 *    设置新浪微博分享时候需要在  onActivityResult 方法中设置以下参数
 *    @Override
 *    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 *        super.onActivityResult(requestCode, resultCode, data);
 *        使用SSO授权必须添加如下代码
 *        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
 *        if(ssoHandler != null){
 *            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
 *        }
 *    }
 *    注意onActivityResult方法必须写在分享所在的Activity或fragment依赖的Activity中，不可以直接写在fragment中
 * ********************************************
 * 公用分享类
 */
public class  ShareProvider {

    public static final String TYPE_WEIXINCIRCLE = "2000000"; //微信朋友圈 a1001
    public static final String TYPE_WEIXIN = "2000001";//微信好友 b1002
    public static final String TYPE_QQ = "2000002";//qq好友 c1003
    public static final String TYPE_SINA = "2000003";//新浪 d1004

    public static final int CODE_LIFECRILE = 1;//生活圈
    public static final int CODE_VOTE = 2;     //投票
    public static final int CODE_NEIGHBOR = 3; //邻居帮
    public static final int CODE_RUNFOR = 4;   //竞选
    public static final int CODE_CHART = 5;    //排行榜

    private static int code;
    private static String url;
    private static String message;
    private static String title;
    private static String imageUrl = "";
    private Activity mContext;
    public static UMSocialService mController = UMServiceFactory.getUMSocialService(Constant.DESCRIPTOR);

    private static ShareProvider mShareProvider;

    /**
     * 只每只分享参数，需要配置分享内容信息
     * 调用这个方法   setShareContent(emobid,inviteType);
     * 注意,context 必须是activity
     *
     * @param context
     */
    private ShareProvider(Activity context) {
        mContext = context;
        configPlatforms();
    }

    /**
     * 获取初始化分享方法
     *
     * @param activity
     * @return
     */
    public static ShareProvider getInitShareProvider(Activity activity) {
        mShareProvider = new ShareProvider(activity);
        return mShareProvider;
    }

    /**
     * 获得取分享类
     *
     * @param activity
     * @return
     */
    public static ShareProvider getShareProvider(Activity activity) {
        if (mShareProvider == null) {
            mShareProvider = new ShareProvider(activity);
        }
        return mShareProvider;
    }

    /**
     * 传入activity
     */
    public void showShareActivity(String url, String message, String title, int type) {
        this.code = type;
        this.url = url;
        this.message = message;
        this.title = title;
        this.imageUrl = "";
        configPlatforms();
        Intent intent = new Intent(mContext, ShareDialogActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 传入activity
     */
    public void showShareActivity(String url, String message, String title, int type, String imageUrl) {
        this.code = type;
        this.url = url;
        this.message = message;
        this.title = title;
        this.imageUrl = imageUrl;
        configPlatforms();
        Intent intent = new Intent(mContext, ShareDialogActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 分享到微信
     */
    public void shareWeixin() {
        if (CommonUtils.isNetWorkConnected(mContext)) {
            setShareContent(url + "&type=" + TYPE_WEIXIN + "&code=" + code, message, title, imageUrl);
            performShare(SHARE_MEDIA.WEIXIN);
        } else {
            showNetErrorToast();
        }
    }

    /**
     * 分享到QQ
     */
    public void shareQQ() {
        if (CommonUtils.isNetWorkConnected(mContext)) {
            setShareContent(url + "&type=" + TYPE_QQ + "&code=" + code, message, title, imageUrl);
            performShare(SHARE_MEDIA.QQ);
        } else {
            showNetErrorToast();
        }
    }


    /**
     * api/v1/doors/sharePage?communityId={小区ID}

     * 分享到微信门贴
     */
    public void shareWeixinForDoorPaste(int communityId,String message,String title, SocializeListeners.SnsPostListener listener) {
        if (CommonUtils.isNetWorkConnected(mContext)) {
            setShareContentForDoorPaste(Config.NET_BASE + "/api/v1/doors/sharePage?communityId="+communityId, message, title);
            performShare(SHARE_MEDIA.WEIXIN,listener);
        } else {
            showNetErrorToast();
        }
    }

    /**
     * 分享到QQ门贴
     */
    public void shareQQForDoorPaste(int communityId,String message,String title, SocializeListeners.SnsPostListener listener) {
        if (CommonUtils.isNetWorkConnected(mContext)) {
            setShareContentForDoorPaste(Config.NET_BASE + "/api/v1/doors/sharePage?communityId="+communityId, message, title);
            performShare(SHARE_MEDIA.QQ,listener);
        } else {
            showNetErrorToast();
        }
    }

    /**
     * 分享微信朋友圈
     */
    public void shareWeixinCircle() {
        if (CommonUtils.isNetWorkConnected(mContext)) {
            setShareContent(url + "&type=" + TYPE_WEIXINCIRCLE + "&code=" + code, message, title, imageUrl);
            performShare(SHARE_MEDIA.WEIXIN_CIRCLE);
        } else {
            showNetErrorToast();
        }
    }


    protected void showNetErrorToast() {
        ToastUtils.showToast(mContext, "网络异常，请稍后重试！");
    }

    /**
     * 配置分享平台参数
     */
    private void configPlatforms() {
        addQQQZonePlatform();
        addWXPlatform();
        addQZonePlatform();
        addWeiBoPlatform();
    }

//    doorpaste_doshare_icon


    public void setShareContent(String url, String message, String title, String imageUrl) {

        if (!TextUtils.isEmpty(imageUrl)) {
            //// 分享URL图片
            setShareContentImageUrl(url, message, title, imageUrl);
        } else {
            /// 分享drawable 图片
            setShareContentRes(url, message, title, R.drawable.bangbang_logonew);
        }
    }

    /**
     * 分享门贴
     * @param url
     * @param message
     * @param title
     */
    public void setShareContentForDoorPaste(String url , String message, String title){
        setShareContentRes(url,message,title,R.drawable.doorpaste_doshare_icon);
    }

    /**
     * 设置分享内容
     */
    public void setShareContentRes(String url, String message, String title, int res) {
        mController.setShareContent(message);
        UMImage shareImage;
        shareImage = new UMImage(mContext, res);
        mController.setShareMedia(shareImage);
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(title);
        weixinContent.setTitle(message);
        weixinContent.setTargetUrl(url);
        weixinContent.setShareMedia(shareImage);//之前是 localImage
        mController.setShareMedia(weixinContent);

        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(message);
        circleMedia.setTitle(message);
        circleMedia.setShareImage(shareImage);
        circleMedia.setTargetUrl(url);
        mController.setShareMedia(circleMedia);

        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent(title);
        qqShareContent.setTitle(message);
        qqShareContent.setShareMedia(shareImage);
        qqShareContent.setTargetUrl(url);
        mController.setShareMedia(qqShareContent);
    }

    /**
     * 设置分享内容
     */
    public void setShareContentImageUrl(String url, String message, String title, String imageUrl) {
        mController.setShareContent(message);
        UMImage shareImage;
        shareImage = new UMImage(mContext, imageUrl);
        mController.setShareMedia(shareImage);
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(title);
        weixinContent.setTitle(message);
        weixinContent.setTargetUrl(url);
        weixinContent.setShareMedia(shareImage);//之前是 localImage
        mController.setShareMedia(weixinContent);

        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(message);
        circleMedia.setTitle(message);
        circleMedia.setShareImage(shareImage);
        circleMedia.setTargetUrl(url);
        mController.setShareMedia(circleMedia);

        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent(title);
        qqShareContent.setTitle(message);
        qqShareContent.setShareMedia(shareImage);
        qqShareContent.setTargetUrl(url);
        mController.setShareMedia(qqShareContent);
    }

    /**
     * 调用分享的方法
     *
     * @param platform
     */
    private void performShare(SHARE_MEDIA platform) {
        mController.postShare(mContext, platform, new SocializeListeners.SnsPostListener() {
            @Override
            public void onStart() {


            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {


            }
        });
    }

    /**
     * 调用分享的方法
     *
     * @param platform
     */
    private void performShare(SHARE_MEDIA platform ,SocializeListeners.SnsPostListener listener ) {
        mController.postShare(mContext, platform, listener);
    }

    /**
     * @return
     * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
     * image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
     * 要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
     * : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
     */
    private void addQQQZonePlatform() {
        // 添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity) mContext, Config.QQAPPID,
                Config.QQKEY);
        qqSsoHandler.setTargetUrl("http://m.mi.com");
        qqSsoHandler.addToSocialSDK();
    }

    private void addQZonePlatform() {
        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler((Activity) mContext, Config.QQAPPID,
                Config.QQKEY);
        qZoneSsoHandler.addToSocialSDK();
    }

    private void addWeiBoPlatform() {
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
    }

    /**
     * @return
     * @功能描述 : 添加微信平台分享
     */
    private void addWXPlatform() {
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(mContext, Config.APP_ID, Config.WEIXINAPPSCRET);
        wxHandler.addToSocialSDK();
        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(mContext, Config.APP_ID,
                Config.WEIXINAPPSCRET);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }

    public static String getMessage() {
        return message;
    }

    public static void setMessage(String message) {
        ShareProvider.message = message;
    }

    public static int getCode() {
        return code;
    }

    public static void setCode(int code) {
        ShareProvider.code = code;
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        ShareProvider.url = url;
    }

    public static String getTitle() {
        return title;
    }

    public static void setTitle(String title) {
        ShareProvider.title = title;
    }

    public static String getImageUrl() {
        return imageUrl;
    }

    public static void setImageUrl(String imageUrl) {
        ShareProvider.imageUrl = imageUrl;
    }

}
