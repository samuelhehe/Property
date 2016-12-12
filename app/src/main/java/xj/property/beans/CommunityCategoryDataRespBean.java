package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/12/15.
 */
public class CommunityCategoryDataRespBean {


    /**
     * status : yes
     * info : [{"labelContent":"减肥专家","range":"1"},{"labelContent":"中医","range":"1"},{"labelContent":"空姐","range":"1"},{"labelContent":"模特","range":"1"},{"labelContent":"企业高管","range":"1"},{"labelContent":"猎头/HR","range":"2"}]
     */

    private String status;

    private String message;

    private List<InfoEntity> info;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setInfo(List<InfoEntity> info) {
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public List<InfoEntity> getInfo() {
        return info;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class InfoEntity {
        /**
         * labelContent : 减肥专家
         * range : 1
         */

        private String labelContent;
        private String range;

        public void setLabelContent(String labelContent) {
            this.labelContent = labelContent;
        }

        public void setRange(String range) {
            this.range = range;
        }

        public String getLabelContent() {
            return labelContent;
        }

        public String getRange() {
            return range;
        }
    }
}
