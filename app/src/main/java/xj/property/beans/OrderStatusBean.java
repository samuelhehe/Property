package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/3/23.
 */
public class OrderStatusBean {
    public String  status;
public String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OrderStatusBean{" +
                "status='" + status + '\'' +
                '}';
    }
}
