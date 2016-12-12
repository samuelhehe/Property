package xj.property.beans;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/4/2.
 */
public class FastShopCatChildBean implements XJ {
    private FastShopDetailListBean.PagerItemBean bean;
    private int count ;
    private int shopId;

    public FastShopDetailListBean.PagerItemBean getBean() {
        return bean;
    }

    public void setBean(FastShopDetailListBean.PagerItemBean bean) {
        this.bean = bean;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    @Override
    public String toString() {
        return "FastShopCatChildBean{" +
                "bean=" + bean +
                ", count=" + count +
                ", shopId=" + shopId +
                '}';
    }
}
