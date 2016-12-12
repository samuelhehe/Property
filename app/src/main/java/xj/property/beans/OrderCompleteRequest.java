package xj.property.beans;

/**
 * Created by Administrator on 2015/4/21.
 */
public class OrderCompleteRequest extends BaseBean{
    public String status;
    public String orderPrice;
    public String online;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }
}
