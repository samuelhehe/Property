package xj.property.beans;

/**
 * Created by n on 2015/4/8.
 */
public class UserMessageBean  implements  XJ{

    private String status;
    private Info info;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public static class Info implements XJ {

        private int age;
        private String avatar;
        private long communityId;
        private long createTime;
        private String emobId;
        private String gender;
        private String idcard;
        private String idnumber;
        private String nickname;
        private String password;
        private String phone;
        private String role;
        private String room;
        private String salt;
        private String signature;
        private String status;
        private int userId;
        private String username;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public long getCommunityId() {
            return communityId;
        }

        public void setCommunityId(long communityId) {
            this.communityId = communityId;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getEmobId() {
            return emobId;
        }

        public void setEmobId(String emobId) {
            this.emobId = emobId;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getIdcard() {
            return idcard;
        }

        public void setIdcard(String idcard) {
            this.idcard = idcard;
        }

        public String getIdnumber() {
            return idnumber;
        }

        public void setIdnumber(String idnumber) {
            this.idnumber = idnumber;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getRoom() {
            return room;
        }

        public void setRoom(String room) {
            this.room = room;
        }

        public String getSalt() {
            return salt;
        }

        public void setSalt(String salt) {
            this.salt = salt;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        @Override
        public String toString() {
            return "Info{" +
                    "age=" + age +
                    ", avatar='" + avatar + '\'' +
                    ", communityId=" + communityId +
                    ", createTime=" + createTime +
                    ", emobId='" + emobId + '\'' +
                    ", gender='" + gender + '\'' +
                    ", idcard='" + idcard + '\'' +
                    ", idnumber='" + idnumber + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", password='" + password + '\'' +
                    ", phone='" + phone + '\'' +
                    ", role='" + role + '\'' +
                    ", room='" + room + '\'' +
                    ", salt='" + salt + '\'' +
                    ", signature='" + signature + '\'' +
                    ", status='" + status + '\'' +
                    ", userId=" + userId +
                    ", username='" + username + '\'' +
                    '}';
        }
    }
}
