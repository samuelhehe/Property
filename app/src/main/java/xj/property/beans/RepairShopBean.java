package xj.property.beans;

/**
 * Created by maxwell on 15/3/10.
 */
public class RepairShopBean {

    private int shopId;
    private String shopName;
    private String score;
    private String status;
    private String emobId;
    private String shopsCategoryId;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmobId() {
        return emobId;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    public String getShopsCategoryId() {
        return shopsCategoryId;
    }

    public void setShopsCategoryId(String shopsCategoryId) {
        this.shopsCategoryId = shopsCategoryId;
    }
}
