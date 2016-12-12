package xj.property.netbasebean;

/**
 * Created by Administrator on 2016/3/18.
 */
public class CommunityInfoRespBean {
    /**
     * longitude : 116.43070220947266
     * latitude : 39.891780853271484
     * cityId : 373
     * cityName : 北京
     * communityId : 1
     * communityName : 天华
     * communitiesDesc : xingfuxiaoqu
     * communityAddress : 北京市东城区法华南里天华公馆
     */

    private double longitude;
    private double latitude;
    private int cityId;
    private String cityName;
    private int communityId;
    private String communityName;
    private String communitiesDesc;
    private String communityAddress;

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public void setCommunitiesDesc(String communitiesDesc) {
        this.communitiesDesc = communitiesDesc;
    }

    public void setCommunityAddress(String communityAddress) {
        this.communityAddress = communityAddress;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public int getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public int getCommunityId() {
        return communityId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public String getCommunitiesDesc() {
        return communitiesDesc;
    }

    public String getCommunityAddress() {
        return communityAddress;
    }

    /*

    "longitude": {经度},
        "latitude": {纬度},
        "cityId": {城市ID},
        "cityName": "{城市名称}",
        "communityId": {小区ID},
        "communityName": "{小区名称}",
        "communitiesDesc": "{小区描述}",
        "communityAddress": "{小区地址}"
     */


}
