package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/3/23.
 */
public class OrderBean {


    public String emobIdShop;
    public List<OrderDetailBeanList> orderDetailBeanList;
    public String orderPrice = "";
    public String action = "";

    public String getEmobIdShop() {
        return emobIdShop;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setEmobIdShop(String emobIdShop) {
        this.emobIdShop = emobIdShop;
    }

    public List<OrderDetailBeanList> getOrderDetailBeanList() {
        return orderDetailBeanList;
    }

    public void setOrderDetailBeanList(List<OrderDetailBeanList> orderDetailBeanList) {
        this.orderDetailBeanList = orderDetailBeanList;
    }


}
