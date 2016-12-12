package xj.property.beans;

/**
 * Created by Administrator on 2015/4/9.
 */
public class AdminBean {
    public String status;
    public AdminInfo info;
    public String message;
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AdminInfo getInfo() {
        return info;
    }

    public void setInfo(AdminInfo info) {
        this.info = info;
    }

    public static class AdminInfo{
        public String emobId;
        public String startTime;
public String nickname;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String endTime;
        public String getEmobId() {
            return emobId;
        }

        public void setEmobId(String emobId) {
            this.emobId = emobId;
        }

        @Override
        public String toString() {
            return "AdminInfo{" +
                    "emobId='" + emobId + '\'' +
                    ", startTime='" + startTime + '\'' +
                    ", endTime='" + endTime + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "AdminBean{" +
                "status='" + status + '\'' +
                ", info=" + info +
                ", message='" + message + '\'' +
                '}';
    }

    /*{
        "status": "yes",
            "info": {
        "adminId": 22,
                "username": "admin3",
                "emobId": "32cacb2f994f6b42183a1300d9a3e8d6",
                "password": "7ddaad481948c5c70d55c388852d9716",
                "nickname": "",
                "phone": "",
                "age": 0,
                "avatar": "",
                "salt": "B6E3,0U/",
                "gender": "",
                "idcard": "",
                "idnumber": "",
                "status": "",
                "room": "",
                "createTime": 0,
                "role": "owner",
                "communityId": 1,
                "adminStatus": "users"
    }
    }*/
}
