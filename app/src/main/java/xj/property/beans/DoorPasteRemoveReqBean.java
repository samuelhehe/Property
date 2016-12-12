package xj.property.beans;

/**
 * Created by Administrator on 2016/3/24.
 */
public class DoorPasteRemoveReqBean extends  xj.property.netbase.BaseBean {


    /**
     * emobId : d463b16dfc014466a1e441dd685ba505
     * times : 3
     */

    private String emobId;
    private int times;

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String getEmobId() {
        return emobId;
    }

    public int getTimes() {
        return times;
    }
}
