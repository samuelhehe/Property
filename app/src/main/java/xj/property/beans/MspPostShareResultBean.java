package xj.property.beans;

/**
 * Created by Administrator on 2015/10/16.
 */
public class MspPostShareResultBean {


    /**
     * status : yes
     * info : ${分享成功获取到的帮帮币数量}
     */

    private String status;
    private String message;
    private String info;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public String getInfo() {
        return info;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
