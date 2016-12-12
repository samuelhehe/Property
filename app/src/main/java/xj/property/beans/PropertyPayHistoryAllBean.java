package xj.property.beans;

/**
 * Created by asia on 2015/11/23.
 */
public class PropertyPayHistoryAllBean {
    public String status;

    public PropertyPayHistoryInfoBean info;//我自己投票的信息

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PropertyPayHistoryInfoBean getInfo() {
        return info;
    }

    public void setInfo(PropertyPayHistoryInfoBean info) {
        this.info = info;
    }
}
