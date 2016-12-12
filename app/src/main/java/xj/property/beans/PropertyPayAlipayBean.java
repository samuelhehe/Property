package xj.property.beans;


/**
 * Created by asia on 2015/11/23.
 */
public class PropertyPayAlipayBean extends BaseBean {
    private String beanId;    //支付类型
    private String price;    //商品金额，支付金额   计算完优惠的价格
    private String subject;    //商品名称
    private String orderId;    //订单ID
    private String paymentPrice;// 未优惠的总价格价格
    private Integer bonusCoinCount;//帮帮币数量
    private Integer userBonusId;//帮帮券id
    private String bonusPrice;//帮帮券价格
    private String type;//缴费类型
    private Integer unitCount;//缴费时间
    private String name;//用户名
    private String adress;//地址
    private Integer communityOwnerId;
    private Integer communityPaymentId;//订单id





    public String getBeanId() {
        return beanId;
    }

    public void setBeanId(String beanId) {
        this.beanId = beanId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentPrice() {
        return paymentPrice;
    }

    public void setPaymentPrice(String paymentPrice) {
        this.paymentPrice = paymentPrice;
    }

    public Integer getBonusCoinCount() {
        return bonusCoinCount;
    }

    public void setBonusCoinCount(Integer bonusCoinCount) {
        this.bonusCoinCount = bonusCoinCount;
    }

    public Integer getUserBonusId() {
        return userBonusId;
    }

    public void setUserBonusId(Integer userBonusId) {
        this.userBonusId = userBonusId;
    }

    public String getBonusPrice() {
        return bonusPrice;
    }

    public void setBonusPrice(String bonusPrice) {
        this.bonusPrice = bonusPrice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getUnitCount() {
        return unitCount;
    }

    public void setUnitCount(Integer unitCount) {
        this.unitCount = unitCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Integer getCommunityOwnerId() {
        return communityOwnerId;
    }

    public void setCommunityOwnerId(Integer communityOwnerId) {
        this.communityOwnerId = communityOwnerId;
    }

    public Integer getCommunityPaymentId() {
        return communityPaymentId;
    }

    public void setCommunityPaymentId(Integer communityPaymentId) {
        this.communityPaymentId = communityPaymentId;
    }
}