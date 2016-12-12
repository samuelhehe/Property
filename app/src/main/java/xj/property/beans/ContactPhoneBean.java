package xj.property.beans;

/**
 * Created by chenxiangyu on 2015/3/30.
 *
 * v3 2016/03/17
 */
public class ContactPhoneBean implements XJ {
    /**
     * catId : 1
     * catName : 特卖
     * catDesc : 减价
     */

        /*
    "catId": {商品分类ID},
            "catName": "{商品分类名称}",
            "catDesc": "{商品分类描述}"
     */
    private int catId;
    private String catName;
    private String catDesc;

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public void setCatDesc(String catDesc) {
        this.catDesc = catDesc;
    }

    public int getCatId() {
        return catId;
    }

    public String getCatName() {
        return catName;
    }

    public String getCatDesc() {
        return catDesc;
    }
}
