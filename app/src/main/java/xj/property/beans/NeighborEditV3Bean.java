package xj.property.beans;

/**
 * 作者：che on 2016/3/9 14:11
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class NeighborEditV3Bean {

    /**'
     * "cooperationId": {邻居帮ID},
     "title": "{邻居帮标题}",
     "content": "{邻居帮内容}",
     "emobId": "{用户环信ID}",
     "communityId": {小区ID},
     "createTime": {创建时间},
     "nickname": "{用户昵称}",
     "avatar": "{用户头像}",
     "grade": "{用户帮主角色}",
     "identity": "{用户牛人角色}"
     */
    private int cooperationId;
    private String title;
    private String content;
    private String emobId;
    private int communityId;
    private Long createTime;
    private String nickname;
    private String avatar;
    private String grade;
    private String identity;

    public int getCooperationId() {
        return cooperationId;
    }

    public void setCooperationId(int cooperationId) {
        this.cooperationId = cooperationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEmobId() {
        return emobId;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
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
