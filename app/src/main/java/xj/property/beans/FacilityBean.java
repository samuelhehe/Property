package xj.property.beans;

/**
 * Created by maxwell on 15/2/3.
 */
public class FacilityBean {
    private int facilitiesClassId;
    private String facilitiesClassName;
    private int communityId;
    private int weight;
    private String picName;

    public int getFacilitiesClassId() {
        return facilitiesClassId;
    }

    public void setFacilitiesClassId(int facilitiesClassId) {
        this.facilitiesClassId = facilitiesClassId;
    }

    public String getFacilitiesClassName() {
        return facilitiesClassName;
    }

    public void setFacilitiesClassName(String facilitiesClassName) {
        this.facilitiesClassName = facilitiesClassName;
    }

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }
}
