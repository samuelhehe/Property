package xj.property.beans;

/**
 * Created by Administrator on 2015/5/4.
 */
public class VistorEvent extends BaseBean {
    public int hour;
    public String serviceId;
    public String serviceName;
    public String label;
    public String appVersion;
    private String  userId;
    private String emobId;


    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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
        return "VistorEvent{" +
                "hour=" + hour +
                ", serviceId='" + serviceId + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", label='" + label + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", userId=" + userId +
                ", emobId='" + emobId + '\'' +
                '}';
    }
}
