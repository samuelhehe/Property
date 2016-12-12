package xj.property.beans;

import java.util.List;

/**
 * Created by che on 2015/6/10.
 * v3 2016/03/21
 */
public class MyLifeCircleBean {

       /*
    {
        "page": {页码},
        "limit": {页面大小},
        "data": [{
            "timstamp": "{生活圈发布日期}",
            "lifeCircleId": {生活圈ID},
            "content": "{生活圈内容}",
            "contentSum": {评论数量},
            "praiseSum": {点赞数量},
            "createTime": {生活圈发布时间},
            "photoes": "{生活圈图片}"
        }]
    }
     */



    /**
     * page : 1
     * limit : 10
     * data : [{"timstamp":"2016-03-02","lifeCircleId":1,"content":"我的第一条生活圈","contentSum":2,"praiseSum":1,"createTime":1456892259,"photoes":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FpgAe-5Aan5MCL4g0BonwAnYjos9,http://7d9lcl.com2.z0.glb.qiniucdn.com/FpgAe-5Aan5MCL4g0BonwAnYjos9"}]
     */

    private int page;
    private int limit;
    private List<LifeCircleSimpleBean> data;

    public void setPage(int page) {
        this.page = page;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setData(List<LifeCircleSimpleBean> data) {
        this.data = data;
    }

    public int getPage() {
        return page;
    }

    public int getLimit() {
        return limit;
    }

    public List<LifeCircleSimpleBean> getData() {
        return data;
    }

    public static class LifeCircleSimpleBean {
        /**
         * timstamp : 2016-03-02
         * lifeCircleId : 1
         * content : 我的第一条生活圈
         * contentSum : 2
         * praiseSum : 1
         * createTime : 1456892259
         * photoes : http://7d9lcl.com2.z0.glb.qiniucdn.com/FpgAe-5Aan5MCL4g0BonwAnYjos9,http://7d9lcl.com2.z0.glb.qiniucdn.com/FpgAe-5Aan5MCL4g0BonwAnYjos9
         */

        private String timstamp;
        private int lifeCircleId;
        private String content;
        private int contentSum;
        private int praiseSum;
        private int createTime;
        private String photoes;

        public void setTimstamp(String timstamp) {
            this.timstamp = timstamp;
        }

        public void setLifeCircleId(int lifeCircleId) {
            this.lifeCircleId = lifeCircleId;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setContentSum(int contentSum) {
            this.contentSum = contentSum;
        }

        public void setPraiseSum(int praiseSum) {
            this.praiseSum = praiseSum;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }

        public void setPhotoes(String photoes) {
            this.photoes = photoes;
        }

        public String getTimstamp() {
            return timstamp;
        }

        public int getLifeCircleId() {
            return lifeCircleId;
        }

        public String getContent() {
            return content;
        }

        public int getContentSum() {
            return contentSum;
        }

        public int getPraiseSum() {
            return praiseSum;
        }

        public int getCreateTime() {
            return createTime;
        }

        public String getPhotoes() {
            return photoes;
        }
    }


}
