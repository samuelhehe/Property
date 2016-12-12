package xj.property.beans;

/**
 * Created by Administrator on 2015/10/16.
 * v3 2016/03/14
 */
public class RatingReqBean extends xj.property.netbase.BaseBean {

    /**
     * {
     "star": {评价星级},
     "orderNo": "{订单号}",
     "emobIdUser": "{评价用户环信ID}"
     }
     */

    private String emobIdUser;

    private String orderNo;
    private String star;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getEmobIdUser() {
        return emobIdUser;
    }

    public void setEmobIdUser(String emobIdUser) {
        this.emobIdUser = emobIdUser;
    }
    public void setStar(String star) {
        this.star = star;
    }

    public String getStar() {
        return star;
    }
}
