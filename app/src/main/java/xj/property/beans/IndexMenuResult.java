package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/4/28.
 */
public class IndexMenuResult {
    private String status;
    private List<IndexBean> info;
    private int createTime;

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<IndexBean> getInfo() {
        return info;
    }

    public void setInfo(List<IndexBean> info) {
        this.info = info;
    }
}
