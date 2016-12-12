package xj.property.beans;

import android.text.TextUtils;

/**
 * Created by Administrator on 2015/11/4.
 */
public class VoteIndexDiscussInfoNotify implements XJ {


//    private String nickname;
//    public String avatar;
//    private String type;
//    private String sourceId;


    public String contentShow;

    /**
     * nickname : 我是6号
     * avatar : http://7d9lcl.com2.z0.glb.qiniucdn.com/FobiXm7f18ZvXGbWvoDrmUbR0qof
     * type : reply
     * sourceId : 1
     */

    private String nickname;
    private String avatar;
    private String type;
    private String sourceId;


    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getType() {
        return type;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getContentShow() {
        return contentShow;
    }

    public void setContentShow(String nikeName) {

        if(TextUtils.equals("reply", this.getType())){
            this.contentShow = nikeName+ "回复了你的投票评论";
        }
        else if(TextUtils.equals("vote",this.getType())){
            this.contentShow = nikeName+ "参与了你的投票";
        }
        else if(TextUtils.equals("election",this.getType())){
            this.contentShow = nikeName+ "对你投了一票";
        }
        else if(TextUtils.equals("comment",this.getType())){
            this.contentShow = nikeName+ "对你的投票发表了评论";
        }
    }

//            1、帮主投票里，有人给我投票了     XXX对你投了一票-->election-->CMD_CODE 141     sourceId ==0
//
//            2、我发起的投票有人投了         XXX参与了你的投票-->vote-->CMD_CODE 142
//
//            3、有人给我发起的投票评论了     XXX对你的投票发表了评论-->comment-->CMD_CODE 143
//
//            4、有人回复了我对某投票的评论。   XXX回复了你的投票评论 -->reply-->CMD_CODE 144


}
