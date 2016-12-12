package xj.property.beans;

/**
 * 作者：asia on 2015/12/8 15:23
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class PropertyBBbiAllResponse extends BaseBean{

    private String status;
    private PropertyBBbiRequest info;
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

    public PropertyBBbiRequest getInfo() {
        return info;
    }

    public void setInfo(PropertyBBbiRequest info) {
        this.info = info;
    }
}
