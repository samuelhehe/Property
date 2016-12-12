package xj.property.beans;

/**
 * 作者：asia on 2015/12/8 15:23
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class PropertyBBbiRequestV3 extends xj.property.netbase.BaseBean{

    private String emobId;//用户环信ID
    private int communityId;//小区ID
    private int communityOwnerId;//小区业主信息ID
    private int communityPaymentId;//小区物业缴费信息ID
    private int unitCount;//缴费月份
    private int bonusCoin;//帮帮币数量
    private String address;//业主地址
    private String name;//业主真实姓名
    private String photoes;//福利分享图片
    private String lifeContent;//生活圈内容

    /*

    {
    "communityId": {小区ID},
    "emobId": "{用户环信ID}",
    "photoes": "{福利分享图片}",
    "lifeContent": "{生活圈内容}"
    }

     */

    public String getPhotoes() {
        return photoes;
    }

    public void setPhotoes(String photoes) {
        this.photoes = photoes;
    }

    public String getLifeContent() {
        return lifeContent;
    }

    public void setLifeContent(String lifeContent) {
        this.lifeContent = lifeContent;
    }

    public String getEmobId() {
        return emobId;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
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

    public int getUnitCount() {
        return unitCount;
    }

    public void setUnitCount(int unitCount) {
        this.unitCount = unitCount;
    }

    public int getBonusCoin() {
        return bonusCoin;
    }

    public void setBonusCoin(int bonusCoin) {
        this.bonusCoin = bonusCoin;
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
}
