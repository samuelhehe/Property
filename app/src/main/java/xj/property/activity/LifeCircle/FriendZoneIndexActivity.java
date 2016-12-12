package xj.property.activity.LifeCircle;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.repo.xw.library.views.PullListView;
import com.repo.xw.library.views.PullToRefreshLayout;
import com.umeng.socialize.sso.UMSsoHandler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.XjApplication;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.HXBaseActivity.MainActivity;
import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
import xj.property.activity.cooperation.LastWeekRPValueTopListActivity;
import xj.property.activity.user.ShowBigImageViewPager;
import xj.property.adapter.ExpressionAdapter;
import xj.property.adapter.ExpressionPagerAdapter;
import xj.property.adapter.LifeMessageAdapter;
import xj.property.adapter.XJBaseAdapter;
import xj.property.beans.CircleListRespone;
import xj.property.beans.LifeCircleBean;
import xj.property.beans.LifeCircleDetail;
import xj.property.beans.NewPraiseNotify;
import xj.property.beans.NewPraiseResponse;
import xj.property.beans.UserInfoDetailBean;
import xj.property.cache.ZambiaCache;
import xj.property.event.EvaEvent;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.provider.ShareProvider;
import xj.property.utils.CommonUtils;
import xj.property.utils.DensityUtil;
import xj.property.utils.SmileUtils;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.FriendZoneUtil;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.UserUtils;
import xj.property.widget.ExpandGridView;
import xj.property.widget.com.viewpagerindicator.CirclePageIndicator;

/**
 * Created by Administrator on 2015/6/8.
 * v3 2016/03/04
 */
public class FriendZoneIndexActivity extends HXBaseActivity implements PullToRefreshLayout.OnRefreshListener {
    final static int REQUEST_NEW_RECORE = 778;
    private static final int LIMIT_NUM = 10;
    public ListView lv_eva;
    ImageView avatar;
    UserInfoDetailBean userInfoDetailBean;
    HashMap<Integer, LifeCircleBean> map = new HashMap<>();
    List<LifeCircleBean> circleBeanList = new ArrayList<>();
    List<NewPraiseNotify> praiseNotifies = new ArrayList<>();
    TextView tv_title, tv_person_value, tv_content;

    ImageView iv_right_text;

    LifeMessageAdapter lifeMessageAdapter;
    XJBaseAdapter adapter;

    LinearLayout ll_character_llay;

    FrameLayout ll_none_eva;
    Button btnSend, btnCharter;
    xj.property.widget.PasteEditText editText;
    PopupWindow popupWindow, popupWindowEva;
    View headView;
    int pageIndex;
    View root;
    private ArrayList<ImageView> images = new ArrayList<ImageView>();

    ArrayList<String> currentlifePhotos = new ArrayList<>();

    TextView tv_zan_rpvalue, tv_eva_ta, tv_coyp_txt;

    private LinearLayout ll_face_container;

    private ViewPager vPager;

    private CirclePageIndicator vpager_indicator;
    private List<String> reslist; //// 表情资源列表
    /// 发送表情, 显示表情容器
    private CheckBox send_img_checkbox;

    private InputMethodManager imm;
    private PullToRefreshLayout pull_to_refreshlayout;
    public PullListView lv_zone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShareProvider.getInitShareProvider(FriendZoneIndexActivity.this);
        setContentView(R.layout.activity_friendzondindex);
        userInfoDetailBean = PreferencesUtil.getLoginInfo(this);
        PreferencesUtil.saveUnReadCircleCount(this, 0);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (userInfoDetailBean != null) {
            praiseNotifies.addAll(PreferencesUtil.getNewPraise(this, userInfoDetailBean.getEmobId()));
            getCircleTipsList(userInfoDetailBean.getEmobId());
        }
        initView();
        getData();
        pull_to_refreshlayout.autoRefresh();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        userInfoDetailBean = PreferencesUtil.getLoginInfo(this);
        if (userInfoDetailBean != null) {
            getCircleTipsList(userInfoDetailBean.getEmobId());
        }
    }

    private void initView() {
        tv_coyp_txt = (TextView) findViewById(R.id.tv_coyp_txt);
        root = findViewById(R.id.root);

        pull_to_refreshlayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refreshlayout);
        pull_to_refreshlayout.setOnRefreshListener(this);

        lv_zone = (PullListView) findViewById(R.id.lv_zone);
