package xj.property.activity.tags;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.beans.SearchUserResultRespBean;
import xj.property.beans.TagsA2BAddReqBean;
import xj.property.beans.TagsA2BAddRespBean;
import xj.property.beans.TagsA2BDelReqBean;
import xj.property.beans.TagsA2BDelRespBean;
import xj.property.beans.TagsA2BRespBean;
import xj.property.beans.UserGroupBean;
import xj.property.beans.UserGroupBeanForDel;
import xj.property.event.FriendsChoicedBackEvent;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;

public class GroupAddOrDeleteConfrimDialog extends Dialog implements View.OnClickListener {

    private static String TAG = "GroupAddOrDeleteConfrimDialog";
    private final OnMemberAddDeleteListener onMemberAddDeleteListener;
    private FriendsChoicedBackEvent friendsChoicedBackEvent;

    private Context mContext;

    /// 取消
    private Button btn_cancel;
    /// 确定
    private Button btn_ok;
    private ImageView btn_diver_iv;
    private TextView mytags_del_confirm_tv;


    public GroupAddOrDeleteConfrimDialog(Context context, FriendsChoicedBackEvent friendsChoicedBackEvent, OnMemberAddDeleteListener onMemberAddDeleteListener) {
        super(context, R.style.Theme_CustomDialog);
        this.mContext = context;
        this.friendsChoicedBackEvent = friendsChoicedBackEvent;
        this.onMemberAddDeleteListener = onMemberAddDeleteListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_group_addordelete_confrim_mgr);
        initView();
        initData();
    }

    private void initData() {
        if (friendsChoicedBackEvent != null) {
            if (friendsChoicedBackEvent.getFlag() == FriendsChoicedBackEvent.FLAG_MEMBERS_ADD) {
                btn_cancel.setVisibility(View.GONE);
                btn_diver_iv.setVisibility(View.GONE);

                List<String> emobIds = friendsChoicedBackEvent.getEmobIds();
                if(emobIds!=null&&emobIds.size()>0){
                    mytags_del_confirm_tv.setText("您已成功邀请"+emobIds.size()+"位邻居，等待对方确认");
                }

            } else if (friendsChoicedBackEvent.getFlag() == FriendsChoicedBackEvent.FLAG_MEMBERS_DELETE) {

                btn_diver_iv.setVisibility(View.VISIBLE);
                btn_cancel.setVisibility(View.VISIBLE);

                Map<String, UserGroupBeanForDel> mapLists = friendsChoicedBackEvent.getMapLists();
                StringBuilder delNotices = new StringBuilder("确定要删除");
                int count = 0;
                for (Map.Entry<String, UserGroupBeanForDel> entry : mapLists.entrySet()) {
                    System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
                    delNotices.append("“").append(entry.getValue().nickname).append("”");
                    count++;
                    if (count >= 2) {
                        break;
                    }
                }
                delNotices.append("等成员吗？");
                mytags_del_confirm_tv.setText(delNotices.toString());

//                您确定要删除“没名字”,“闲来没事” 等成员吗？

            }


        }

    }

    public interface OnMemberAddDeleteListener {

        void onMemberAddOK(FriendsChoicedBackEvent friendsChoicedBackEvent);

        void onMemberAddCancel(FriendsChoicedBackEvent friendsChoicedBackEvent);

        void onMemberDelSuccessOK(FriendsChoicedBackEvent friendsChoicedBackEvent);

        void onMemberDelSuccessCancel(FriendsChoicedBackEvent friendsChoicedBackEvent);

    }


    private void initView() {

        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);
        btn_diver_iv = (ImageView) findViewById(R.id.btn_diver_iv);
        mytags_del_confirm_tv = (TextView) findViewById(R.id.mytags_del_confirm_tv);

    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_cancel:

                if (friendsChoicedBackEvent != null) {

                    if (friendsChoicedBackEvent.getFlag() == FriendsChoicedBackEvent.FLAG_MEMBERS_ADD) {
                        if(onMemberAddDeleteListener!=null){
                            onMemberAddDeleteListener.onMemberAddCancel(friendsChoicedBackEvent);
                        }

                    } else if (friendsChoicedBackEvent.getFlag() == FriendsChoicedBackEvent.FLAG_MEMBERS_DELETE) {

                        if(onMemberAddDeleteListener!=null){
                            onMemberAddDeleteListener.onMemberDelSuccessCancel(friendsChoicedBackEvent);
                        }
                    }
                }
                cancel();
                break;

            case R.id.btn_ok:

                if (friendsChoicedBackEvent != null) {

                    if (friendsChoicedBackEvent.getFlag() == FriendsChoicedBackEvent.FLAG_MEMBERS_ADD) {

                        if(onMemberAddDeleteListener!=null) {
                            onMemberAddDeleteListener.onMemberAddOK(friendsChoicedBackEvent);
                        }
                    } else if (friendsChoicedBackEvent.getFlag() == FriendsChoicedBackEvent.FLAG_MEMBERS_DELETE) {
                        if (onMemberAddDeleteListener != null) {
                            onMemberAddDeleteListener.onMemberDelSuccessOK(friendsChoicedBackEvent);
                        }
                    }
                }
                dismiss();
                break;

        }
    }

    @Override
    public void cancel() {
        super.cancel();
    }

}
