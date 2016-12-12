package xj.property.beans;

import java.io.Serializable;

/**
 * Created by asia on 2015/11/23.
 */
public class PropertyPayHistoryBean implements Serializable {
    private int paymentId;//物业缴费订单ID
    private float paymentPrice;//物业缴费金额，单位元
    private float money;//物业缴费实际支付金额，单位元
    private int bonusCoin;//物业缴费使用帮帮币数量
    private int userBonusId;//用户的帮帮券ID
    private String type;
    private String status;//订单状态
    private String serial;
    private String emobId;//业主环信ID
    private Long createTime;//订单创建时间
    private Long updateTime;//订单修改时间
    private String name;//业主真实姓名
    private String adress;//缴费地址
    private int communityId;//小区ID
    private int communityOwnerId;//业主数据ID
    private int communityPaymentId;//物业缴费信息ID
    private PropertyPaySubmitPayInfoBean communityPayment;

    /**
     * bonusPrice : 0
     * orderNo : 1040020160226153835525741
     * address : 北京东城天华公馆209
     * unitCount : 12
     */

    private int bonusPrice;//帮帮券抵扣金额，单位元
    private String orderNo;//内部交易号
    private String address;//缴费地址
    private int unitCount;//订单缴费月数

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public float getPaymentPrice() {
        return paymentPrice;
    }

    public void setPaymentPrice(float paymentPrice) {
        this.paymentPrice = paymentPrice;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public int getBonusCoin() {
        return bonusCoin;
    }

    public void setBonusCoin(int bonusCoin) {
        this.bonusCoin = bonusCoin;
    }

    public int getUserBonusId() {
        return userBonusId;
    }

    public void setUserBonusId(int userBonusId) {
        this.userBonusId = userBonusId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getEmobId() {
        return emobId;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
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

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public int getCommunityOwnerId() {
        return communityOwnerId;
    }

    public void setCommunityOwnerId(int communityOwnerId) {
        this.communityOwnerId = communityOwnerId;
    }

    public int getCommunityPaymentId() {
        return communityPaymentId;
    }

    public void setCommunityPaymentId(int communityPaymentId) {
        this.communityPaymentId = communityPaymentId;
    }

    public PropertyPaySubmitPayInfoBean getCommunityPayment() {
        return communityPayment;
    }

    public void setCommunityPayment(PropertyPaySubmitPayInfoBean communityPayment) {
        this.communityPayment = communityPayment;
    }
    public void setBonusPrice(int bonusPrice) {
        this.bonusPrice = bonusPrice;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setUnitCount(int unitCount) {
        this.unitCount = unitCount;
    }

    public int getBonusPrice() {
        return bonusPrice;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public String getAddress() {
        return address;
    }

    public int getUnitCount() {
        return unitCount;
    }
}
