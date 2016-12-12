package xj.property.beans;

/**
 * Created by Administrator on 2015/3/24.
 */
public class XJUserInfoBean {
    private String status;
    private UserInfoDetailBean info;
    private String cityName;
    private String communityName;
    private String longitude;
    private String latitude;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserInfoDetailBean getInfo() {
        return info;
    }

    public void setInfo(UserInfoDetailBean info) {
        this.info = info;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    @Override
    public String toString() {
        return "XJUserInfoBean{" +
                "status='" + status + '\'' +
                ", info=" + info +
                '}';
    }
}
