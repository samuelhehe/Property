package xj.property.beans;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/3/16.
 */
public class RepairMenuListBean {
private String status;
    private ArrayList<RepairMenuBean> info;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<RepairMenuBean> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<RepairMenuBean> info) {
        this.info = info;
    }
}
