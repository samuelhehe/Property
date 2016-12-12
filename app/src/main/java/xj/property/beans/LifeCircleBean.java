package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/6/9.
 * v3 2016/03/04
 *  生活圈列表和生活圈详情公用bean
 */
public class LifeCircleBean {

    private Integer lifeCircleId;/// 生活圈ID

    private Integer communityId; // 小区id

    private String emobId; // 环信id

    private Integer createTime; // 创建时间

    private String lifeContent; // 内容

    private Integer praiseSum; //赞的人的总数

    private String avatar; // 头像
    private String nickname; // 昵称
    private double characterPercent;  //// v3 2016/03/04 {生活圈创建者的人品值打败同小区内用户数量的百分比}
    private Integer characterValues; // 人品值
    private String emobGroupId; // 群id

    private List<LifeCircleDetail> lifeCircleDetails; // 评论内容

    private List<LifePraise> lifePraises; /// 赞 for 生活圈详情

    private List<LifeLabelBean> labels; /// 个人标签

    private Integer type =0; //生活圈信息类型

    private String typeContent = ""; // 店铺默认的生活圈内容


    private String superPraise ; // 是否有超赞 yes/no

    private Integer bzPraiseSum ; //标志赞的个数

    private Integer praiseUserSum ; //20151012 添加  // 赞人品数

    private String grade;//用户的身份

    private int hasPraised;  /// 是否点了赞,  显示红心儿.. 是否给这个人点过赞-->1：当日点过；0：当日未点过 {今天是否给这个人点过赞：1->点过，0->未点过},

    private String identity;//是普通人还是牛人


    /**
     *
     * {
     "lifeCircleId": {生活圈ID},
     "communityId": {小区ID},
     "createTime": {生活圈创建时间},
     "lifeContent": "{生活圈内容}",
     "photoes": "{生活圈图片，多张图会用英文逗号隔开}",
     "emobGroupId": "{生活圈环信群组ID}",
     "type": {快店类型},
     "typeContent": "{快店类型名称}",
     "praiseSum": {生活圈被赞次数},
     "bzPraiseSum": {标识赞数量},
     "emobId": "{生活圈创建者环信ID}",
     "characterValues": {生活圈创建者人品值},
     "avatar": "{生活圈创建者头像}",
     "nickname": "{生活圈创建者昵称}",
     "grade": "{生活圈创建者帮主角色：bangzhu->帮主，fubangzhu->副帮主，bangzhong->达人，normal->普通用户}",
     "identity": "{生活圈创建者牛人角色：famous->牛人，normal->不是牛人}",
     "activityTitle": "{生活圈创建的群组的标题}",
     "superPraise": "{生活圈是否被超级赞：yes->是，no->否}",
     "hasPraised": {今天是否给这个人点过赞：1->点过，0->未点过},
     "lifeCircleDetails": [{
     "lifeCircleDetailId": {生活圈评论ID},
     "lifeCircleId": {生活圈ID},
     "emobIdFrom": "{评论者环信ID}",
     "fromName": "{评论者昵称}",
     "emobIdTo": "{被评论者环信ID}",
     "toName": "{被评论者昵称}"
     "detailContent": "{评论内容}",
     "praiseSum": {评论被赞次数},
     "createTime": {评论创建时间},
     "updateTime": {修改时间}
     }],
     "labels": [{
     "labelContent": "{标签}",
     "count": {被贴次数}
     }]
     }


     *
     */


    private String photoes;     //// 生活圈的图片, 逗号隔开


