package xj.property.activity.tags;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.activity.HXBaseActivity.MainActivity;
import xj.property.activity.HXBaseActivity.RegisterLoginActivity;
import xj.property.beans.TagsA2BAddReqBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.NetBaseTagUtils;
import xj.property.widget.FilterFlowLayout;

/**
 * 标签管理
 */
public class ActivityAddTagsList extends HXBaseActivity {

    private final int common_choiced_color = R.color.white;

    private final int left_top_color = R.color.sys_label_color_red;
    private final int left_moddle_color = R.color.sys_label_color_pink;
    private final int left_bottom_color = R.color.sys_label_color_blue;

    private final int right_top_color = R.color.sys_label_color_green;
    private final int right_moddle_color = R.color.sys_label_color_yellow;


    private final int right_bottom_color = R.color.sys_label_color_purple;



//    新锐CEO	企业高管
//    创业者	投资
//    财经高管	律师
//    银行高管	翻译
//    金融分析师	人力资源


    private final String[] left_top_labels = {
            "新锐CEO", "企业高管",
            "创业者",  "投资",
            "财经高管", "律师",
            "银行高管", "翻译",
            "金融分析师","人力资源"
    };


//    医院院长	心理咨询
//    医生	宠物医生
//    校长	教授
//    教师	家教
//    职业培训	英语培训
//    营养师	室内设计
//    健身教练	导游
//    公务员	警察

    private final String[] left_moddle_labels = {
            "医院院长", "心理咨询",
            "医生", "宠物医生",
            "校长", "教授",
            "教师", "家教",
            "职业培训","英语培训",
            "营养师",  "室内设计",
            "健身教练","导游",
            "公务员","警察"
    };




//    导演	演员
//    主持人 经纪人	撰稿人
//    自媒体	记者
//    摄影师	模特
//    设计师	空姐
//    作家	音乐人
//    网红	艺术家


    private final String[] left_bottom_labels = {
            "导演", "演员",
            "主持人", "经纪人",
            "撰稿人", "自媒体",
            "记者", "摄影师",
            "模特","设计师",
            "空姐","作家",
            "音乐人","网红",
            "艺术家"

    };


//   电影	拍客
//    唱歌	军迷
//    游戏	书虫
//    LOL	二次元
//    DOTA	桌游
//    瑜伽	足球
//    跑步	篮球
//    羽毛球	乒乓球
//    桌游	游泳
//    网购	乐器
//    舞蹈	垂钓
//    DIY攒机	追剧


    private final String[] right_top_labels = {
            "电影", "拍客",
            "唱歌","军迷",
            "游戏","书虫",
            "LOL", "二次元",
            "DOTA","桌游",
            "瑜伽","足球",
            "跑步","篮球",
            "羽毛球","乒乓球",
            "桌游","游泳",
            "网购","乐器",
            "舞蹈","垂钓",
            "DIY攒机","追剧",
    };


//    自驾	汽车发烧友
//    驴友	烘焙
//    装修	养生
//    美食达人	减肥专家
//    周易	收藏
//    塔罗牌	星座达人
//    时尚	美妆

    private final String[] right_moddle_labels = {
            "自驾", "汽车发烧友",
            "驴友", "烘焙",
            "装修","养生",
            "美食达人","减肥专家",
            "周易","收藏",
            "塔罗牌", "星座达人",
            "时尚","美妆"
    };


//    中国好邻居	热心肠
//    小鲜肉	萌妹子
//    汪星人	喵星人
//    居家小能手	厨神
//    小区百事通	广场舞
//    家有萌娃	精打细算

    private final String[] right_bottom_labels = {
            "中国好邻居", "热心肠",
            "小鲜肉", "萌妹子",
            "汪星人","喵星人",
            "居家小能手","厨神",
            "小区百事通", "广场舞",
            "家有萌娃", "精打细算"
    };


    /// 目前已经选择的标签
    private List<String> choicedLables = new ArrayList<>();

    private UserInfoDetailBean bean;

    /// 添加标签按钮
    private Button mytags_addtags_btn;

