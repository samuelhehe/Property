package xj.property.beans;

import java.util.List;

/**
 * Created by asia on 2015/11/23.
 */
public class RunForScoreHistoryInfoBean {
    List<RunForHistoryBean> rankList;
    RunForHistoryBean myselfRank;

    public List<RunForHistoryBean> getRankList() {
        return rankList;
    }

    public void setRankList(List<RunForHistoryBean> rankList) {
        this.rankList = rankList;
    }

    public RunForHistoryBean getMyselfRank() {
        return myselfRank;
    }

    public void setMyselfRank(RunForHistoryBean myselfRank) {
        this.myselfRank = myselfRank;
    }
}