    private String activityTitle; /// 生活圈创建时的群组标题

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }


    public Integer getPraiseUserSum() {
        return praiseUserSum;
    }

    public void setPraiseUserSum(Integer praiseUserSum) {
        this.praiseUserSum = praiseUserSum;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }


    public String getSuperPraise() {
        return superPraise;
    }

    public void setSuperPraise(String superPraise) {
        this.superPraise = superPraise;
    }

    public Integer getBzPraiseSum() {
        return bzPraiseSum;
    }

    public void setBzPraiseSum(Integer bzPraiseSum) {
        this.bzPraiseSum = bzPraiseSum;
    }

    public String getTypeContent() {
        return typeContent;
    }

    public void setTypeContent(String typeContent) {
        this.typeContent = typeContent;
    }

    public List<LifePraise> getLifePraises() {
        return lifePraises;
    }

    public void setLifePraises(List<LifePraise> lifePraises) {
        this.lifePraises = lifePraises;
    }

    public double getCharacterPercent() {
        return characterPercent;
    }

    public void setCharacterPercent(double characterPercent) {
        this.characterPercent = characterPercent;
    }

    public List<LifeCircleDetail> getLifeCircleDetails() {
        return lifeCircleDetails;
    }

    public void setLifeCircleDetails(List<LifeCircleDetail> lifeCircleDetails) {
        this.lifeCircleDetails = lifeCircleDetails;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<LifeLabelBean> getLabels() {
        return labels;
    }

    public void setLabels(List<LifeLabelBean> labels) {
        this.labels = labels;
    }

    public int getHasPraised() {
        return hasPraised;
    }

    public void setHasPraised(int hasPraised) {
        this.hasPraised = hasPraised;
    }



    public Integer getLifeCircleId() {
        return lifeCircleId;
    }

    public void setLifeCircleId(Integer lifeCircleId) {
        this.lifeCircleId = lifeCircleId;
    }

    public Integer getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }

    public String getEmobId() {
        return emobId;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public String getLifeContent() {
        return lifeContent;
    }

    public void setLifeContent(String lifeContent) {
        this.lifeContent = lifeContent;
    }

    public Integer getPraiseSum() {
        return praiseSum;
    }

    public void setPraiseSum(Integer praiseSum) {
        this.praiseSum = praiseSum;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getCharacterValues() {
        return characterValues;
    }

    public void setCharacterValues(Integer characterValues) {
        this.characterValues = characterValues;
    }

    public String getEmobGroupId() {
        return emobGroupId;
    }

    public void setEmobGroupId(String emobGroupId) {
        this.emobGroupId = emobGroupId;
    }

    public String getPhotoes() {
        return photoes;
    }

    public void setPhotoes(String photoes) {
        this.photoes = photoes;
    }


    public static class LifeLabelBean {

        /**
         * labelContent : 中国好邻居
         * count : 1
         */

        private String labelContent;
        private String count;

        /**
         * emobIdTo : d463b16dfc014466a1e441dd685ba505
         * v3 2016/03/04
         */
        private String emobIdTo;


        public void setLabelContent(String labelContent) {
            this.labelContent = labelContent;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getLabelContent() {
            return labelContent;
        }

        public String getCount() {
            return count;
        }

        public void setEmobIdTo(String emobIdTo) {
            this.emobIdTo = emobIdTo;
        }

        public String getEmobIdTo() {
            return emobIdTo;
        }
    }

    /**
     * 生活圈点赞
     *
     */
    public static class LifePraise {
        public String emobId;
        public String avatar;
        public String nickname;
        public String grade;
        public int praiseSum;
        public String identity ; /// 是否牛人

        /**
         * {
         "emobId": "fcb6adf78bef4ee4940d2af8ee7905f9",
         "nickname": "天昭",
         "avatar": "http://7d9lcl.com2.z0.glb.qiniucdn.com/FpgAe-5Aan5MCL4g0BonwAnYjos9",
         "grade": "normal",
         "identity": "normal",
         "praiseSum": 2
         }
         */

        public int getPraiseSum() {
            return praiseSum;
        }

        public void setPraiseSum(int praiseSum) {
            this.praiseSum = praiseSum;
        }

        public String getIdentity() {
            return identity;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }

        public String getEmobId() {
            return emobId;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public void setEmobId(String emobId) {
            this.emobId = emobId;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

    }
}
