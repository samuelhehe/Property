package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/5/7.
 */
public class UnitRoomBean {

    public String userUnit;
    public List<String> userRooms;

    public String getUserUnit() {
        return userUnit;
    }

    public void setUserUnit(String userUnit) {
        this.userUnit = userUnit;
    }

    public List<String> getUserRooms() {
        return userRooms;
    }

    public void setUserRooms(List<String> userRooms) {
        this.userRooms = userRooms;
    }
}
