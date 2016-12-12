package xj.property.beans;

/**
 * Created by Administrator on 2015/5/15.
 */
public class OrderInfoBean {

    public String status;
    public String info;

    public String message;

    private String others; /// 订单号



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }
}
