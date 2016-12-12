package xj.property.netbasebean;

import xj.property.netbase.BaseBean;

/**
 * Created by Administrator on 2016/3/3.
 */
public class DestoryGroupReqBean extends  BaseBean {

    /**
     * emobGroupId : {环信群组ID}
     * emobIdOwner : {群主环信ID}
     */

    private String emobGroupId;
    private String emobIdOwner;

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
