package xj.property.beans;

/**
 * Created by Administrator on 2015/5/21.
 */
public class StatusBean {
    public  String status;
    public String message;
    public int resultId;

    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

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

    @Override
    public String toString() {
        return "StatusBean{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", resultId=" + resultId +
                '}';
    }
}
