package xj.property.activity.cooperation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
import xj.property.adapter.ExpressionAdapter;
import xj.property.adapter.ExpressionPagerAdapter;
import xj.property.beans.CooperationContentRespBean;
import xj.property.beans.IWantProviderReqBean;
import xj.property.beans.IWantProviderRespBean;
import xj.property.beans.NeighborEditV3Bean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.SmileUtils;
import xj.property.utils.other.PreferencesUtil;
import xj.property.widget.ExpandGridView;
import xj.property.widget.com.viewpagerindicator.CirclePageIndicator;

/**
 * 发起邻居帮
 */
public class IwantProviderActivity extends HXBaseActivity {

    private LinearLayout ll_errorpage;
    private LinearLayout ll_neterror;
    private LinearLayout ll_nomessage;
    private ImageView iv_nomessage_image;
    private TextView tv_nomessage;

    private UserInfoDetailBean bean;

    private TextView tv_title;

    ////标题
    private EditText iwant_provider_title_et;
    /// 内容
    private EditText iwant_provider_content_et;
    /// 发布按钮
    private Button iwant_provider_submit_btn;
    /// CooperationID 默认为发布状态 -1
    private int iProvideredCooperationId = -1;
    private InputMethodManager imm;
    private LinearLayout bar_bottom;
    private LinearLayout ll_face_container;
    private ViewPager vPager;
    private CirclePageIndicator vpager_indicator;
    private List<String> reslist;
    private CheckBox send_img_checkbox;
    private Handler handler = new Handler();
    private TextView tv_right_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iwant_provider);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        bean = PreferencesUtil.getLoginInfo(this);
        if (bean != null) {
            initView();

            initData();
        } else {
            Intent intent = new Intent(this, RegisterLoginActivity.class);
            startActivity(intent);
        }

    }

    private void initData() {
        iProvideredCooperationId = getIntent().getIntExtra("iProvideredCooperationId", -1);
        if (iProvideredCooperationId != -1) {
            getCooperationContentM(iProvideredCooperationId);
        }/// 如果是修改状态
    }

    private void initView() {
        initTitle();

        iwant_provider_submit_btn = (Button) this.findViewById(R.id.iwant_provider_submit_btn);
        iwant_provider_submit_btn.setOnClickListener(this);

        iwant_provider_title_et = (EditText) this.findViewById(R.id.iwant_provider_title_et);
        iwant_provider_title_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    bar_bottom.setVisibility(View.GONE);
                    ll_face_container.setVisibility(View.GONE);
                } else {
                    bar_bottom.setVisibility(View.GONE);
                    ll_face_container.setVisibility(View.GONE);
                }

            }
        });

        iwant_provider_content_et = (EditText) this.findViewById(R.id.iwant_provider_content_et);

        iwant_provider_content_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
//                    boolean bool = imm.showSoftInput(v,InputMethodManager.SHOW_IMPLICIT);
//                    if (bool) {
//                        send_img_checkbox.setButtonDrawable(getResources().getDrawable(R.drawable.chatting_biaoqing_btn_normal));
                    bar_bottom.setVisibility(View.VISIBLE);
                    ll_face_container.setVisibility(View.GONE);
