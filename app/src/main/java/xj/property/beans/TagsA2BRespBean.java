package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/10/30.
 */
public class TagsA2BRespBean {

    /**
     * status : yes
     * info : ["中国好邻居","113"]
     */

    private String status;


    private  String message;

    private List<String> data;

    public void setStatus(String status) {
        this.status = status;
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

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
