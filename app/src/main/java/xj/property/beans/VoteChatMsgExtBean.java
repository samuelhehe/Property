package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/11/30.
 */
public class VoteChatMsgExtBean {

    /**
     * voteId : 11
     * voteTitle : dbhsddsdbhvnjdsbhcjvnbdhsdjnc hxjzdc hjzdf chjcdzsdhbc gdhzbsjsc hjzdc hbjdzcgh jdzbdhjxd zgzhdjc zhdjzbc kjdcj nzdfncjdkcnjkd jkvxv
     * emobId : 3df0987f6398d632750e2c22edd486d1
     * nickname : xw
     * avatar : http://7d9lcl.com2.z0.glb.qiniucdn.com/FsRurC7P9g8BxOP1YUeG56eFA46j
     * voteOptionsList : [{"voteOptionsId":109,"voteOptionsContent":"fvgbhnjcfvgbhn","sort":0,"percent":16.67,"strPersent":"16.67","ischoosed":0},{"voteOptionsId":110,"voteOptionsContent":"cvgbhjnvgbhnj","sort":1,"percent":16.67,"strPersent":"16.67","ischoosed":0},{"voteOptionsId":111,"voteOptionsContent":"ghjgvbh","sort":2,"percent":16.67,"strPersent":"16.67","ischoosed":1},{"voteOptionsId":112,"voteOptionsContent":"uuuuuuuui","sort":3,"percent":0,"strPersent":"0.00","ischoosed":0},{"voteOptionsId":113,"voteOptionsContent":"bhjkijkpl","sort":4,"percent":0,"strPersent":"0.00","ischoosed":0},{"voteOptionsId":114,"voteOptionsContent":"hhjkklllll","sort":5,"percent":0,"strPersent":"0.00","ischoosed":0},{"voteOptionsId":115,"voteOptionsContent":"aaaaaaa","sort":6,"percent":16.67,"strPersent":"16.67","ischoosed":0},{"voteOptionsId":116,"voteOptionsContent":"bbbbbb","sort":7,"percent":16.67,"strPersent":"16.67","ischoosed":0},{"voteOptionsId":117,"voteOptionsContent":"ccccccc","sort":8,"percent":16.67,"strPersent":"16.67","ischoosed":0}]
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
         * voteOptionsId : 109
         * voteOptionsContent : fvgbhnjcfvgbhn
         * sort : 0
         * percent : 16.67
         * strPersent : 16.67
         * ischoosed : 0
         */

        private int voteOptionsId;
        private String voteOptionsContent;
        private int sort;
        private double percent;
        private String strPersent;
        private int ischoosed;

        public void setVoteOptionsId(int voteOptionsId) {
            this.voteOptionsId = voteOptionsId;
        }

        public void setVoteOptionsContent(String voteOptionsContent) {
            this.voteOptionsContent = voteOptionsContent;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public void setPercent(double percent) {
            this.percent = percent;
        }

        public void setStrPersent(String strPersent) {
            this.strPersent = strPersent;
        }

        public void setIschoosed(int ischoosed) {
            this.ischoosed = ischoosed;
        }

        public int getVoteOptionsId() {
            return voteOptionsId;
        }

        public String getVoteOptionsContent() {
            return voteOptionsContent;
        }

        public int getSort() {
            return sort;
        }

        public double getPercent() {
            return percent;
        }

        public String getStrPersent() {
            return strPersent;
        }

        public int getIschoosed() {
            return ischoosed;
        }
    }
}
