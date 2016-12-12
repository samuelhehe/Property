package xj.property.beans;

/**
 * Created by Administrator on 2016/3/2.
 */
public class PropertyAfterShareRespBean  {

    /**
     * status : yes
     * info : ${分享成功获取到的帮帮币数量}
     */

    private String status;
    private String info;

    private String message;




    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
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
}
