package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/11/24.
 * v3 2016/03/16
 */
public class IHasLaunchedVoteRespBean {


    /**
     * "page": {页码},
     * "limit": {页面大小},
     * "data": [{
     * "voteId": {投票ID},
     * "voteTitle": "{投票标题}",
     * "createTime": {创建时间},
     * "status": "{投票状态}",
     * "nickname": "{用户昵称}",
     * "avatar": "{用户头像}",
     * "grade": "{用户帮主身份}",
     * "identity": "{用户牛人身份}",
     * "voteSum": {投票次数},
     * "options": [{
     * "content": "{投票选项}",
     * "percent": {该选项被投比例},
     * "percentText": "{该选项被投比例}",
     * "voted": {是否投了此项：1->投了，0->没投},
     * "count": {被投次数}
     * }]
     * }]
     */
    private int page;
    private int limit;

    /**
     * 发起投票的状态：
     * unpass：未通过；
     * auditing：审核中；
     * normal：审核通过
     */
    private List<PageDataEntity> data;

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

    public List<PageDataEntity> getPageData() {
        return data;
    }

    public void setPageData(List<PageDataEntity> data) {
        this.data = data;
    }

    public static class PageDataEntity {
        private int voteId;
        private String voteTitle;
        private int createTime;
        private int voteSum;
        private String status;
        private String nickname;
        private String avatar;
        /// 2015/12/10 add grade
        private String grade;
        private String identity;


        /**
         * {
         * "voteId": 1,
         * "voteTitle": "Android和iOS，你更喜欢哪个",
         * "createTime": 1456996488,
         * "status": "normal",
         * "nickname": "浮白",
         * "avatar": "http://7d9lcl.com2.z0.glb.qiniucdn.com/FpgAe-5Aan5MCL4g0BonwAnYjos9",
         * "grade": "normal",
         * "identity": "famous",
         * "voteSum": 0,
         * "options": [{
         * "content": "Android",
         * "percent": 0,
         * "percentText": "0.00",
         * "voted": 0,
         * "count": 0
         * }, {
         * "content": "iOS",
         * "percent": 0,
         * "percentText": "0.00",
         * "voted": 0,
         * "count": 0
         * }]
         * }
         */


        private List<VoteOptionsListEntity> options;

        public void setVoteId(int voteId) {
            this.voteId = voteId;
        }

        public void setVoteTitle(String voteTitle) {
            this.voteTitle = voteTitle;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }

        public void setVoteSum(int voteSum) {
            this.voteSum = voteSum;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getVoteId() {
            return voteId;
        }

        public String getVoteTitle() {
            return voteTitle;
        }

        public int getCreateTime() {
            return createTime;
        }

        public int getVoteSum() {
            return voteSum;
        }

        public String getStatus() {
            return status;
        }

        public String getNickname() {
            return nickname;
        }

        public String getAvatar() {
            return avatar;
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

        public List<VoteOptionsListEntity> getOptions() {
            return options;
        }

        public void setOptions(List<VoteOptionsListEntity> options) {
            this.options = options;
        }

        public static class VoteOptionsListEntity {

            private String content;

            private float percent;
            //// 2016/1/6 票数
            private int count;

            private String percentText;

            //// 1 : 0
            private int voted;

            /**
             * {
             * "content": "Android",
             * "percent": 0,
             * "percentText": "0.00",
             * "voted": 0,
             * "count": 0
             * }
             */

            public float getPercent() {
                return percent;
            }

            public void setPercent(float percent) {
                this.percent = percent;
            }


            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getPercentText() {
                return percentText;
            }

            public void setPercentText(String percentText) {
                this.percentText = percentText;
            }

            public int getVoted() {
                return voted;
            }

            public void setVoted(int voted) {
                this.voted = voted;
            }
        }
    }
}
