package xj.property.activity.vote;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import xj.property.R;
import xj.property.beans.TagsA2BDelReqBean;
import xj.property.beans.TagsA2BDelRespBean;
import xj.property.beans.VoteGoReqBean;
import xj.property.beans.VoteGoRespBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;

/**
 * 确定投票给XX
 */
public class MyVoteConfirmDialog extends Dialog implements View.OnClickListener {

    private static final String TAG = "MyTagsManagerDialog";
    private  String optVoteName;
    private VoteGoReqBean votegoreqbean;
    private Context mContext;
    //// 获取A2B的标签
    private List<String> tagsA2Btags;

    /// 取消
    private Button btn_cancel;
    /// 确定
    private Button btn_ok;


    private onTagsA2BDelStatusListener onTagsA2BDelStatusListener;

    private onDoVoteStatusListener onDoVoteStatusListener;


    private String labelContent;
    private String emobiIdTo;

    private TextView mytags_del_confirm_tv;

    public MyVoteConfirmDialog(Context context, String labeldelContent, String emobiIdToStr, onTagsA2BDelStatusListener onTagsA2BDelStatusListener) {
        super(context, R.style.Theme_CustomDialog);
        this.mContext = context;
        this.labelContent = labeldelContent;
        this.emobiIdTo = emobiIdToStr;
        this.onTagsA2BDelStatusListener = onTagsA2BDelStatusListener;
    }


    public MyVoteConfirmDialog(Context context,String optVoteName,  VoteGoReqBean votegoreqbean, onDoVoteStatusListener onDoVoteStatusListener) {
        super(context, R.style.Theme_CustomDialog);
        this.mContext = context;
        this.optVoteName = optVoteName;
        this.votegoreqbean = votegoreqbean;
        this.onDoVoteStatusListener = onDoVoteStatusListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_vote_confirm_mgr);
        initView();
        initData();
    }

    private void initData() {

        if(mytags_del_confirm_tv!=null){
            if(!TextUtils.isEmpty(optVoteName)){
                mytags_del_confirm_tv.setText("本次投票"+optVoteName+"是否确定");
            }else{
                //2015/12/08
                mytags_del_confirm_tv.setText("本次投票空白是否确定");
            }
        }

    }

    public MyVoteConfirmDialog.onDoVoteStatusListener getOnDoVoteStatusListener() {
        return onDoVoteStatusListener;
    }

    public void setOnDoVoteStatusListener(MyVoteConfirmDialog.onDoVoteStatusListener onDoVoteStatusListener) {
        this.onDoVoteStatusListener = onDoVoteStatusListener;
    }

    interface MspCardListService {

        @PUT("/api/v1/communities/{communityId}/labels/user")
        void getTagsA2BDel(@Header("signature") String signature, @Body TagsA2BDelReqBean qt, @Path("communityId") int communityId, Callback<TagsA2BDelRespBean> cb);

//        @POST("/api/v1/communities/{communityId}/vote/doVote")
//        void getDoVoteResult(@Header("signature") String signature, @Body VoteGoReqBean qt, @Path("communityId") int communityId, Callback<VoteGoRespBean> cb);
//        @POST("/api/v1/communities/{communityId}/vote/doVote")

//        /api/v3/votes/vote
        @POST("/api/v3/votes/vote")
        void getDoVoteResult(@Body VoteGoReqBean qt, Callback<CommonRespBean<String>> cb);

    }

    interface onTagsA2BDelStatusListener {

        void onTagsA2BDelSuccess(String message);

        void onTagsA2BDelFail(String message);
    }


    public interface onDoVoteStatusListener {

        void onDoVoteSuccess(String message);

        void onDoVoteFail(String message);
    }

    /**
     *
     * 投票
     *
     * @param quaryToken
     */
    private void getDoVoteResult(VoteGoReqBean quaryToken) {
        MspCardListService service = RetrofitFactory.getInstance().create(getContext(), quaryToken, MspCardListService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> respBean, Response response) {
                btn_ok.setClickable(true);
                if (respBean != null && TextUtils.equals(respBean.getStatus(), "yes")) {

                    if (onDoVoteStatusListener != null) {
                        onDoVoteStatusListener.onDoVoteSuccess(respBean.getMessage());
                    }
                    Toast.makeText(getContext(), "投票成功", Toast.LENGTH_SHORT).show();
                    dismiss();
                } else if (respBean != null && TextUtils.equals(respBean.getStatus(), "no")) {
                    if (onDoVoteStatusListener != null) {
                        onDoVoteStatusListener.onDoVoteFail(respBean.getMessage());
                    }

                    Toast.makeText(getContext(), "投票失败:"+respBean.getMessage(), Toast.LENGTH_SHORT).show();

                    dismiss();
                } else {

                    Toast.makeText(getContext(), "投票出错", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                btn_ok.setClickable(true);
                Toast.makeText(getContext(), "网络错误", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        };
        service.getDoVoteResult( quaryToken, callback);
    }

    /**
     * 删除标签
     */
    private void getTagsA2BDel() {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        MspCardListService service = restAdapter.create(MspCardListService.class);
        Callback<TagsA2BDelRespBean> callback = new Callback<TagsA2BDelRespBean>() {
            @Override
            public void success(TagsA2BDelRespBean respBean, Response response) {


                if (respBean != null && TextUtils.equals(respBean.getStatus(), "yes")) {

                    if (onTagsA2BDelStatusListener != null) {
                        onTagsA2BDelStatusListener.onTagsA2BDelSuccess(respBean.getMessage());
                    }
                    Toast.makeText(getContext(), "标签删除成功", Toast.LENGTH_SHORT).show();
                    dismiss();
                } else if (respBean != null && TextUtils.equals(respBean.getStatus(), "no")) {
                    if (onTagsA2BDelStatusListener != null) {
                        onTagsA2BDelStatusListener.onTagsA2BDelFail(respBean.getMessage());
                    }
                    Toast.makeText(getContext(), "no", Toast.LENGTH_SHORT).show();
                    dismiss();
                } else {
                    Toast.makeText(getContext(), "删除标签错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();

                Toast.makeText(getContext(), "网络错误", Toast.LENGTH_SHORT).show();
            }
        };
        TagsA2BDelReqBean quaryToken = new TagsA2BDelReqBean();
        quaryToken.setEmobIdTo(emobiIdTo);
        quaryToken.setLabelContent(labelContent);
        service.getTagsA2BDel(StrUtils.string2md5(Config.BANGBANG_TAG + StrUtils.dataHeader(quaryToken)), quaryToken, PreferencesUtil.getCommityId(getContext()), callback);
    }


    private void initView() {

        mytags_del_confirm_tv = (TextView) findViewById(R.id.mytags_del_confirm_tv);
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
//                getTagsA2BDel();

                btn_ok.setClickable(false);
                getDoVoteResult(votegoreqbean);

                break;

        }
    }

    @Override
    public void cancel() {
        super.cancel();
    }

}
