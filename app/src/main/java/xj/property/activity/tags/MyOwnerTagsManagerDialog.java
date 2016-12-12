package xj.property.activity.tags;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;
import xj.property.R;
import xj.property.beans.SysDefaultTagsV3Bean;
import xj.property.beans.TagsA2BAddReqBean;
import xj.property.beans.TagsA2BRespBean;
import xj.property.beans.UserInfoDetailBean;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.NetBaseTagUtils;
import xj.property.utils.DensityUtil;
import xj.property.utils.ToastUtils;
import xj.property.utils.other.PreferencesUtil;
import xj.property.widget.FilterFlowLayout;

/**
 * 自己添加标签个数限制
 */
public class MyOwnerTagsManagerDialog extends Dialog implements View.OnClickListener {

    private static final String TAG = "MyTagsManagerDialog";
    private static final int OWNER_TAGS_MAX_LIMIT = 10;
    private List<String> systemDefaultTags;
    private final String fromemobId;
    private final String toEmobId;
    private final String communityId;
    private Context mContext;
    //// 获取A2B的标签
    private List<String> tagsA2Btags;

    private FilterFlowLayout mytags_mgr_fflay;
    /// 还可以添加几个标签
    private TextView mytags_mgr_left_nums_tv;
    //// 输入的自定义标签
    private EditText mytags_mgr_inputtags_et;
    //// 系统默认标签
    private FilterFlowLayout mytags_mgr_system_defaulttags_fflay;
    //// 添加按钮
    private Button mytags_mgr_inputtags_btn;
    /// 取消
    private Button btn_cancel;
    /// 确定
    private Button btn_ok;

    private LayoutInflater layoutInflater;
    /// 临时标签列表
    private List<String> currentTempList = new ArrayList<String>();

    private onTagsA2BAddStatusListener onTagsA2BAddStatusListener;

    private UserInfoDetailBean bean;


    //// 默认loading textview
    private TextView sysdefault_tags_loading_tv;

