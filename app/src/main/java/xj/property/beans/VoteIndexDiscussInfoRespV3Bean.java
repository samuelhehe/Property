package xj.property.beans;

import android.text.TextUtils;

/**
 * 作者：che on 2016/3/10 10:54
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class VoteIndexDiscussInfoRespV3Bean  implements XJ{
    private String nickname;
    private String avatar;
    private String type;
    private String sourceId;
    public String contentShow;

    public String getContentShow() {
        return contentShow;
    }

    public void setContentShow(String nikeName) {

//        {提示类型：vote->新投票，comment->新评论，reply->新回复}",

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }
}
