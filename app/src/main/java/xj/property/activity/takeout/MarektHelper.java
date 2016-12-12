package xj.property.activity.takeout;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.http.Body;
import retrofit.http.POST;
import xj.property.beans.ContactPhoneListBean;
import xj.property.beans.FastGoodsModel;
import xj.property.beans.FastShopCatBean;
import xj.property.beans.OrderDetailBeanList;
import xj.property.beans.OrdersBeanRequest;
import xj.property.beans.SerialIdBean;
import xj.property.beans.ShopInfoBean;
import xj.property.cache.FastShopCatModel;
import xj.property.netbase.CommonRespBean;
import xj.property.netbase.RetrofitFactory;
import xj.property.netbasebean.ShopGoodsItemBean;
import xj.property.utils.ToastUtils;
import xj.property.utils.other.Config;
import xj.property.utils.other.FastShopCarDBUtil;
import xj.property.utils.other.PreferencesUtil;

/**
 * Created by Administrator on 2015/5/4.
 * v3 2016/03/17
 */
public class MarektHelper {

    /**
     * 获取Order部分
     */
    static interface OrderIdService {
        // http://114.215.105.202:8080/api/v1/communities/1/users/1/orders
//        @POST("/api/v1/communities/{communityId}/users/{emobIdUser}/orders")
//        void getOrderId(@Header("signature")String signature ,@Body OrdersBeanRequest ob, @Path("communityId") int communityId, @Path("emobIdUser") String emobIdUser, Callback<SerialIdBean> cb);
//        @POST("/api/v1/communities/{communityId}/users/{emobIdUser}/orders")


//        /api/v3/shopOrders

        @POST("/api/v3/shopOrders")
        void getOrderId(@Body OrdersBeanRequest ob, Callback<CommonRespBean<String>> cb);

    }


    /**
     *  下订单
     *
     * @param context
     * @param emobId
     * @param fastshopcatbeanlist
     * @param handler
     */
    public static void getSerialId(final Context context, final String emobId, final ArrayList<FastShopCatBean> fastshopcatbeanlist, final Handler handler) {

        if(fastshopcatbeanlist==null||fastshopcatbeanlist.isEmpty()){
            ToastUtils.showToast(context,"下单失败，数据异常");
            return ;
        }
        OrdersBeanRequest  request = new OrdersBeanRequest();
        request.setCommunityId(PreferencesUtil.getCommityId(context));
        request.setEmobIdUser(emobId);
        request.setEmobIdShop(PreferencesUtil.getFastShopIndexInfo(context).getEmobId());

//        request.setOrderAddress(PreferencesUtil.getUserAddress_floor(context)+PreferencesUtil.getUserAddress_unit(context)+PreferencesUtil.getUserAddress_room(context)); //// 暂定不传

        //// 基于店铺下的所有订单
        for (FastShopCatBean fastShopCatBean : fastshopcatbeanlist) {
            //// 某店的商品总价格
            request.setPrice(fastShopCatBean.getPrice() + "");
            List<ShopGoodsItemBean> shopItems = new ArrayList<>();
            request.setShopItems(shopItems);

            List<FastShopCatModel> fastShopCatModelList = fastShopCatBean.getChildList();
            for (FastShopCatModel  fastShopCatModel : fastShopCatModelList) {

                ShopGoodsItemBean shopGoodsItemBean =new ShopGoodsItemBean();
                shopGoodsItemBean.setPrice(""+fastShopCatModel.getPrice());
                shopGoodsItemBean.setCount(fastShopCatModel.getCount());
                shopGoodsItemBean.setServiceId(fastShopCatModel.getServiceId());
                shopGoodsItemBean.setServiceName(fastShopCatModel.getServiceName());
                if(fastShopCatModel.getShopItemSkuId()!=0){
                    shopGoodsItemBean.setShopItemSkuId(fastShopCatModel.getShopItemSkuId()); ///
                }
                shopItems.add(shopGoodsItemBean);
            }
        }

        OrderIdService service = RetrofitFactory.getInstance().create(context,request,OrderIdService.class);
        Callback<CommonRespBean<String>> callback = new Callback<CommonRespBean<String>>() {
            @Override
            public void success(CommonRespBean<String> bean, retrofit.client.Response response) {
                Log.i("onion","bean"+bean);
                if(bean==null||!TextUtils.equals("yes", bean.getStatus())){
                    Message message=Message.obtain();
                    message.obj=bean.getMessage();
                    message.what=Config.TASKERROR;
                    handler.sendMessage(message);
                    return;
                }
                String serialId = bean.getData();

                if(TextUtils.isEmpty(serialId)){
                    Message message=Message.obtain();
                    message.obj=bean.getMessage();
                    message.what=Config.TASKERROR;
                    handler.sendMessage(message);
                }

                ContactPhoneListBean fastShopIndexInfo = PreferencesUtil.getFastShopIndexInfo(context);
                singleOrder(serialId, fastShopIndexInfo, fastshopcatbeanlist, emobId, handler);
                handler.sendEmptyMessage(Config.TASKCOMPLETE);
            }

            @Override
            public void failure(RetrofitError error) {
               // Toast.makeText(context, error.toString(), 1).show();
                Message message=Message.obtain();
                message.obj=error.toString();
                message.what=Config.TASKERROR;
                handler.sendMessage(message);
                error.printStackTrace();
            }
        };

        service.getOrderId(request,  callback);
    }

