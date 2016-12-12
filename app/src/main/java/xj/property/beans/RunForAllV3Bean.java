package xj.property.beans;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：che on 2016/3/8 15:13
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class RunForAllV3Bean {
    private int page;
    private int limit;
    private List<RunForDataV3Bean> data;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<RunForDataV3Bean> getData() {
        return data;
    }

    public void setData(List<RunForDataV3Bean> data) {
        this.data = data;
    }

    public class RunForDataV3Bean implements Serializable {
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
        private boolean voted;
        private boolean arrowUpOrDown;//箭头方向

        public boolean isArrowUpOrDown() {
            return arrowUpOrDown;
        }

        public void setArrowUpOrDown(boolean arrowUpOrDown) {
            this.arrowUpOrDown = arrowUpOrDown;
        }

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

        public boolean isVoted() {
            return voted;
        }

        public void setVoted(boolean voted) {
            this.voted = voted;
        }
    }

}
