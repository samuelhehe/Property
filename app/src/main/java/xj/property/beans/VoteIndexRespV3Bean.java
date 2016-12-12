package xj.property.beans;

import java.util.List;

/**
 * 作者：che on 2016/3/8 12:27
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class VoteIndexRespV3Bean extends xj.property.netbase.BaseBean{
    private int code;
    private String status;
    private VoteIndexInfoV3Bean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public VoteIndexInfoV3Bean getData() {
        return data;
    }

    public void setData(VoteIndexInfoV3Bean data) {
        this.data = data;
    }

    public class VoteIndexInfoV3Bean {
        private int page;
        private int limit;
        private List<VoteIndexDataV3Bean> data;

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

        public List<VoteIndexDataV3Bean> getData() {
            return data;
        }

        public void setData(List<VoteIndexDataV3Bean> data) {
            this.data = data;
        }

        public class VoteIndexDataV3Bean {

            private int voteId;
            private String voteTitle;
            private String emobId;
            private Long createTime;
//            private List<VoteIndexOptionV3Bean> options;
            private String nickname;
            private String avatar;
            private String grade;
            private String identity;
            private int voteSum;

            public int getVoteId() {
                return voteId;
            }

            public void setVoteId(int voteId) {
                this.voteId = voteId;
            }

            public String getVoteTitle() {
                return voteTitle;
            }

            public void setVoteTitle(String voteTitle) {
                this.voteTitle = voteTitle;
            }

            public String getEmobId() {
                return emobId;
            }

            public void setEmobId(String emobId) {
                this.emobId = emobId;
            }

            public Long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(Long createTime) {
                this.createTime = createTime;
            }

//            public List<VoteIndexOptionV3Bean> getOptions() {
//                return options;
//            }
//
//            public void setOptions(List<VoteIndexOptionV3Bean> options) {
//                this.options = options;
//            }

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

            public int getVoteSum() {
                return voteSum;
            }

            public void setVoteSum(int voteSum) {
                this.voteSum = voteSum;
            }

            public class VoteIndexOptionV3Bean {
                private String content;
                private int percent;
                private String percentText;
                private int voted;
                private int count;

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public int getPercent() {
                    return percent;
                }

                public void setPercent(int percent) {
                    this.percent = percent;
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

                public int getCount() {
                    return count;
                }

                public void setCount(int count) {
                    this.count = count;
                }
            }
        }
    }
}