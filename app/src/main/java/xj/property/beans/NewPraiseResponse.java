package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/6/11.
 *
 * v3 2016/03/04
 */
public class NewPraiseResponse {

//    "emobId": "{用户环信ID}",
//            "nickname": "{用户昵称}",
//            "avatar": "{用户头像}",
//            "characterValues": {用户头像},
//            "characterPercent": {用户的人品值打败同小区内用户数量的百分比},
//            "grade": "{用户帮主角色：bangzhu->帮主，fubangzhu->副帮主，bangzhong->达人，normal->普通用户}",
//            "identity": "{用户牛人角色：famous->牛人，normal->不是牛人}",
//            "time": {最新消息的产生时间}




    /*


     "emobId": "{用户环信ID}",
        "nickname": "{用户昵称}",
        "avatar": "{用户头像}",
        "characterValues": {用户头像},
        "characterPercent": {用户的人品值打败同小区内用户数量的百分比},
        "grade": "{用户帮主角色：bangzhu->帮主，fubangzhu->副帮主，bangzhong->达人，normal->普通用户}",
        "identity": "{用户牛人角色：famous->牛人，normal->不是牛人}",
        "time": {最新消息的产生时间},
        "list": [{
            "lifeCircleId": {生活圈ID},
            "praiseSum": {被赞数量},
            "contentSum": {被评论数量},
            "users": [{
                "nickname": "{点赞或评论的用户的昵称}",
                "avatar": "{点赞或评论的用户的头像}"
            }]
        }]



     */
    private String emobId;
    private String avatar;
    private String nickname;
    private String grade;
    private String identity;
    public int time;
    public int characterValues;
    public double characterPercent;
    public List<NewPraiseNotify> list;

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getCharacterValues() {
        return characterValues;
    }

    public void setCharacterValues(int characterValues) {
        this.characterValues = characterValues;
    }

    public double getCharacterPercent() {
        return characterPercent;
    }

    public void setCharacterPercent(double characterPercent) {
        this.characterPercent = characterPercent;
    }

    public List<NewPraiseNotify> getList() {
        return list;
    }

    public void setList(List<NewPraiseNotify> list) {
        this.list = list;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
