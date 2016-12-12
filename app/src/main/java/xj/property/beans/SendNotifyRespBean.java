package xj.property.beans;

/**
 * Created by Administrator on 2015/10/26.
 */
public class SendNotifyRespBean {


    /**
     * status : yes
     * message : 发布成功了
     * resultId : 10
     */

    private String status;
    private String message;
    private int resultId;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public int getResultId() {
        return resultId;
    }
}
