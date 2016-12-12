package xj.property.beans;

/**
 * Created by Administrator on 2015/5/4.
 */
public class ActiveUserInfo extends BaseBean {

    public int day;
    public int userId;
    public String emobId;
    public String appVersion;

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmobId() {
        return emobId;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    @Override
    public String toString() {
        return "ActiveUserInfo{" +
                "day=" + day +
                ", userId='" + userId + '\'' +
                ", emobId='" + emobId + '\'' +
                ", appVersion='" + appVersion + '\'' +
                '}';
    }
}
