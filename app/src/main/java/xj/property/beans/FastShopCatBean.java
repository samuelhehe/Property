package xj.property.beans;

import java.util.ArrayList;

import xj.property.cache.FastShopCatModel;

/**
 * Created by Administrator on 2015/4/2.
 */
public class FastShopCatBean implements XJ {

    private int shopId;
    private String shopName;
    private double price;
    private ArrayList<FastShopCatModel> childList;
    public String shopemboid;

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ArrayList<FastShopCatModel> getChildList() {
        return childList;
    }

    public void setChildList(ArrayList<FastShopCatModel> childList) {
        this.childList = childList;
        if (!childList.isEmpty())
            this.shopemboid = childList.get(0).getShopEmobId();
    }

    @Override
    public String toString() {
        return "FastShopCatBean{" +
                "shopId=" + shopId +
                ", shopName='" + shopName + '\'' +
                ", price=" + price +
                ", childList=" + childList +
                ", shopemboid='" + shopemboid + '\'' +
                '}';
    }
}
