package xj.property.beans;

/**
 * Created by Administrator on 2015/10/16.
 * v3 2016/02/29
 */
public class MspBangbiRespPayBean {


    /**
     * nearbyVipcardOrderId : 22
     * communityId : 1
     * emobIdUser : cd4f67bd91fe4a3db351254801189df7
     * emobIdShop : 38dfc72648939d5f0150f5cbdcb62a95
     * orderNo : 1030020160322153132240356
     * createTime : 1458631892
     * orderPrice : 0.1
     * bonuscoin : 8
     * money : 0
     * status : paid
     * discount : 8.0
     * discountPrice : 0.08
     */

    private int nearbyVipcardOrderId;
    private int communityId;
    private String emobIdUser;
    private String emobIdShop;
    private String orderNo;
    private int createTime;
    private double orderPrice;
    private int bonuscoin;
    private int money;
    private String status;
    private double discount;
    private double discountPrice;

    public void setNearbyVipcardOrderId(int nearbyVipcardOrderId) {
        this.nearbyVipcardOrderId = nearbyVipcardOrderId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public void setEmobIdUser(String emobIdUser) {
        this.emobIdUser = emobIdUser;
    }

    public void setEmobIdShop(String emobIdShop) {
        this.emobIdShop = emobIdShop;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public void setBonuscoin(int bonuscoin) {
        this.bonuscoin = bonuscoin;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public int getNearbyVipcardOrderId() {
        return nearbyVipcardOrderId;
    }

    public int getCommunityId() {
        return communityId;
    }

    public String getEmobIdUser() {
        return emobIdUser;
    }

    public String getEmobIdShop() {
        return emobIdShop;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public int getCreateTime() {
        return createTime;
    }

    public double getOrderPrice() {
        return orderPrice;
    }

    public int getBonuscoin() {
        return bonuscoin;
    }

    public int getMoney() {
        return money;
    }

    public String getStatus() {
        return status;
    }

    public double getDiscount() {
        return discount;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }
}
