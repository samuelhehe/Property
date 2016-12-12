package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2016/1/4.
 */
public class LocationVerficationRespBean {

    /**
     * status : yes
     * info : [{"communityId":58,"communityName":"天华公馆","population":0,"communitiesDesc":"","longitude":116.430725,"latitude":39.89178,"shopServices":"1,2,3","facilityServices":"1,2,3","cityId":38,"cityName":"北京","createTime":1448855232,"status":"available","communityAddress":"","distance":56},{"communityId":1,"communityName":"天华","population":2000,"communitiesDesc":"xingfuxiaoqu","longitude":116.4307,"latitude":39.89178,"shopServices":"1,2,3","facilityServices":"1,2,3","cityId":38,"cityName":"北京","createTime":0,"status":"available","communityAddress":"北京市东城区法华南里天华公馆","distance":57},{"communityId":41,"communityName":"天中豪园","population":0,"communitiesDesc":"","longitude":114.02468,"latitude":33.012825,"shopServices":"1,2,3","facilityServices":"1,2,3","cityId":0,"cityName":"","createTime":1443411651,"status":"available","communityAddress":"","distance":795357}]
     * others : 2000.0
     */

    private String status;
    private String message;
    private String others;
    private List<InfoEntity> info;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public void setInfo(List<InfoEntity> info) {
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public String getOthers() {
        return others;
    }

    public List<InfoEntity> getInfo() {
        return info;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class InfoEntity {
        /**
         * communityId : 58
         * communityName : 天华公馆
         * population : 0
         * communitiesDesc :
         * longitude : 116.430725
         * latitude : 39.89178
         * shopServices : 1,2,3
         * facilityServices : 1,2,3
         * cityId : 38
         * cityName : 北京
         * createTime : 1448855232
         * status : available
         * communityAddress :
         * distance : 56
         */

        private int communityId;
        private String communityName;
        private int population;
        private String communitiesDesc;
        private double longitude;
        private double latitude;
        private String shopServices;
        private String facilityServices;
        private int cityId;
        private String cityName;
        private int createTime;
        private String status;
        private String communityAddress;
        private int distance;

        public void setCommunityId(int communityId) {
            this.communityId = communityId;
        }

        public void setCommunityName(String communityName) {
            this.communityName = communityName;
        }

        public void setPopulation(int population) {
            this.population = population;
        }

        public void setCommunitiesDesc(String communitiesDesc) {
            this.communitiesDesc = communitiesDesc;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public void setShopServices(String shopServices) {
            this.shopServices = shopServices;
        }

        public void setFacilityServices(String facilityServices) {
            this.facilityServices = facilityServices;
        }

        public void setCityId(int cityId) {
            this.cityId = cityId;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setCommunityAddress(String communityAddress) {
            this.communityAddress = communityAddress;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public int getCommunityId() {
            return communityId;
        }

        public String getCommunityName() {
            return communityName;
        }

        public int getPopulation() {
            return population;
        }

        public String getCommunitiesDesc() {
            return communitiesDesc;
        }

        public double getLongitude() {
            return longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public String getShopServices() {
            return shopServices;
        }

        public String getFacilityServices() {
            return facilityServices;
        }

        public int getCityId() {
            return cityId;
        }

        public String getCityName() {
            return cityName;
        }

        public int getCreateTime() {
            return createTime;
        }

        public String getStatus() {
            return status;
        }

        public String getCommunityAddress() {
            return communityAddress;
        }

        public int getDistance() {
            return distance;
        }
    }
}