    /**
     *
     * v3 2016/03/17
     * @param orderId
     * @param fastShopIndexInfo
     * @param fastShopCatModelList
     * @param userEmobId
     * @param handler
     */
    private static void singleOrder(String orderId,
                                    ContactPhoneListBean fastShopIndexInfo,
                                    ArrayList<FastShopCatBean> fastShopCatModelList, String userEmobId, Handler handler) {
        if(fastShopCatModelList==null||fastShopCatModelList.isEmpty()){
            return;
        }
        //从大集合中找到小订单
        FastShopCatBean fastShopCatBean = null;

        /// v3 不用过滤是否是同一个店铺下的订单  2016/03/17
        Log.i("onion","fastShopCatModelList"+fastShopCatModelList.toString());
        for (FastShopCatBean  fscbean : fastShopCatModelList) {
            if (fastShopIndexInfo.getEmobId().equals(fscbean.shopemboid)) {
                fastShopCatBean = fscbean;
                break;
            }
        }
        if (fastShopCatBean == null) return;
        FastGoodsModel fastGoodsModel = new FastGoodsModel();
        fastGoodsModel.emobIdShop = fastShopIndexInfo.getEmobId();
        fastGoodsModel.sort = 1;
        fastGoodsModel.emobIdUser = userEmobId;

        ArrayList<OrderDetailBeanList> orderDetailBeanLists = new ArrayList<>();
        for (int j = 0; j < fastShopCatBean.getChildList().size(); j++) {
            OrderDetailBeanList orderDetailBeanList = new OrderDetailBeanList();
            FastShopCatModel model = fastShopCatBean.getChildList().get(j);
            orderDetailBeanList.setCount(model.count);
            orderDetailBeanList.setServiceName(model.serviceName);
            orderDetailBeanList.setPrice(model.getPrice()+"");
            orderDetailBeanList.setServiceId(model.serviceId);
            orderDetailBeanLists.add(orderDetailBeanList);
        }

        fastGoodsModel.orderDetailBeanList = orderDetailBeanLists;
        fastGoodsModel.totalPrice = fastShopCatBean.getPrice()+"";
        fastGoodsModel.totalCount =FastShopCarDBUtil.getSumCountByShopId(fastShopIndexInfo.getEmobId(),userEmobId);

        Log.i("onion","fastGoodsModel.totalCount"+fastGoodsModel.totalCount);

        Message message=Message.obtain();
        Object[] objects=new Object[3];
        objects[1]=fastGoodsModel;
        objects[2]=orderId;
        message.obj=objects;
        message.what=Config.SINGLETASKCOMPLETE;
        handler.sendMessage(message);
    }
}
