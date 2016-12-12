package xj.property.beans;

import java.util.List;

/**
 * Created by n on 2015/5/25.
 */
public class BlackListBean implements XJ {
    private String status;
    private List<BlackUserInfo> info;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<BlackUserInfo> getInfo() {
        return info;
    }

    public void setInfo(List<BlackUserInfo> info) {
        this.info = info;
    }


}
