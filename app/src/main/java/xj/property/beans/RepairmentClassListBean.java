package xj.property.beans;

import java.util.List;

/**
 * Created by maxwell on 15/3/10.
 */
public class RepairmentClassListBean {

    private String status;
    private List<RepairmentClassBean> info;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public List<RepairmentClassBean> getInfo() {
        return info;
    }

    public void setInfo(List<RepairmentClassBean> info) {
        this.info = info;
    }
}
