package xj.property.beans;

/**
 * Created by n on 2015/4/22.
 */
public class AuthCodeBean implements XJ {
    private  String status;
    private  String info;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "AuthCodeBean{" +
                "status='" + status + '\'' +
                ", info='" + info + '\'' +
                '}';
    }
}
