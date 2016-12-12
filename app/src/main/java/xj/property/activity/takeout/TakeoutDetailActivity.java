package xj.property.activity.takeout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;


import com.google.gson.Gson;


import java.util.ArrayList;
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
import xj.property.Constant;
import xj.property.R;
import xj.property.activity.HXBaseActivity.ChatActivity;
import xj.property.adapter.TakeoutMenuAdapter;
import xj.property.adapter.TakeoutSubmenuAdapter;
import xj.property.event.ButtonOnClickEvent;
import xj.property.hxMessageExtModel.Hx200203Model;
import xj.property.hxMessageExtModel.HxOrderDetailModel;
import xj.property.beans.ShopItemBean;
import xj.property.beans.ShopItemDetailBean;
import xj.property.utils.other.Config;

public class TakeoutDetailActivity extends Activity {
    /**
     * logger
     */
    /**
     * listview of takeout menu
     */
    private ListView lv_takeout_menu;
    /**
     * listview of takeout submenu
     */
    private ListView lv_takeout_submenu;
    /**
     * button of settlement
     */
    private Button btn_settlement_takeout;

    /**
     * menu list
     */
//    private List<String> menuList = new ArrayList<String>();
    private List<Map<Integer,String>> menuList = new ArrayList<Map<Integer,String>>();
    /**
     * submenu list belong to one class,to show
     */
    private List<ShopItemDetailBean> shopItemDetailBeanList = new ArrayList<ShopItemDetailBean>();

    /**
     * all submenu list
     */
    private List<List<ShopItemDetailBean>> subMenuList = new ArrayList<List<ShopItemDetailBean>>();

    /**
     * adapter of menu
     */
    private TakeoutMenuAdapter takeoutMenuAdapter;
    /**
     * adapter of submenu
     */
    private TakeoutSubmenuAdapter takeoutSubmenuAdapter;

    /**
     * 详细订单信息
     */
    private String cmdDetail;
    /**
     * 点菜数量
     */
    private int itemCount = 0;

    /**
     * 订单bean
     */
    private Hx200203Model hx200203Model;
    /**
     * 订单详情bean
     */
    private List<HxOrderDetailModel> hxOrderDetailModelList = new ArrayList<HxOrderDetailModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takeout_detail);
        initView();
        EventBus.getDefault().register(this);
        getTakeoutList();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 事件处理
     * @param event
     */
    public void onEvent(ButtonOnClickEvent event){
        itemCount = shopItemDetailBeanList.get(event.position).getCount();
        if(event.btnIndex == 0){
            if(itemCount>=1){
                itemCount --;
            }
        }else{
            itemCount ++;
        }

        //获取需要更新的item
        View view = lv_takeout_submenu.getChildAt(event.position - lv_takeout_submenu.getFirstVisiblePosition());
        //设置新的值
        shopItemDetailBeanList.get(event.position).setCount(itemCount);
        //更新特定item
        lv_takeout_submenu.getAdapter().getView(event.position, view, lv_takeout_submenu);
    }

    /**
     * 初始化view
     */
    private void initView(){
        //init left listview for menu
        lv_takeout_menu = (ListView)findViewById(R.id.lv_takeout_menu);
        takeoutMenuAdapter = new TakeoutMenuAdapter(this,menuList);
        lv_takeout_menu.setAdapter(takeoutMenuAdapter);
        lv_takeout_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //清空对应之前分类的所有菜品列表
                shopItemDetailBeanList.clear();
                //添加对应当前分类的所有菜品列表
                shopItemDetailBeanList.addAll(subMenuList.get(i));
                //刷新显示
                takeoutSubmenuAdapter.notifyDataSetChanged();
            }
        });

        //菜单明细listview
        lv_takeout_submenu = (ListView)findViewById(R.id.lv_takeout_submenu);
        takeoutSubmenuAdapter = new TakeoutSubmenuAdapter(this,shopItemDetailBeanList);

