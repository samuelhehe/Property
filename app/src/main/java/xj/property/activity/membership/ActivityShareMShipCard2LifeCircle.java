package xj.property.activity.membership;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.LifeCircle.FriendZoneIndexActivity;
import xj.property.beans.MspPostShareResultBean;
import xj.property.beans.PostShareMspCardLifeCircleBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.other.PreferencesUtil;

/**
 * 分享会员卡到生活圈
 */
public class ActivityShareMShipCard2LifeCircle extends HXBaseActivity {

    private ImageView iv_back;
    private TextView tv_right_text;
    private EditText tv_share_content;
    private GridView gv_goods_img;
    private Myadapter myadapter;

    private String[] infoEntity;


    private String shopVipcardId;


    private float discountPrice;

    private String shopemobid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msp_share_goods_circle);
        initView();
        initData();
    }

    private void initView() {
        initTitle(null, "分享到生活圈", "   分享");

        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_right_text = (TextView) findViewById(R.id.tv_right_text);
        tv_right_text.setVisibility(View.VISIBLE);
        tv_share_content = (EditText) findViewById(R.id.tv_share_content);
        tv_share_content.setHint("这个会员折扣怎么样?");
        gv_goods_img = (GridView) findViewById(R.id.gv_goods_img);
        iv_back.setOnClickListener(this);
        tv_right_text.setOnClickListener(this);
    }


    private List<String> lifePhotos = new ArrayList<String>();

    private void initData() {

        shopVipcardId = getIntent().getStringExtra("shopVipcardId");

        shopemobid = getIntent().getStringExtra("shopemobid");


        infoEntity = getIntent().getStringArrayExtra("photos");

        discountPrice = getIntent().getFloatExtra("discountPrice", 0.01f);


        if (!TextUtils.isEmpty(infoEntity[1])) {
            lifePhotos.add(infoEntity[1]);
        }

        if (!TextUtils.isEmpty(infoEntity[0])) {

            lifePhotos.add(infoEntity[0]);
        }

        myadapter = new Myadapter(this);
        gv_goods_img.setAdapter(myadapter);
    }

    private String getLifePicStrs(List<String> lifePhotos) {
        StringBuilder sb = new StringBuilder();

        for (String s : lifePhotos) {
            sb.append(s).append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                noFinish();
                break;
            case R.id.tv_right_text:
                newRecord();
                break;
        }
    }


    private void yesFinish() {
        Intent it = new Intent();
        setResult(RESULT_OK, it);
        finish();
    }

    private void noFinish() {
        Intent it = new Intent();
        setResult(RESULT_CANCELED, it);
        finish();
    }


    interface NewCircleRecordService {
        @POST("/api/v1/communities/{communityId}/users/{emobId}/share/{beanId}/{dataId}")
        void newCircleRecord(@Header("signature") String signature, @Body PostShareMspCardLifeCircleBean postShareLifeCircleBean,
                             @Path("communityId") long communityId, @Path("emobId") String emobId, @Path("beanId") String beanId, @Path("dataId") String dataId, Callback<MspPostShareResultBean> cb);
//        @POST("/api/v1/communities/{communityId}/users/{emobId}/share/{beanId}/{dataId}")

        ///api/v3/nearbyVipcards/{会员卡ID}/share
        @POST("/api/v3/nearbyVipcards/{dataId}/share")
        void newCircleRecord(@Body PostShareMspCardLifeCircleBean postShareLifeCircleBean,
                             @Path("dataId") String dataId, Callback<CommonRespBean<Integer>> cb);
    }
    private void newRecord() {
        mLdDialog.show();
        PostShareMspCardLifeCircleBean bean = new PostShareMspCardLifeCircleBean();
        String input = tv_share_content.getText().toString().trim();
        bean.setLifeContent(input);
        bean.setDiscountPrice(discountPrice);
        bean.setCommunityId(PreferencesUtil.getCommityId(getmContext()));
        bean.setEmobId(PreferencesUtil.getLoginInfo(getmContext()).getEmobId());
        bean.setPhotoes(getLifePicStrs(lifePhotos));

        NewCircleRecordService newCircleRecordService = RetrofitFactory.getInstance().create(getmContext(),bean,NewCircleRecordService.class);
        Callback<CommonRespBean<Integer>> callback = new Callback<CommonRespBean<Integer>>() {
            @Override
            public void success(CommonRespBean<Integer> commonPostResultBean, Response response) {
                if ("yes".equals(commonPostResultBean.getStatus())) {

                    showToast("恭喜您获得" + commonPostResultBean.getData() + "个帮帮币!");

                    startActivity(new Intent(ActivityShareMShipCard2LifeCircle.this, FriendZoneIndexActivity.class));
                    finish();
                } else {
                    showToast("分享失败:"+commonPostResultBean.getMessage());
                }
                mLdDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                showNetErrorToast();
            }
        };
        newCircleRecordService.newCircleRecord(bean,shopVipcardId, callback);
    }


    private class Myadapter extends BaseAdapter {
        private Context context;

        private Myadapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return lifePhotos.size();
        }

        @Override
        public Object getItem(int position) {
            return lifePhotos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(context, R.layout.share_img_gridview_layout, null);
                holder.iv_goods_itme = (ImageView) convertView.findViewById(R.id.iv_goods_pic);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.iv_goods_itme.setScaleType(ImageView.ScaleType.FIT_XY);
            ImageLoader.getInstance().displayImage(lifePhotos.get(position), holder.iv_goods_itme, options);

            return convertView;
        }

        class ViewHolder {
            private ImageView iv_goods_itme;
        }
    }


    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnFail(ru.truba.touchgallery.R.drawable.default_img)
            .showImageForEmptyUri(ru.truba.touchgallery.R.drawable.default_img)
            .showImageOnLoading(ru.truba.touchgallery.R.drawable.default_img)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();
}