//        lv_zone.setOnScrollListener(new PauseOnScrollListener());
        lv_zone.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (lv_zone.getLastVisiblePosition() == (lv_zone.getCount() - 1)) {
                            pull_to_refreshlayout.autoLoad();
                        }
                        // 判断滚动到顶部
                        if (lv_zone.getFirstVisiblePosition() == 0) {
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

//        lv_zone=(ListView)findViewById(R.id.lv_zone);
        // lv_eva=(ListView)findViewById(R.id.lv_eva);
        findViewById(R.id.iv_search).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        avatar = (ImageView) findViewById(R.id.iv_avatar);
        avatar.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        ll_none_eva = (FrameLayout) findViewById(R.id.ll_none_eva);
        ll_character_llay = (LinearLayout) findViewById(R.id.ll_character_llay);
        ll_character_llay.setOnClickListener(this);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_person_value = (TextView) findViewById(R.id.tv_person_value);
        if (userInfoDetailBean != null) {

            ImageLoader.getInstance().displayImage(userInfoDetailBean.getAvatar(), avatar, UserUtils.options);
//            tv_title.setText(userInfoDetailBean.getNickname());   2015/12/16
        }
        iv_right_text = (ImageView) findViewById(R.id.iv_right_text);
        iv_right_text.setOnClickListener(this);

        lifeMessageAdapter = new LifeMessageAdapter(this, circleBeanList);

        if (headView == null) {
            headView = View.inflate(this, R.layout.circyle_headview, null);
            lv_eva = (ListView) headView.findViewById(R.id.lv_eva);

            lv_zone.addHeaderView(headView);
        }

        lv_zone.setAdapter(lifeMessageAdapter);


        headView.findViewById(R.id.search).setOnClickListener(this);
//       NewPraiseNotify praiseNotify=new NewPraiseNotify();
//        praiseNotify.setAvatar4Show("http://baidu.logo");
//        praiseNotify.setContent4Show("测试");
//        praiseNotifies.add(praiseNotify);

        adapter = new XJBaseAdapter(this,
                R.layout.item_neweva_notify, praiseNotifies,
                new String[]{"content4Show"}, new String[]{"getAvatar4Show"},
                new int[]{R.id.neweva_head}, UserUtils.options);

//        adapter = new XJBaseAdapter(this, R.layout.item_neweva_notify, praiseNotifies, new String[]{"content4Show"}, new String[]{"getAvatar4Show"}, new int[]{R.id.neweva_head}, UserUtils.options);

        lv_eva.setAdapter(adapter);
        lv_eva.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewPraiseNotify newPraiseNotify = praiseNotifies.remove(position);
                for (int i = 0; i < praiseNotifies.size(); ) {
                    if (newPraiseNotify.getLifeCircleId() == praiseNotifies.get(i).getLifeCircleId()) {
                        praiseNotifies.remove(i);
                    } else {
                        i++;
                    }
                }
                PreferencesUtil.saveNewPraise(FriendZoneIndexActivity.this, userInfoDetailBean.getEmobId(), praiseNotifies);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        lifeMessageAdapter.notifyDataSetChanged();
                    }
                });
                if (newPraiseNotify.lifeCircleId == 0) {
                    Intent intent = new Intent(FriendZoneIndexActivity.this, MyPraiseActivity.class);
                    intent.putExtra(Config.Emobid, userInfoDetailBean.getEmobId());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(FriendZoneIndexActivity.this, ZoneItemActivity.class);
                    intent.putExtra(Config.INTENT_PARMAS1, userInfoDetailBean.getEmobId());
                    intent.putExtra(Config.INTENT_PARMAS2, newPraiseNotify.lifeCircleId);
                    startActivity(intent);
                }

            }
        });
        if (userInfoDetailBean != null) {
            ll_none_eva.setVisibility(View.GONE);
            ll_character_llay.setVisibility(View.VISIBLE);
        } else {
            ll_character_llay.setVisibility(View.GONE);
            ll_none_eva.setVisibility(View.VISIBLE);
        }

        initPopupWindow();
        initPopupWindow2();
    }

    private void initPopupWindow() {
        View bottomview = View.inflate(this, R.layout.bottom_input, null);
        popupWindow = new PopupWindow(bottomview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        ll_face_container = (LinearLayout) bottomview.findViewById(R.id.ll_face_container);

        vPager = (ViewPager) bottomview.findViewById(R.id.vPager);
        vpager_indicator = (CirclePageIndicator) bottomview.findViewById(R.id.vpager_indicator);

        // 表情list 目前表情数量为99个
        reslist = getExpressionRes(99);

        // 初始化表情viewpager
        List<View> views = new ArrayList<View>();
        View gv1 = getGridChildView(1);
        View gv2 = getGridChildView(2);
        View gv3 = getGridChildView(3);
        View gv4 = getGridChildView(4);
        View gv5 = getGridChildView(5);

        views.add(gv1);
        views.add(gv2);
        views.add(gv3);
        views.add(gv4);
        views.add(gv5);

        vPager.setAdapter(new ExpressionPagerAdapter(views));
        vpager_indicator.setViewPager(vPager);

        editText = (xj.property.widget.PasteEditText) bottomview.findViewById(R.id.et_sendmessage);

//        editText.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
////                edittext_layout.setBackgroundResource(R.drawable.login_line);
////                send_img_checkbox.setChecked(false);
//        send_img_checkbox.setButtonDrawable(getResources().getDrawable(R.drawable.chatting_biaoqing_btn_normal));
//        ll_face_container.setVisibility(View.GONE);
////                showKeyBoard();
//            }
//        });


        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //强制显示软键盘
//                boolean bool = imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
                boolean bool = imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
                if (bool) {
                    send_img_checkbox.setButtonDrawable(getResources().getDrawable(R.drawable.chatting_biaoqing_btn_normal));
                    ll_face_container.setVisibility(View.GONE);
                }
                return false;
            }
        });

        send_img_checkbox = (CheckBox) bottomview.findViewById(R.id.send_img_checkbox);
        send_img_checkbox.setOnCheckedChangeListener(new onMyCheckedChangeListener());

        btnSend = (Button) bottomview.findViewById(R.id.btn_send);
        btnSend.setOnClickListener(this);

        btnCharter = (Button) bottomview.findViewById(R.id.btn_charter);
        btnCharter.setOnClickListener(this);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (editText != null) {
                    editText.setText("");
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                }
            }
        });

    }


    private void initPopupWindow2() {
        View evaview = View.inflate(this, R.layout.life_circle_eva_popwindow, null);
        popupWindowEva = new PopupWindow(evaview, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        tv_zan_rpvalue = (TextView) evaview.findViewById(R.id.tv_zan_rpvalue);
        tv_eva_ta = (TextView) evaview.findViewById(R.id.tv_eva_ta);
        popupWindowEva.setFocusable(false);
        popupWindowEva.setOutsideTouchable(true);
        popupWindowEva.setBackgroundDrawable(new BitmapDrawable());

        tv_zan_rpvalue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<LifeCircleDetail> list = map.get(currentEvent.getCircleLifeId()).getLifeCircleDetails();
                if (currentEvent.getCircleLifeDetialId() == 0) {
                    zambia(map.get(currentEvent.getCircleLifeId()));
                    return;
                }
                for (int i = 0; i < list.size(); i++) {
                    if (currentEvent.getCircleLifeDetialId() == list.get(i).getLifeCircleDetailId()) {
                        zambia(list.get(i));
                    }
                }
                popupWindowEva.dismiss();
            }
        });
        tv_eva_ta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.showAtLocation(root, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                if (ll_face_container != null) {
                    ll_face_container.setVisibility(View.GONE);
//                    send_img_checkbox.setChecked(false);
                    showKeyBoard();
                }
                popupWindowEva.dismiss();
            }
        });
    }

    public void shareShow(String url, String message) {
        ShareProvider.getShareProvider(FriendZoneIndexActivity.this).showShareActivity(url, message, "邻居帮帮", ShareProvider.CODE_LIFECRILE);
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        pageIndex = 0;
        getData();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        if (pageIndex == 1) {
            lv_zone.setSelection(lv_zone.getCount());
        }
        pageIndex++;
        getData();
    }


    private class onMyCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                if (ll_face_container != null) {
                    ll_face_container.setVisibility(View.VISIBLE);
//                    closeBoard();
                    //// 强制隐藏输入法
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                }
            } else {
                if (ll_face_container != null) {
                    ll_face_container.setVisibility(View.GONE);
                    showKeyBoard();
                }
            }
        }
    }

    public void closeBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
        if (imm.isActive())  //一直是true
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 获取表情的gridview的子view
     *
     * @param i
     * @return
     */
    private View getGridChildView(int i) {
        View view = View.inflate(this, R.layout.expression_gridview, null);
        ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
        List<String> list = new ArrayList<String>();
        if (i == 1) {
            List<String> list1 = reslist.subList(0, 20);/// 20/27//34
            list.addAll(list1);
        } else if (i == 2) {
            list.addAll(reslist.subList(20, 40));
        } else if (i == 3) {
            list.addAll(reslist.subList(40, 60));
        } else if (i == 4) {
            list.addAll(reslist.subList(60, 80));
        } else if (i == 5) {
            list.addAll(reslist.subList(80, reslist.size()));
        }
        list.add("delete_expression");
        final ExpressionAdapter expressionAdapter = new ExpressionAdapter(this, 1, list);
        gv.setAdapter(expressionAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filename = expressionAdapter.getItem(position);
                try {
                    if (filename != "delete_expression") { // 不是删除键，显示表情
                        // 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
                        Class clz = Class.forName("xj.property.utils.SmileUtils");

                        Field field = clz.getField(filename);


                        int selectionStart = editText.getSelectionStart();// 获取光标的位置
                        Spannable smiledText = SmileUtils.getSmiledText(getmContext(), (String) field.get(null));
                        Editable editableText = editText.getEditableText();
                        editableText.insert(selectionStart, smiledText);

//                        editText.append(SmileUtils.getSmiledText(getmContext(), (String) field.get(null)));


                    } else {
                        // 删除文字或者表情
                        if (!TextUtils.isEmpty(editText.getText())) {

                            int selectionStart = editText.getSelectionStart();// 获取光标的位置

                            if (selectionStart > 0) {

                                String body = editText.getText().toString();

                                String tempStr = body.substring(0, selectionStart);

                                int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                                if (i != -1) {
                                    /// 截取最后一个表情
                                    CharSequence cs = tempStr.substring(i, selectionStart);

                                    if (SmileUtils.containsKey(cs.toString()))
                                        /// 删除最后一个表情字符串的占位符
                                        editText.getEditableText().delete(i, selectionStart);
                                    else
                                        editText.getEditableText().delete(selectionStart - 1, selectionStart);
                                } else {
                                    editText.getEditableText().delete(selectionStart - 1, selectionStart);
                                }
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        return view;
    }

    public List<String> getExpressionRes(int getSum) {
        List<String> reslist = new ArrayList<String>();
        for (int x = 1; x <= getSum; x++) {
            String filename = "ee_" + x;

            reslist.add(filename);

        }
        return reslist;

    }


    private void showPopupWindowEva(final View view, String nickname) {
//        Log.i("debbug", "nickname=" + nickname);
//        tv_coyp_txt.setVisibility(View.GONE);
//        tv_coyp_txt.setText(nickname);
//        tv_coyp_txt.setVisibility(View.INVISIBLE);
//        Log.i("debbug", "nicknamesize=" + tv_coyp_txt.getMeasuredWidth());

        int[] location = new int[2];

        view.getLocationOnScreen(location);

        int height = view.getMeasuredHeight();
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);

        int statusBarHeight = frame.top;


        Paint paint = new Paint();
        paint.setTextSize(tv_coyp_txt.getTextSize());

        float strWidth = paint.measureText(nickname);

//        Log.i("debbug", "nicknamesize=" + strWidth);
//
//        Log.i("debbug", "getMeasuredHeight =" + height + " location[1] " +location[1] + "sh "+ statusBarHeight + " Math.abs( location[1]  + height / 2) "+ Math.abs( location[1]  -statusBarHeight + height / 2) );


        int baseHeight = (DensityUtil.dip2px(getmContext(), 79f) + statusBarHeight);

        if (location[1] <= baseHeight) {
            popupWindowEva.showAtLocation(view, Gravity.NO_GRAVITY, (int) (location[0] + strWidth), Math.abs(baseHeight));
        } else {
            popupWindowEva.showAtLocation(view, Gravity.NO_GRAVITY, (int) (location[0] + strWidth), Math.abs(location[1] - statusBarHeight + height / 2));
        }


//        view.invalidate();
//        popupWindowEva.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                view.invalidate();
//            }
//        });
//        popupWindowEva.showAtLocation(view, Gravity.NO_GRAVITY, (int) (location[0] + strWidth), location[1] - view.getHeight() / 2);


    }


    private ArrayList<String> imageuris;

    public void changeCurrent(int lifeCircleId, int postion) {

        imageuris = new ArrayList<String>();
        currentlifePhotos.clear();
        currentlifePhotos.addAll(Arrays.asList(map.get(lifeCircleId).getPhotoes().split(",")));
        for (int i = 0; i < currentlifePhotos.size(); i++) {
            imageuris.add(currentlifePhotos.get(i));
        }
        if (currentlifePhotos.size() <= postion) return;
        Intent intent = new Intent(FriendZoneIndexActivity.this, ShowBigImageViewPager.class);
        intent.putExtra("images", imageuris);
        intent.putExtra("position", postion);
        startActivity(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (popupWindow != null) {
            popupWindow.dismiss();
        }

//        PreferencesUtil.saveLifeCircleCountTime(this,(new Date().getTime()/1000)+"");

//        PreferencesUtil.saveLifeCircleCountTime(XjApplication.getInstance(), "" + System.currentTimeMillis() / 1000);
    }


    public void showKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

//        inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//
//
//        inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
                return true;
            } else {
                if (getIntent().getBooleanExtra(Config.INTENT_BACKMAIN, false)) {
                    startActivity(new Intent(this, MainActivity.class));
                }
                finish();
                return true;
            }
        }


        return super.onKeyDown(keyCode, event);
    }

    private void getData() {
        if (pageIndex == 0) {
            List<LifeCircleBean> lifeCircleBeans = PreferencesUtil.getCircleList(this);
            if (lifeCircleBeans.isEmpty()) {

            } else {//显示缓存的
                map.clear();
                circleBeanList.clear();
                circleBeanList.addAll(lifeCircleBeans);
                for (int i = 0; i < circleBeanList.size(); i++) {
                    map.put(circleBeanList.get(i).getLifeCircleId(), circleBeanList.get(i));
                }
                lifeMessageAdapter.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
                lv_zone.setSelection(0);
            }
            pageIndex = 1;
        }
        getCircleLifeList();

    }

    @Override
    public void onClick(View v) {
        userInfoDetailBean = PreferencesUtil.getLoginInfo(this);
        switch (v.getId()) {
            case R.id.ll_character_llay:
                startActivity(new Intent(this, LastWeekRPValueTopListActivity.class));
//                startActivity(new Intent(this, RPValueTopListActivity.class));
                break;
            case R.id.iv_search:

//                startActivity(new Intent(this,LifeSearchActivity.class));TODO
                startActivity(new Intent(this, SearchLifeCircle.class));
                break;
            case R.id.iv_right_text:
                if (userInfoDetailBean == null) {
                    startActivityForResult(new Intent(this, RegisterLoginActivity.class), 0);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(this, NewRecoreActivity.class);
                    startActivityForResult(intent, REQUEST_NEW_RECORE);
                }
                break;
            case R.id.iv_back:
                if (popupWindow != null) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
                if (getIntent().getBooleanExtra(Config.INTENT_BACKMAIN, false)) {
                    startActivity(new Intent(this, MainActivity.class));
                }
                finish();
//                shareShow("","");
//                ShareProvider.shareFragment(FriendZoneIndexActivity.this);
                break;
            case R.id.iv_avatar:
                v.setClickable(false);
                if (userInfoDetailBean == null) {
                    startActivityForResult(new Intent(this, RegisterLoginActivity.class), 0);
                } else {
                    Intent intent = new Intent(this, MyLifeCircleActivity.class);
                    intent.putExtra(Config.Emobid, userInfoDetailBean.getEmobId());
                    startActivity(intent);
                }
                v.setClickable(true);
                break;
            case R.id.btn_send:
                final String content = editText.getText().toString();
                if (content == null || TextUtils.isEmpty(content)) {
                    showToast("请输入评论内容");
                    return;
                }
                if (currentEvent != null) {
                    mLdDialog.show();
                    FriendZoneUtil.eva(getmContext(), userInfoDetailBean.getCommunityId(),  currentEvent.getFrom(), userInfoDetailBean.getEmobId(),content, currentEvent.getCircleLifeId(), new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            mLdDialog.dismiss();
                            switch (msg.what) {
                                case Config.NETERROR:
                                    showNetErrorToast();
                                    break;
                                case Config.TASKCOMPLETE:
                                    showToast("评论成功");
                                    List<LifeCircleDetail> list = map.get(currentEvent.getCircleLifeId()).getLifeCircleDetails();
                                    if (list == null || list.size() <= 0) {
                                        list = new ArrayList<LifeCircleDetail>();
                                        map.get(currentEvent.getCircleLifeId()).setLifeCircleDetails(list);
                                    }
                                    int time = (int) (System.currentTimeMillis() / 1000);
//                                    Log.d("FriendZoneIndexActivity","arg1: "+ msg.arg1+" userInfoDetailBean.getEmobId() "+ userInfoDetailBean.getEmobId());
//                                    Log.d("FriendZoneIndexActivity"," currentEvent.getFrom(): "+ currentEvent.getFrom()+"  userInfoDetailBean.getNickname() "+  userInfoDetailBean.getNickname());
//                                    Log.d("FriendZoneIndexActivity","currentEvent.getFromNike(): "+ currentEvent.getFromNike()+" userInfoDetailBean.getEmobId() "+ userInfoDetailBean.getEmobId());
                                    list.add(new LifeCircleDetail(msg.arg1, userInfoDetailBean.getEmobId(),
                                            currentEvent.getFrom(),
                                            userInfoDetailBean.getNickname(),
                                            currentEvent.getFromNike(), 0,
                                            time, time,
                                            currentEvent.getCircleLifeId(),
                                            content));
                                    lifeMessageAdapter.notifyDataSetChanged();
                                    break;
                                case Config.TASKERROR:
                                    showToast("评论失败");
                                    break;
                            }
                        }
                    });
                }
                editText.getText().clear();
                popupWindow.dismiss();
                break;
            case R.id.btn_charter:
                List<LifeCircleDetail> list = map.get(currentEvent.getCircleLifeId()).getLifeCircleDetails();
                if (currentEvent.getCircleLifeDetialId() == 0) {
                    zambia(map.get(currentEvent.getCircleLifeId()));
                    return;
                }
                for (int i = 0; i < list.size(); i++) {
                    if (currentEvent.getCircleLifeDetialId() == list.get(i).getLifeCircleDetailId()) {
                        zambia(list.get(i));
                    }
                }

                break;
            default:
        }
    }

    public void onEvent(EvaEvent event) {
        if (userInfoDetailBean != null) {
            if (event.getFrom() == null && event.getFromNike() == null) {
                Log.i("debbug", "debbugnull");
                return;
            }

            if (event.getCircleLifeDetialId() == 0) {
                btnCharter.setVisibility(View.GONE);
            } else {
                btnCharter.setVisibility(View.GONE);
            }
            editText.setHint("回复" + event.getFromNike() + ":");
            currentEvent = event;

            if (event.getView() != null) {
                showPopupWindowEva(event.getView(), event.getFromNike());
                return;
            }
            popupWindow.showAtLocation(root, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            if (ll_face_container != null) {
                send_img_checkbox.setChecked(false);
                ll_face_container.setVisibility(View.GONE);
                showKeyBoard();
            }

        } else {
            Intent intent = new Intent(this, RegisterLoginActivity.class);
            startActivityForResult(intent, Config.TASKCOMPLETE);

        }
    }


    EvaEvent currentEvent;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_NEW_RECORE) {
                pageIndex = 0;
                getData();
                return;
            }
            PreferencesUtil.saveUnReadCircleCount(this, 0);
            userInfoDetailBean = PreferencesUtil.getLoginInfo(this);
