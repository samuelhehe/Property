package xj.property.beans;

/**
 * Created by Administrator on 2015/11/5.
 */
public class MyTagsChangeRespBean {


    /**
     * status : yes
     * info : {"labelContent":"精打细算","nickname":"拉芳你好"}
     */

    private String status;

    private String message;
    private InfoEntity info;

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
         * labelContent : 精打细算
         * nickname : 拉芳你好
         */

        private String labelContent;
        private String nickname;

        public void setLabelContent(String labelContent) {
            this.labelContent = labelContent;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getLabelContent() {
            return labelContent;
        }

        public String getNickname() {
            return nickname;
        }
    }
}
