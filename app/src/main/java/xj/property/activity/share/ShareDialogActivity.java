package xj.property.activity.share;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;

import xj.property.Constant;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.provider.ShareProvider;

/**
 * 作者：asia on 2015/12/30 11:01
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class ShareDialogActivity extends HXBaseActivity {

    private LinearLayout mLl_bg;
    private LinearLayout mLl_weixinquan;
    private ImageButton mIb_weixinquan;
    private LinearLayout mLl_weixin;
    private ImageButton mIb_wechat;
    private LinearLayout mLl_weibo;
    private ImageButton mIb_weibo;
    private LinearLayout mLl_qq;
    private ImageButton mIb_qq;
    private TextView mShare_text;

    public static UMSocialService mController = UMServiceFactory.getUMSocialService(Constant.DESCRIPTOR);

    protected int activityCloseEnterAnimation;
    protected int activityCloseExitAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_board);
        initView();
        initAnima();
        initListenner();
    }

    private void initAnima() {
        TypedArray activityStyle = getTheme().obtainStyledAttributes(new int[] {android.R.attr.windowAnimationStyle});
        int windowAnimationStyleResId = activityStyle.getResourceId(0, 0);
        activityStyle.recycle();
        activityStyle = getTheme().obtainStyledAttributes(windowAnimationStyleResId, new int[] {android.R.attr.activityCloseEnterAnimation, android.R.attr.activityCloseExitAnimation});
        activityCloseEnterAnimation = activityStyle.getResourceId(0, 0);
        activityCloseExitAnimation = activityStyle.getResourceId(1, 0);
        activityStyle.recycle();
    }

    private void initListenner() {
        mLl_weixinquan.setOnClickListener(this);
        mLl_weixin.setOnClickListener(this);
        mLl_weibo.setOnClickListener(this);
        mLl_qq .setOnClickListener(this);
        mShare_text.setOnClickListener(this);
        mLl_bg.setOnClickListener(this);
    }

    private void initView() {
        mLl_bg = (LinearLayout) findViewById(R.id.ll_bg);
        mLl_weixinquan = (LinearLayout) findViewById(R.id.ll_weixinquan);
        mIb_weixinquan = (ImageButton) findViewById(R.id.ib_weixinquan);
        mLl_weixin = (LinearLayout) findViewById(R.id.ll_weixin);
        mIb_wechat = (ImageButton) findViewById(R.id.ib_wechat);
        mLl_weibo = (LinearLayout) findViewById(R.id.ll_weibo);
        mIb_weibo = (ImageButton) findViewById(R.id.ib_weibo);
        mLl_qq = (LinearLayout) findViewById(R.id.ll_qq);
        mIb_qq = (ImageButton) findViewById(R.id.ib_qq);
        mShare_text = (TextView) findViewById(R.id.share_text);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_weixin:
                finish();
                ShareProvider.getShareProvider(ShareDialogActivity.this).shareWeixin();
                break;
            case R.id.ll_weixinquan:
                finish();
                ShareProvider.getShareProvider(ShareDialogActivity.this).shareWeixinCircle();
                break;
            case R.id.ll_weibo:
                shareWeibo();
                break;
            case R.id.ll_qq:
                finish();
                ShareProvider.getShareProvider(ShareDialogActivity.this).shareQQ();
                break;
            case R.id.share_text:
                finish();
                break;
            case R.id.ll_bg:
                finish();
                break;
            default:
                break;
        }
    }

    private void shareWeibo(){
        String message = ShareProvider.getShareProvider(ShareDialogActivity.this).getMessage();
        int code = ShareProvider.getShareProvider(ShareDialogActivity.this).getCode();
        String url = ShareProvider.getShareProvider(ShareDialogActivity.this).getUrl();
        String imageUrl=ShareProvider.getShareProvider(ShareDialogActivity.this).getImageUrl();
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        mController.setShareContent(message + url + "&type=" + ShareProvider.TYPE_SINA + "&code=" + code+" @邻居帮帮");
        UMImage shareImage;
        if(imageUrl!=null&&!"".equals(imageUrl)){
            shareImage= new UMImage(ShareDialogActivity.this, imageUrl);
        }else{
            shareImage= new UMImage(ShareDialogActivity.this, R.drawable.bangbang_logonew);
        }
        mController.setShareMedia(shareImage);
        performShare(SHARE_MEDIA.SINA);
    }

    /**
     * 调用分享的方法
     * @param platform
     */
    private void performShare(SHARE_MEDIA platform) {
        mController.postShare(ShareDialogActivity.this, platform, new SocializeListeners.SnsPostListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
        if(ssoHandler != null){
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);
    }
}
