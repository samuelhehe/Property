package xj.property.netbasebean;

import xj.property.netbase.BaseBean;

/**
 * Created by Administrator on 2016/3/17.
 */
public class ModifyUserAddressReqBean extends  BaseBean {


    /**
     * userFloor : 2
     * userUnit : 2
     * room : 202
     */

    private String userFloor;
    private String userUnit;
    private String room;

    public void setUserFloor(String userFloor) {
        this.userFloor = userFloor;
    }

    public void setUserUnit(String userUnit) {
        this.userUnit = userUnit;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getUserFloor() {
        return userFloor;
    }

    public String getUserUnit() {
        return userUnit;
    }

    public String getRoom() {
        return room;
    }
}

