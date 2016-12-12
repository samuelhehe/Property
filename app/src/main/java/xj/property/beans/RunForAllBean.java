package xj.property.beans;

/**
 * Created by asia on 2015/11/23.
 */
public class RunForAllBean {
    public String status;

    public RunForAllInfoBean info;//我自己投票的信息

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RunForAllInfoBean getInfo() {
        return info;
    }

    public void setInfo(RunForAllInfoBean info) {
        this.info = info;
    }
}
