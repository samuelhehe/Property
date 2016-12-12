package xj.property.beans;

import java.util.List;

/**
 * Created by maxwell on 15/2/1.
 */
public class ShopItemBean {
    private int catId;
    private String catName;
    private List<ShopItemDetailBean> list;

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public List<ShopItemDetailBean> getList() {
        return list;
    }

    public void setList(List<ShopItemDetailBean> list) {
        this.list = list;
    }
}

