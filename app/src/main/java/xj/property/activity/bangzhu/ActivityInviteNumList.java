package xj.property.activity.bangzhu;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.beans.InvatePhoneResult;
import xj.property.beans.InvitatePhone;
import xj.property.beans.InvitedNeighborBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.NetBaseUtils;
import xj.property.netbasebean.SendSMSReqBean;
import xj.property.utils.CommonUtils;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.widget.pinyinsearch.helper.ContactsHelper;
import xj.property.widget.pinyinsearch.helper.ContactsIndexHelper;
import xj.property.widget.pinyinsearch.model.Contacts;
import xj.property.widget.pinyinsearch.view.ContactsOperationView;


/**
 * 获取通讯录进行点击邀请
 * <p/>
 * 1. 获取通讯录加载到主页面中
 * 2. 点击单个联系人可以调用发送短信接口进行邀请.
 */
public class ActivityInviteNumList extends HXBaseActivity implements ContactsHelper.OnContactsLoad, ContactsOperationView.OnContactsOperationView {

    private static final String TAG = "ActivityInviteNumList";
    private EditText mSearchEt;

    private ContactsOperationView mContactsOperationView;

    private UserInfoDetailBean bean;


    private View search_icon_rlay;

    private View ll_errorpage;
    private View ll_neterror;

