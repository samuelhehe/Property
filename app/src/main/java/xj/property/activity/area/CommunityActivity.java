package xj.property.activity.area;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.beans.CommunityBean;
import xj.property.utils.other.Config;
import xj.property.widget.sectionList.CharacterParser;
import xj.property.widget.sectionList.ClearEditText;
import xj.property.widget.sectionList.PinyinComparator;
import xj.property.widget.sectionList.SideBar;
import xj.property.widget.sectionList.SortAdapter;
import xj.property.widget.sectionList.SortModel;

public class CommunityActivity extends HXBaseActivity {

    /**
     * logger
     */

    /**
     * result code
     */
    public static final int resultCode_community = 2;

    /**
     * sort listview
     */
    private ListView sortListView;
    /**
     * sidd bar
     */
    private SideBar sideBar;
    private TextView dialog;
    private SortAdapter adapter;
    private ClearEditText mClearEditText;
    private TextView tv_other;
    private String district;

    /**
     * 显示字母的TextView
     */
    private CharacterParser characterParser;
    private List<SortModel> SourceDateList = new ArrayList<SortModel>();

    /**
     * 汉字转换成拼音的类
     */
    private PinyinComparator pinyinComparator;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        initView();
        getCommunityList();
    }


    private void initView() {
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar = (SideBar) findViewById(R.id.sidrbar_district);
        dialog = (TextView) findViewById(R.id.dialog_district);
        sideBar.setTextView(dialog);

        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if(position != -1){
                    sortListView.setSelection(position);
                }

            }
        });

        sortListView = (ListView) findViewById(R.id.lv_district);
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //这里要利用adapter.getItem(position)来获取当前position所对应的对象
//                district = ((SortModel)adapter.getItem(position)).getName();


                Intent intentPop = new Intent();

                Bundle bundle = new Bundle();
                bundle.putInt("communityId",((SortModel) adapter.getItem(position)).getId());
                bundle.putString("communityName",((SortModel) adapter.getItem(position)).getName());
                intentPop.putExtras(bundle);
                setResult(resultCode_community, intentPop);
                finish();
            }
        });


        adapter = new SortAdapter(this, SourceDateList);
        sortListView.setAdapter(adapter);


        mClearEditText = (ClearEditText) findViewById(R.id.filter_et_district);

        //根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    /**
     * 为ListView填充数据
     * @return
     */
    private List<SortModel> prepareData(List<CommunityBean> cityBeanList){
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for(int i=0; i<cityBeanList.size(); i++){

            SortModel sortModel = new SortModel();
            sortModel.setName(cityBeanList.get(i).getCommunityName());
            sortModel.setId(cityBeanList.get(i).getCityId());
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(cityBeanList.get(i).getCommunityName());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if(sortString.matches("[A-Z]")){
                sortModel.setSortLetters(sortString.toUpperCase());
            }else{
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     * @param filterStr
     */
    private void filterData(String filterStr){
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if(TextUtils.isEmpty(filterStr)){
            filterDateList = SourceDateList;
        }else{
            filterDateList.clear();
            for(SortModel sortModel : SourceDateList){
                String name = sortModel.getName();
                if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }


    /**
     * construct interface for REST
     */
    interface CommunityListService {
        @GET("/api/v1/communities/summary/byCityId/{cityId}/?pageNum=1&pageSize=10")
        void listCommunities(@Path("cityId") int cityId, Callback<List<CommunityBean>> cb);
    }

    /**
     * get city list
     */
    private void getCommunityList() {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        CommunityListService communityListService = restAdapter.create(CommunityListService.class);

        Callback<List<CommunityBean>> callback = new Callback<List<CommunityBean>>() {
            @Override
            public void success(List<CommunityBean> communityBeanList, Response response) {
                SourceDateList.clear();
                SourceDateList = prepareData(communityBeanList);
                adapter.updateListView(SourceDateList);
            }

            @Override
            public void failure(RetrofitError error) {
            }
        };
        communityListService.listCommunities(1,callback);
    }

    @Override
    public void onClick(View v) {

    }
}
