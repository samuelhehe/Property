package xj.property.beans;

/**
 * Created by asia on 2015/11/23.
 */
public class PropertyPayAllBean {
    public String status;

    public PropertyPayInfoBean info;//我自己投票的信息

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PropertyPayInfoBean getInfo() {
        return info;
    }

    public void setInfo(PropertyPayInfoBean info) {
        this.info = info;
    }
}
