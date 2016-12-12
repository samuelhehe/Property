package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/11/24.
 */
public class IWantVoteReqBean extends  xj.property.netbase.BaseBean {

    /**
     * voteTitle : 测试投票3
     * emobId : 30fbf0be239f5afd52440cf31d98f23e
     * createTime : 1448359647
     * communityId : 1
     * voteOptionsList : [{"voteOptionsContent":"3a","sort":1},{"voteOptionsContent":"3b","sort":2},{"voteOptionsContent":"3c","sort":3}]
     */

    private String voteTitle;
    private String emobId;
    private int communityId;

    /**
     *
     *
     "voteTitle": "{投票标题}",
     "emobId": "{用户环信ID}",
     "communityId": {小区ID},
     "options": [{
     "content": "{选项内容}"
     }]
     *
     *
     */

    private List<VoteOptionsListEntity> options;

    public void setVoteTitle(String voteTitle) {
        this.voteTitle = voteTitle;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }


    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }


    public String getVoteTitle() {
        return voteTitle;
    }

    public String getEmobId() {
        return emobId;
    }


    public int getCommunityId() {
        return communityId;
    }

    public List<VoteOptionsListEntity> getOptions() {
        return options;
    }

    public void setOptions(List<VoteOptionsListEntity> options) {
        this.options = options;
    }

    public static class VoteOptionsListEntity {

        private String content;


        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