    private RelativeLayout invite_num_list_content_rlay;
    /// 邀请邻居类型 默认邀请用户类型.
    private String inviteType= "owner";
    private boolean mFirstRefreshView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_num_list);
        bean = PreferencesUtil.getLoginInfo(this);
        inviteType = getIntent().getStringExtra("inviteType");
        initView();
        initListener();
        initData();
    }

    private void initView() {
        initTitle();
        search_icon_rlay = this.findViewById(R.id.search_icon_rlay);
        search_icon_rlay.setOnClickListener(this);
        mSearchEt = (EditText) this.findViewById(R.id.search_edit_text);
        mContactsOperationView = (ContactsOperationView) findViewById(R.id.contacts_operation_layout);
        mContactsOperationView.setOnContactsOperationView(this);
        invite_num_list_content_rlay = (RelativeLayout) this.findViewById(R.id.invite_num_list_content_rlay);
        ll_errorpage = this.findViewById(R.id.ll_errorpage);
        ll_neterror = this.findViewById(R.id.ll_neterror);
        this.findViewById(R.id.tv_getagain).setOnClickListener(this);
    }


    private void setNetworkErrorView() {
        if (ll_errorpage != null && ll_neterror != null) {
            ll_neterror.setVisibility(View.VISIBLE);
            ll_errorpage.setVisibility(View.VISIBLE);
        }
        if (invite_num_list_content_rlay != null) {
            invite_num_list_content_rlay.setVisibility(View.GONE);
        }
    }


    private void setNetworkOKView() {
        if (ll_errorpage != null && ll_neterror != null) {
            ll_neterror.setVisibility(View.GONE);
            ll_errorpage.setVisibility(View.GONE);
        }
        if (invite_num_list_content_rlay != null) {
            invite_num_list_content_rlay.setVisibility(View.VISIBLE);
        }
    }

    protected void initData() {
        if (CommonUtils.isNetWorkConnected(this)) {
            setNetworkOKView();
        } else {
            setNetworkErrorView();
            return;
        }

        ContactsHelper.getInstance().setOnContactsLoad(this);
        setFirstRefreshView(true);

//        getRegisterNeighborsNewest();
        /// 默认加载全部通讯录
        boolean startLoad = ContactsHelper.getInstance().startLoadContacts(null);
        if (true == startLoad) {
            mContactsOperationView.contactsLoading();
        }
        setNetworkOKView();

    }

    interface InvitateService {

        /**
         * 获取所有已邀请邻居列表
         *
         * @param emobId
         * @param cb
         */
        @GET("/api/v1/users/{emobId}/neighbors/all")
        void getAllInvitedNeighbors(@Path("emobId") String emobId, Callback<InvitedNeighborBean> cb);

    }


    /**
     * 获取所有已邀请邻居列表
     */
    private void getRegisterNeighborsNewest() {
        mLdDialog.show();
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        InvitateService service = restAdapter.create(InvitateService.class);
        Callback<InvitedNeighborBean> callback = new Callback<InvitedNeighborBean>() {
            @Override
            public void success(InvitedNeighborBean bean, Response response) {

                if (bean != null) {

                    if ("yes".equals(bean.getStatus())) {

                        List<InvitedNeighborBean.InfoEntity> infoEntities = bean.getInfo();

                        boolean startLoad = ContactsHelper.getInstance().startLoadContacts(infoEntities);
                        if (true == startLoad) {
                            mContactsOperationView.contactsLoading();
                        }
                        setNetworkOKView();
                        mLdDialog.dismiss();
                    } else {

                        mLdDialog.dismiss();
                        showToast(bean.getMessage());
                    }
                } else {
                    mLdDialog.dismiss();
                    showToast("数据异常");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mLdDialog.dismiss();
                error.printStackTrace();
                showNetErrorToast();
            }
        };
        service.getAllInvitedNeighbors(bean == null ? "null" : bean.getEmobId(), callback);
    }


    protected void initListener() {
        mSearchEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                String curCharacter = s.toString().trim();

                if (TextUtils.isEmpty(curCharacter)) {
                    ContactsHelper.getInstance().parseQwertyInputSearchContacts(null);
                } else {
                    ContactsHelper.getInstance().parseQwertyInputSearchContacts(curCharacter);
                }
                mContactsOperationView.updateContactsList(TextUtils.isEmpty(curCharacter));
            }
        });

    }

    @Override
    public void onResume() {
        if (false == isFirstRefreshView()) {
            mContactsOperationView.updateContactsList();
        } else {
            setFirstRefreshView(false);
        }
        super.onResume();
    }


    public boolean isFirstRefreshView() {
        return mFirstRefreshView;
    }


    public void setFirstRefreshView(boolean firstRefreshView) {
        mFirstRefreshView = firstRefreshView;
    }

    /**
     * 初始化标题
     */
    private void initTitle() {
        this.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) this.findViewById(R.id.tv_title)).setText("手机通讯录");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.search_icon_rlay:
                search_icon_rlay.setVisibility(View.GONE);
                if (mSearchEt != null) {
                    mSearchEt.setVisibility(View.VISIBLE);
                    mSearchEt.setFocusable(true);
                    mSearchEt.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                    mSearchEt.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
                }
                break;

            case R.id.tv_getagain:

                initData();

                break;
        }
    }


    @Override
    protected void onDestroy() {

        mSearchEt.setText("");
        ContactsHelper.getInstance().parseQwertyInputSearchContacts(null);

        List<Contacts> selectedContactsList = new ArrayList<Contacts>();
        selectedContactsList.addAll(ContactsHelper.getInstance().getSelectedContacts().values());
        Log.i(TAG, "onDestroy() selectedContactsList.size()=" + selectedContactsList.size());
        for (Contacts cs : selectedContactsList) {
            Log.i(TAG, "onDestroy() name=[" + cs.getName() + "] phoneNumber=[" + cs.getPhoneNumber() + "]");
        }
        mContactsOperationView.clearSelectedContacts();
        ContactsHelper.getInstance().clearSelectedContacts();
        super.onDestroy();
    }


    /*Start: OnContactsLoad*/
    @Override
    public void onContactsLoadSuccess() {
        ContactsHelper.getInstance().parseQwertyInputSearchContacts(null);
        mContactsOperationView.contactsLoadSuccess();
        ContactsIndexHelper.getInstance().praseContacts(ContactsHelper.getInstance().getBaseContacts());
    }

    @Override
    public void onContactsLoadFailed() {
        mContactsOperationView.contactsLoadFailed();

    }
    /*End: OnContactsLoad*/

    /*Start: OnContactsOperationView*/
    @Override
    public void onListItemClick(Contacts contacts, int position) {
        ContactsHelper.getInstance().parseQwertyInputSearchContacts(null);
        mContactsOperationView.updateContactsList(true);
    }

    @Override
    public void onAddContactsSelected(Contacts contacts) {
        if (null != contacts) {
            Log.i(TAG, "onAddContactsSelected name=[" + contacts.getName() + "] phoneNumber=[" + contacts.getPhoneNumber() + "]");
            Toast.makeText(getmContext(), "Add [" + contacts.getName() + ":" + contacts.getPhoneNumber() + "]", Toast.LENGTH_SHORT).show();
            ContactsHelper.getInstance().addSelectedContacts(contacts);
        }
    }


    @Override
    public void onRemoveContactsSelected(Contacts contacts) {
        if (null != contacts) {
            Log.i(TAG, "onRemoveContactsSelected name=[" + contacts.getName() + "] phoneNumber=[" + contacts.getPhoneNumber() + "]");
            Toast.makeText(getmContext(), "Remove [" + contacts.getName() + ":" + contacts.getPhoneNumber() + "]", Toast.LENGTH_SHORT).show();
            ContactsHelper.getInstance().removeSelectedContacts(contacts);
        }
    }

    @Override
    public void onContactsCall(Contacts contacts) {
        //Toast.makeText(mContext, "onContactsCall"+contacts.getPhoneNumber(), Toast.LENGTH_SHORT).show();
        if (null != contacts) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contacts.getPhoneNumber()));
            startActivity(intent);
        }

        ////打电话
    }


    @Override
    public void onContactsSms(Contacts contacts) {
        //Toast.makeText(mContext, "onContactsSms"+contacts.getPhoneNumber(), Toast.LENGTH_SHORT).show();
//        ShareUtil.shareTextBySms(getmContext(), contacts.getPhoneNumber(), null);
        //// 发送短信
    }

    @Override
    public void onInviteNeighbors(Contacts contacts) {

        if (null != contacts) {
            invateByPhone(contacts.getPhoneNumber());
        } else {
//            Toast.makeText(mContext, "onContactsSms"+contacts.getPhoneNumber(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "contacts is null  error ");

        }
    }

