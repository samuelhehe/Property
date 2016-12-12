package xj.property.beans;

import java.io.Serializable;

/**
 * Created by asia on 2015/11/23.
 */
public class RunForBean implements Serializable {
    private String status;//数据状态
    private String emobId;//环信id
    private int score;//获得分数
    private String nickname;//昵称
    private String avatar;//头像地址
    private int rank;//排名
    private boolean arrowUpOrDown;//箭头方向

    public boolean isArrowUpOrDown() {
        return arrowUpOrDown;
    }

    public void setArrowUpOrDown(boolean arrowUpOrDown) {
        this.arrowUpOrDown = arrowUpOrDown;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getEmobId() {
        return emobId;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
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

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
