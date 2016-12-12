package xj.property.beans;

/**
 * Created by maxwell on 15/2/9.
 */
public class CommonPostResultBean {
    private String status;
    public String message;

    private int resultId;
    private String emobId;

    private InfoEntity info;

    public InfoEntity getInfo() {
        return info;
    }

    public void setInfo(InfoEntity info) {
        this.info = info;
    }

    public class InfoEntity{

        /**
         * activityId : 0
         * activityTitle : 打架一起嗨8
         * activityDetail : 低调
         * emobIdOwner : c37ce94085bbea46562a3ffaa17dc5a4
         * place : 低调低调
         * createTime : 0
         * communityId : 1
         * emobGroupId : 147365513981329896
         * activityTime : 1451836800
         * bzPraiseSum : 0
         * type : activity
         */
        private int activityId;
        private String activityTitle;
        private String activityDetail;
        private String emobIdOwner;
        private String place;
        private int createTime;
        private int communityId;
        private String emobGroupId;
        private int activityTime;
        private int bzPraiseSum;
        private String type;

        public void setActivityId(int activityId) {
            this.activityId = activityId;
        }

        public void setActivityTitle(String activityTitle) {
            this.activityTitle = activityTitle;
        }

        public void setActivityDetail(String activityDetail) {
            this.activityDetail = activityDetail;
        }

        public void setEmobIdOwner(String emobIdOwner) {
            this.emobIdOwner = emobIdOwner;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }

        public void setCommunityId(int communityId) {
            this.communityId = communityId;
        }

        public void setEmobGroupId(String emobGroupId) {
            this.emobGroupId = emobGroupId;
        }

        public void setActivityTime(int activityTime) {
            this.activityTime = activityTime;
        }

        public void setBzPraiseSum(int bzPraiseSum) {
            this.bzPraiseSum = bzPraiseSum;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getActivityId() {
            return activityId;
        }

        public String getActivityTitle() {
            return activityTitle;
        }

        public String getActivityDetail() {
            return activityDetail;
        }

        public String getEmobIdOwner() {
            return emobIdOwner;
        }

        public String getPlace() {
            return place;
        }

        public int getCreateTime() {
            return createTime;
        }

        public int getCommunityId() {
            return communityId;
        }

        public String getEmobGroupId() {
            return emobGroupId;
        }

        public int getActivityTime() {
            return activityTime;
        }

        public int getBzPraiseSum() {
            return bzPraiseSum;
        }

        public String getType() {
            return type;
        }
    }



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmobId() {
        return emobId;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }
}
