package xj.property.beans;

/**
 * Created by asia on 2015/11/23.
 */
public class PropertyPayWeiXinBean extends BaseBean {
    private String beanId;	//支付类型
    private String price;	//商品金额，支付金额
    private String subject;	//商品名称
    private String orderId;	//订单ID
    private String paymentPrice; // 订单总价
    private String bonusCoinCount;	 //帮帮币数量
    private Integer userBonusId;     //帮帮券id
    private String bonusPrice;	 //帮帮券价格
    private String type ;		 //缴费类型
    private Integer unitCount;
    private String name;
    private String adress;
    private Integer communityOwnerId;//缴费id
    private Integer communityId;  //用户小区ID
    private String emobId; //抢购福利的用户的环信ID
    private String body;  //福利名称
    private String dataId;//福利id
    private Integer totalFee;//订单金额，单位分，整数
    private String tradeType;//交易类型，默认为APP
    private Integer communityPaymentId;

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

    public String getBonusCoinCount() {
        return bonusCoinCount;
    }

    public void setBonusCoinCount(String bonusCoinCount) {
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

    public String getEmobId() {
        return emobId;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public Integer getCommunityPaymentId() {
        return communityPaymentId;
    }

    public void setCommunityPaymentId(Integer communityPaymentId) {
        this.communityPaymentId = communityPaymentId;
    }

    public Integer getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }
}
