package xj.property.activity.doorpaste;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.adapter.DoorPasteIndexAdapter;
import xj.property.beans.CommunityCategoryDataRespBean;
import xj.property.beans.DoorPasteIndexBean;
import xj.property.beans.DoorPasteIndexRespBean;
import xj.property.beans.SysDefaultTagsBean;
import xj.property.beans.TagsA2BAddReqBean;
import xj.property.beans.TagsA2BAddRespBean;
import xj.property.beans.TagsA2BRespBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.provider.ShareProvider;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;


/**
 * 贴或者添加门贴之后的分享
 */
public class DoorPastedoShareDialog extends Dialog {

    private static final String TAG = "DoorPastedoShareDialog";

    private Activity mContext;
    private TextView tv_share_qq;
    private TextView tv_share_weixin;
    private ShareProvider initShareProvider;

    private SocializeListeners.SnsPostListener shareResultListener;
    private LinearLayout share_rootview;

    public DoorPastedoShareDialog(Activity context, SocializeListeners.SnsPostListener listener) {
        super(context, R.style.Dialog_Fullscreen_fordoorpaste);
        this.mContext = context;
        this.shareResultListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_doorpaste_doshare);
        initView();
        initData();
    }

    private void initData() {
        initShareProvider = ShareProvider.getInitShareProvider(mContext);
        getDoorPasteIndexDataList();
    }

    private void initView() {

        share_rootview = (LinearLayout) findViewById(R.id.share_rootview);
        share_rootview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tv_share_qq = (TextView) findViewById(R.id.tv_share_qq);
        tv_share_weixin = (TextView) findViewById(R.id.tv_share_weixin);
        tv_share_weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initShareProvider = ShareProvider.getInitShareProvider(mContext);
                initShareProvider.shareWeixinForDoorPaste(PreferencesUtil.getCommityId(getContext()), "抵制小区不文明行为从我做起", PreferencesUtil.getDoorPasteShareContent(getContext()), shareResultListener);
            }
        });
        tv_share_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initShareProvider = ShareProvider.getInitShareProvider(mContext);
                initShareProvider.shareQQForDoorPaste(PreferencesUtil.getCommityId(getContext()), "抵制小区不文明行为从我做起", PreferencesUtil.getDoorPasteShareContent(getContext()), shareResultListener);
            }
        });


    }

    interface FastShopDetailService {
        @GET("/api/v3/doors")
        void getRepairList(@QueryMap Map<String, String> map, Callback<CommonRespBean<DoorPasteIndexRespBean>> cb);
    }

    private void getDoorPasteIndexDataList() {
        HashMap<String, String> option = new HashMap<>();
        option.put("communityId", "" + PreferencesUtil.getCommityId(getContext()));
        option.put("page", "1");
        option.put("limit", "0");
        FastShopDetailService service = RetrofitFactory.getInstance().create(getContext(), option, FastShopDetailService.class);
        Callback<CommonRespBean<DoorPasteIndexRespBean>> callback = new Callback<CommonRespBean<DoorPasteIndexRespBean>>() {
            @Override
            public void success(CommonRespBean<DoorPasteIndexRespBean> bean, retrofit.client.Response response) {
                if (bean != null && TextUtils.equals("yes", bean.getStatus()) && bean.getData() != null && bean.getData().getData() != null) {

                    int doorCount = bean.getField("doorCount", Integer.class);
                    int stickCount = bean.getField("stickCount", Integer.class);
                    PreferencesUtil.saveDoorPasteShareContent(getContext(), "小区有" + doorCount + "个房被贴，共计被贴" + stickCount + "次");
                }
            }
            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        };
        service.getRepairList(option, callback);
    }

    @Override
    public void show() {
        super.show();
    }


    @Override
    public void cancel() {
        super.cancel();
    }

}
