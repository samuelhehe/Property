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

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import xj.property.R;
import xj.property.adapter.GroupMembersForDeleteAdapter;
import xj.property.beans.UserGroupBeanForDel;
import xj.property.beans.UserInfoDetailBean;
import xj.property.event.FriendsChoicedBackEvent;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.netbasebean.UserGroupForDel;
import xj.property.utils.other.PreferencesUtil;


/**
 * 选择要删除的群成员
 */
public class NewFriendsDeleteActivity extends HXBaseActivity {


    private String groupId;

    private ListView group_friends_delete_lv;
    private TextView tv_right_text;

    private GroupMembersForDeleteAdapter adapter ;
    private List<UserGroupBeanForDel> beans = new ArrayList<>();

    private Map<String, UserGroupBeanForDel> mapLists = new HashMap<>();
    private UserInfoDetailBean userInfoDetailBean;
    private EMGroup emGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friends_delete_process);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.tv_title)).setText("邀请邻居");
        tv_right_text = (TextView)findViewById(R.id.tv_right_text);
        tv_right_text.setVisibility(View.VISIBLE);
        tv_right_text.setText("删除");
        tv_right_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goDelete();
            }
        });
        groupId  = getIntent().getStringExtra("groupId");
        emGroup = EMGroupManager.getInstance().getGroup(groupId);
        userInfoDetailBean = PreferencesUtil.getLoginInfo(getmContext());

        if (!TextUtils.isEmpty(groupId)&&userInfoDetailBean!=null) {
            initView();
            initDataLoadingView();
        } else {
            showToast("该群组数据异常");
            finish();
        }
    }

    private void initDataLoadingView() {
        /// 加载所有群成员
        getGroupInfo(groupId);
    }

    private void initView() {
        group_friends_delete_lv = (ListView)findViewById(R.id.group_friends_delete_lv);
        adapter = new GroupMembersForDeleteAdapter(getmContext(),beans,emGroup,mapLists);
        group_friends_delete_lv.setAdapter(adapter);
        group_friends_delete_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (beans != null && beans.size() > 0) {

                    UserGroupBeanForDel searchUserResultRespBean = beans.get(position);
                    if(TextUtils.equals(emGroup.getOwner(),searchUserResultRespBean.getEmobId())){
                        /// 群主不能把自己删除
//                        showToast("群主不能把自己删除");
                        searchUserResultRespBean.isChecked =false;
                        mapLists.remove(searchUserResultRespBean.getEmobId());
                        return ;
                    }
                    searchUserResultRespBean.isChecked = !searchUserResultRespBean.isChecked;

                    adapter.notifyDataSetChanged();
                    if (mapLists != null) {
                        if (searchUserResultRespBean.isChecked) {
                            mapLists.put(searchUserResultRespBean.getEmobId(), searchUserResultRespBean);
                        } else {
                            mapLists.remove(searchUserResultRespBean.getEmobId());
                        }
                    }
                }
            }
        });
    }


    private void goDelete() {
        if (mapLists == null || mapLists.size() <= 0) {
            showToast("请选择要删除的群成员");
            return;
        }
        for (Map.Entry<String, UserGroupBeanForDel> entry : mapLists.entrySet()) {
            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
        }
        EventBus.getDefault().post(new FriendsChoicedBackEvent(FriendsChoicedBackEvent.FLAG_MEMBERS_DELETE,mapLists));
        finish();

    }

    @Override
    public void onClick(View v) {

    }


    interface getOrderInfoService {
        ///api/v1/communities/{communityId}/groups/{groupId}/members
//        @GET("/api/v1/communities/{communityId}/groups/{groupId}/members")
//        void getOrderInfo(@Path("communityId") int communityId, @Path("groupId") String groupId, Callback<ResultGroupInfoForDel> cb);
//        @GET("/api/v1/communities/{communityId}/groups/{groupId}/members")

//        /api/v3/activities/{环信群组ID}/membersWithLabel?page={页码}&limit={页面大小}

        @GET("/api/v3/activities/{groupId}/membersWithLabel")
        void getOrderInfo(@Path("groupId") String groupId, @QueryMap HashMap<String, String> queryMap, Callback<CommonRespBean<UserGroupForDel>> cb);
    }

    private void getGroupInfo(final String groupId) {
        HashMap<String,String> queryMap = new HashMap<>();
        queryMap.put("page","1");
        queryMap.put("limit","100");
        getOrderInfoService service = RetrofitFactory.getInstance().create(getmContext(),queryMap, getOrderInfoService.class);
        Callback<CommonRespBean<UserGroupForDel>> callback = new Callback<CommonRespBean<UserGroupForDel>>() {
            @Override
            public void success(CommonRespBean<UserGroupForDel> bean, retrofit.client.Response response) {
                if(bean!=null&&"yes".equals(bean.getStatus())&&bean.getData()!=null){
                    beans.clear();
                    beans.addAll(bean.getData().getData());
                    adapter.notifyDataSetChanged();
                    mapLists.clear();
                }
            }
            @Override
            public void failure(RetrofitError error) {
                showNetErrorToast();
                error.printStackTrace();
            }
        };
        service.getOrderInfo(groupId, queryMap,callback);
    }


}
