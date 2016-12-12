package xj.property.beans;

/**
 * Created by Administrator on 2015/9/25.
 * v3 2016/03/15
 */
public class BangInternalNews {

    /**
     * message : {提示消息}
     * time : {提示消息产生时间}
     */

    private String message;
    private int time;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
