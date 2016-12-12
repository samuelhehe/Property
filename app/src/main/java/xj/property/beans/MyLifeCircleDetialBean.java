package xj.property.beans;

/**
 * Created by che on 2015/6/10.
 */
public class MyLifeCircleDetialBean {
    //        {"timstamp":"2015-06-10","content":"可爱的小猫咪！","contentSum":4,"praiseSum":2,"createTime":1433905609}
    String timstamp;
    String content;
    int contentSum;
    int praiseSum;
    int createTime;
    String photoes;
    int lifeCircleId;


    /**
     * {
     * "timstamp": "{生活圈发布日期}",
     * "lifeCircleId": {生活圈ID},
     * "content": "{生活圈内容}",
     * "contentSum": {评论数量},
     * "praiseSum": {点赞数量},
     * "createTime": {生活圈发布时间},
     * "photoes": "{生活圈图片}"
     * }
     */


    public String getPhotoes() {
        return photoes;
    }

    public void setPhotoes(String photoes) {
        this.photoes = photoes;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public int getLifeCircleId() {
        return lifeCircleId;
    }

    public void setLifeCircleId(int lifeCircleId) {
        this.lifeCircleId = lifeCircleId;
    }

    public String getTimstamp() {
        return timstamp;
    }

    public void setTimstamp(String timstamp) {
        this.timstamp = timstamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getContentSum() {
        return contentSum;
    }

    public void setContentSum(int contentSum) {
        this.contentSum = contentSum;
    }

    public int getPraiseSum() {
        return praiseSum;
    }

    public void setPraiseSum(int praiseSum) {
        this.praiseSum = praiseSum;
    }


    public class ListPhoto {
        //        {"lifePhotoId":6,"lifeCircleId":8,"photoUrl":"http://ltzmaxwell.qiniudn.com/FoDToTfEzTmS_f-5n2y8yhAabQKQ","createTime":1433912492}
        int lifePhotoId;
        int lifeCircleId;
        String photoUrl;
        long createTime;

        public int getLifePhotoId() {
            return lifePhotoId;
        }

        public void setLifePhotoId(int lifePhotoId) {
            this.lifePhotoId = lifePhotoId;
        }

        public int getLifeCircleId() {
            return lifeCircleId;
        }

        public void setLifeCircleId(int lifeCircleId) {
            this.lifeCircleId = lifeCircleId;
        }

        public String getPhotoUrl() {
            return photoUrl;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }
    }
}
