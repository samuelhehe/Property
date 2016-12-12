package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2016/3/24.
 */
public class DoorPasteDetailsRespBean {
    /**
     * code : 200
     * status : yes
     * data : [{"doorId":1,"doorStickerId":1,"content":"老把车停单元楼门口，进出都不方面","times":1,"createTime":1458735796},{"doorId":1,"doorStickerId":2,"content":"噪声太大","times":1,"createTime":1458736734}]
     */

    private String status;

    private String message;

    private List<DoorPasteDetailsBean> info;


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

    public List<DoorPasteDetailsBean> getInfo() {
        return info;
    }

    public void setInfo(List<DoorPasteDetailsBean> info) {
        this.info = info;
    }
}
