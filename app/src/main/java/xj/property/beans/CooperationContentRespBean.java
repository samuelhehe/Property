package xj.property.beans;

/**
 * Created by Administrator on 2015/11/11.
 */
public class CooperationContentRespBean {


    /**
     * status : yes
     * info : {"cooperationId":28,"title":"牙科医生","content":"专注牙医30年，祖传技术值得信赖！","emobId":"98b0ce078094640917cdb2f59f5ff572","createTime":1446536459,"contentSum":0,"nickname":"我是007","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FrndDYZkZed_QTvWduthZI_PvE0P"}
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
         * cooperationId : 28
         * title : 牙科医生
         * content : 专注牙医30年，祖传技术值得信赖！
         * emobId : 98b0ce078094640917cdb2f59f5ff572
         * createTime : 1446536459
         * contentSum : 0
         * nickname : 我是007
         * avatar : http://7d9lcl.com2.z0.glb.qiniucdn.com/FrndDYZkZed_QTvWduthZI_PvE0P
         */

        private int cooperationId;
        private String title;
        private String content;
        private String emobId;
        private int createTime;
        private int contentSum;
        private String nickname;
        private String avatar;

        public void setCooperationId(int cooperationId) {
            this.cooperationId = cooperationId;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setEmobId(String emobId) {
            this.emobId = emobId;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }

        public void setContentSum(int contentSum) {
            this.contentSum = contentSum;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getCooperationId() {
            return cooperationId;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }

        public String getEmobId() {
            return emobId;
        }

        public int getCreateTime() {
            return createTime;
        }

        public int getContentSum() {
            return contentSum;
        }

        public String getNickname() {
            return nickname;
        }

        public String getAvatar() {
            return avatar;
        }
    }
}
