package xj.property.beans;

/**
 * 作者：che on 2016/3/16 16:58
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class ComplainRequestV3 extends xj.property.netbase.BaseBean {

    private Integer communityId;
    private String emobIdFrom;
    private String emobIdTo;
    private String detail;
    private String type;



    private String orderId ;// v3 216/03/17 订单ID , 针对快店添加的兼容字段


    /**
     * {
     "communityId": {小区ID},
     "emobIdFrom": "{投诉顾客环信ID}",
     "emobIdTo": "{被投诉店家环信ID}",
     "detail": "{投诉内容}",
     "type": "shop",
     "orderId": {订单ID}
     *
     */



    public String getEmobIdFrom() {
        return emobIdFrom;
    }

    public void setEmobIdFrom(String emobIdFrom) {
        this.emobIdFrom = emobIdFrom;
    }

    public String getEmobIdTo() {
        return emobIdTo;
    }

    public void setEmobIdTo(String emobIdTo) {
        this.emobIdTo = emobIdTo;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }
}