//                    }


                } else {

                    bar_bottom.setVisibility(View.GONE);
                    ll_face_container.setVisibility(View.GONE);
//                    closeBoard();
                }

            }
        });

        iwant_provider_content_et.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                boolean bool = imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
                if (bool) {
                    send_img_checkbox.setButtonDrawable(getResources().getDrawable(R.drawable.chatting_biaoqing_btn_normal));
                    bar_bottom.setVisibility(View.VISIBLE);
                    ll_face_container.setVisibility(View.GONE);

                }

                return false;
            }
        });

        bar_bottom = (LinearLayout) findViewById(R.id.bar_bottom);

        ll_face_container = (LinearLayout) findViewById(R.id.ll_face_container);

        vPager = (ViewPager) findViewById(R.id.vPager);

        vpager_indicator = (CirclePageIndicator) findViewById(R.id.vpager_indicator);

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

        send_img_checkbox = (CheckBox) findViewById(R.id.send_img_checkbox);
        send_img_checkbox.setOnCheckedChangeListener(new onMyCheckedChangeListener());


        ll_errorpage = (LinearLayout) findViewById(R.id.ll_errorpage);
        ll_neterror = (LinearLayout) findViewById(R.id.ll_neterror);

        ll_neterror.setOnClickListener(this);

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

                        int selectionStart = iwant_provider_content_et.getSelectionStart();// 获取光标的位置
                        Spannable smiledText = SmileUtils.getSmiledText(getmContext(), (String) field.get(null));
                        Editable editableText = iwant_provider_content_et.getEditableText();
                        editableText.insert(selectionStart, smiledText);

