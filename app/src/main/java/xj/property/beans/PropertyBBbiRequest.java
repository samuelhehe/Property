package xj.property.beans;

/**
 * 作者：asia on 2015/12/8 15:23
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class PropertyBBbiRequest  extends BaseBean{

    private String paymentPrice;      // 订单总价
    private String money;
    private Integer bonusCoinCount;    //帮帮币数量
    private String type;              //缴费类型
    private Integer unitCount;        //订单单价
    private String name;              //用户名称
    private String adress;            //地址
    private Integer communityOwnerId; //缴费id



    private Integer communityId;      //用户小区ID
    private String emobId;            //抢购福利的用户的环信ID
    private Integer communityPaymentId;
    private String status;            //订单状态  Property  模块默认 paid
    private Integer bonusCoin;        //话费帮帮币的个数
    private Integer bonusPrice;
    private String serial;
    private Long createTime;
    private Long updateTime;
    private int paymentId;

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentPrice() {
        return paymentPrice;
    }

    public void setPaymentPrice(String paymentPrice) {
        this.paymentPrice = paymentPrice;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public Integer getBonusCoinCount() {
        return bonusCoinCount;
    }

    public void setBonusCoinCount(Integer bonusCoinCount) {
        this.bonusCoinCount = bonusCoinCount;
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

    public Integer getCommunityPaymentId() {
        return communityPaymentId;
    }

    public void setCommunityPaymentId(Integer communityPaymentId) {
        this.communityPaymentId = communityPaymentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getBonusCoin() {
        return bonusCoin;
    }

    public void setBonusCoin(Integer bonusCoin) {
        this.bonusCoin = bonusCoin;
    }

    public Integer getBonusPrice() {
        return bonusPrice;
    }

    public void setBonusPrice(Integer bonusPrice) {
        this.bonusPrice = bonusPrice;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
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
    public Integer getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }
}
