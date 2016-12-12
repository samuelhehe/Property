package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/3/23.
 */
public class SerialIdBean {
    private String status;
    public List<OrderMap> resultOrders;
    public String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<OrderMap> getResultOrders() {
        return resultOrders;
    }

    public void setResultOrders(List<OrderMap> resultOrders) {
        this.resultOrders = resultOrders;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public static class OrderMap {
        public ShopInfoBean shop;
        public String orderId;

        public ShopInfoBean getShop() {
            return shop;
        }

        public void setShop(ShopInfoBean shop) {
            this.shop = shop;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }
    }

}
