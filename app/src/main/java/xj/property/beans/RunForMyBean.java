package xj.property.beans;

/**
 * Created by asia on 2015/11/23.
 */
public class RunForMyBean {
    public String status;

    public RunForBean info;//我自己投票的信息

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RunForBean getInfo() {
        return info;
    }

    public void setInfo(RunForBean info) {
        this.info = info;
    }
}
