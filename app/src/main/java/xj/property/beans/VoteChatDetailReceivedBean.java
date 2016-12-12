package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/11/30.
 */
public class VoteChatDetailReceivedBean {


    /**
     * voteId : 16
     * voteTitle : 选项50测试
     * emobId : 4a057fbb6e6441f1b1017eb805c3d03d
     * nickname : 解放军
     * avatar : http://7d9lcl.com2.z0.glb.qiniucdn.com/Fgiyt1En_MyzLWBfSB41ohbpy7G-
     * voteOptionsList : [{"voteOptionsContent":"a","percent":100,"strPersent":"100.00"},{"voteOptionsContent":"b","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"c","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"d","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"e","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"f","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"g","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"h","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"i","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"j","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"k","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"l","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"m","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"n","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"o","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"p","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"q","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"r","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"s","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"t","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"u","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"v","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"w","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"x","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"y","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"z","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"aa","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"as","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"aq","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"aw","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"ae","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"ar","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"at","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"ay","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"ac","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"ax","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"ad","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"ag","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"ah","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"ab","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"au","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"ai","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"ao","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"ap","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"ui","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"uh","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"uj","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"in","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"om","percent":0,"strPersent":"0.00"},{"voteOptionsContent":"ij","percent":0,"strPersent":"0.00"}]
     */

    private int voteId;
    private String voteTitle;
    private String emobId;
    private String nickname;
    private String avatar;
    private List<VoteOptionsListEntity> voteOptionsList;

    public void setVoteId(int voteId) {
        this.voteId = voteId;
    }

    public void setVoteTitle(String voteTitle) {
        this.voteTitle = voteTitle;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setVoteOptionsList(List<VoteOptionsListEntity> voteOptionsList) {
        this.voteOptionsList = voteOptionsList;
    }

    public int getVoteId() {
        return voteId;
    }

    public String getVoteTitle() {
        return voteTitle;
    }

    public String getEmobId() {
        return emobId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public List<VoteOptionsListEntity> getVoteOptionsList() {
        return voteOptionsList;
    }

    public static class VoteOptionsListEntity {
        /**
         * voteOptionsContent : a
         * percent : 100.0
         * strPersent : 100.00
         */

        private String voteOptionsContent;
        private double percent;
        private String strPersent;

        private int count ;

        public void setVoteOptionsContent(String voteOptionsContent) {
            this.voteOptionsContent = voteOptionsContent;
        }

        public void setPercent(double percent) {
            this.percent = percent;
        }

        public void setStrPersent(String strPersent) {
            this.strPersent = strPersent;
        }

        public String getVoteOptionsContent() {
            return voteOptionsContent;
        }

        public double getPercent() {
            return percent;
        }

        public String getStrPersent() {
            return strPersent;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
