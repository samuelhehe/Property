package xj.property.beans;

import java.util.List;

/**
 * Created by maxwell on 15/2/3.
 */
public class FacilityListBean {
    private String status;
    private List<FacilityBean> info;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<FacilityBean> getInfo() {
        return info;
    }

    public void setInfo(List<FacilityBean> info) {
        this.info = info;
    }
}
