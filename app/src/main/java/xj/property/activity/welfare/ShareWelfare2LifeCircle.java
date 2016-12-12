package xj.property.activity.welfare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

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
import xj.property.beans.CommonPostResultBean;
import xj.property.beans.LifePhotosEntity;
import xj.property.beans.PostShareLifeCircleBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;

/**
 * Created by che on 2015/9/18.
 */
public class ShareWelfare2LifeCircle extends HXBaseActivity {

    private ImageView iv_back;
    private TextView tv_right_text;
    private EditText tv_share_content;
    private GridView gv_goods_img;
    private Myadapter myadapter ;
    private String [] photoInfo;
    private String orderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_goods_life_circle);
        initView();
        initData();
    }

    private void initView(){
        initTitle(null,"分享到生活圈","   分享");
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_right_text = (TextView) findViewById(R.id.tv_right_text);
        tv_right_text.setVisibility(View.VISIBLE);
        tv_share_content = (EditText) findViewById(R.id.tv_share_content);
        tv_share_content.setHint("这个福利怎么样？");
        gv_goods_img = (GridView) findViewById(R.id.gv_goods_img);
        iv_back.setOnClickListener(this);
        tv_right_text.setOnClickListener(this);
    }

    private void initData(){
        orderId = getIntent().getStringExtra("orderId");
        photoInfo = getIntent().getStringArrayExtra("photos");
        myadapter = new Myadapter(this);
        gv_goods_img.setAdapter(myadapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                noFinish();
                break;
            case R.id.tv_right_text:
                newRecord(orderId);
                break;
        }
    }

    private void yesFinish(){
        Intent it = new Intent();
        setResult(RESULT_OK, it);
        finish();
    }

    private void noFinish(){
        Intent it = new Intent();
        setResult(RESULT_CANCELED, it);
        finish();
    }

    interface NewCircleRecordService {
//        @POST("/api/v1/communities/{communityId}/welfares/{serial}/user/{emobId}/share")
//        void newCircleRecord(@Header("signature") String signature, @Body PostShareLifeCircleBean postShareLifeCircleBean,
//                             @Path("communityId") int communityId,@Path("emobId") String emobId ,@Path("serial") String serial, Callback<CommonPostResultBean> cb);

        ////api/v3/welfares/{福利ID}/share
        /**
         * 分享福利到生活圈
         * @param postShareLifeCircleBean
         * @param cb
         */
        @POST("/api/v3/welfares/{welfareId}/share")
        void newCircleRecordV3(@Path("welfareId") String welfareId, @Body PostShareLifeCircleBean postShareLifeCircleBean, Callback<CommonRespBean<String>> cb);

    }

    private void newRecord(String welfareId) {
        if(TextUtils.isEmpty(welfareId)){
            showDataErrorToast();
            return;
        }
        mLdDialog.show();
        PostShareLifeCircleBean reqbean = new PostShareLifeCircleBean();
        reqbean.setCommunityId(PreferencesUtil.getCommityId(this));
        reqbean.setLifeContent("" + tv_share_content.getText().toString());
        StringBuilder photoes = new StringBuilder();
        for(String s : photoInfo){
            photoes.append(s).append(",");
        }
        if(!TextUtils.isEmpty(photoes)){
            photoes.substring(0,photoes.length()-1);
        }
        reqbean.setPhotoes(photoes.toString());
        reqbean.setEmobId(PreferencesUtil.getLoginInfo(this).getEmobId());

        NewCircleRecordService service  = RetrofitFactory.getInstance().create(getmContext(),reqbean,NewCircleRecordService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> commonPostResultBean, Response response) {
                if("yes".equals(commonPostResultBean.getStatus())){
//                    yesFinish();
                    startActivity(new Intent(ShareWelfare2LifeCircle.this,FriendZoneIndexActivity.class));
                    finish();
                }else {
                    showToast("分享失败");
                }
                mLdDialog.dismiss();
            }
            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                showNetErrorToast();
            }
        };
        service.newCircleRecordV3(welfareId,reqbean, callback);
    }


    private class Myadapter extends BaseAdapter {
        private Context context;

        private Myadapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return photoInfo.length;
        }

        @Override
        public Object getItem(int position) {
            return photoInfo[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null){
                holder=new ViewHolder();
                convertView=View.inflate(context, R.layout.share_img_gridview_layout, null);
                holder.iv_goods_itme=(ImageView) convertView.findViewById(R.id.iv_goods_pic);
                convertView.setTag(holder);
            }else{
                holder=(ViewHolder) convertView.getTag();
            }
            holder.iv_goods_itme.setScaleType(ImageView.ScaleType.FIT_XY);
            ImageLoader.getInstance().displayImage(photoInfo[position],holder.iv_goods_itme, UserUtils.options);

            return convertView;
        }

        class ViewHolder{
            private ImageView iv_goods_itme;
        }
    }
}
