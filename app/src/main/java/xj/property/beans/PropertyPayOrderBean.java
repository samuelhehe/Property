package xj.property.beans;

/**
 * Created by asia on 2015/11/23.
 */
public class PropertyPayOrderBean extends BaseBean {
    private String status;
    private String others;
    private PropertyPayOrderInfoBean info;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public PropertyPayOrderInfoBean getInfo() {
        return info;
    }

    public void setInfo(PropertyPayOrderInfoBean info) {
        this.info = info;
    }
}