    private LayoutInflater layoutInflater;
    //// 左侧
    private FilterFlowLayout mytags_fflay_lefttop;
    private FilterFlowLayout mytags_fflay_leftmoddle;
    private FilterFlowLayout mytags_fflay_leftbottom;
    //// 右侧
    private FilterFlowLayout mytags_fflay_righttop;
    private FilterFlowLayout mytags_fflay_rightmoddle;
    private FilterFlowLayout mytags_fflay_rightbottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtags_list);
        bean = (UserInfoDetailBean) getIntent().getSerializableExtra("userInfoDetailBean");
        layoutInflater = LayoutInflater.from(this);
        if (bean != null) {
            initView();
            loadingViews();
        } else {
            Intent intent = new Intent(this, RegisterLoginActivity.class);
            startActivity(intent);
        }

    }

    private void loadingViews() {
        if (layoutInflater != null) {
            //// 左侧
            loadingSubView(mytags_fflay_lefttop, left_top_labels, left_top_color);
            loadingSubView(mytags_fflay_leftmoddle, left_moddle_labels, left_moddle_color);
            loadingSubView(mytags_fflay_leftbottom, left_bottom_labels, left_bottom_color);

            /// 右侧
            loadingSubView(mytags_fflay_righttop, right_top_labels, right_top_color);
            loadingSubView(mytags_fflay_rightmoddle, right_moddle_labels, right_moddle_color);
            loadingSubView(mytags_fflay_rightbottom, right_bottom_labels, right_bottom_color);
        }
    }


    private void loadingSubView(FilterFlowLayout fflay, String[] label_names, int colorType) {
        fflay.removeAllViews();

        int length = label_names.length;

        for (int i = 0; i < length; i++) {

            Log.d("loadingSubView ","length : "+ length+" name : "+ label_names[i] +" i: "+  i);

            CheckBox checkBox = (CheckBox) layoutInflater.inflate(R.layout.common_addtags_list_checkbox_item, null);
            checkBox.setText(label_names[i]);
            checkBox.setChecked(false);
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setOnCheckedChangeListener(new MyTagscheckChangeListener(checkBox, colorType));
            switch (colorType) {
                case left_top_color:
                    checkBox.setTextColor(getResources().getColor(left_top_color));
                    checkBox.setBackgroundDrawable(getResources().getDrawable(R.drawable.label_red));
                    break;
                case left_moddle_color:
                    checkBox.setTextColor(getResources().getColor(left_moddle_color));
                    checkBox.setBackgroundDrawable(getResources().getDrawable(R.drawable.label_pink));
                    break;
                case left_bottom_color:
                    checkBox.setTextColor(getResources().getColor(left_bottom_color));
                    checkBox.setBackgroundDrawable(getResources().getDrawable(R.drawable.label_blue));
                    break;
                case right_top_color:
                    checkBox.setTextColor(getResources().getColor(right_top_color));
                    checkBox.setBackgroundDrawable(getResources().getDrawable(R.drawable.label_green));
                    break;
                case right_moddle_color:
                    checkBox.setTextColor(getResources().getColor(right_moddle_color));
                    checkBox.setBackgroundDrawable(getResources().getDrawable(R.drawable.label_yellow));
                    break;
                case right_bottom_color:
                    checkBox.setTextColor(getResources().getColor(right_bottom_color));
                    checkBox.setBackgroundDrawable(getResources().getDrawable(R.drawable.label_purple));
                    break;
            }
//            fflay.addView(checkBox);
            fflay.addView(checkBox,i);
            Log.d("loadingSubView "," addView length : "+ length+" name : "+ label_names[i] +" i: "+  i+ " checkBox "+ checkBox.getText());
        }
        fflay.requestLayout();
    }

    private void initView() {
        initTitle();

        mytags_addtags_btn = (Button) this.findViewById(R.id.mytags_addtags_btn);
        mytags_addtags_btn.setOnClickListener(this);

        mytags_fflay_lefttop = (xj.property.widget.FilterFlowLayout) this.findViewById(R.id.mytags_fflay_lefttop);
        mytags_fflay_leftmoddle = (xj.property.widget.FilterFlowLayout) this.findViewById(R.id.mytags_fflay_leftmoddle);
        mytags_fflay_leftbottom = (xj.property.widget.FilterFlowLayout) this.findViewById(R.id.mytags_fflay_leftbottom);

        mytags_fflay_righttop = (xj.property.widget.FilterFlowLayout) this.findViewById(R.id.mytags_fflay_righttop);
        mytags_fflay_rightmoddle = (xj.property.widget.FilterFlowLayout) this.findViewById(R.id.mytags_fflay_rightmoddle);
        mytags_fflay_rightbottom = (xj.property.widget.FilterFlowLayout) this.findViewById(R.id.mytags_fflay_rightbottom);


    }

    private void initTitle() {
        this.findViewById(R.id.iv_back).setVisibility(View.GONE);
        TextView tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setText("设置个人标签");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.mytags_addtags_btn:
                for (String str : choicedLables) {
                    Log.d("ActivityAddTagsList ", "choicedLables str : " + str);
                }
                getTagsA2BAdd();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLdDialog != null) {
            mLdDialog.dismiss();
        }
    }

    /**
     * 添加标签
     */
    private void getTagsA2BAdd() {
        NetBaseTagUtils.getTagsA2BAdd(getApplicationContext(), choicedLables, bean.getEmobId(), bean.getEmobId(), new NetBaseTagUtils.NetRespListener<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> respBean, Response response) {
                if (respBean != null && TextUtils.equals("yes", respBean.getStatus())) {
                    onBackPressed();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                showNetErrorToast();
            }
        });
    }

    /**
     * 获取封装后的A2B的添加标签列表
     *
     * @param choicedLables
     * @return
     */
    private List<TagsA2BAddReqBean.LabelListEntity> getcurrentA2BTagsList(List<String> choicedLables, String fromemobId, String toEmobId) {
        List<TagsA2BAddReqBean.LabelListEntity> labelList = new ArrayList<>();
        for (String tag : choicedLables) {
            TagsA2BAddReqBean.LabelListEntity labelListEntity = new TagsA2BAddReqBean.LabelListEntity();
            labelListEntity.setEmobIdFrom(fromemobId);
            labelListEntity.setEmobIdTo(toEmobId);
            labelListEntity.setLabelContent(tag);
            labelList.add(labelListEntity);
        }
        return labelList;
    }


    private class MyTagscheckChangeListener implements CompoundButton.OnCheckedChangeListener {

        private final CheckBox checkBox;
        private final int colorType;

        public MyTagscheckChangeListener(CheckBox cb, int colorType) {
            this.checkBox = cb;
            this.colorType = colorType;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                switch (colorType) {
                    case left_top_color:
                        checkBox.setTextColor(getResources().getColor(common_choiced_color));
                        checkBox.setBackgroundDrawable(getResources().getDrawable(R.drawable.label_red_select));
                        break;
                    case left_moddle_color:
                        checkBox.setTextColor(getResources().getColor(common_choiced_color));
                        checkBox.setBackgroundDrawable(getResources().getDrawable(R.drawable.label_pink_select));
                        break;
                    case left_bottom_color:
                        checkBox.setTextColor(getResources().getColor(common_choiced_color));
                        checkBox.setBackgroundDrawable(getResources().getDrawable(R.drawable.label_blue_select));
                        break;
                    case right_top_color:
                        checkBox.setTextColor(getResources().getColor(common_choiced_color));
                        checkBox.setBackgroundDrawable(getResources().getDrawable(R.drawable.label_green_select));
                        break;
                    case right_moddle_color:
                        checkBox.setTextColor(getResources().getColor(common_choiced_color));
                        checkBox.setBackgroundDrawable(getResources().getDrawable(R.drawable.label_yellow_select));
                        break;
                    case right_bottom_color:
                        checkBox.setTextColor(getResources().getColor(common_choiced_color));
                        checkBox.setBackgroundDrawable(getResources().getDrawable(R.drawable.label_purple_select));
                        break;
                }
                choicedLables.add(checkBox.getText().toString());
            } else {
                switch (colorType) {
                    case left_top_color:
                        checkBox.setTextColor(getResources().getColor(left_top_color));
                        checkBox.setBackgroundDrawable(getResources().getDrawable(R.drawable.label_red));
                        break;
                    case left_moddle_color:
                        checkBox.setTextColor(getResources().getColor(left_moddle_color));
                        checkBox.setBackgroundDrawable(getResources().getDrawable(R.drawable.label_pink));
                        break;
                    case left_bottom_color:
                        checkBox.setTextColor(getResources().getColor(left_bottom_color));
                        checkBox.setBackgroundDrawable(getResources().getDrawable(R.drawable.label_blue));
                        break;
                    case right_top_color:
                        checkBox.setTextColor(getResources().getColor(right_top_color));
                        checkBox.setBackgroundDrawable(getResources().getDrawable(R.drawable.label_green));
                        break;
                    case right_moddle_color:
                        checkBox.setTextColor(getResources().getColor(right_moddle_color));
                        checkBox.setBackgroundDrawable(getResources().getDrawable(R.drawable.label_yellow));
                        break;
                    case right_bottom_color:
                        checkBox.setTextColor(getResources().getColor(right_bottom_color));
                        checkBox.setBackgroundDrawable(getResources().getDrawable(R.drawable.label_purple));
                        break;
                }
                choicedLables.remove(checkBox.getText().toString());
            }

        }
    }
}
