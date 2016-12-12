package xj.property.beans;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/4/3.
 */
public class FastGoodsModel {
    public int totalCount;
    public int sort;
    public String totalPrice;
    public String emobIdShop;

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

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
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

    public String emobIdUser;

    public ArrayList<OrderDetailBeanList> orderDetailBeanList;
}
