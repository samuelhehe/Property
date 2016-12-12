package xj.property.beans;

import java.util.List;

/**
 * Created by che on 2015/6/8.
 */
public class TimeLineBean {
    String time ;
    List<TimeLineBeanDetail> info;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<TimeLineBeanDetail> getInfo() {
        return info;
    }

    public void setInfo(List<TimeLineBeanDetail> info) {
        this.info = info;
    }


}
