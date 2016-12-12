package xj.property.beans;

import java.io.Serializable;

public class MspCardGetOrderInfoRequest extends xj.property.netbase.BaseBean implements Serializable{

    /*
    {
    "beanId": "alipayNearbyVipcard",
    "subject": "{商品名称}",
    "cityId": {城市ID},
    "communityId": {小区ID},
    "emobId": "{用户环信ID}",
    "shopEmobId": {周边店家环信ID},
    "totalFee": "{通过会员卡折扣之后的金额，单位元}",
    "orderPrice": {订单总价},
    "discount": {订单折扣},
    "bonuscoinCount": {帮帮币数量}
    }
     */

    private String beanId="alipayNearbyVipcard";
    private String subject;
    private int cityId;
    private int communityId;
    private String emobId;
    private String shopEmobId;
    private String totalFee;
    private String orderPrice;
    private String discount;
    private int bonuscoinCount;

    public void setBeanId(String beanId) {
        this.beanId = beanId;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    public void setShopEmobId(String shopEmobId) {
        this.shopEmobId = shopEmobId;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public void setBonuscoinCount(int bonuscoinCount) {
        this.bonuscoinCount = bonuscoinCount;
    }

    public String getBeanId() {
        return beanId;
    }

    public String getSubject() {
        return subject;
    }

    public int getCityId() {
        return cityId;
    }

    public int getCommunityId() {
        return communityId;
    }

    public String getEmobId() {
        return emobId;
    }

    public String getShopEmobId() {
        return shopEmobId;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public String getDiscount() {
        return discount;
    }

    public int getBonuscoinCount() {
        return bonuscoinCount;
    }

}
