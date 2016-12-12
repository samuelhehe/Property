package xj.property.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.activity.HXBaseActivity.MainActivity;
import xj.property.adapter.ChatAllHistoryAdapter;
import xj.property.adapter.ChatSameAdapter;
import xj.property.beans.ChatSameAllBean;
import xj.property.beans.ChatSameDataBean;
import xj.property.beans.ChatSameInfoBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.db.InviteMessgeDao;
import xj.property.event.DialogEvent;
import xj.property.event.ExitEvent;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.ToastUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.widget.SelfListView;

/**
 * 显示所有会话记录，比较简单的实现，更好的可能是把陌生人存入本地，这样取到的聊天记录是可控的
 * <p/>
 * 联系
 */
public class ChatAllHistoryFragment extends Fragment {

    private String TAG = "ChatAllHistoryFragment";

    private int labSize = 10;
    private int labNum = 1;
    private boolean isLoad = true;
    private final int UPLOAD_HISTORY = 1;

    private InputMethodManager inputMethodManager;
    private SelfListView listView;
    private ChatAllHistoryAdapter adapter;
    private ChatSameAdapter mChatSameAdapter;
    private EditText query;
    private ImageButton clearSearch;
    public RelativeLayout errorItem;
    public TextView errorText;
    private boolean hidden;
    private List<EMGroup> groups;
    private List<EMConversation> conversationList;
    private LinearLayout ll_empty;
    private LinearLayout ll_empty1;
    private View chathistory_dliver_v;
    private TextView tv_same;
    private LinearLayout mLl_refresh;
    private ImageView mIv_loadmore;
    private LinearLayout mLl_refresh_include;
    private ImageView mIv_refresh_include;
    /// 相似列表
    private SelfListView my_similar_mlv;
    private LinearLayout ll_more;

    private boolean mFirstLoad = true;//是否是第一次加载

