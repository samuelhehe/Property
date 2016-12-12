package xj.property.beans;

/**
 * Created by Administrator on 2015/12/31.
 */
public class AddMemberToGroupRespBean {

    /**
     * status : yes
     * resultId : 1
     */

    private String status;
    private String message;

    private int resultId;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public String getStatus() {
        return status;
    }

    public int getResultId() {
        return resultId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
