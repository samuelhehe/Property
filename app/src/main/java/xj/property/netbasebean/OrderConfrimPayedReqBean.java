package xj.property.netbasebean;

import xj.property.netbase.BaseBean;

/**
 * Created by <a> samuelnotes <a href="http://www.samuelnotes.cn">.
 * packageName: xj.property.netbasebean
 * date: 2016/4/1
 * company： xiaojian
 * action:
 */
public class OrderConfrimPayedReqBean extends  BaseBean {


    /**
     * orderPrice : 50
     */

    private String orderPrice;

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderPrice() {
        return orderPrice;
    }
}
