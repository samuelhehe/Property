package xj.property.beans;

/**
 * Created by Administrator on 2016/1/11.
 *
 * v3 2016/03/21
 */
public class UserFeedBackRespBean {


    /*
     "adminId": {客服ID},
        "username": "{客服用户名}",
        "nickname": "{客服名称}",
        "emobId": "{客服环信ID}",
        "startTime": "{客服服务开始时间}",
        "endTime": "{客服服务结束时间}"

     */

    /*


     "adminId": 324,
        "username": "yy_qlkf",
        "nickname": "帮帮客服",
        "emobId": "b83d999a7cd7eaef1d8d2b7609efd149",
        "startTime": "",
        "endTime": ""


     */

    private int adminId;
    private String username;
    private String emobId;
    private String nickname;
    private String avatar;
    private String startTime;

    private String endTime;

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

    public void setAdminId(int adminId) {
        this.adminId = adminId;
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

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public int getAdminId() {
        return adminId;
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

    public String getAvatar() {
        return avatar;
    }

}
