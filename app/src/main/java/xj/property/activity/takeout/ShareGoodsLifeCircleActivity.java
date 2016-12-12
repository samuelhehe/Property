package xj.property.activity.takeout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Path;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.beans.PostShareLifeCircleBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;


/**
 *
 * 分享快店购物到生活圈
 *  v3 2016/03/04
 */
public class ShareGoodsLifeCircleActivity extends HXBaseActivity {

    private ImageView iv_back;
    private TextView tv_right_text;
    private EditText tv_share_content;
    private GridView gv_goods_img;
    private Myadapter myadapter ;
    private String emobidShop;
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
        tv_share_content = (EditText) findViewById(R.id.tv_share_content);
        gv_goods_img = (GridView) findViewById(R.id.gv_goods_img);
        iv_back.setOnClickListener(this);
        tv_right_text.setOnClickListener(this);
    }

    private void initData(){
        orderId = getIntent().getStringExtra("orderId");
        emobidShop = getIntent().getStringExtra("shopid");
        photoInfo = getIntent().getStringArrayExtra("photos");
        myadapter = new Myadapter(ShareGoodsLifeCircleActivity.this);
        gv_goods_img.setAdapter(myadapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                noFinish();
                break;
            case R.id.tv_right_text:
                newRecord();

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
//        @POST("/api/v1/communities/{communityId}/circles/{emobId}/{serial}/share")
//        void newCircleRecord(@Header("signature") String signature, @Body PostShareLifeCircleBean postShareLifeCircleBean,
//                             @Path("communityId") int communityId,@Path("emobId") String emobId ,@Path("serial") String serial,Callback<CommonPostResultBean> cb);

//        @POST("/api/v1/communities/{communityId}/circles/{emobId}/{serial}/share")
//api/v3/shopOrders/{快店订单号}/share
        /////api/v3/lifeCircles/share/{快店订单号}
        @POST("/api/v3/shopOrders/{serial}/share")
        void newCircleRecord(@Body PostShareLifeCircleBean postShareLifeCircleBean, @Path("serial") String serial, Callback<CommonRespBean<Integer>> cb);
    }

    private void newRecord() {
        mLdDialog.show();

        PostShareLifeCircleBean bean = new PostShareLifeCircleBean();
        bean.setEmobIdShop(emobidShop);
        bean.setCommunityId(PreferencesUtil.getCommityId(this));
        bean.setLifeContent(""+tv_share_content.getText().toString());
        bean.setEmobId(PreferencesUtil.getLoginInfo(this).getEmobId());
        bean.setPhotoes(getPhotoes(photoInfo));

        NewCircleRecordService newCircleRecordService = RetrofitFactory.getInstance().create(getmContext(),bean,NewCircleRecordService.class);
        Callback<CommonRespBean<Integer>> callback = new Callback<CommonRespBean<Integer>>() {
            @Override
            public void success(CommonRespBean<Integer> commonPostResultBean, Response response) {
                mLdDialog.dismiss();
                if("yes".equals(commonPostResultBean.getStatus())){
                    yesFinish();
                }else {
                    showToast("分享失败:"+commonPostResultBean.getMessage());
                }
            }
            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                showNetErrorToast();
            }
        };
        newCircleRecordService.newCircleRecord(bean , orderId, callback);
    }

    /**
     *
     * 获取所有需要分享的生活圈图片信息, 通过逗号连接
     * @param photoInfo
     * @return
     */
    private String getPhotoes(String[] photoInfo) {
        if(photoInfo!=null&&photoInfo.length>0){
         StringBuilder stringBuilder= new StringBuilder();
            for(String p : photoInfo){
                stringBuilder.append(p).append(",");
            }
            stringBuilder.substring(0,stringBuilder.length()-1);
            return  stringBuilder.toString();
        }else{
            return  null;
        }
    }


    private class Myadapter extends BaseAdapter{
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

            ImageLoader.getInstance().displayImage(photoInfo[position],holder.iv_goods_itme, UserUtils.options);

            return convertView;
        }

        class ViewHolder{
            private ImageView iv_goods_itme;
        }
    }
}
