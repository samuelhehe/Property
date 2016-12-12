package xj.property.beans;

/**
 * Created by Administrator on 2016/1/29.
 */
public class UserRegistHasRespBean {


    /**
     * status : yes
     * info : {"userId":5583437,"username":"18603851550","emobId":"3f0509c0dc824bcfb77d0b947c812bfe","nickname":"abc","status":"normal","room":"","createTime":1454067272,"role":"owner","communityId":1,"userFloor":"","userUnit":"","characterValues":0,"identity":"normal","test":0,"registerTime":1454067272,"bonuscoinCount":0,"grade":"normal"}
     */

    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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

    public static class InfoEntity {
        /**
         * userId : 5583437
         * username : 18603851550
         * emobId : 3f0509c0dc824bcfb77d0b947c812bfe
         * nickname : abc
         * status : normal
         * room :
         * createTime : 1454067272
         * role : owner
         * communityId : 1
         * userFloor :
         * userUnit :
         * characterValues : 0
         * identity : normal
         * test : 0
         * registerTime : 1454067272
         * bonuscoinCount : 0
         * grade : normal
         */

        private int userId;
        private String username;
        private String emobId;
        private String nickname;
        private String status;
        private String room;
        private int createTime;
        private String role;
        private int communityId;
        private String userFloor;
        private String userUnit;
        private int characterValues;
        private String identity;
        private int test;
        private int registerTime;
        private int bonuscoinCount;
        private String grade;

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setEmobId(String emobId) {
            this.emobId = emobId;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setRoom(String room) {
            this.room = room;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public void setCommunityId(int communityId) {
            this.communityId = communityId;
        }

        public void setUserFloor(String userFloor) {
            this.userFloor = userFloor;
        }

        public void setUserUnit(String userUnit) {
            this.userUnit = userUnit;
        }

        public void setCharacterValues(int characterValues) {
            this.characterValues = characterValues;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }

        public void setTest(int test) {
            this.test = test;
        }

        public void setRegisterTime(int registerTime) {
            this.registerTime = registerTime;
        }

        public void setBonuscoinCount(int bonuscoinCount) {
            this.bonuscoinCount = bonuscoinCount;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public int getUserId() {
            return userId;
        }

        public String getUsername() {
            return username;
        }

        public String getEmobId() {
            return emobId;
        }

        public String getNickname() {
            return nickname;
        }

        public String getStatus() {
            return status;
        }

        public String getRoom() {
            return room;
        }

        public int getCreateTime() {
            return createTime;
        }

        public String getRole() {
            return role;
        }

        public int getCommunityId() {
            return communityId;
        }

        public String getUserFloor() {
            return userFloor;
        }

        public String getUserUnit() {
            return userUnit;
        }

        public int getCharacterValues() {
            return characterValues;
        }

        public String getIdentity() {
            return identity;
        }

        public int getTest() {
            return test;
        }

        public int getRegisterTime() {
            return registerTime;
        }

        public int getBonuscoinCount() {
            return bonuscoinCount;
        }

        public String getGrade() {
            return grade;
        }
    }
}
