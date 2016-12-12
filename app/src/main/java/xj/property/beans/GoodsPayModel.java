package xj.property.beans;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/4/3.
 */
public class GoodsPayModel {
   public  int totalCount;
   public  int sort;
   public  double totalPrice ;
public String emobIdShop;
    public String emobIdUser ;

    public ArrayList<OrderDetailBeanList> orderDetailBeanList;
    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getEmobIdShop() {
        return emobIdShop;
    }

    public void setEmobIdShop(String emobIdShop) {
        this.emobIdShop = emobIdShop;
    }

    public String getEmobIdUser() {
        return emobIdUser;
    }

    public void setEmobIdUser(String emobIdUser) {
        this.emobIdUser = emobIdUser;
    }

    public ArrayList<OrderDetailBeanList> getOrderDetailBeanList() {
        return orderDetailBeanList;
    }

    public void setOrderDetailBeanList(ArrayList<OrderDetailBeanList> orderDetailBeanList) {
        this.orderDetailBeanList = orderDetailBeanList;
    }

}
