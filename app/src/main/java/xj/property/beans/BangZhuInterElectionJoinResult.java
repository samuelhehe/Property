package xj.property.beans;

/**
 * Created by Administrator on 2015/9/25.
 */
public class BangZhuInterElectionJoinResult {


    /**
     * status : yes
     * info : 参与竞选成功!
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
