package xj.property.beans;

import java.util.List;

import xj.property.netbasebean.ShopGoodsItemBean;

/**
 * Created by Administrator on 2015/5/5.
 *
 * v3 2016/03/17
 */
public class OrdersBeanRequest extends xj.property.netbase.BaseBean{

    /*
    "price": "{订单总价}",
    "communityId": {小区ID},
    "emobIdShop": "{店主环信ID}",
    "emobIdUser": "{顾客环信ID}",
    "orderAddress": "{顾客地址}",
    "shopItems":[{
        "shopItemSkuId": {商品规格ID},
        "serviceId": {商品ID},
        "serviceName": "{商品名称}",
        "price": "{商品单价}",
        "count": {购买数量}
    }]
     */


    /**
     * price : 150
     * communityId : 2
     * emobIdShop : fcb6adf78bef4ee4940d2af8ee7905f9
     * emobIdUser : d463b16dfc014466a1e441dd685ba505
     * orderAddress : 2号楼2单元202
     * shopItems : [
     * {"shopItemSkuId":4,"serviceId":56,"serviceName":"雪花啤酒 听 500ml","price":"5","count":4},
     * {"serviceId":59,"serviceName":"海之蓝 棉柔性白酒500ml 42c度","price":"130","count":1}]
     */

    private String price;
    private int communityId;
    private String emobIdShop;
    private String emobIdUser;

//    "price": "150",
//            "communityId": 2,
//            "emobIdShop": "fcb6adf78bef4ee4940d2af8ee7905f9",
//            "emobIdUser": "d463b16dfc014466a1e441dd685ba505",
//            "online": "yes",

    private List<ShopGoodsItemBean> shopItems;

    public void setPrice(String price) {
        this.price = price;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public void setEmobIdShop(String emobIdShop) {
        this.emobIdShop = emobIdShop;
    }

    public void setEmobIdUser(String emobIdUser) {
        this.emobIdUser = emobIdUser;
    }

    public String getPrice() {
        return price;
    }

    public int getCommunityId() {
        return communityId;
    }

    public String getEmobIdShop() {
        return emobIdShop;
    }

    public String getEmobIdUser() {
        return emobIdUser;
    }


    public List<ShopGoodsItemBean> getShopItems() {
        return shopItems;
    }

    public void setShopItems(List<ShopGoodsItemBean> shopItems) {
        this.shopItems = shopItems;
    }
}