//    interface InvitateByPhoneNumService {
//        @POST("/api/v1/users/{emobId}/neighbors/invite")
//        void inviateByPhoneNum(@Header("signature") String signature, @Body InvitatePhone acr, @Path("emobId") String emobId, Callback<InvatePhoneResult> cb);
//    }


    private String filterPhone(String phoneNum) {

        if(TextUtils.isEmpty(phoneNum)){

            return phoneNum;
        }
        if(phoneNum.contains("-")|| phoneNum.contains(" ")||phoneNum.contains("(")|| phoneNum.contains(")")){
            return  phoneNum.replaceAll("\\ ","").replaceAll("\\-","").replace("\\(","").replace("\\)","");
        }else{
            return phoneNum;
        }
    }

    /**
     * 通过手机号发送短信邀请邻居
     *
     * @param phoneNum
     */
    private void invateByPhone(String phoneNum) {
        mLdDialog.show();
        phoneNum = filterPhone(phoneNum);

        SendSMSReqBean sendSMSReqBean = new SendSMSReqBean();
        sendSMSReqBean.setEmobId(bean.getEmobId());
        sendSMSReqBean.setCommunityName(PreferencesUtil.getCommityName(getmContext()));
        sendSMSReqBean.setNickname(bean.getNickname());

        NetBaseUtils.sendSMSbyPhone(getmContext(), PreferencesUtil.getCommityId(getmContext()), phoneNum, sendSMSReqBean, new NetBaseUtils.NetRespListener<CommonRespBean<String>>() {
            @Override
            public void successYes(CommonRespBean<String> commonRespBean, Response response) {
                showMiddleToast("帮帮已将免费信息发送给邻居");
                mLdDialog.dismiss();
            }

            @Override
            public void successNo(CommonRespBean<String> commonRespBean, Response response) {
                showMiddleToast("邀请短信发送失败:" + commonRespBean.getMessage());
                mLdDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                showMiddleToast("邀请短信发送失败,请重试");
                mLdDialog.dismiss();
            }
        });

//
//        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE2).build();
//        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
//        InvitateByPhoneNumService service = restAdapter.create(InvitateByPhoneNumService.class);
//        Callback<InvatePhoneResult> callback = new Callback<InvatePhoneResult>() {
//            @Override
//            public void success(InvatePhoneResult bean, retrofit.client.Response response) {
//                if (bean != null && TextUtils.equals(bean.getStatus(), "yes")) {
//                    showMiddleToast("帮帮已将免费信息发送给邻居");
//                } else {
//                    showMiddleToast(bean.getMessage());
//                }
//                mLdDialog.dismiss();
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                showMiddleToast("邀请短信发送失败,请重试");
//                mLdDialog.dismiss();
//            }
//        };
//
//        InvitatePhone request = new InvitatePhone();
//        request.setPhone(phoneNum);
//        if(TextUtils.isEmpty(inviteType)){
//            request.setType("owner");
//        }else{
//            request.setType(inviteType);
//        }
//        request.setCommunityName(PreferencesUtil.getCommityName(getmContext()));
//        if (bean == null) {
//            bean = PreferencesUtil.getLoginInfo(getmContext());
//        }
//        service.inviateByPhoneNum(StrUtils.string2md5(Config.BANGBANG_TAG + StrUtils.dataHeader(request)), request, bean.getEmobId(), callback);
    }




}
