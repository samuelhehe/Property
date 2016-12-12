package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/4/9.
 *
 * 历史订单
 * v3 2016/03/17
 *
 */
public class HistoryFastShopBean {



    /*

     {
        "page": {页码},
        "limit": {页面大小},
        "data": [{
            "shopOrderId": {订单ID},
            "serial": "{订单号}",
            "money": {订单总价},
            "endTime": {订单结束时间},
            "shopItems": [{
                "status": "{商品状态}",
                "shopItemSkuId": {商品规格ID},
                "serviceId": {商品ID},
                "serviceName": "{商品名称}",
                "serviceImg": "{商品图片}",
                "originPrice": "{原价}",
                "currentPrice": "{现价}",
                "price": "{购买时的价格}",
                "count": {购买数量},
                "shopId": {店家ID},
                "shopName": "{店家名称}",
                "shopEmobId": "{店主环信ID}"
            }]
        }]
    }

     */





    public String serial;

    public String shopOrderId;
    public String money;
    public int endTime;
    public String emobIdShop;
    public List<ShopBean> shopItems;


    public String getShopOrderId() {
        return shopOrderId;
    }

    public void setShopOrderId(String shopOrderId) {
        this.shopOrderId = shopOrderId;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public List<ShopBean> getShopItems() {
        return shopItems;
    }

    public void setShopItems(List<ShopBean> shopItems) {
        this.shopItems = shopItems;
    }

    public String getEmobIdShop() {
        return emobIdShop;
    }

    public void setEmobIdShop(String emobIdShop) {
        this.emobIdShop = emobIdShop;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public static class ShopBean implements XJ {


        /*

        "status": "{商品状态}",
                "shopItemSkuId": {商品规格ID},
                "serviceId": {商品ID},
                "serviceName": "{商品名称}",
                "serviceImg": "{商品图片}",
                "originPrice": "{原价}",
                "currentPrice": "{现价}",
                "price": "{购买时的价格}",
                "count": {购买数量},
                "shopId": {店家ID},
                "shopName": "{店家名称}",
                "shopEmobId": "{店主环信ID}"


         */

        public String shopName;///店家名称
        public String serviceName;///商品名称
        public String serviceImg;///商品图片
        public String score;
        public String currentPrice;//currentPrice
        public String originPrice; //原价
        public String shopEmobId;//店主环信ID
        public int count;//购买数量
        public int serviceId;//商品ID
        public int shopId;//店家ID
        public String status;//商品状态
        public String shopItemSkuId;//商品规格ID


        public String getShopItemSkuId() {
            return shopItemSkuId;
        }

        public void setShopItemSkuId(String shopItemSkuId) {
            this.shopItemSkuId = shopItemSkuId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public String getServiceImg() {
            return serviceImg;
        }

        public void setServiceImg(String serviceImg) {
            this.serviceImg = serviceImg;
        }

        public String getShopEmobId() {
            return shopEmobId;
        }

        public void setShopEmobId(String shopEmobId) {
            this.shopEmobId = shopEmobId;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getServiceId() {
            return serviceId;
        }

        public void setServiceId(int serviceId) {
            this.serviceId = serviceId;
        }

        public int getShopId() {
            return shopId;
        }

        public void setShopId(int shopId) {
            this.shopId = shopId;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getCurrentPrice() {
            return currentPrice;
        }

        public void setCurrentPrice(String currentPrice) {
            this.currentPrice = currentPrice;
        }

        public String getOriginPrice() {
            return originPrice;
        }

        public void setOriginPrice(String originPrice) {
            this.originPrice = originPrice;
        }
    }
}