//        takeoutSubmenuAdapter.setOnClickListener(new TakeoutSubmenuAdapter.OnClickListener() {
//            @Override
//            public void onClick(int position, String tag) {
//                logger.info("position is :"+position);
//                logger.info("tag is :"+tag);
//            }
//        });

        lv_takeout_submenu.setAdapter(takeoutSubmenuAdapter);

        btn_settlement_takeout = (Button)findViewById(R.id.btn_settlement_takeout);
        btn_settlement_takeout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPush = new Intent();
                intentPush.setClass(TakeoutDetailActivity.this, ChatActivity.class);
                intentPush.putExtra("userId", "tz");
                intentPush.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
                intentPush.putExtra("CMD_CODE", 200);

                buildCmdDetail();

                intentPush.putExtra("CMD_DETAIL", cmdDetail);
                startActivity(intentPush);
            }
        });
    }

    /**
     * 构建环信传输的订单消息
     */
    private void buildCmdDetail(){
        hx200203Model = new Hx200203Model();
        //清空菜单明细列表
        hxOrderDetailModelList.clear();
        for(int i=0;i<subMenuList.size();i++){
            for(int j=0;j<subMenuList.get(i).size();j++) {
                //数量不为0
                if(subMenuList.get(i).get(j).getCount()!=0){
                    //构建订单明细，用来传送消息
                    HxOrderDetailModel hxOrderDetailModel = new HxOrderDetailModel();
                    hxOrderDetailModel.setServiceId(subMenuList.get(i).get(j).getServiceId());
                    hxOrderDetailModel.setCount(subMenuList.get(i).get(j).getCount());
                    hxOrderDetailModel.setServiceName(subMenuList.get(i).get(j).getServiceName());
                    hxOrderDetailModel.setPrice(subMenuList.get(i).get(j).getPrice());
                    //订单明细列表
                    hxOrderDetailModelList.add(hxOrderDetailModel);
                }
            }
        }

        //订单其他信息
        hx200203Model.setSerial("123");
        hx200203Model.setTotalCount(1);
        hx200203Model.setOrderDetailBeanList(hxOrderDetailModelList);
        Gson gson = new Gson();
        cmdDetail = gson.toJson(hx200203Model,Hx200203Model.class);
    }

    /**
     * 构建查询菜单的service
     */
    interface TakeoutDetailService {
        @GET("/api/v1/communities/{communityId}/shops/{shopId}/shopItems")
        void listShops(@Path("communityId") int communityId, @Path("shopId") int shopId, Callback<List<ShopItemBean>> cb);
    }

    /**
     * 获取菜单列表
     */
    private void getTakeoutList(){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Config.NET_BASE)
                .build();
        TakeoutDetailService takeoutDetailService = restAdapter.create(TakeoutDetailService.class);
        Callback<List<ShopItemBean>> callback = new Callback<List<ShopItemBean>>() {
            @Override
            public void success(List<ShopItemBean> shopItemBeanList, Response response) {
                /**
                 * 第一级菜单列表，即菜品分类
                 */
                menuList.clear();
                //子菜单列表,包含所有分类对应的菜单详细
                subMenuList.clear();
                for(ShopItemBean shopItemBean :shopItemBeanList){
                    Map<Integer,String> mapMenu = new HashMap<Integer,String>();
                    mapMenu.put(shopItemBean.getCatId(),shopItemBean.getCatName());
                    menuList.add(mapMenu);
                    subMenuList.add(shopItemBean.getList());
                }
                //待显示的菜品明细，对应一个分类，即对应subMenuList中一项
                shopItemDetailBeanList.clear();
                //默认选中第一项
                if(subMenuList.size()>=1)
                shopItemDetailBeanList.addAll(subMenuList.get(0));
                takeoutSubmenuAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
            }
        };
        takeoutDetailService.listShops(1,51,callback);
    }

}
