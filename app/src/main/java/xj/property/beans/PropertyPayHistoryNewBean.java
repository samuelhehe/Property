package xj.property.beans;

/**
 * 作者：che on 2016/3/3 10:50
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class PropertyPayHistoryNewBean {
    private String status;
    private PropertyPayHistoryBean info;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PropertyPayHistoryBean getInfo() {
        return info;
    }

    public void setInfo(PropertyPayHistoryBean info) {
        this.info = info;
    }
}
