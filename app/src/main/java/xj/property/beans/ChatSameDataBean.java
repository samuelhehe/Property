package xj.property.beans;

import java.util.List;

/**
 * 作者：asia on 2015/12/30 18:55
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 *  * v3 2016/03/18
 */
public class ChatSameDataBean  {
    /**
     *  "emobId": "{邻居的环信ID}",
     "nickname": "{邻居的昵称}",
     "avatar": "{邻居的头像}",
     "grade": "{邻居的帮主身份}",
     "identity": "{邻居的牛人身份}",
     "labels": [{
     "labelContent": "{标签}",
     "count": {邻居被贴此标签的次数},
     "match": "{是否和当前用户的标签匹配：yes->匹配，no->不匹配}"
     }]
     *
     *
     */

    private String emobId;
    private String nickname;
    private String avatar;
    private String grade;
    private String identity;

    private List<ChatSameLabelBean> labels;

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

    public List<ChatSameLabelBean> getLabels() {
        return labels;
    }

    public void setLabels(List<ChatSameLabelBean> labels) {
        this.labels = labels;
    }

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
}
