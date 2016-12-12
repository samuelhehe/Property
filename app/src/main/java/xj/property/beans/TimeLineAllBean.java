package xj.property.beans;

import java.util.List;

/**
 * Created by che on 2015/6/9.
 */
public class TimeLineAllBean {
    String status;
    List<TimeLineBean> allinfo;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TimeLineBean> getAllinfo() {
        return allinfo;
    }

    public void setAllinfo(List<TimeLineBean> allinfo) {
        this.allinfo = allinfo;
    }
}
