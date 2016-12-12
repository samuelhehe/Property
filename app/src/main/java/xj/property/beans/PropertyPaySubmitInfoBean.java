package xj.property.beans;

/**
 * Created by asia on 2015/11/23.
 */
public class PropertyPaySubmitInfoBean {
    private int communityId;
    private String floor;
    private String unit;
    private String room;
    private String name;
    private String area;
    private int communityOwnerId;

    private PropertyPaySubmitPayInfoBean communityPayment;

    public int getCommunityOwnerId() {
        return communityOwnerId;
    }

    public void setCommunityOwnerId(int communityOwnerId) {
        this.communityOwnerId = communityOwnerId;
    }

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public PropertyPaySubmitPayInfoBean getCommunityPayment() {
        return communityPayment;
    }

    public void setCommunityPayment(PropertyPaySubmitPayInfoBean communityPayment) {
        this.communityPayment = communityPayment;
    }
}
