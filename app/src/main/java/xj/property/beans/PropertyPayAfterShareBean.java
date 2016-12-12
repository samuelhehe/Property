package xj.property.beans;

/**
 * Created by Administrator on 2016/3/2.
 */
public class PropertyPayAfterShareBean {

    /**
     * status : yes
     * info : {"photo":"${图片地址}","content":"${详情}"}
     */

    private String status;
    private InfoEntity info;
    private String message;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setInfo(InfoEntity info) {
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public InfoEntity getInfo() {
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
         * photo : ${图片地址}
         * content : ${详情}
         */

        private String photo;
        private String content;

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPhoto() {
            return photo;
        }

        public String getContent() {
            return content;
        }
    }
}
