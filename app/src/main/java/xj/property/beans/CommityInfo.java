package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/4/24.
 */
public class CommityInfo {
    public String status;
public List<CommitiyBean> info;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CommitiyBean> getInfo() {
        return info;
    }

    public void setInfo(List<CommitiyBean> info) {
        this.info = info;
    }

    public static class CommitiyBean{
        public int communityId;
        public String communityName;
        public int  population;
        public String communitiesDesc;
        public double longitude;
        public double latitude;
        public String shopServices;
        public String status;
        public String communityAddress;
        public String facilityServices;
        public int cityId;
        public int createTime;

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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCommunityAddress() {
            return communityAddress;
        }

        public void setCommunityAddress(String communityAddress) {
            this.communityAddress = communityAddress;
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

        public int getCreateTime() {
            return createTime;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }
    }
}
