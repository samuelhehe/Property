package xj.property.beans;

/**
 * Created by Administrator on 2015/4/17.
 */
public class ActivitiesSearchBean {
public String status;

    public ActivitiesBean info;

    private String others;//活动首页条幅地址

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ActivitiesBean getInfo() {
        return info;
    }

    public void setInfo(ActivitiesBean info) {
        this.info = info;
    }
}
