package xj.property.beans;

/**
 * Created by Administrator on 2015/12/31.
 */
public class AddMemberToGroupReqBean extends  BaseBean {

    /**
     * emobUserId : 123456789
     * groupStatus : block
     */

    private String emobUserId;
    private String groupStatus;

    public void setEmobUserId(String emobUserId) {
        this.emobUserId = emobUserId;
    }

    public void setGroupStatus(String groupStatus) {
        this.groupStatus = groupStatus;
    }

    public String getEmobUserId() {
        return emobUserId;
    }

    public String getGroupStatus() {
        return groupStatus;
    }
}
