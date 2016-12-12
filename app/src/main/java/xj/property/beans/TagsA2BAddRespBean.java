package xj.property.beans;

/**
 * Created by Administrator on 2015/10/30.
 */
public class TagsA2BAddRespBean {


    /**
     * status : no
     * message : 您只能对该用户添加3个标签
     */

    private String status;
    private String message;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
