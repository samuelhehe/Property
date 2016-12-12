package xj.property.beans;

/**
 * 作者：asia on 2015/11/23 21:17
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class RunForScoreBean {

    private int score; //分数
    private String emobIdFrom;//投票人的id
    private int type;//
    private String nickname;//昵称
    private String avatar;//头像路径

    public String getEmobIdFrom() {
        return emobIdFrom;
    }

    public void setEmobIdFrom(String emobIdFrom) {
        this.emobIdFrom = emobIdFrom;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
