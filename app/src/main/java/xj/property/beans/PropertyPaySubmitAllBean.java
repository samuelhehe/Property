package xj.property.beans;

/**
 * Created by asia on 2015/11/23.
 */
public class PropertyPaySubmitAllBean {
    public String status;

    public PropertyPaySubmitInfoBean info;//我自己投票的信息

    private String message;

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

    public PropertyPaySubmitInfoBean getInfo() {
        return info;
    }

    public void setInfo(PropertyPaySubmitInfoBean info) {
        this.info = info;
    }
}
