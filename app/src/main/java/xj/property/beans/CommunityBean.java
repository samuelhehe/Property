package xj.property.beans;

/**
 * Created by maxwell on 15/2/3.
 */
public class CommunityBean {

    private int communityId;
    private String communityName;
    private int population;
    private String communitiesDesc;
    private double longitude;
    private double latitude;
    private String shopServices;
    private String facilityServices;
    private int cityId;
    private long createTime;
    private String status;

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public String getCommunitiesDesc() {
        return communitiesDesc;
    }

    public void setCommunitiesDesc(String communitiesDesc) {
        this.communitiesDesc = communitiesDesc;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getShopServices() {
        return shopServices;
    }

    public void setShopServices(String shopServices) {
        this.shopServices = shopServices;
    }

    public String getFacilityServices() {
        return facilityServices;
    }

    public void setFacilityServices(String facilityServices) {
        this.facilityServices = facilityServices;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
