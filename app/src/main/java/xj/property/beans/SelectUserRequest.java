package xj.property.beans;

/**
 * Created by Administrator on 2015/4/15.
 */
public class SelectUserRequest {
public String status;
    public UserGroupBean info;
public  String message;

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

    public UserGroupBean getInfo() {
        return info;
    }

    public void setInfo(UserGroupBean info) {
        this.info = info;
    }
}
