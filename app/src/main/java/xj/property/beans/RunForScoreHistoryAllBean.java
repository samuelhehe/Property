package xj.property.beans;

/**
 * Created by asia on 2015/11/23.
 */
public class RunForScoreHistoryAllBean {
    public String status;

    public RunForScoreHistoryInfoBean info;//我自己投票的信息

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RunForScoreHistoryInfoBean getInfo() {
        return info;
    }

    public void setInfo(RunForScoreHistoryInfoBean info) {
        this.info = info;
    }
}
