package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/5/7.
 */
public class Floor {
public String userFloor;
    public List<UnitRoomBean> list;

    public String getUserFloor() {
        return userFloor;
    }

    public void setUserFloor(String userFloor) {
        this.userFloor = userFloor;
    }

    public List<UnitRoomBean> getList() {
        return list;
    }

    public void setList(List<UnitRoomBean> list) {
        this.list = list;
    }
}
