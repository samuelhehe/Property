package xj.property.beans;

/**
 * Created by Administrator on 2016/3/24.
 */
public class CanPasteRespBean {

    /**
     * code : 200
     * status : yes
     * info : true
     */

    private int code;
    private String status;

    private String message;

    private boolean info;

    public void setCode(int code) {
        this.code = code;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isInfo() {
        return info;
    }

    public void setInfo(boolean info) {
        this.info = info;
    }
}
