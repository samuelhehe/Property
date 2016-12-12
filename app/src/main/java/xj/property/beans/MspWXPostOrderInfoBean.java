package xj.property.beans;

/**
 * Created by che on 2015/9/24.
 */
public class MspWXPostOrderInfoBean extends xj.property.netbase.BaseBean {

    /**
     *
     * {
     "beanId": "wxpayShopVipcard",
     "subject": "{商品名称}",
     "cityId": {城市ID},
     "communityId": {小区ID},
     "emobId": "{用户环信ID}",
     "shopEmobId": {周边店家环信ID},
     "totalFee": "{通过会员卡折扣之后的金额，单位分}",
     "orderPrice": {订单总价},
     "discount": {订单折扣},
     "bonuscoinCount": {帮帮币数量}
     }
     *
     *
     * {
     "beanId": "wxpayShopVipcard",
     "subject": "吉野家",
     "cityId": 373,
     "communityId": 2,
     "emobId": "d463b16dfc014466a1e441dd685ba505",
     "shopEmobId": "fcb6adf78bef4ee4940d2af8ee7905f9",
     "totalFee": "9000",
     "orderPrice": "100",
     "discount": "9",
     "bonuscoinCount":1000
     }
     *
     */

    private String beanId= "wxpayNearbyVipcard";

    private String subject;
    private int cityId;
    private int communityId;
    private String emobId;
    private String shopEmobId;
    private String totalFee;
    private String orderPrice;
    private String discount;
    private int bonuscoinCount;

    /*

    {
    "beanId": "wxpayNearbyVipcard",
    "subject": "吉野家",
    "cityId": 373,
    "communityId": 2,
    "emobId": "d463b16dfc014466a1e441dd685ba505",
    "shopEmobId": "fcb6adf78bef4ee4940d2af8ee7905f9",
    "totalFee": "9000",
    "orderPrice": "100",
    "discount": "9",
    "bonuscoinCount":1000
}


     */

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

    public String getBeanId() {
        return beanId;
    }

    public void setBeanId(String beanId) {
        this.beanId = beanId;
    }
}
