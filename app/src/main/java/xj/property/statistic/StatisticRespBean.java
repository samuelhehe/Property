package xj.property.statistic;

/**
 * Created by Administrator on 2016/1/13.
 */
public class StatisticRespBean {


    /**
     * code : 200
     * status : yes
     *
     */

    private int code;
    private String status;
    private String message;

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
}
