package xj.property.beans;

/**
 * Created by Administrator on 2015/11/24.
 * v3 2016/02/29
 */
public class BangBiPayRespBean {


    /**
     *
     *
     *
     * {
     "shopOrderId": {快店订单数据ID},
     "serial": "{快店订单号}",
     "orderNo": "{内部交易号}",
     "orderPrice": {订单金额，单位元},
     "money": {实际支付金额，单位元},
     "communityId": {用户所属小区ID},
     "emobIdUser": "{用户环信ID}",
     "emobIdShop": "{店家环信ID}",
     "startTime": {订单开始时间},
     "endTime": {订单完成时间},
     "status": "{订单状态}",
     "share": "{是否分享}"
     }
     *
     *
     *
     *
     * shopOrderId : 1
     * serial : 1503271817510654
     * orderNo : 1010020160226141412115141
     * orderPrice : 10.5
     * money : 0
     * communityId : 2
     * emobIdUser : d463b16dfc014466a1e441dd685ba505
     * emobIdShop : fcb6adf78bef4ee4940d2af8ee7905f9
     * startTime : 1456463327
     * endTime : 1456467251
     * status : paid
     * share : no
     */

    private int shopOrderId;
    private String serial;
    private String orderNo;
    private double orderPrice;
    private int money;
    private int communityId;
    private String emobIdUser;
    private String emobIdShop;
    private int startTime;
    private int endTime;
    private String status;
    private String share;

    public void setShopOrderId(int shopOrderId) {
        this.shopOrderId = shopOrderId;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public void setEmobIdUser(String emobIdUser) {
        this.emobIdUser = emobIdUser;
    }

    public void setEmobIdShop(String emobIdShop) {
        this.emobIdShop = emobIdShop;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public int getShopOrderId() {
        return shopOrderId;
    }

    public String getSerial() {
        return serial;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public double getOrderPrice() {
        return orderPrice;
    }

    public int getMoney() {
        return money;
    }

    public int getCommunityId() {
        return communityId;
    }

    public String getEmobIdUser() {
        return emobIdUser;
    }

    public String getEmobIdShop() {
        return emobIdShop;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public String getStatus() {
        return status;
    }

    public String getShare() {
        return share;
    }
}
