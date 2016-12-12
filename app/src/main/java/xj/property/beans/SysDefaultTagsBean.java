package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/10/30.
 */
public class SysDefaultTagsBean {

    /**
     * status : yes
     * info : ["中国好邻居","天华好邻居"]
     * createTime : 1446200179
     */

    private String status;

    private String message;


    private int createTime;
    private List<String> info;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public void setInfo(List<String> info) {
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public int getCreateTime() {
        return createTime;
    }

    public List<String> getInfo() {
        return info;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