//            ImageLoader.getInstance().displayImage(userInfoDetailBean.getAvatar(),avatar);
            if (userInfoDetailBean != null) {
                praiseNotifies.addAll(PreferencesUtil.getNewPraise(this, userInfoDetailBean.getEmobId()));
                getCircleTipsList(userInfoDetailBean.getEmobId());
            }
            initView();
//            getData();
        }
        UMSsoHandler ssoHandler = ShareProvider.mController.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        popupWindowEva.dismiss();
    }


    interface CircleLifeListService {
        ///api/v1/communities/{communityId}/circles?q={emobId}&pageNum=1&pageSize=10

        //        @GET("/api/v1/communities/{communityId}/circles")
//        @GET("/api/v2/communities/{communityId}/circles")
//        void getList(@Path("communityId") int communityId, @QueryMap HashMap<String, Object> option, Callback<CircleListRespone> cb);
//        @GET("/api/v2/communities/{communityId}/circles")
        ///api/v3/lifeCircles?communityId={小区ID}&emobId={用户环信ID}&page={页码}&limit={页面大小}
        @GET("/api/v3/lifeCircles")
        /// v3 2013/03/04
        void getList(@QueryMap HashMap<String, String> option, Callback<CommonRespBean<CircleListRespone>> cb);
    }

    public void getCircleLifeList() {

        HashMap<String, String> querymap = new HashMap<>();
        querymap.put("emobId", PreferencesUtil.getLogin(this) ? PreferencesUtil.getLoginInfo(this).getEmobId() : PreferencesUtil.getTourist(this));
        querymap.put("communityId", "" + PreferencesUtil.getCommityId(this));
        querymap.put("page", "" + pageIndex);
        querymap.put("limit", "" + 10);

        CircleLifeListService service = RetrofitFactory.getInstance().create(getmContext(), querymap, CircleLifeListService.class);
        Callback<CommonRespBean<CircleListRespone>> callback = new Callback<CommonRespBean<CircleListRespone>>() {
            @Override
            public void success(CommonRespBean<CircleListRespone> respone, Response response) {
                if (respone != null && "yes".equals(respone.getStatus()) && respone.getData() != null) {
                    List<LifeCircleBean> pageData = respone.getData().getData();
                    int currentPagerCount = pageData.size();
                    if (pageIndex > 1) {
                        if (currentPagerCount < LIMIT_NUM) {
                            showNoMoreToast();
                        }
                    }
                    if (pageIndex == 1) {
                        circleBeanList.clear();
                        map.clear();
                        PreferencesUtil.saveCircleList(FriendZoneIndexActivity.this, pageData);
                        String time = "" + System.currentTimeMillis() / 1000;
//                        Log.i("debbug","" + time);
                        PreferencesUtil.saveLifeCircleCountTime(XjApplication.getInstance(), "" + time);//保存indexfragment页面刷新时间
                    }
                    circleBeanList.addAll(pageData);
                    for (int i = 0; i < currentPagerCount; i++) {
                        map.put(pageData.get(i).getLifeCircleId(), pageData.get(i));
                    }
                    lifeMessageAdapter.notifyDataSetChanged();

//                 NewPraiseNotify praiseNotify=new NewPraiseNotify();
//                    praiseNotify.setAvatar4Show("http://baidu.logo");
//                    praiseNotify.setContent4Show("测试");
//                    praiseNotifies=  PreferencesUtil.getNewPraise(FriendZoneIndexActivity.this);
//                    Log.i("onion","praiseNotifies"+praiseNotifies.size());
                    adapter.notifyDataSetChanged();
                } else {
                    showNetErrorToast();
                }
                if (pageIndex == 1) {
                    pull_to_refreshlayout.refreshFinish(true);
                } else {
                    pull_to_refreshlayout.loadMoreFinish(true);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                showNetErrorToast();
                if (pageIndex == 1) {
                    pull_to_refreshlayout.refreshFinish(true);
                } else {
                    pull_to_refreshlayout.loadMoreFinish(true);
                }
                error.printStackTrace();
            }
        };
//        communityId={小区ID}&emobId={用户环信ID}&page={页码}&limit={页面大小}
        service.getList(querymap, callback);
    }


    interface CircleTipsListService {
        ///api/v1/communities/{communityId}/circles/{emobId}/tips?time=0

//        @GET("/api/v1/communities/{communityId}/circles/{emobId}/tips")
//        void getList(@Path("communityId") int communityId, @Path("emobId") String emobId, @QueryMap HashMap<String, Integer> option, Callback<NewPraiseResponse> cb);

        /////api/v3/lifeCircles/tips?communityId={小区ID}&emobId={用户环信ID}&time={上次获取到的最新消息的产生时间}

        //        @GET("/api/v1/communities/{communityId}/circles/{emobId}/tips")
        @GET("/api/v3/lifeCircles/tips")
        ///v3 2016/03/04
        void getList(@QueryMap HashMap<String, String> option, Callback<CommonRespBean<NewPraiseResponse>> cb);
    }

    public void getCircleTipsList(final String emobid) {
        final int time = PreferencesUtil.getNewCircleTime(this);

        HashMap<String, String> option = new HashMap<>();
        option.put("time", "" + time);
        option.put("communityId", "" + PreferencesUtil.getCommityId(this));
        option.put("emobId", emobid);
        ///api/v3/lifeCircles/tips?communityId={小区ID}&emobId={用户环信ID}&time={上次获取到的最新消息的产生时间}
//        option.put("time",1434335978);

        CircleTipsListService service = RetrofitFactory.getInstance().create(getmContext(), option, CircleTipsListService.class);
        Callback<CommonRespBean<NewPraiseResponse>> callback = new Callback<CommonRespBean<NewPraiseResponse>>() {
            @Override
            public void success(CommonRespBean<NewPraiseResponse> respone, Response response) {
                if ("yes".equals(respone.getStatus())) {
                    PreferencesUtil.saveNewCircleTime(FriendZoneIndexActivity.this, respone.getData().getTime());
                    for (int i = 0; i < respone.getData().getList().size(); i++) {
                        NewPraiseNotify newPraiseNotify = respone.getData().getList().get(i);
                        newPraiseNotify.setUsers(newPraiseNotify.getUsers());
                    }
                    praiseNotifies.addAll(respone.getData().getList());
                    PreferencesUtil.saveNewPraise(FriendZoneIndexActivity.this, emobid, praiseNotifies);
                    lifeMessageAdapter.notifyDataSetChanged();
                    adapter.notifyDataSetChanged();
                    if (respone.getData().getCharacterValues() > 0) {

                        ll_character_llay.setVisibility(View.VISIBLE);
                        ll_none_eva.setVisibility(View.GONE);

                        tv_content.setText("打败了本小区" + StrUtils.getPrecent(respone.getData().getCharacterPercent()) + "%的居民！");
//                        tv_content.setText("打败了" + StrUtils.getPrecent(respone.getInfo().getCharacterPercent()) + "%的" + PreferencesUtil.getCommityName(FriendZoneIndexActivity.this) + "居民！");
                        tv_person_value.setText("" + respone.getData().getCharacterValues());

//                        tv_content.setText("打败了100.00%的本小区居民！");
//                        tv_person_value.setText("" +1000);
                    } else {
                        ll_character_llay.setVisibility(View.GONE);
                        ll_none_eva.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        };
        service.getList(option, callback);
    }

    private void zambia(final LifeCircleDetail circleDetail) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(popupWindow.getContentView().getWindowToken(), 0);
        if (!CommonUtils.isNetWorkConnected(this)) {
            showNetErrorToast();
            return;
        }
        ZambiaCache zambiaCache = new Select().from(ZambiaCache.class).where("emobid = ? and emobidhost = ?", circleDetail.getEmobIdFrom(), userInfoDetailBean.getEmobId()).executeSingle();
        if (zambiaCache != null) {
            if (!StrUtils.isInDay(zambiaCache.getZambiatime())) {
                zambiaCache.setZambiatime((int) (new Date().getTime() / 1000));
                circleDetail.setPraiseSum(circleDetail.getPraiseSum() + 1);
                lifeMessageAdapter.notifyDataSetChanged();
                zambiaCache.save();
            } else {//本地检验未通过
                Toast.makeText(this, "同一天，同一人只能赞一次", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            circleDetail.setPraiseSum(circleDetail.getPraiseSum() + 1);
            lifeMessageAdapter.notifyDataSetChanged();
            zambiaCache = new ZambiaCache();
            zambiaCache.setEmobid(circleDetail.getEmobIdFrom());
            zambiaCache.setEmobidhost(userInfoDetailBean.getEmobId());
            zambiaCache.setZambiatime((int) (new Date().getTime() / 1000));
            zambiaCache.save();
        }
//        mLdDialog.show();
        FriendZoneUtil.zambia(circleDetail.getEmobIdFrom(), circleDetail.getLifeCircleId(), circleDetail.getLifeCircleDetailId(), 2, FriendZoneIndexActivity.this, new Handler() {
            @Override
            public void handleMessage(Message msg) {
//                mLdDialog.dismiss();
                switch (msg.what) {
                    case Config.TASKCOMPLETE:
                        Toast.makeText(FriendZoneIndexActivity.this, getString(R.string.praise), Toast.LENGTH_LONG).show();
                        break;
                    case Config.NETERROR:
                        showNetErrorToast();
                        new Delete().from(ZambiaCache.class).where("emobid = ? and emobidhost = ?", circleDetail.getEmobIdFrom(), userInfoDetailBean.getEmobId()).execute();
                        break;
                    case Config.TASKERROR:
                        showToast("同一天，同一人只能赞一次");
                        break;
                }

            }
        });
    }

    private void zambia(final LifeCircleBean circleBean) {
        mLdDialog.show();
        FriendZoneUtil.zambia(circleBean.getEmobId(), 0, 1, this, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mLdDialog.dismiss();
                switch (msg.what) {
                    case Config.TASKCOMPLETE:
                        Toast.makeText(FriendZoneIndexActivity.this, getString(R.string.praise), Toast.LENGTH_LONG).show();
                        circleBean.setPraiseSum(circleBean.getPraiseSum() + 1);
                        lifeMessageAdapter.notifyDataSetChanged();
                        break;
                    case Config.NETERROR:
                        showNetErrorToast();
                        break;
                    case Config.TASKERROR:
                        showToast("同一天，同一人只能赞一次");
                        break;
                }
            }
        });
    }

    private String getVersionName() throws Exception {
// 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
// getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        String version = packInfo.versionName;
        return version;
    }

}
