package xj.property.beans;

/**
 * Created by asia on 2015/11/23.
 */
public class RunForScoreAllBean {
    public String status;

    public RunForScoreAllPageBean info;//我自己投票的信息

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RunForScoreAllPageBean getInfo() {
        return info;
    }

    public void setInfo(RunForScoreAllPageBean info) {
        this.info = info;
    }
}
