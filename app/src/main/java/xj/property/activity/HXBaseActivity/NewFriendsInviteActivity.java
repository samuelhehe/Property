/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xj.property.activity.HXBaseActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.beans.HomeSearchResultRespBean;
import xj.property.beans.LocationVerficationRespBean;
import xj.property.beans.SearchUserResultRespBean;
import xj.property.event.FriendsChoicedBackEvent;
import xj.property.utils.DensityUtil;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.utils.other.ViewUtils;


/**
 * 邀请邻居页,搜索结果页是另外页面
 */
public class NewFriendsInviteActivity extends HXBaseActivity {

    public static final int FLAG_CLOSE_INVITE = 10;
    private TextView tv_right_text;
    private ArrayAdapter<String> arrayAdapter;
//    private PopupWindow popupWindow;
//    private ListView drop_downlist_lv;

    private EditText iwant_search_key_et;
    private boolean isPublishActivity;
    private String groupId;
    //// 搜索Key
    private String searchName;
    private String groupowner;
    private ArrayList<String> membersInGroup = new ArrayList<>();
    private ListView lv_search_prepare;
    private LinearLayout ll_search_his_empty;
    private StringAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friends_invite_process);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.tv_title)).setText("邀请邻居");

        //// 是否是从发布活动页面进入
        isPublishActivity = getIntent().getBooleanExtra("isPublishActivity", false);
        groupId = getIntent().getStringExtra("groupId");
        groupowner = getIntent().getStringExtra("groupowner");
        ////从群组详情过来的.
        if(!isPublishActivity){
            membersInGroup = getIntent().getStringArrayListExtra("membersInGroup");
        }
        tv_right_text = (TextView) findViewById(R.id.tv_right_text);
        if (isPublishActivity) {
            tv_right_text.setText("跳过");
            tv_right_text.setVisibility(View.VISIBLE);
            tv_right_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /// TODO 跳转至活动列表
                    onBackPressed();
                }
            });

        } else {
            tv_right_text.setVisibility(View.GONE);
        }
//        initPopupWindow();

        ll_search_his_empty = (LinearLayout) findViewById(R.id.ll_search_his_empty);
        lv_search_prepare = (ListView)findViewById(R.id.lv_search_prepare);
        final String[] arr = {"广场舞", "专业家长", "中国好邻居", "网游"};

        adapter = new StringAdapter(this, R.layout.common_choiced_list_item, Arrays.asList(arr));
        lv_search_prepare.setAdapter(adapter);
        lv_search_prepare.setVisibility(View.GONE);
        lv_search_prepare.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < 0 || position > arr.length - 1) {
                    return;
                }
                searchName = arr[position].toString();

                Intent intent = new Intent(getmContext(), NewFriendsInviteSearchActivity.class);
                intent.putExtra("searchName", searchName);
                intent.putExtra("groupId", groupId);
                intent.putExtra("isPublishActivity", isPublishActivity);
                intent.putExtra("membersInGroup",membersInGroup);

                startActivityForResult(intent, FLAG_CLOSE_INVITE);
                lv_search_prepare.setVisibility(View.GONE);
                ll_search_his_empty.setVisibility(View.VISIBLE);
            }
        });

        iwant_search_key_et = (EditText) findViewById(R.id.iwant_search_key_et);
        iwant_search_key_et.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(KeyEvent.ACTION_UP ==event.getAction()){
//                    showPopupWindow(iwant_search_key_et);
                     showSearchPreparelv();

                }
                return false;
            }
        });

//        iwant_search_key_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//
//                if(hasFocus){
//                    showPopupWindow(iwant_search_key_et);
//                }
//            }
//        });

//        iwant_search_key_et.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (!TextUtils.isEmpty(s)) {
//                    showPopupWindow(iwant_search_key_et);
//                } else {
//                    popupWindow.dismiss();
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        iwant_search_key_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    if (iwant_search_key_et.getText().toString().length() == 0) {

//                        search_clear.setVisibility(View.INVISIBLE);
                        showToast("请输入搜索内容");

//                        resetSearchResult();

                    } else {
                        searchName = iwant_search_key_et.getText().toString();
//                        home_index_search_result_llay.setVisibility(View.VISIBLE);

                        Intent intent = new Intent(getmContext(), NewFriendsInviteSearchActivity.class);
                        intent.putExtra("searchName", searchName);
                        intent.putExtra("groupId", groupId);
                        intent.putExtra("groupowner", groupowner);
                        intent.putExtra("membersInGroup",membersInGroup);
                        intent.putExtra("isPublishActivity", isPublishActivity);
//                        startActivity(intent);
                        startActivityForResult(intent,FLAG_CLOSE_INVITE);

                    }
                }
                return false;
            }
        });

    }

    private void showSearchPreparelv() {

        lv_search_prepare.setVisibility(View.VISIBLE);
        ll_search_his_empty.setVisibility(View.GONE);

    }


//    private void initPopupWindow() {
//        View superZan = View.inflate(this, R.layout.drop_down_list_view, null);
//        popupWindow = new PopupWindow(superZan, ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//
//        drop_downlist_lv = (ListView) superZan.findViewById(R.id.drop_downlist_lv);
//        drop_downlist_lv.setFocusable(true);
//        popupWindow.setFocusable(true);
//
////        popupWindow.setOutsideTouchable(false);
//
//        popupWindow.setBackgroundDrawable(new BitmapDrawable());
//        /// 2016/01/28
//        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
////        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
//        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//
//        final String[] arr = {"广场舞", "专业家长", "中国好邻居", "网游"};
//
//        adapter = new StringAdapter(this, R.layout.common_choiced_list_item, Arrays.asList(arr));
//        drop_downlist_lv.setAdapter(adapter);
//        drop_downlist_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position < 0 || position > arr.length - 1) {
//                    return;
//                }
//                searchName = arr[position].toString();
//
//                Intent intent = new Intent(getmContext(), NewFriendsInviteSearchActivity.class);
//                intent.putExtra("searchName", searchName);
//                intent.putExtra("groupId", groupId);
//                intent.putExtra("isPublishActivity", isPublishActivity);
//                intent.putExtra("membersInGroup",membersInGroup);
//
////                startActivity(intent);
//                startActivityForResult(intent,FLAG_CLOSE_INVITE);
//                popupWindow.dismiss();
//            }
//        });
//
//        int w = View.MeasureSpec.makeMeasureSpec(0,
//                View.MeasureSpec.UNSPECIFIED);
//        int h = View.MeasureSpec.makeMeasureSpec(0,
//                View.MeasureSpec.UNSPECIFIED);
//    }
//
//    private void showPopupWindow(View view) {
//        int[] location = new int[2];
//        view.getLocationOnScreen(location);
//        popupWindow.showAsDropDown(view, 0, 0);
////        popupWindow.showAsDropDown(view);
//    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventBus.getDefault().post(new FriendsChoicedBackEvent());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==FLAG_CLOSE_INVITE){
            if(resultCode==RESULT_OK){
                onBackPressed();
            }
        }
    }

    class StringAdapter extends ArrayAdapter<String> {
        private int mResourceId;

        public StringAdapter(Context context, int textViewResourceId, List<String> list) {
            super(context, textViewResourceId, list);
            this.mResourceId = textViewResourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String user = getItem(position);
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(mResourceId, null);
            TextView choiced_list_tv = (TextView) view.findViewById(R.id.choiced_list_tv);
            choiced_list_tv.setText(user);
            return view;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (popupWindow != null) {
//            popupWindow.dismiss();
//        }
    }
}
