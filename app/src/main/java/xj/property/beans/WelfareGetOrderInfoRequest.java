package xj.property.beans;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/5/15.
 */
public class WelfareGetOrderInfoRequest extends xj.property.netbase.BaseBean implements Serializable {

    public String subject; /// 商品名称

    public String price; /// 商品总价格

    public String beanId = "alipayWelfare";  /// 标识  2016/02/29

    private String bonusCoinCount; /// 用了多少帮帮币


    private int dataId;///福利ID


    private int cityId;

    private int communityId;

    private int bonusId;  /// {帮帮券ID},

    private String emobId;
    /**
     * totalFee : 233
     * bonuscoinCount : 100
     */

    private String totalFee;
    private int bonuscoinCount;


    //    {
//        "beanId": "alipayWelfare",
//            "totalFee": "{支付金额，未减去帮帮券和帮帮币折扣之前的金额，单位元}",
//            "subject": "{商品名称}",
//            "cityId": {城市ID},
//        "communityId": {小区ID},
//        "emobId": "{用户环信ID}",
//            "dataId": {福利ID},
//        "bonuscoinCount": {帮帮币数量}
//    }
//
    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public int getBonusId() {
        return bonusId;
    }

    public void setBonusId(int bonusId) {
        this.bonusId = bonusId;
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


    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }


    public String getBeanId() {
        return beanId;
    }

    public void setBeanId(String beanId) {
        this.beanId = beanId;
    }

    public String getBonusCoinCount() {
        return bonusCoinCount;
    }

    public void setBonusCoinCount(String bonusCoinCount) {
        this.bonusCoinCount = bonusCoinCount;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public void setBonuscoinCount(int bonuscoinCount) {
        this.bonuscoinCount = bonuscoinCount;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public int getBonuscoinCount() {
        return bonuscoinCount;
    }
}