    private Handler mHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_conversation_history, container, false);
        initView(contentView);
        return contentView;
    }

    private void initView(View contentView) {
        initTitle(contentView);
    }

    private void initTitle(View view) {
        view.findViewById(R.id.iv_back).setVisibility(View.INVISIBLE);
        TextView title = (TextView) view.findViewById(R.id.tv_title);
        title.setText("联系");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        groups = EMGroupManager.getInstance().getAllGroups();
        EventBus.getDefault().register(this);
        initView();
        initListenner();
        initData();
    }

    private void initData() {
        conversationList = loadConversationsWithRecentChat();
        adapter = new ChatAllHistoryAdapter(getActivity(), 1, conversationList);
        listView.setAdapter(adapter);
        List<ChatSameDataBean> mList = new ArrayList<ChatSameDataBean>();
        mChatSameAdapter = new ChatSameAdapter(getActivity(), mList);
        my_similar_mlv.setAdapter(mChatSameAdapter);
        my_similar_mlv.setVisibility(View.GONE);
        ll_empty1.setVisibility(View.VISIBLE);
        UserInfoDetailBean bean = PreferencesUtil.getLoginInfo(getActivity());
        if (null != bean && null != bean.getEmobId()) {
            selectLabInfo(bean.getEmobId());//TODO 正常环境用
        }
        emptyPage();
        mLl_refresh_include.setVisibility(View.GONE);
        ll_more.setVisibility(View.GONE);
    }

    private void initListenner() {
        ll_more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_more.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        labNum++;
                        UserInfoDetailBean bean = PreferencesUtil.getLoginInfo(getActivity());
                        showLoadRefreshAnim();
                        selectLabInfo(bean.getEmobId());
                    }
                }, 40);
            }
        });

        listView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 隐藏软键盘
                hideSoftKeyboard();
                return false;
            }

        });

        query.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
                if (s.length() > 0) {
                    clearSearch.setVisibility(View.VISIBLE);
                } else {
                    clearSearch.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void afterTextChanged(Editable s) {
            }

        });

        clearSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                query.getText().clear();
                hideSoftKeyboard();
            }
        });
    }

    private void initView() {
        errorItem = (RelativeLayout) getView().findViewById(R.id.rl_error_item);
        errorText = (TextView) errorItem.findViewById(R.id.tv_connect_errormsg);
        ll_empty = (LinearLayout) getView().findViewById(R.id.ll_empty);
        ll_empty1 = (LinearLayout) getView().findViewById(R.id.ll_empty1);
        /// 聊天与相似列表中间的分割线
        chathistory_dliver_v = getView().findViewById(R.id.chathistory_dliver_v);
        tv_same = (TextView) getView().findViewById(R.id.tv_same);
        my_similar_mlv = (SelfListView) getView().findViewById(R.id.my_similar_mlv);
        ll_more = (LinearLayout) getView().findViewById(R.id.ll_more);

        mLl_refresh = (LinearLayout) getView().findViewById(R.id.ll_refresh);
        mIv_loadmore = (ImageView) getView().findViewById(R.id.iv_loadmore);
        mLl_refresh_include = (LinearLayout) getView().findViewById(R.id.ll_refresh_include);
        mIv_refresh_include = (ImageView) getView().findViewById(R.id.iv_refresh_include);
        listView = (SelfListView) getView().findViewById(R.id.list);
        query = (EditText) getView().findViewById(R.id.query);
        clearSearch = (ImageButton) getView().findViewById(R.id.search_clear);
    }

    // ||PreferencesUtil.isFirstLogin(getActivity())==false
    private void emptyPage() {
        if (conversationList == null || conversationList.size() == 0) {
            if (ll_empty != null)
                ll_empty.setVisibility(View.VISIBLE);
        } else {
            ll_empty.setVisibility(View.GONE);
        }
    }

    //    v3 2016/03/18
    interface getShopInfoService {

//        @GET("/api/v1/communities/{communityId}/users/user/{emobId}/recommend")
//        void getLabInfo(@Path("communityId") int communityId, @Path("emobId") String emobId, @QueryMap Map<String, String> map, Callback<ChatSameAllBean> cb);
//        @GET("/api/v1/communities/{communityId}/users/user/{emobId}/recommend")


        ///api/v3/communities/{小区ID}/users/{用户环信ID}/recommend?page={页码}&limit={页面大小}
        @GET("/api/v3/communities/{communityId}/users/{emobId}/recommend")
        void getLabInfo(@Path("communityId") int communityId, @Path("emobId") String emobId, @QueryMap Map<String, String> map, Callback<CommonRespBean<ChatSameInfoBean>> cb);
    }

    private void showRefreshAnim() {
        if (ll_empty1.isShown()) {
            mLl_refresh_include.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.tip);
            LinearInterpolator lir = new LinearInterpolator();
            animation.setInterpolator(lir);
            mIv_refresh_include.startAnimation(animation);
            ll_empty1.setVisibility(View.GONE);
        }
    }

    public void onEventMainThread(DialogEvent event) {
        EMConversation tobeDeleteCons = adapter.getItem(Integer.parseInt(event.getMessage()));
        // 删除此会话
        EMChatManager.getInstance().deleteConversation(tobeDeleteCons.getUserName(), tobeDeleteCons.isGroup());
        InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
        inviteMessgeDao.deleteMessage(tobeDeleteCons.getUserName());
        adapter.remove(tobeDeleteCons);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        // 更新消息未读数
        ((MainActivity) getActivity()).updateUnreadLabel();
    }

    private void stopRefreshAnim() {
        mLl_refresh_include.setVisibility(View.GONE);
    }

    private void showLoadRefreshAnim() {
        mLl_refresh.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.tip);
        LinearInterpolator lir = new LinearInterpolator();
        animation.setInterpolator(lir);
        mIv_loadmore.startAnimation(animation);
        ll_more.setVisibility(View.GONE);
    }

    private void stopLoadRefreshAnim() {
        if (mLl_refresh.isShown()) {
            mLl_refresh.setVisibility(View.GONE);
            ll_more.setVisibility(View.VISIBLE);
        }
    }

    private void selectLabInfo(final String emobId) {

        HashMap<String, String> option = new HashMap<String, String>();
        option.put("page", labNum + "");
        option.put("limit", labSize + "");
//        page={页码}&limit={页面大小}
        getShopInfoService service = RetrofitFactory.getInstance().create(getActivity(), option, getShopInfoService.class);
        Callback<CommonRespBean<ChatSameInfoBean>> callback = new Callback<CommonRespBean<ChatSameInfoBean>>() {
            @Override
            public void success(CommonRespBean<ChatSameInfoBean> bean, retrofit.client.Response response) {
                stopRefreshAnim();
                stopLoadRefreshAnim();
                if (bean != null) {
                    boolean isRefreshSame = PreferencesUtil.isRefreshSame(getActivity());
                    if ("yes".equals(bean.getStatus()) && bean.getData() != null && bean.getData().getData() != null) {
                        ll_empty1.setVisibility(View.GONE);
                        if (mFirstLoad) {
                            chathistory_dliver_v.setVisibility(View.VISIBLE);
                            tv_same.setVisibility(View.VISIBLE);
                            my_similar_mlv.setVisibility(View.VISIBLE);
                            ll_more.setVisibility(View.VISIBLE);
                            mFirstLoad = false;
                            PreferencesUtil.setRefreshSame(getActivity(), false);
                            initLabAdapter(bean.getData().getData());
                        } else if (labNum != 1 || isRefreshSame) {
                            ll_empty1.setVisibility(View.GONE);
                            if (bean.getData().getData().size()<10){
                                ToastUtils.showNoMoreToast(getActivity());
                                ll_more.setVisibility(View.GONE);
                            }
                            isLoad = false;
                            PreferencesUtil.setRefreshSame(getActivity(), false);
                            mChatSameAdapter.LoadMoreRefresh(bean.getData().getData());
                            ll_more.setVisibility(View.VISIBLE);
                            my_similar_mlv.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    my_similar_mlv.setAdapter(mChatSameAdapter);
                                }
                            }, 40);
                        }


                    } else {
                        PreferencesUtil.setRefreshSame(getActivity(), false);
                        sameEmpty();
                        Log.i(TAG, "亲，没有找到相同标签的邻居。");
                    }
                } else {
                    PreferencesUtil.setRefreshSame(getActivity(), false);
                    sameEmpty();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                PreferencesUtil.setRefreshSame(getActivity(), false);
                stopRefreshAnim();
                sameEmpty();
                stopLoadRefreshAnim();
                error.printStackTrace();
            }
        };

        service.getLabInfo(PreferencesUtil.getCommityId(getActivity()), emobId, option, callback);
    }

    private void sameEmpty() {
        if (mChatSameAdapter != null && mChatSameAdapter.mList != null) {
            if (mChatSameAdapter.mList.size() > 0) {
                ll_empty1.setVisibility(View.GONE);
            } else {
                ll_empty1.setVisibility(View.VISIBLE);
            }
        } else {
            ll_empty1.setVisibility(View.VISIBLE);
        }
    }

    public void onEventMainThread(ExitEvent event) {
        mChatSameAdapter.mList.clear();
    }

    private void initLabAdapter(List<ChatSameDataBean> bean) {
        mChatSameAdapter = new ChatSameAdapter(getActivity(), bean);
        my_similar_mlv.setAdapter(mChatSameAdapter);
    }

    void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 刷新页面
     */
    public void refresh() {

        if (conversationList != null) {
            conversationList.clear();
            conversationList.addAll(loadConversationsWithRecentChat());
        }
        if (mChatSameAdapter != null && PreferencesUtil.getLogin(getActivity())) {
            UserInfoDetailBean bean = PreferencesUtil.getLoginInfo(getActivity());
            boolean isRefreshSame = PreferencesUtil.isRefreshSame(getActivity());
            if (null != bean && null != bean.getEmobId() && mChatSameAdapter.mList == null
                    || null != bean && null != bean.getEmobId() && mChatSameAdapter.mList.size() == 0 || isRefreshSame) {
                labNum = 1;
                mChatSameAdapter.clearDate();
                my_similar_mlv.setAdapter(mChatSameAdapter);
                ll_more.setVisibility(View.GONE);
                showRefreshAnim();
                selectLabInfo(bean.getEmobId());//TODO 正常环境用
            }
        }
        if (adapter != null) {
            adapter.reSetFilter();
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
        }
        emptyPage();
    }

    /**
     * 获取所有会话
     *
     * @return
     */
    private List<EMConversation> loadConversationsWithRecentChat() {
        // 获取所有会话，包括陌生人
        Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();

        List<EMConversation> list = new ArrayList<EMConversation>();
        List<EMConversation> delList = new ArrayList<EMConversation>();

        // 过滤掉messages seize为0的conversation
        for (EMConversation conversation : conversations.values()) {

            if (conversation.getAllMessages().size() != 0) {
                if (conversation.getLastMessage().getChatType() == EMMessage.ChatType.GroupChat && EMGroupManager.getInstance().getGroup(conversation.getUserName()) == null)
                    delList.add(conversation);
                else list.add(conversation);
            }
        }
        // 排序
        sortConversationByLastChatTime(list);
        for (int i = 0; i < delList.size(); i++) {
            EMChatManager.getInstance().deleteConversation(delList.get(i).getUserName());
        }
        ((MainActivity) getActivity()).updateUnreadLabel();
        return list;
    }

    /**
     * 根据最后一条消息的时间排序
     */
    private void sortConversationByLastChatTime(List<EMConversation> conversationList) {
        Collections.sort(conversationList, new Comparator<EMConversation>() {
            @Override
            public int compare(final EMConversation con1, final EMConversation con2) {

                EMMessage con2LastMessage = con2.getLastMessage();
                EMMessage con1LastMessage = con1.getLastMessage();
                if (con2LastMessage.getMsgTime() == con1LastMessage.getMsgTime()) {
                    return 0;
                } else if (con2LastMessage.getMsgTime() > con1LastMessage.getMsgTime()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden) {
            refresh();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            Log.d(TAG, "isVisibleToUser " + isVisibleToUser);

            if (!hidden && !((MainActivity) getActivity()).isConflict) {
                refresh();
            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (((MainActivity) getActivity()).isConflict)
            outState.putBoolean("isConflict", true);
        super.onSaveInstanceState(outState);

    }

}
