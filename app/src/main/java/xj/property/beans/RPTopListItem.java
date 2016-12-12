package xj.property.beans;

/**
 * Created by Administrator on 2015/6/26.
 */
public class RPTopListItem {
    public String emobId;
    public String avatar;
    public String nickname;
    public int  praiseSum;
    public int  characterValues;

    private String grade;  /// {生活圈创建者帮主角色：bangzhu->帮主，fubangzhu->副帮主，bangzhong->达人，normal->普通用户}",
    private String identity; /// "{生活圈创建者牛人角色：famous->牛人，normal->不是牛人}",




    /*

    {
            "emobId": "{用户环信ID}",
            "nickname": "{用户昵称}",
            "avatar": "{用户头像}",
            "characterValues": {用户人品值},
            "grade": "{生活圈创建者帮主角色：bangzhu->帮主，fubangzhu->副帮主，bangzhong->达人，normal->普通用户}",
            "identity": "{生活圈创建者牛人角色：famous->牛人，normal->不是牛人}",
            "praiseSum": {被赞次数}
        }


     */


    public String getEmobId() {
        return emobId;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getPraiseSum() {
        return praiseSum;
    }

    public void setPraiseSum(int praiseSum) {
        this.praiseSum = praiseSum;
    }

    public int getCharacterValues() {
        return characterValues;
    }

    public void setCharacterValues(int characterValues) {
        this.characterValues = characterValues;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getGrade() {
        return grade;
    }

    public String getIdentity() {
        return identity;
    }
}
