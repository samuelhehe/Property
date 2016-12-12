package xj.property.beans;

/**
 * Created by che on 2015/9/11.
 */
public class CommunityInfo4Code {

    /**
     * status : yes
     * info : [{"createTime":0,"cityId":1,"facilityServices":"1,2,3","shopServices":"1,2,3","longitude":116.4307,"communitiesDesc":"xingfuxiaoqu","communityName":"天华","latitude":39.89178,"population":2000,"communityId":1},{"createTime":1420000815,"cityId":1,"facilityServices":"123","shopServices":"123","longitude":116.69189,"communitiesDesc":"123","communityName":"狮子城","latitude":39.49413,"population":1,"communityId":3}]
     */
    private String status;
    private String message;
    private InfoEntity info;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setInfo(InfoEntity info) {
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public InfoEntity getInfo() {
        return info;
    }

    public class InfoEntity {
        /**
         * createTime : 0
         * cityId : 1
         * facilityServices : 1,2,3
         * shopServices : 1,2,3
         * longitude : 116.4307
         * communitiesDesc : xingfuxiaoqu
         * communityName : 天华
         * latitude : 39.89178
         * population : 2000
         * communityId : 1
         */
        private int createTime;
        private int cityId;
        private String facilityServices;
        private String shopServices;
        private double longitude;
        private String communitiesDesc;
        private String communityName;
        private double latitude;
        private int population;
        private int communityId;

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }

        public void setCityId(int cityId) {
            this.cityId = cityId;
        }

        public void setFacilityServices(String facilityServices) {
            this.facilityServices = facilityServices;
        }

        public void setShopServices(String shopServices) {
            this.shopServices = shopServices;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public void setCommunitiesDesc(String communitiesDesc) {
            this.communitiesDesc = communitiesDesc;
        }

        public void setCommunityName(String communityName) {
            this.communityName = communityName;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public void setPopulation(int population) {
            this.population = population;
        }

        public void setCommunityId(int communityId) {
            this.communityId = communityId;
        }

        public int getCreateTime() {
            return createTime;
        }

        public int getCityId() {
            return cityId;
        }

        public String getFacilityServices() {
            return facilityServices;
        }

        public String getShopServices() {
            return shopServices;
        }

        public double getLongitude() {
            return longitude;
        }

        public String getCommunitiesDesc() {
            return communitiesDesc;
        }

        public String getCommunityName() {
            return communityName;
        }

        public double getLatitude() {
            return latitude;
        }

        public int getPopulation() {
            return population;
        }

        public int getCommunityId() {
            return communityId;
        }
    }
}
