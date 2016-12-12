package xj.property.activity.vote;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
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
import retrofit.http.Path;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
import xj.property.adapter.ExpressionAdapter;
import xj.property.adapter.ExpressionPagerAdapter;
import xj.property.beans.CooperationContentRespBean;
import xj.property.beans.IWantVoteReqBean;
import xj.property.beans.IWantVoteRespBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.utils.NumTextConvert;
import xj.property.utils.SmileUtils;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.widget.ExpandGridView;
import xj.property.widget.com.viewpagerindicator.CirclePageIndicator;

/**
 * 发起投票
 */
public class IwantVoteActivity extends HXBaseActivity {

    private static final int MAX_VOTE_ITEM_SIZE = 50;

    private UserInfoDetailBean bean;

    private TextView tv_title;

    /// 发布按钮
    private Button iwant_provider_submit_btn;

    private InputMethodManager imm;
    private LinearLayout bar_bottom;
    private LinearLayout ll_face_container;
    private ViewPager vPager;
    private CirclePageIndicator vpager_indicator;

    private List<String> reslist;
    private CheckBox send_img_checkbox;
    private Handler handler = new Handler();

    private TextView tv_right_text;
    //// 选项List
    private LinearLayout iwant_vote_citem_llay;
    //// 投票内容
    private EditText iwant_vote_content_et;
    /// 添加投票选项
    private Button iwant_vote_add_citem;

    //// vote_items
    private Map<Integer, EditText> vote_items = new HashMap<Integer, EditText>();

