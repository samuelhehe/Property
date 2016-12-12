package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/9/18.
 */
public class WelfareDetailsBean  {


    /**
     * status : yes
     * info : {"welfareId":39,"title":"五常大米 9.9元","poster":"http://7d9lcl.com2.z0.glb.qiniucdn.com/ddcdae1b01d444f4bd702daf71d4e799","content":"http://7d9lcl.com2.z0.glb.qiniucdn.com/a6d08807d9c24b4f9c76c9187c6ec364","charactervalues":5,"total":2000,"rule":"test","phone":"15811078116","status":"ongoing","startTime":1442576661,"endTime":1443625238,"price":0.01,"createTime":1442576658,"modifyTime":1442577883,"remain":0,"communityId":1,"provideInstruction":"2015-9-17由小区物业发放","code":"051569","users":[{"userId":423,"emobId":"2d152c81ae2d443c2a563113460cffdd","nickname":"Kevin","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FgXxJpVp6Y0z8AjAy9cu-raG1B2k"},{"userId":430,"emobId":"98b0ce078094640917cdb2f59f5ff572","nickname":"我是007","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FrndDYZkZed_QTvWduthZI_PvE0P"},{"userId":431,"emobId":"9e68096015e4f9643d6153c4d9ab81a9","nickname":"英亮","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FlQhHUGM5V_gbjB9FPQP4ITgbKCJ"},{"userId":432,"emobId":"aad8e70c725f5362c28852b281297e86","nickname":"我是008","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Ftg21BkE8FpI11e3fsFKDDLiAqDH"},{"userId":491,"emobId":"bf1fd76cc783d754d377091264a2c933","nickname":"?肖伟Ⓜ️","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FqE8ft9hdDsnozey1xu6x98KMJ6e"},{"userId":4669,"emobId":"e310c31375e2d4d49e901d12caf7fd60","nickname":"hfjd"}]}
     */

    private String status;
    private InfoEntity info;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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

    public static class InfoEntity {
        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        /**
         * welfareId : 39
         * title : 五常大米 9.9元
         * poster : http://7d9lcl.com2.z0.glb.qiniucdn.com/ddcdae1b01d444f4bd702daf71d4e799
         * content : http://7d9lcl.com2.z0.glb.qiniucdn.com/a6d08807d9c24b4f9c76c9187c6ec364
         * charactervalues : 5
         * total : 2000
         * rule : test
         * phone : 15811078116
         * status : ongoing
         * startTime : 1442576661
         * endTime : 1443625238
         * price : 0.01
         * createTime : 1442576658
         * modifyTime : 1442577883
         * remain : 0
         * communityId : 1
         * provideInstruction : 2015-9-17由小区物业发放
         * code : 051569
         * users : [{"userId":423,"emobId":"2d152c81ae2d443c2a563113460cffdd","nickname":"Kevin","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FgXxJpVp6Y0z8AjAy9cu-raG1B2k"},{"userId":430,"emobId":"98b0ce078094640917cdb2f59f5ff572","nickname":"我是007","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FrndDYZkZed_QTvWduthZI_PvE0P"},{"userId":431,"emobId":"9e68096015e4f9643d6153c4d9ab81a9","nickname":"英亮","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FlQhHUGM5V_gbjB9FPQP4ITgbKCJ"},{"userId":432,"emobId":"aad8e70c725f5362c28852b281297e86","nickname":"我是008","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Ftg21BkE8FpI11e3fsFKDDLiAqDH"},{"userId":491,"emobId":"bf1fd76cc783d754d377091264a2c933","nickname":"?肖伟Ⓜ️","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FqE8ft9hdDsnozey1xu6x98KMJ6e"},{"userId":4669,"emobId":"e310c31375e2d4d49e901d12caf7fd60","nickname":"hfjd"}]
         */


        private String reason;
        private int welfareId;
        private String title;
        private String poster;
        private String content;
        private int charactervalues;
        private int total;
        private String rule;
        private String phone;
        private String status;
        private int startTime;
        private int endTime;
        private double price;
        private int createTime;
        private int modifyTime;
        private int remain;
        private int communityId;
        private String provideInstruction;
        private String code;
        private List<UsersEntity> users;


        public void setWelfareId(int welfareId) {
            this.welfareId = welfareId;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setPoster(String poster) {
            this.poster = poster;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setCharactervalues(int charactervalues) {
            this.charactervalues = charactervalues;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public void setRule(String rule) {
            this.rule = rule;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setStartTime(int startTime) {
            this.startTime = startTime;
        }

        public void setEndTime(int endTime) {
            this.endTime = endTime;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }

        public void setModifyTime(int modifyTime) {
            this.modifyTime = modifyTime;
        }

        public void setRemain(int remain) {
            this.remain = remain;
        }

        public void setCommunityId(int communityId) {
            this.communityId = communityId;
        }

        public void setProvideInstruction(String provideInstruction) {
            this.provideInstruction = provideInstruction;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public void setUsers(List<UsersEntity> users) {
            this.users = users;
        }

        public int getWelfareId() {
            return welfareId;
        }

        public String getTitle() {
            return title;
        }

        public String getPoster() {
            return poster;
        }

        public String getContent() {
            return content;
        }

        public int getCharactervalues() {
            return charactervalues;
        }

        public int getTotal() {
            return total;
        }

        public String getRule() {
            return rule;
        }

        public String getPhone() {
            return phone;
        }

        public String getStatus() {
            return status;
        }

        public int getStartTime() {
            return startTime;
        }

        public int getEndTime() {
            return endTime;
        }

        public double getPrice() {
            return price;
        }

        public int getCreateTime() {
            return createTime;
        }

        public int getModifyTime() {
            return modifyTime;
        }

        public int getRemain() {
            return remain;
        }

        public int getCommunityId() {
            return communityId;
        }

        public String getProvideInstruction() {
            return provideInstruction;
        }

        public String getCode() {
            return code;
        }

        public List<UsersEntity> getUsers() {
            return users;
        }

        public static class UsersEntity {
            /**
             * userId : 423
             * emobId : 2d152c81ae2d443c2a563113460cffdd
             * nickname : Kevin
             * avatar : http://7d9lcl.com2.z0.glb.qiniucdn.com/FgXxJpVp6Y0z8AjAy9cu-raG1B2k
             */

            private int userId;
            private String emobId;
            private String nickname;
            private String avatar;

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public void setEmobId(String emobId) {
                this.emobId = emobId;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public int getUserId() {
                return userId;
            }

            public String getEmobId() {
                return emobId;
            }

            public String getNickname() {
                return nickname;
            }

            public String getAvatar() {
                return avatar;
            }
        }
    }
}
