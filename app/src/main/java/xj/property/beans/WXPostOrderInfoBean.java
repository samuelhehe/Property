package xj.property.beans;

/**
 * Created by che on 2015/9/24.
 */
public class WXPostOrderInfoBean extends xj.property.netbase.BaseBean {
    /**
     * beanId : wxpayWelfare
     * totalFee : 2000
     * subject : 帮帮福利
     * cityId : 373
     * communityId : 1
     * dataId : 93
     * emobId : d463b16dfc014466a1e441dd685ba505
     * bonuscoinCount : 100
     */
//    {
//        "beanId": "wxpayWelfare",
//            "totalFee": "{支付金额，未减去帮帮券和帮帮币折扣之前的金额，单位分}",
//            "subject": "{商品名称}",
//            "cityId": {城市ID},
//        "communityId": {小区ID},
//        "emobId": "{用户环信ID}",
//            "dataId": {福利ID},
//        "bonuscoinCount": {帮帮币数量}
//    }


    private String beanId= "wxpayWelfare";
    private String totalFee;  //// 单位分
    private String subject;
    private int cityId;
    private int communityId;
    private int dataId;
    private String emobId;
    private int bonuscoinCount;

    public void setBeanId(String beanId) {
        this.beanId = beanId;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
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

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    public void setBonuscoinCount(int bonuscoinCount) {
        this.bonuscoinCount = bonuscoinCount;
    }

    public String getBeanId() {
        return beanId;
    }

    public String getTotalFee() {
        return totalFee;
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

    public int getDataId() {
        return dataId;
    }

    public String getEmobId() {
        return emobId;
    }

    public int getBonuscoinCount() {
        return bonuscoinCount;
    }






}
