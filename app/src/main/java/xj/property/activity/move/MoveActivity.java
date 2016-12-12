package xj.property.activity.move;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.activeandroid.util.Log;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.Path;
import xj.property.R;
import xj.property.activity.ABaseActivity;
import xj.property.beans.HasMove;
import xj.property.beans.UserInfoDetailBean;
import xj.property.event.MoveEvent;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.other.PreferencesUtil;

/**
 * 作者：asia on 2016/1/11 12:59
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：搬家
 */
public class MoveActivity extends ABaseActivity implements View.OnClickListener {

    private String TAG = "MoveActivity";
    private final int MOVERESULT=1;

    private TextView mTv_move;
    private ImageView mIv_back;
    private TextView mTv_title;
    private TextView mTv_message;
    private String cname;

    private boolean isMove=false;


    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.activity_move);
        cname = PreferencesUtil.getCommityName(getApplicationContext());
    }

    @Override
    protected void initView() {
        mTv_move = (TextView) findViewById(R.id.tv_move);
        mIv_back = (ImageView) findViewById(R.id.iv_back);
        mTv_title = (TextView) findViewById(R.id.tv_title);
        mTv_message = (TextView) findViewById(R.id.tv_message);
    }

    @Override
    protected void initDate() {
        mTv_title.setText("搬家");
        mTv_title.setTextColor(0xff2FCC71);
        mTv_message.setText("30天内只能搬家一次，只有您个人的基本资料将会搬到新小区，您当前获得的人品值、帮帮 券、帮帮币以及您发布的所有信息会保留在"+cname+"小区。");
        UserInfoDetailBean info = PreferencesUtil.getLoginInfo(getApplicationContext());
        if(info!=null){
            HasMove();
        }
    }

    @Override
    protected void initListenner() {
        mTv_move.setOnClickListener(this);
        mIv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_move:
                Intent intent = new Intent(MoveActivity.this,MoveDialogActivity.class);
                if(isMove){
                    intent.putExtra("isMoved", true);
                }else{
                    intent.putExtra("isMoved", false);
                }
                startActivityForResult(intent,MOVERESULT);
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    interface HasMoveService {
        @GET("/api/v1/communities/{communityId}/users/{emobId}/hasMove")
        void HasMove(@Path("communityId") long communityId, @Path("emobId") String emobId, Callback<HasMove> cb);

        @GET("/api/v3/communities/{communityId}/users/{emobId}/hasMove")
        void HasMoveV3(@Path("communityId") long communityId, @Path("emobId") String emobId, Callback<HasMove> cb);
    }

    private void HasMove() {
        HasMoveService service = RetrofitFactory.getInstance().create(MoveActivity.this,HasMoveService.class);
        Callback<HasMove> callback = new Callback<HasMove>() {
            @Override
            public void success(HasMove bean, retrofit.client.Response response) {
                if (bean != null && "yes".equals(bean.getStatus())) {
                    String info = bean.getData();
                    if (info != null&&!"yes".equals(info)) {
                        isMove=false;
                    } else {
                        isMove=true;
                    }
                } else {
                    Log.d(TAG,"HasMove   bean=======================null");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Log.d(TAG, "HasMove   network=======================error"+error.toString());
            }
        };
        UserInfoDetailBean info = PreferencesUtil.getLoginInfo(getApplicationContext());
        service.HasMoveV3(info.getCommunityId(), info.getEmobId(), callback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==MOVERESULT){
            if(resultCode==RESULTSUBMIT){
                PreferencesUtil.saveHasMove(getApplicationContext(), true);
                EventBus.getDefault().post(new MoveEvent());
                finish();
            }else{
                Log.d(TAG, "onActivityResult   bean=======================close");
            }
        }
    }
}
