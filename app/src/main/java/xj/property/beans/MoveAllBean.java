package xj.property.beans;

/**
 * 作者：che on 2016/1/11 17:01
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class MoveAllBean extends BaseBean {
    private String status;
    private MoveInfoBean info;
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

    public MoveInfoBean getInfo() {
        return info;
    }

    public void setInfo(MoveInfoBean info) {
        this.info = info;
    }
}
