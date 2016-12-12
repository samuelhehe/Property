package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/10/30.
 */
public class MyTagsRespBean {


    /**
     * status : yes
     * info : [{"labelContent":"中国好邻居","count":"1"},{"labelContent":"113","count":"1"}]
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
         * labelContent : 中国好邻居
         * count : 1
         */

        private String labelContent;
        private String count;
        private String emobIdTo;

        public String getEmobIdTo() {
            return emobIdTo;
        }

        public void setEmobIdTo(String emobIdTo) {
            this.emobIdTo = emobIdTo;
        }

        public void setLabelContent(String labelContent) {
            this.labelContent = labelContent;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getLabelContent() {
            return labelContent;
        }

        public String getCount() {
            return count;
        }
    }
}
