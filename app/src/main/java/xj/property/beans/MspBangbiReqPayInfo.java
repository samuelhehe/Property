package xj.property.beans;

/**
 * Created by Administrator on 2015/9/18.
 * v3
 */
public class MspBangbiReqPayInfo extends xj.property.netbase.BaseBean {

    /**
     * {
     * "communityId": "{小区ID}",
     * "emobIdUser": "{用户环信ID}",
     * "emobIdShop": "{店家环信ID}",
     * "orderPrice": "{订单价格，单位元}",
     * "discount": "{会员卡折扣}",
     * "discountPrice": "{打折之后的价格，单位元}",
     * "bonuscoin": "{帮帮币数量}"
     * }
     */

    private String emobIdShop;
    private String emobIdUser;
    private int communityId;
    private int bonuscoin;
    private String orderPrice;
    private String discount;

    private String discountPrice; //// 折扣后价格 10/22 添加


    /*
    {
    "communityId": {小区ID},
    "emobIdUser": "{用户环信ID}",
    "emobIdShop": "{店家环信ID}",
    "orderPrice": "{订单价格，单位元}",
    "discount": "{会员卡折扣}",
    "discountPrice": "{打折之后的价格，单位元}",
    "bonuscoin": {帮帮币数量}
}


     */







    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public int getBonuscoin() {
        return bonuscoin;
    }

    public void setBonuscoin(int bonuscoin) {
        this.bonuscoin = bonuscoin;
    }

    public void setEmobIdShop(String emobIdShop) {
        this.emobIdShop = emobIdShop;
    }

    public void setEmobIdUser(String emobIdUser) {
        this.emobIdUser = emobIdUser;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getEmobIdShop() {
        return emobIdShop;
    }

    public String getEmobIdUser() {
        return emobIdUser;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }
}
