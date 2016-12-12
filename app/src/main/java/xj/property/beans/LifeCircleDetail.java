package xj.property.beans;

/**
 * Created by Administrator on 2015/6/9.
 * 生活圈评论类
 */
public class LifeCircleDetail {

    /**
     *     {"lifeCircleDetailId": 3,
     "emobIdFrom": "b20c8d84dfb54f458154bae72a4c85d6",
     "emobIdTo": "b20c8d84dfb54f458154bae72a4c85d6",
     "detailContent": "男女童了",
     "lifeCircleId": 14,
     "createTime": 1456985074,
     "updateTime": 1456985074,
     "praiseSum": 0,
     "fromName": "7会",
     "toName": "7会"}
     *  v3 2016/03/04
     */


    private Integer lifeCircleDetailId;

    private String emobIdFrom; // 评论者

    private String emobIdTo; // 被评论者

    private String fromName; // 评论者名称

    private String toName; // 被评论者名称

    private Integer praiseSum; //赞的个数

    private Integer createTime; // 添加时间

    private Integer updateTime; // 修改时间

    private Integer lifeCircleId;

    private String detailContent; // 评论内容

    public Integer getLifeCircleDetailId() {
        return lifeCircleDetailId;
    }

    public void setLifeCircleDetailId(Integer lifeCircleDetailId) {
        this.lifeCircleDetailId = lifeCircleDetailId;
    }

    public String getEmobIdFrom() {
        return emobIdFrom;
    }

    public void setEmobIdFrom(String emobIdFrom) {
        this.emobIdFrom = emobIdFrom;
    }

    public String getEmobIdTo() {
        return emobIdTo;
    }

    public void setEmobIdTo(String emobIdTo) {
        this.emobIdTo = emobIdTo;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public Integer getPraiseSum() {
        return praiseSum;
    }

    public void setPraiseSum(Integer praiseSum) {
        this.praiseSum = praiseSum;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getLifeCircleId() {
        return lifeCircleId;
    }

    public void setLifeCircleId(Integer lifeCircleId) {
        this.lifeCircleId = lifeCircleId;
    }

    public String getDetailContent() {
        return detailContent;
    }

    public void setDetailContent(String detailContent) {
        this.detailContent = detailContent;
    }

    public LifeCircleDetail() {
    }

    public LifeCircleDetail(Integer lifeCircleDetailId, String emobIdFrom, String emobIdTo, String fromName, String toName, Integer praiseSum, Integer createTime, Integer updateTime, Integer lifeCircleId, String detailContent) {
        this.lifeCircleDetailId = lifeCircleDetailId;
        this.emobIdFrom = emobIdFrom;
        this.emobIdTo = emobIdTo;
        this.fromName = fromName;
        this.toName = toName;
        this.praiseSum = praiseSum;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.lifeCircleId = lifeCircleId;
        this.detailContent = detailContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LifeCircleDetail that = (LifeCircleDetail) o;

        if (fromName != null ? !fromName.equals(that.fromName) : that.fromName != null)
            return false;
        if (lifeCircleDetailId != null ? !lifeCircleDetailId.equals(that.lifeCircleDetailId) : that.lifeCircleDetailId != null)
            return false;
        if (lifeCircleId != null ? !lifeCircleId.equals(that.lifeCircleId) : that.lifeCircleId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = lifeCircleDetailId != null ? lifeCircleDetailId.hashCode() : 0;
        result = 31 * result + (fromName != null ? fromName.hashCode() : 0);
        result = 31 * result + (lifeCircleId != null ? lifeCircleId.hashCode() : 0);
        return result;
    }
}
