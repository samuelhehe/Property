package xj.property.beans;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/2/29.
 */
public class ShopOrderInfoReqBean extends  xj.property.netbase.BaseBean  implements Serializable{


    /**
     *
     *{
     "beanId": "alipayShopOrder",
     "totalFee": "{支付金额，未减去帮帮券和帮帮币折扣之前的金额，单位元}",
     "orderNo": "{订单号}",
     "subject": "{商品名称}",
     "cityId": {城市ID},
     "communityId": {小区ID},
     "emobId": "{用户环信ID}",
     "bonusId": {帮帮券ID},
     "bonuscoinCount": {帮帮币数量}
     }
     *
     * beanId : alipayShopOrder
     * totalFee : 1
     * orderNo : 123123123123
     * subject : 快店商品
     * cityId : 373
     * communityId : 1
     * emobId : d463b16dfc014466a1e441dd685ba505
     * bonuscoinCount : 90
     */

    private String beanId="alipayShopOrder";

    private String totalFee;
    private String orderNo;
    private String subject;
    private int cityId;
    private int communityId;
    private String emobId;
    private Integer bonuscoinCount;
    private Integer bonusId;


    public void setBeanId(String beanId) {
        this.beanId = beanId;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }


    public String getBeanId() {
        return beanId;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public String getOrderNo() {
        return orderNo;
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

    public String getEmobId() {
        return emobId;
    }


    public Integer getBonusId() {
        return bonusId;
    }

    public void setBonusId(Integer bonusId) {
        this.bonusId = bonusId;
    }

    public Integer getBonuscoinCount() {
        return bonuscoinCount;
    }

    public void setBonuscoinCount(Integer bonuscoinCount) {
        this.bonuscoinCount = bonuscoinCount;
    }
}
