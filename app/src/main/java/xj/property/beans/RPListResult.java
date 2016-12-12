package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/6/26.
 * v3 2016/03/02
 */
public class RPListResult {


    /*

    userRank": {用户排名},
        "userCharacterValue": {用户人品值},
        "list": [{
            "emobId": "{用户环信ID}",
            "nickname": "{用户昵称}",
            "avatar": "{用户头像}",
            "characterValues": {用户人品值},
            "grade": "{生活圈创建者帮主角色：bangzhu->帮主，fubangzhu->副帮主，bangzhong->达人，normal->普通用户}",
            "identity": "{生活圈创建者牛人角色：famous->牛人，normal->不是牛人}",
            "praiseSum": {被赞次数}
        }]

     */

    public int userCharacterValue;/// {用户人品值},
    public int userRank; //// 用户排名
    public List<RPTopListItem> list;

    public int getUserCharacterValue() {
        return userCharacterValue;
    }

    public void setUserCharacterValue(int userCharacterValue) {
        this.userCharacterValue = userCharacterValue;
    }

    public int getUserRank() {
        return userRank;
    }

    public void setUserRank(int userRank) {
        this.userRank = userRank;
    }

    public List<RPTopListItem> getList() {
        return list;
    }

    public void setList(List<RPTopListItem> list) {
        this.list = list;
    }


}
