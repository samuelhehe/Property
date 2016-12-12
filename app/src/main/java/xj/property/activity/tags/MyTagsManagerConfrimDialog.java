package xj.property.activity.tags;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.Header;
import retrofit.http.PUT;
import retrofit.http.Path;
import xj.property.R;
import xj.property.beans.TagsA2BDelReqBean;
import xj.property.beans.TagsA2BDelRespBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.TypedJsonString;
import xj.property.utils.other.PreferencesUtil;

public class MyTagsManagerConfrimDialog extends Dialog implements View.OnClickListener {

    private static final String TAG = "MyTagsManagerDialog";
    private Context mContext;
    //// 获取A2B的标签
    private List<String> tagsA2Btags;


    /// 取消
    private Button btn_cancel;
    /// 确定
    private Button btn_ok;


    private onTagsA2BDelStatusListener onTagsA2BDelStatusListener;
    private String labelContent;
    private String emobiIdTo;

    public MyTagsManagerConfrimDialog(Context context, String labeldelContent , String emobiIdToStr,  onTagsA2BDelStatusListener onTagsA2BDelStatusListener) {
        super(context, R.style.Theme_CustomDialog);
        this.mContext = context;
        this.labelContent = labeldelContent;
        this.emobiIdTo = emobiIdToStr;
        this.onTagsA2BDelStatusListener =onTagsA2BDelStatusListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_mytags_delcofrim_mgr);
        initView();
    }

    interface MspCardListService {


        @PUT("/api/v1/communities/{communityId}/labels/user")
        void getTagsA2BDel(@Header("signature") String signature, @Body TagsA2BDelReqBean qt, @Path("communityId") int communityId, Callback<TagsA2BDelRespBean> cb);

//        @DELETE("/api/v3/labels")
//        void getTagsA2BDelV3(@Body TypedJsonString body, Callback<CommonRespBean<String>> cb);

        @PUT("/api/v3/labels")
        void getTagsA2BDelV3(@Body TagsA2BDelReqBean body, Callback<CommonRespBean<String>> cb);

    }

    interface onTagsA2BDelStatusListener{

        void onTagsA2BDelSuccess(String message);

        void onTagsA2BDelFail(String message);
    }


    /**
     * 删除标签
     */
    private void getTagsA2BDel() {

        TagsA2BDelReqBean quaryToken = new TagsA2BDelReqBean();
        quaryToken.setEmobIdTo(emobiIdTo);
        quaryToken.setLabelContent(labelContent);

        MspCardListService service = RetrofitFactory.getInstance().create(getContext(),quaryToken,MspCardListService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> respBean, Response response) {
                if(respBean!=null&& TextUtils.equals(respBean.getStatus(),"yes")){
                    if(onTagsA2BDelStatusListener!=null){
                        onTagsA2BDelStatusListener.onTagsA2BDelSuccess(respBean.getMessage());
                    }
                    PreferencesUtil.setRefreshSame(getContext(), true);
                    Toast.makeText(getContext(),"标签删除成功", Toast.LENGTH_SHORT).show();
                    dismiss();
                }else if(respBean!=null&&TextUtils.equals(respBean.getStatus(),"no")){
                    if(onTagsA2BDelStatusListener!=null){
                        onTagsA2BDelStatusListener.onTagsA2BDelFail(respBean.getMessage());
                    }
                    dismiss();
                }else{
                    Toast.makeText(getContext(), "删除标签错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Toast.makeText(getContext(), "网络错误", Toast.LENGTH_SHORT).show();
            }
        };


//        HashMap map = new HashMap<String, String>();
//        map.put("emobIdTo", emobiIdTo+"");
//        map.put("labelContent", labelContent + "");
//        String str = "{\"emobIdTo\":\""+emobiIdTo+"\",\"labelContent\":\""+labelContent+"\"}";
//        TypedString  str = new TypedString("{\"emobIdTo\":\""+emobiIdTo+"\",\"labelContent\":\""+labelContent+"\"}");
//        TypedInput in = null ;
//        try {
//            in = new TypedByteArray("application/json", str.getBytes("UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        TypedJsonString tjs = new TypedJsonString(str);


        service.getTagsA2BDelV3(quaryToken, callback);
    }

    private void initView() {
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_cancel:
                cancel();
                break;

            case R.id.btn_ok:
                getTagsA2BDel();
                break;

        }
    }

    @Override
    public void cancel() {
        super.cancel();
    }

}