    public MyOwnerTagsManagerDialog(Context context, List<String> systemDefaulttags, String communityId, String fromemobId, String toEmobId, onTagsA2BAddStatusListener onTagsA2BAddStatusListener) {
        super(context, R.style.Theme_CustomDialog);
        this.mContext = context;
        layoutInflater = LayoutInflater.from(context);
        this.systemDefaultTags = systemDefaulttags;
        this.fromemobId = fromemobId;
        this.toEmobId = toEmobId;
        this.communityId = communityId;
        this.onTagsA2BAddStatusListener = onTagsA2BAddStatusListener;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_myownertags_mgr);
        initView();
        initData();
        bean = PreferencesUtil.getLoginInfo(getContext());
        if(currentTempList==null){
            currentTempList = new ArrayList<>();
        }
        currentTempList.clear();
    }

    // / 初始化数据,初始化UI
    private void initData() {
        getTagsA2BTags();
        if(systemDefaultTags==null||systemDefaultTags.size()<=0){
            getSystemDefaultTags();
        }else {
            initSystemDefaultTags(systemDefaultTags);
        }
    }

    public MyOwnerTagsManagerDialog.onTagsA2BAddStatusListener getOnTagsA2BAddStatusListener() {
        return onTagsA2BAddStatusListener;
    }

    public void setOnTagsA2BAddStatusListener(MyOwnerTagsManagerDialog.onTagsA2BAddStatusListener onTagsA2BAddStatusListener) {
        this.onTagsA2BAddStatusListener = onTagsA2BAddStatusListener;
    }

    public interface onTagsA2BAddStatusListener {

        void onTagsA2BAddSuccess(String message);

        void onTagsA2BAddFail(String message);

    }


    /**
     * 获取系统默认Tags
      */
    private void getSystemDefaultTags() {
        sysdefault_tags_loading_tv.setVisibility(View.VISIBLE);
        HashMap<String, String> option = new HashMap<String, String>();
        option.put("communityId", bean.getCommunityId()+"");
        option.put("time", "" + PreferencesUtil.getLastReqTagsTime(getContext()));
        NetBaseTagUtils.getSysDefaultTags(fromemobId,getContext(),option, new NetBaseTagUtils.NetRespListener<CommonRespBean<SysDefaultTagsV3Bean>>() {
            @Override
            public void success(CommonRespBean<SysDefaultTagsV3Bean> tagsbean, Response response) {
                if (tagsbean != null) {
                    sysdefault_tags_loading_tv.setVisibility(View.GONE);
                    initSystemDefaultTags(systemDefaultTags);
                }else{
                    sysdefault_tags_loading_tv.setText("加载默认标签错误,请连接网络重试");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }


    /**
     * 添加标签
     */
    private void getTagsA2BAdd() {
        NetBaseTagUtils.getTagsA2BAdd(getContext(),currentTempList, fromemobId, toEmobId, new NetBaseTagUtils.NetRespListener<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> respBean, Response response) {
                if (respBean != null && TextUtils.equals("yes", respBean.getStatus())) {
                    if (onTagsA2BAddStatusListener != null) {
                        onTagsA2BAddStatusListener.onTagsA2BAddSuccess(respBean.getMessage());
                    }
                    dismiss();
                } else if (respBean != null && TextUtils.equals("no", respBean.getStatus())) {
                    if (onTagsA2BAddStatusListener != null) {
                        onTagsA2BAddStatusListener.onTagsA2BAddFail(respBean.getMessage());
                    }

                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                ToastUtils.showToast(getContext(), "网络异常");
            }
        });
    }

    /**
     * 获取封装后的A2B的添加标签列表
     *
     * @param currentTempList
     * @return
     */
    private List<TagsA2BAddReqBean.LabelListEntity> getcurrentA2BTagsList(List<String> currentTempList) {
        List<TagsA2BAddReqBean.LabelListEntity> labelList = new ArrayList<>();
        for (String tag : currentTempList) {
            TagsA2BAddReqBean.LabelListEntity labelListEntity = new TagsA2BAddReqBean.LabelListEntity();
            labelListEntity.setEmobIdFrom(fromemobId);
            labelListEntity.setEmobIdTo(toEmobId);
            labelListEntity.setLabelContent(tag);
            labelList.add(labelListEntity);
        }
        return labelList;
    }

    /**
     * 获取A2B添加的标签
     */
    private void getTagsA2BTags() {
        HashMap<String, String> option = new HashMap<String, String>();
        option.put("emobIdFrom", fromemobId);
        option.put("emobIdTo", toEmobId);
        NetBaseTagUtils.getTagsA2BTags(getContext(),option, new NetBaseTagUtils.NetRespListener<TagsA2BRespBean>() {
            @Override
            public void success(TagsA2BRespBean tagsbean, Response response) {
                if (tagsbean != null) {
                    if ("yes".equals(tagsbean.getStatus())) {
                        tagsA2Btags = tagsbean.getData();
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("getTagsA2BTags ", "tagsbean is null ");
            }
        });
    }

    /**
     * 初始化A2B标签
     * 设置成临时列表
     *
     * @param tagsA2Btags
     */
    private void initTagA2B(List<String> tagsA2Btags) {
        if (tagsA2Btags == null || tagsA2Btags.isEmpty()) {
            if (mytags_mgr_fflay != null) {
                mytags_mgr_fflay.removeAllViews();
            }
            return;
        }

        if (mytags_mgr_fflay != null) {
            mytags_mgr_fflay.removeAllViews();
            View view = new View(getContext());
            view.setLayoutParams(new FilterFlowLayout.LayoutParams(1, DensityUtil.dip2px(getContext(),30f)));
            view.setBackgroundColor(getContext().getResources().getColor(android.R.color.transparent));
            mytags_mgr_fflay.addView(view);

            for (final String tagA2B : tagsA2Btags) {
                View common_tags_item_fora2b = layoutInflater.inflate(R.layout.common_tags_item_fora2b, null);
                TextView common_tags_name_tv = (TextView) common_tags_item_fora2b.findViewById(R.id.common_tags_name_tv);
                common_tags_name_tv.setText(tagA2B);

                common_tags_item_fora2b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(getContext(), "已删除" + tagA2B, Toast.LENGTH_SHORT).show();
                        currentTempList.remove(tagA2B);
                        ///刷新当前标签临时列表
                        initTagA2B(currentTempList);

                    }
                });
                mytags_mgr_fflay.addView(common_tags_item_fora2b);
            }
        }
    }

    /**
     * 初始化A2B标签
     *
     * @param systemDefaultTags
     */
    private void initSystemDefaultTags(List<String> systemDefaultTags) {
        if (mytags_mgr_system_defaulttags_fflay != null) {
            mytags_mgr_system_defaulttags_fflay.removeAllViews();
            for (final String tagA2B : systemDefaultTags) {
                View common_tags_item_fora2b = layoutInflater.inflate(R.layout.common_tags_item, null);
                TextView common_tags_name_tv = (TextView) common_tags_item_fora2b.findViewById(R.id.common_tags_name_tv);
                common_tags_name_tv.setText(tagA2B);

                TextView common_tags_nums_tv = (TextView) common_tags_item_fora2b.findViewById(R.id.common_tags_nums_tv);
                common_tags_nums_tv.setVisibility(View.GONE);

                common_tags_item_fora2b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (currentTempList.contains(tagA2B)) {
                            ToastUtils.showToast(getContext(), "你已添加该标签");
                            return;
                        }
                        if(currentTempList.size()<OWNER_TAGS_MAX_LIMIT){
                            currentTempList.add(tagA2B);
                            initTagA2B(currentTempList);
                        }else{
                            ToastUtils.showToast(getContext(), "最多每次只能添加"+OWNER_TAGS_MAX_LIMIT+"个标签");
                        }
                    }
                });
                mytags_mgr_system_defaulttags_fflay.addView(common_tags_item_fora2b);
            }
        }
    }

    private void initView() {
        mytags_mgr_fflay = (FilterFlowLayout) findViewById(R.id.mytags_mgr_fflay);
        mytags_mgr_system_defaulttags_fflay = (FilterFlowLayout) findViewById(R.id.mytags_mgr_system_defaulttags_fflay);
        mytags_mgr_left_nums_tv = (TextView) findViewById(R.id.mytags_mgr_left_nums_tv);
        mytags_mgr_left_nums_tv.setVisibility(View.GONE);
        sysdefault_tags_loading_tv = (TextView) findViewById(R.id.sysdefault_tags_loading_tv);

        mytags_mgr_inputtags_et = (EditText) findViewById(R.id.mytags_mgr_inputtags_et);
        mytags_mgr_inputtags_et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        mytags_mgr_inputtags_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });

        /// 限制最长6位
        mytags_mgr_inputtags_btn = (Button) findViewById(R.id.mytags_mgr_inputtags_btn);
        mytags_mgr_inputtags_btn.setOnClickListener(this);
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

            case R.id.mytags_mgr_inputtags_btn:

                if (mytags_mgr_inputtags_et != null) {


                    String inputTagName = mytags_mgr_inputtags_et.getText().toString().trim();

                    if (TextUtils.isEmpty(inputTagName)) {
                        ToastUtils.showToast(getContext(), "输入标签不能为空");
                        return;
                    }
                    if (inputTagName.length() > 6) {
                        ToastUtils.showToast(getContext(), "输入标签最多6个字");
                        return;
                    }

                    if (currentTempList.contains(inputTagName)) {
                        ToastUtils.showToast(getContext(), "你已添加该标签");
                        return;
                    }

                    currentTempList.add(inputTagName);
                    mytags_mgr_inputtags_et.setText("");

                    initTagA2B(currentTempList);
                }
                break;
            case R.id.btn_cancel:
                cancel();
                break;

            case R.id.btn_ok:
                getTagsA2BAdd();
                break;

        }
    }

    @Override
    public void cancel() {
        super.cancel();
    }

}