    private LayoutInflater layoutInflater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iwant_vote);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        layoutInflater = LayoutInflater.from(this);
        bean = PreferencesUtil.getLoginInfo(this);
        if (bean != null) {
            initView();
        } else {
            Intent intent = new Intent(this, RegisterLoginActivity.class);
            startActivity(intent);
        }
    }


    private void initView() {
        initTitle();

        iwant_provider_submit_btn = (Button) this.findViewById(R.id.iwant_provider_submit_btn);
        iwant_provider_submit_btn.setOnClickListener(this);

        iwant_vote_add_citem = (Button) this.findViewById(R.id.iwant_vote_add_citem);
        iwant_vote_add_citem.setOnClickListener(this);

        iwant_vote_content_et = (EditText) this.findViewById(R.id.iwant_vote_content_et);
        iwant_vote_content_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    bar_bottom.setVisibility(View.VISIBLE);
                    ll_face_container.setVisibility(View.GONE);

                } else {
                    bar_bottom.setVisibility(View.GONE);
                    ll_face_container.setVisibility(View.GONE);
                }

            }
        });

        iwant_vote_content_et.setOnTouchListener(new View.OnTouchListener() {
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


        iwant_vote_citem_llay = (LinearLayout) this.findViewById(R.id.iwant_vote_citem_llay);
        iwant_vote_citem_llay.removeAllViews();/// 清除所有选项

        /// 默认选项一
        addVoteItem();
        /// 默认选项二
        addVoteItem();


        bar_bottom = (LinearLayout) findViewById(R.id.bar_bottom);

        ll_face_container = (LinearLayout) findViewById(R.id.ll_face_container);

        vPager = (ViewPager) findViewById(R.id.vPager);

        vpager_indicator = (CirclePageIndicator) findViewById(R.id.vpager_indicator);

        // 表情list 目前表情数量为99个
        reslist = getExpressionRes(99);

        // 初始化表情viewpager
        List<View> views = new ArrayList<View>();
        View gv1 = getGridChildView(1, iwant_vote_content_et);
        View gv2 = getGridChildView(2, iwant_vote_content_et);
        View gv3 = getGridChildView(3, iwant_vote_content_et);
        View gv4 = getGridChildView(4, iwant_vote_content_et);
        View gv5 = getGridChildView(5, iwant_vote_content_et);

        views.add(gv1);
        views.add(gv2);
        views.add(gv3);
        views.add(gv4);
        views.add(gv5);

        vPager.setAdapter(new ExpressionPagerAdapter(views));
        vpager_indicator.setViewPager(vPager);

        send_img_checkbox = (CheckBox) findViewById(R.id.send_img_checkbox);
        send_img_checkbox.setOnCheckedChangeListener(new onMyCheckedChangeListener(iwant_vote_content_et));

    }


    /**
     * 添加投票选项至LinearLayout布局
     */
    private void addVoteItem() {
        if (checkIsHasDuplicateVoteItem()) {
            showToast("投票选项重复");
            return;
        }
        //// 0---49
        int current_size = vote_items.size();
        if (current_size >= MAX_VOTE_ITEM_SIZE) {
            showToast("最多添加" + MAX_VOTE_ITEM_SIZE + "条投票选项");
            return;
        }

        // 0 1, 1,2, 2,3, 3/4
        int current_location = current_size + 1;

        View inflaterdView = layoutInflater.inflate(R.layout.common_vote_content_item, null);
        TextView iwant_vote_item_title_tv = (TextView) inflaterdView.findViewById(R.id.iwant_vote_item_title_tv);


        iwant_vote_item_title_tv.setText("选项" + NumTextConvert.convertNumToText(current_location));

        EditText iwant_vote_item_content_et = (EditText) inflaterdView.findViewById(R.id.iwant_vote_item_content_et);
        iwant_vote_item_content_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        iwant_vote_citem_llay.addView(inflaterdView);
        vote_items.put(current_location, iwant_vote_item_content_et);

    }


    /**
     * 判断是否有空值
     * 如果选项中有空选项则不允许添加新选项.
     *
     * @return
     */
    public boolean isHasEmpty() {

        for (Map.Entry<Integer, EditText> entry : vote_items.entrySet()) {
            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
            String vote_item_input = entry.getValue().getText().toString().trim();
            if (TextUtils.isEmpty(vote_item_input)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 点击发布按钮去除误点空内容选项
     *
     */
    public void removeAllEmptyItem(){
//       Iterator<Map.Entry<Integer, EditText>> it = vote_items.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry<Integer, EditText> entry = it.next();
//            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
//
//            if(entry.getValue()==null||TextUtils.isEmpty(entry.getValue().getText())||TextUtils.isEmpty(entry.getValue().getText().toString())){
//                System.out.println("remove key= " + entry.getKey() + " and value= " + entry.getValue());
////                vote_items.remove(entry.getKey());
//                it.remove();
//            }
//
//        }

        List<Integer> emptyList = new ArrayList<>();

        for (Map.Entry<Integer, EditText> entry : vote_items.entrySet()) {
            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());

            if(entry.getValue()==null||TextUtils.isEmpty(entry.getValue().getText())||TextUtils.isEmpty(entry.getValue().getText().toString())){
                System.out.println("remove key= " + entry.getKey() + " and value= " + entry.getValue());
//                vote_items.remove(entry.getKey());
                emptyList.add(entry.getKey());
            }
        }
        if(emptyList!=null&&emptyList.size()>0){
            for (int i : emptyList){
                if(vote_items!=null){
                    vote_items.remove(i);
                }
            }
        }
    }





    /**
     * 检测用户的输入选项是否合法
     *
     * @return
     */
    public boolean checkIsRightVoteItems() {
        int not_empty_count = 0;
        for (Map.Entry<Integer, EditText> entry : vote_items.entrySet()) {
            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
            String vote_item_input = entry.getValue().getText().toString().trim();
            if (!TextUtils.isEmpty(vote_item_input)) {
                not_empty_count++;
                if (not_empty_count >= 2) {
                    System.out.println("not_empty_count= " + not_empty_count);
                    /// 投票选项个数>=2 , 合法
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 投票项是否含有重复的选项
     *
     * @return
     */
    public boolean checkIsHasDuplicateVoteItem() {

        List<String> container = new ArrayList<String>();
        for (Map.Entry<Integer, EditText> entry : vote_items.entrySet()) {
            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
            String vote_item_input = entry.getValue().getText().toString().trim();
            if (container.contains(vote_item_input)) {
                return true;
            } else {
                container.add(vote_item_input);
            }
        }
        return false;
    }


    /**
     * 获取表情的gridview的子view
     *
     * @param i
     * @return
     */
    private View getGridChildView(int i, final EditText iwant_provider_content_et) {
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
                        editableText.insert(selectionStart,smiledText);

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

        private final EditText iwant_provider_content_et;

        public onMyCheckedChangeListener(EditText iwant_provider_content_et) {
            this.iwant_provider_content_et = iwant_provider_content_et;
        }

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
                onBackPressed();
            }
        });
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setText("发起投票");
        tv_right_text = (TextView) this.findViewById(R.id.tv_right_text);
        tv_right_text.setText("发布");
        tv_right_text.setVisibility(View.VISIBLE);
        tv_right_text.setTextSize(14f);
        tv_right_text.setTextColor(getResources().getColor(R.color.sys_darker_font_theme_text_color));
        tv_right_text.setOnClickListener(this);

    }

    interface MspCardListService {

//        @POST("/api/v1/communities/{communityId}/vote/newVote")
//        void getNewVote(@Header("signature") String signature, @Body IWantVoteReqBean qt, @Path("communityId") int communityId, Callback<IWantVoteRespBean> cb);
//
//        @POST("/api/v1/communities/{communityId}/vote/newVote")

        @POST("/api/v3/votes")
        void getNewVote(@Body IWantVoteReqBean qt, Callback<CommonRespBean<String>> cb);


        @GET("/api/v1/communities/{communityId}/cooperations/edit/{cooperationId}")
        void getCooperationContent(@Path("communityId") int communityId, @Path("cooperationId") String cooperationId, Callback<CooperationContentRespBean> cb);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
         if(imm!=null){
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                     InputMethodManager.HIDE_NOT_ALWAYS);
         }
    }



    /// 获取修改信息
//    public void getCooperationContentM(final int iProvideredCooperationId) {
//
//        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
//        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
//        MspCardListService service = restAdapter.create(MspCardListService.class);
//        Callback<CooperationContentRespBean> callback = new Callback<CooperationContentRespBean>() {
//            @Override
//            public void success(CooperationContentRespBean respone, Response response) {
//                if (respone != null && "yes".equals(respone.getStatus())) {
//
//                    if (iwant_provider_title_et != null) {
//                        iwant_provider_title_et.setText(respone.getInfo().getTitle());
//                    }
//
//                    if (iwant_provider_content_et != null) {
//
//                        Spannable spanAll = SmileUtils.getSmiledText(getmContext(), respone.getInfo().getContent());
//                        iwant_provider_content_et.setText(spanAll, TextView.BufferType.SPANNABLE);
//                    }
//
//                } else if (respone != null && "no".equals(respone.getStatus())) {
//
//                    showToast(respone.getMessage());
//
//                }
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                error.printStackTrace();
//            }
//        };
//        service.getCooperationContent(PreferencesUtil.getCommityId(this), "" + iProvideredCooperationId, callback);
//    }


    private void getNewVote(String title, List<IWantVoteReqBean.VoteOptionsListEntity> voteOptionsList) {

        IWantVoteReqBean quaryToken = new IWantVoteReqBean();
        quaryToken.setEmobId(bean.getEmobId());
        quaryToken.setCommunityId(PreferencesUtil.getCommityId(getmContext()));
        quaryToken.setVoteTitle(title);
        quaryToken.setOptions(voteOptionsList);
        MspCardListService service = RetrofitFactory.getInstance().create(getmContext(),quaryToken,MspCardListService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> respBean, Response response) {
                if (respBean != null && TextUtils.equals("yes", respBean.getStatus())) {
                    Toast.makeText(getmContext(), "发布成功", Toast.LENGTH_SHORT).show();
                    if(imm!=null && iwant_vote_content_et!=null){
                        imm.hideSoftInputFromWindow(iwant_vote_content_et.getWindowToken(), 0);
                    }
                    setResult(RESULT_OK);
                    finish();
                } else if (respBean != null && TextUtils.equals("no", respBean.getStatus())) {
                    showDataErrorToast(respBean.getMessage());
                } else {
                    showDataErrorToast();
                }
                if (mLdDialog != null) {
                    mLdDialog.dismiss();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                showNetErrorToast();
                if (mLdDialog != null) {
                    mLdDialog.dismiss();
                }

            }
        };

        if (mLdDialog != null) {
            mLdDialog.show();
        }
        service.getNewVote(quaryToken, callback);
    }

    /**
     * 获取所有的投票选项
     *
     * @return
     */
    public List<IWantVoteReqBean.VoteOptionsListEntity> getAllItemValues() {
        List<IWantVoteReqBean.VoteOptionsListEntity> voteOptionsList = new ArrayList<>();
        for (Map.Entry<Integer, EditText> entry : vote_items.entrySet()) {
            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
            String vote_item_input = entry.getValue().getText().toString().trim();
            System.out.println("vote_item_input= " + vote_item_input);
            IWantVoteReqBean.VoteOptionsListEntity item = new IWantVoteReqBean.VoteOptionsListEntity();
//            item.setSort(entry.getKey());
//            item.setVoteOptionsContent(vote_item_input);
            item.setContent(vote_item_input);
            voteOptionsList.add(item);
        }
        return voteOptionsList;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right_text:
            case R.id.iwant_provider_submit_btn:
                //// 合法

                String content = iwant_vote_content_et.getText().toString().trim();

                if (TextUtils.isEmpty(content)) {
                    showToast("请输入投票内容");
                    return;
                }
                if (content.length() > 140) {
                    showToast("请输入140个字以内投票内容");
                    return;
                }

                if (checkIsHasDuplicateVoteItem()) {
                    showToast("投票选项重复");
                    return;
                }

                //// 移除所有空选项
                removeAllEmptyItem();

                /// 判断投票选项是否合法
                if (checkIsRightVoteItems()) {
                    List<IWantVoteReqBean.VoteOptionsListEntity> voteOptionsList = getAllItemValues();
                    //TODO   测试发起接口可以使用
                    getNewVote(content, voteOptionsList);

                } else {
                    showToast("投票至少需要两个选项");
                    return;
                }
                break;

            case R.id.iwant_vote_add_citem:

                /// 如果前两个为空不允许添加选项三
                if (isHasEmpty()) {
                    showToast("选项不能为空");
                    return;
                }
                addVoteItem();

                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLdDialog != null) {
            mLdDialog.dismiss();
        }
//        if(imm!=null){
//            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
//                    InputMethodManager.HIDE_NOT_ALWAYS);
//        }
    }

}
