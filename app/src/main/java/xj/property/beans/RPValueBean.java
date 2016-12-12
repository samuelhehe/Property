package xj.property.beans;

/**
 * Created by che on 2015/6/9.
 *
 * v3 2016/03/04
 */
public class RPValueBean {


    /**
     *
     * {
     "emobId": "{用户环信ID}",
     "nickname": "{用户昵称}",
     "avatar": "{用户头像}",
     "grade": "{生活圈创建者帮主角色：bangzhu->帮主，fubangzhu->副帮主，bangzhong->达人，normal->普通用户}",
     "identity": "{生活圈创建者牛人角色：famous->牛人，normal->不是牛人}",
     "praiseSum": {点赞次数}
     }
     *
     *
     *
     * {
     "emobId": "fcb6adf78bef4ee4940d2af8ee7905f9",
     "nickname": "天昭",
     "avatar": "http://7d9lcl.com2.z0.glb.qiniucdn.com/FpgAe-5Aan5MCL4g0BonwAnYjos9",
     "grade": "normal",
     "identity": "normal",
     "praiseSum": 1
     }

     */



    private String emobId;
    private String avatar;
    private String name;
    private String nickname;
    private String rpValue;
    /// 2015/11/19 帮内身份
    private String grade;

    private String identity;
    private int praiseSum;


    public RPValueBean() {
    }

    public RPValueBean(String avatar, String name, String rpValue) {
        this.avatar = avatar;
        this.name = name;
        this.rpValue = rpValue;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRpValue() {
        return rpValue;
    }

    public void setRpValue(String rpValue) {
        this.rpValue = rpValue;
    }

    public String getEmobId() {
        return emobId;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public void setPraiseSum(int praiseSum) {
        this.praiseSum = praiseSum;
    }

    public String getIdentity() {
        return identity;
    }

    public int getPraiseSum() {
        return praiseSum;
    }
}
