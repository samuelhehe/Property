package xj.property.beans;

/**
 * 作者：che on 2016/3/17 12:09
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class PropertyPayHistoryV3Bean implements XJ {
    private int paymentId;//物业缴费订单id
    private double paymentPrice;//订单总额，单位元
    private double money;//实际支付金额，单位元
    private int bonusCoin;//支付使用帮帮币数量
    private int userBonusId;//支付使用的帮帮券ID
    private int bonusPrice;//帮帮券抵扣金额，单位元
    private String status;//订单状态
    private String orderNo;//内部交易号
    private String address;//缴费地址
    private String name;//业主真实姓名
    private int communityOwnerId;//业主数据id
    private int communityId;//小区id
    private int communityPaymentId;//物业缴费信息id
    private String emobId;//业主环信Id
    private Long createTime;//订单创建时间
    private Long updateTime;//订单修改时间
    private int unitCount;//订单缴费月数
    private String shared;//是否分享了物业缴费
    private int area;//物业业主要缴纳的面积
    private String unitPrice;//物业缴费每月每平米的价格
    private int arrearageCount;//缴纳费用中包含的欠费月数信息
    private String paymentExplain;//物业缴费信息说明
    private String phone;//物业缴费联系电话
    private String arrearage;//缴纳费用中包含的欠费月份信息

    /*


     "paymentId": {物业缴费订单ID},
        "paymentPrice": {订单总额，单位元},
        "money": {实际支付金额，单位元},
        "bonusCoin": {支付使用帮帮币数量},
        "userBonusId": {支付使用的帮帮券ID},
        "bonusPrice": {帮帮券抵扣金额，单位元},
        "status": "{订单状态}",
        "orderNo": "{内部交易号}",
        "address": "{缴费地址}",
        "name": "{业主真实姓名}",
        "communityOwnerId": {业主数据ID},
        "communityId": {小区ID},
        "communityPaymentId": {物业缴费信息ID},
        "emobId": "{业主环信ID}",
        "createTime": {订单创建时间},
        "updateTime": {订单修改时间},
        "unitCount": {订单缴费月数}
        "unitPrice": "{物业缴费每月每平米的价格}",
        "area": {物业业主要缴纳的面积},
        "shared": "{是否分享了物业缴费}",
        "arrearage": "{缴纳费用中包含的欠费月份信息}",
        "arrearageCount": {缴纳费用中包含的欠费月数信息},
        "paymentExplain": "{物业缴费信息说明}",
        "phone": "{物业缴费联系电话}"



     */

    public String getArrearage() {
        return arrearage;
    }

    public void setArrearage(String arrearage) {
        this.arrearage = arrearage;
    }

    public int getUserBonusId() {
        return userBonusId;
    }

    public void setUserBonusId(int userBonusId) {
        this.userBonusId = userBonusId;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public double getPaymentPrice() {
        return paymentPrice;
    }

    public void setPaymentPrice(double paymentPrice) {
        this.paymentPrice = paymentPrice;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getBonusCoin() {
        return bonusCoin;
    }

    public void setBonusCoin(int bonusCoin) {
        this.bonusCoin = bonusCoin;
    }

    public int getBonusPrice() {
        return bonusPrice;
    }

    public void setBonusPrice(int bonusPrice) {
        this.bonusPrice = bonusPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCommunityOwnerId() {
        return communityOwnerId;
    }

    public void setCommunityOwnerId(int communityOwnerId) {
        this.communityOwnerId = communityOwnerId;
    }

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public int getCommunityPaymentId() {
        return communityPaymentId;
    }

    public void setCommunityPaymentId(int communityPaymentId) {
        this.communityPaymentId = communityPaymentId;
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

    public int getUnitCount() {
        return unitCount;
    }

    public void setUnitCount(int unitCount) {
        this.unitCount = unitCount;
    }

    public String getShared() {
        return shared;
    }

    public void setShared(String shared) {
        this.shared = shared;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getArrearageCount() {
        return arrearageCount;
    }

    public void setArrearageCount(int arrearageCount) {
        this.arrearageCount = arrearageCount;
    }

    public String getPaymentExplain() {
        return paymentExplain;
    }

    public void setPaymentExplain(String paymentExplain) {
        this.paymentExplain = paymentExplain;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
