package xj.property.beans;


/**
 * Created by asia on 2015/11/23.
 */
public class PropertyPayAlipayV3Bean extends xj.property.netbase.BaseBean {
    private String beanId;
    private String subject;//商品名称}
    private int cityId;//城市id
    private int communityId;//小区id
    private String emobId;//用户环信id
    private int bonusId;//帮帮券ID
    private int bonuscoinCount;//帮帮币数量
    private String name;//缴费用户的真实姓名
    private String address;//缴费用户的真实地址
    private int unitCount;//缴费单位数量
    private int communityOwnerId;//小区业主信息ID
    private int communityPaymentId;//小区缴费信息ID

    public String getBeanId() {
        return beanId;
    }

    public void setBeanId(String beanId) {
        this.beanId = beanId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public String getEmobId() {
        return emobId;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    public int getBonusId() {
        return bonusId;
    }

    public void setBonusId(int bonusId) {
        this.bonusId = bonusId;
    }

    public int getBonuscoinCount() {
        return bonuscoinCount;
    }

    public void setBonuscoinCount(int bonuscoinCount) {
        this.bonuscoinCount = bonuscoinCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getUnitCount() {
        return unitCount;
    }

    public void setUnitCount(int unitCount) {
        this.unitCount = unitCount;
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
}