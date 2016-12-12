package xj.property.beans;

/**
 * 作者：asia on 2015/12/14 13:42
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class FitmentFinishDetailsResponse {

    private String status;

    private FitmentFinishDetailsInfo info;

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

    public FitmentFinishDetailsInfo getInfo() {
        return info;
    }

    public void setInfo(FitmentFinishDetailsInfo info) {
        this.info = info;
    }
}