//                        iwant_provider_content_et.append(SmileUtils.getSmiledText(getmContext(), (String) field.get(null)));

                    } else {
                        // 删除文字或者表情
                        if (!TextUtils.isEmpty(iwant_provider_content_et.getText())) {

                            int selectionStart = iwant_provider_content_et.getSelectionStart();// 获取光标的位置

                            if (selectionStart > 0) {

                                String body = iwant_provider_content_et.getText().toString();

                                String tempStr = body.substring(0, selectionStart);

                                int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                                if (i != -1) {
                                    /// 截取最后一个表情
                                    CharSequence cs = tempStr.substring(i, selectionStart);

                                    if (SmileUtils.containsKey(cs.toString()))
                                        /// 删除最后一个表情字符串的占位符
                                        iwant_provider_content_et.getEditableText().delete(i, selectionStart);
                                    else
                                        iwant_provider_content_et.getEditableText().delete(selectionStart - 1, selectionStart);
                                } else {
                                    iwant_provider_content_et.getEditableText().delete(selectionStart - 1, selectionStart);
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


    private class onMyCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
//                closeBoard();
                imm.hideSoftInputFromWindow(iwant_provider_content_et.getWindowToken(), 0);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (ll_face_container != null) {
                            ll_face_container.setVisibility(View.VISIBLE);
                            send_img_checkbox.setButtonDrawable(getResources().getDrawable(R.drawable.chatting_biaoqing_btn_enable));
                        }

                    }
                }, 200);

            } else {
                if (ll_face_container != null) {
                    ll_face_container.setVisibility(View.GONE);
                    send_img_checkbox.setButtonDrawable(getResources().getDrawable(R.drawable.chatting_biaoqing_btn_normal));
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        showKeyBoard();
                        imm.showSoftInput(iwant_provider_content_et, InputMethodManager.SHOW_IMPLICIT);

                    }
                }, 200);

            }
        }
    }


    private void initTitle() {

        this.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setText("发起邻居帮");
        tv_right_text = (TextView) this.findViewById(R.id.tv_right_text);
        tv_right_text.setText("发布");
        tv_right_text.setVisibility(View.VISIBLE);
        tv_right_text.setTextSize(14f);
        tv_right_text.setBackground(getResources().getDrawable(R.drawable.neighbor_pubish));
        tv_right_text.setTextColor(getResources().getColor(R.color.sys_green_theme_text_color));
        tv_right_text.setOnClickListener(this);

    }

    interface MspCardListService {
        @PUT("/api/v3/cooperations")
        void getCooperationsV3(@Body IWantProviderReqBean qt, Callback<CommonRespBean<String>> cb);

        @POST("/api/v3/cooperations")
        void addCooperationsV3(@Body IWantProviderReqBean qt, Callback<CommonRespBean<String>> cb);

        /**
         * 获取修改的信息
         *
         * @param cooperationId
         * @param cb
         */
//        /api/v3/cooperations/{邻居帮ID}
        @GET("/api/v3/cooperations/{cooperationId}")
        void getEditCooperationContent(@Path("cooperationId") String cooperationId, Callback<CommonRespBean<NeighborEditV3Bean>> cb);

    }


    /**
     * 获取修改的信息
     *
     * @param iProvideredCooperationId
     */
    public void getCooperationContentM(final int iProvideredCooperationId) {
        MspCardListService service = RetrofitFactory.getInstance().create(getmContext(),MspCardListService.class);
        Callback<CommonRespBean<NeighborEditV3Bean>> callback = new Callback<CommonRespBean<NeighborEditV3Bean>>() {
            @Override
            public void success(CommonRespBean<NeighborEditV3Bean> respone, Response response) {
                if (respone != null && "yes".equals(respone.getStatus())) {

                    if (iwant_provider_title_et != null) {
                        iwant_provider_title_et.setText(respone.getData().getTitle());
                    }

                    if (iwant_provider_content_et != null) {
                        Spannable spanAll = SmileUtils.getSmiledText(getmContext(), respone.getData().getContent());
                        iwant_provider_content_et.setText(spanAll, TextView.BufferType.SPANNABLE);
                    }

                } else if (respone != null && "no".equals(respone.getStatus())) {
                    showToast(respone.getMessage());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        };
        service.getEditCooperationContent(iProvideredCooperationId + "", callback);
    }


    /**
     * 添加邻居帮
     *
     * @param title
     * @param content
     */
    private void getCooperations(String title, String content) {

        IWantProviderReqBean quaryToken = new IWantProviderReqBean();
        quaryToken.setEmobId(bean.getEmobId());
        quaryToken.setTitle(title);
        quaryToken.setCommunityId(Integer.valueOf(String.valueOf(bean.getCommunityId())));
        quaryToken.setContent(content);
        //// 修改邻居帮 v3 2016/03/14
        if (iProvideredCooperationId != -1) {
            quaryToken.setCooperationId(iProvideredCooperationId);
        }

        MspCardListService service = RetrofitFactory.getInstance().create(getmContext(),quaryToken,MspCardListService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> respBean, Response response) {

                if (respBean != null && TextUtils.equals("yes", respBean.getStatus())) {
                    Toast.makeText(getmContext(), "发布成功", Toast.LENGTH_SHORT).show();

                    setResult(RESULT_OK);
                    finish();
                } else if (respBean != null && TextUtils.equals("no", respBean.getStatus())) {
                    Toast.makeText(getmContext(), respBean.getMessage(), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getmContext(), "添加标签失败", Toast.LENGTH_SHORT).show();
                }
                if (mLdDialog != null) {
                    mLdDialog.dismiss();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Toast.makeText(getmContext(), "网络异常", Toast.LENGTH_SHORT).show();

                if (mLdDialog != null) {
                    mLdDialog.dismiss();
                }

            }
        };

        if (mLdDialog != null) {
            mLdDialog.show();
        }
        //// 修改邻居帮 v3 2016/03/14
        if (iProvideredCooperationId != -1) {
            service.getCooperationsV3(quaryToken, callback);
        }else{/// 添加邻居帮
            service.addCooperationsV3(quaryToken, callback);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right_text:
            case R.id.iwant_provider_submit_btn:

                String title = iwant_provider_title_et.getText().toString().trim();

                if (TextUtils.isEmpty(title)) {
                    showToast("请输入邻居帮标题");
                    return;
                }
                if (title.length() > 10) {
                    showToast("请输入10个字以内邻居帮标题");
                    return;
                }
                String content = iwant_provider_content_et.getText().toString().trim();

                if (TextUtils.isEmpty(content)) {
                    showToast("请输入邻居帮内容");
                    return;
                }
                if (content.length() > 140) {
                    showToast("请输入140个字以内邻居帮内容");
                    return;
                }
                getCooperations(title, content);

                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLdDialog != null) {
            mLdDialog.dismiss();
        }
    }

}
