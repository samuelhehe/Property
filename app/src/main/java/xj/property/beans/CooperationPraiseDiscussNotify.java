package xj.property.beans;

import android.text.TextUtils;

/**
 * Created by Administrator on 2015/11/4.
 */
public class CooperationPraiseDiscussNotify implements XJ {

    /**
     * nickname : 葫芦侠
     * avatar : http://7d9lcl.com2.z0.glb.qiniucdn.com/Fls9XjF87nmvcsAs8ilkkMyafrc7
     * type : praise
     * sourceId : 5b72624e68cee8047f5cce9acba426ee
     */

    private String nickname;
    public String avatar;
    private String type;
    private String sourceId;

    public String contentShow;

    /**
     *
     * "nickname": "{昵称}",
     "avatar": "{头像}",
     "type": "{提示类型，content->新评论}",
     "sourceId": "{邻居帮ID}"


     *
     * @param nickname
     */



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
        if(TextUtils.equals("praise", this.getType())){
            this.contentShow = nikeName+ "赞了你的人品";
        }else if(TextUtils.equals("content",this.getType())){
            this.contentShow = nikeName+ "对你发表了评论";
        }
    }
}
