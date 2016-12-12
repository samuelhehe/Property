package xj.property.beans;

import java.util.List;

/**
 * 作者：che on 2016/3/8 18:30
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class RunForScoreV3Bean {
    private int page;
    private int limit;
    private List<RunForScoreV3DataBean> data;

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

    public List<RunForScoreV3DataBean> getData() {
        return data;
    }

    public void setData(List<RunForScoreV3DataBean> data) {
        this.data = data;
    }

    public class RunForScoreV3DataBean {
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
