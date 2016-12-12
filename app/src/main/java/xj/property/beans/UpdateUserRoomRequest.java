package xj.property.beans;

/**
 * Created by n on 2015/4/22.
 */
public class UpdateUserRoomRequest extends BaseBean implements XJ {
    private String room;
    private String userFloor;
    private String userUnit;

    public String getUserFloor() {
        return userFloor;
    }

    public void setUserFloor(String userFloor) {
        this.userFloor = userFloor;
    }

    public String getUserUnit() {
        return userUnit;
    }

    public void setUserUnit(String userUnit) {
        this.userUnit = userUnit;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
