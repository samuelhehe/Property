package xj.property.beans;

import java.util.List;

/**
 * 作者：asia on 2016/3/8 18:04
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class RunForScoreHistoryV3Bean {
    private RunForScoreHistoryDataV3Bean mine;
    private List<RunForScoreHistoryDataV3Bean> rank;

    public RunForScoreHistoryDataV3Bean getMine() {
        return mine;
    }

    public void setMine(RunForScoreHistoryDataV3Bean mine) {
        this.mine = mine;
    }

    public List<RunForScoreHistoryDataV3Bean> getRank() {
        return rank;
    }

    public void setRank(List<RunForScoreHistoryDataV3Bean> rank) {
        this.rank = rank;
    }

    public class RunForScoreHistoryDataV3Bean{
        private int electionRankId;
        private String emobId;
        private String timeMonth;
        private int communityId;
        private int score;
        private int electionCount;
        private int praiseCount;
        private Long updateTime;
        private int rank;
        private String nickname;
        private String avatar;
        private String grade;
        private String identity;


        /**
         "electionRankId": {选举排名ID},
         "emobId": "{用户环信ID}",
         "timeMonth": "{竞选月份}",
         "communityId": {小区ID},
         "score": {竞选得分},
         "electionCount": {得票数量},
         "praiseCount": {被赞数量},
         "updateTime": {最后更新时间},
         "rank": {帮主竞选排名},
         "nickname": "{用户昵称}",
         "avatar": "{用户头像}",
         "grade": "{用户帮主身份}",
         "identity": "{用户牛人身份}"
         *
         */
        public int getElectionRankId() {
            return electionRankId;
        }

        public void setElectionRankId(int electionRankId) {
            this.electionRankId = electionRankId;
        }

        public String getEmobId() {
            return emobId;
        }

        public void setEmobId(String emobId) {
            this.emobId = emobId;
        }

        public String getTimeMonth() {
            return timeMonth;
        }

        public void setTimeMonth(String timeMonth) {
            this.timeMonth = timeMonth;
        }

        public int getCommunityId() {
            return communityId;
        }

        public void setCommunityId(int communityId) {
            this.communityId = communityId;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getElectionCount() {
            return electionCount;
        }

        public void setElectionCount(int electionCount) {
            this.electionCount = electionCount;
        }

        public int getPraiseCount() {
            return praiseCount;
        }

        public void setPraiseCount(int praiseCount) {
            this.praiseCount = praiseCount;
        }

        public Long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Long updateTime) {
            this.updateTime = updateTime;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
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
}
