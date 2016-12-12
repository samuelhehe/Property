package xj.property.beans;

/**
 * Created by Administrator on 2015/6/11.
 *
 * v3 2016/03/03
 */
public class UpDateGroupName extends xj.property.netbase.BaseBean {
    public String activityTitle;
    /**
     * emobGroupId : {环信群组ID}
     * emobIdOwner : {群主环信ID}
     */

    private String emobGroupId;
    private String emobIdOwner;


    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public void setEmobGroupId(String emobGroupId) {
        this.emobGroupId = emobGroupId;
    }

    public void setEmobIdOwner(String emobIdOwner) {
        this.emobIdOwner = emobIdOwner;
    }

    public String getEmobGroupId() {
        return emobGroupId;
    }

    public String getEmobIdOwner() {
        return emobIdOwner;
    }
}
