package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/5/7.
 */
public class FloorResult {

    public String status;
    public List<Floor> info;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Floor> getInfo() {
        return info;
    }

    public void setInfo(List<Floor> info) {
        this.info = info;
    }
}
