package xj.property.beans;

import java.util.List;

/**
 * Created by asia on 2015/11/23.
 */
public class RunForScoreAllV3Bean {
    private int code;
    private String status;
    private RunForScoreInfoV3Bean data;


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

    public RunForScoreInfoV3Bean getData() {
        return data;
    }

    public void setData(RunForScoreInfoV3Bean data) {
        this.data = data;
    }

    public class RunForScoreInfoV3Bean{
        private int page;
        private int limit;
        private List<RunForScoreDateV3Bean> data;

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

        public List<RunForScoreDateV3Bean> getData() {
            return data;
        }

        public void setData(List<RunForScoreDateV3Bean> data) {
            this.data = data;
        }

        public class RunForScoreDateV3Bean{
            private int score;
            private String emobIdFrom;
            private int type;
            private String nickname;
            private String avatar;
            private String grade;
            private String identity;

            public int getScore() {
                return score;
            }

            public void setScore(int score) {
                this.score = score;
            }

            public String getEmobIdFrom() {
                return emobIdFrom;
            }

            public void setEmobIdFrom(String emobIdFrom) {
                this.emobIdFrom = emobIdFrom;
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
}
