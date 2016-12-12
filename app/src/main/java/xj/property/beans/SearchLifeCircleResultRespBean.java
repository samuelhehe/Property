package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/12/21.
 * v3 2016/03/18
 */
public class SearchLifeCircleResultRespBean {

    private int lifeCircleId;
    private int communityId;
    private int type;
    private String emobId;
    private int createTime;
    private String lifeContent;
    private int praiseSum;
    private String emobGroupId;
    private String typeContent;//生活圈类型
    private String avatar;
    private String nickname;
    private String grade;
    private String identity;
    private String superPraise;
    private String photoes; //生活圈图片
    private int characterValues;

    /*
    {
     "lifeCircleId": {生活圈ID},
     "communityId": {小区ID},
     "createTime": {发布时间},
     "lifeContent": "{生活圈内容}",
     "praiseSum": {被赞次数},
     "emobId": "{用户环信ID}",
     "type": {生活圈类型ID},
     "typeContent": "{生活圈类型}",
     "photoes": "{生活圈图片}",
     "superPraise": "{当前是否被超级赞}",
     "characterValues": {人品值},
     "avatar": "{用户头像}",
     "nickname": "{用户昵称}",
     "grade": "{用户帮主身份}",
     "identity": "{用户牛人身份}"
     }
     */

    public String getSuperPraise() {
        return superPraise;
    }

    public void setSuperPraise(String superPraise) {
        this.superPraise = superPraise;
    }

    public String getPhotoes() {
        return photoes;
    }

    public void setPhotoes(String photoes) {
        this.photoes = photoes;
    }

    public void setLifeCircleDetails(List<LifeCircleDetailsEntity> lifeCircleDetails) {
        this.lifeCircleDetails = lifeCircleDetails;
    }

    private List<LifeCircleDetailsEntity> lifeCircleDetails;


    public void setLifeCircleId(int lifeCircleId) {
        this.lifeCircleId = lifeCircleId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public void setLifeContent(String lifeContent) {
        this.lifeContent = lifeContent;
    }

    public void setPraiseSum(int praiseSum) {
        this.praiseSum = praiseSum;
    }

    public void setEmobGroupId(String emobGroupId) {
        this.emobGroupId = emobGroupId;
    }

    public void setTypeContent(String typeContent) {
        this.typeContent = typeContent;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public void setCharacterValues(int characterValues) {
        this.characterValues = characterValues;
    }

    public int getLifeCircleId() {
        return lifeCircleId;
    }

    public int getCommunityId() {
        return communityId;
    }

    public int getType() {
        return type;
    }

    public String getEmobId() {
        return emobId;
    }

    public int getCreateTime() {
        return createTime;
    }

    public String getLifeContent() {
        return lifeContent;
    }

    public int getPraiseSum() {
        return praiseSum;
    }

    public String getEmobGroupId() {
        return emobGroupId;
    }

    public String getTypeContent() {
        return typeContent;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public String getGrade() {
        return grade;
    }

    public String getIdentity() {
        return identity;
    }

    public int getCharacterValues() {
        return characterValues;
    }


    public List<LifeCircleDetailsEntity> getLifeCircleDetails() {
        return lifeCircleDetails;
    }


    /**
     *
     *
     *
     *
     *   "lifeCircleDetails": [{
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
     }]
     *
     *
     */



    public static class LifeCircleDetailsEntity {

        /*

        {
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
     }


         */

        private int lifeCircleDetailId;
        private String emobIdFrom;
        private String emobIdTo;
        private String fromName;
        private String toName;
        private int praiseSum;
        private int createTime;
        private int updateTime;
        private int lifeCircleId;
        private String detailContent;

        public void setLifeCircleDetailId(int lifeCircleDetailId) {
            this.lifeCircleDetailId = lifeCircleDetailId;
        }

        public void setEmobIdFrom(String emobIdFrom) {
            this.emobIdFrom = emobIdFrom;
        }

        public void setEmobIdTo(String emobIdTo) {
            this.emobIdTo = emobIdTo;
        }

        public void setFromName(String fromName) {
            this.fromName = fromName;
        }

        public void setToName(String toName) {
            this.toName = toName;
        }

        public void setPraiseSum(int praiseSum) {
            this.praiseSum = praiseSum;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }

        public void setUpdateTime(int updateTime) {
            this.updateTime = updateTime;
        }

        public void setLifeCircleId(int lifeCircleId) {
            this.lifeCircleId = lifeCircleId;
        }

        public void setDetailContent(String detailContent) {
            this.detailContent = detailContent;
        }

        public int getLifeCircleDetailId() {
            return lifeCircleDetailId;
        }

        public String getEmobIdFrom() {
            return emobIdFrom;
        }

        public String getEmobIdTo() {
            return emobIdTo;
        }

        public String getFromName() {
            return fromName;
        }

        public String getToName() {
            return toName;
        }

        public int getPraiseSum() {
            return praiseSum;
        }

        public int getCreateTime() {
            return createTime;
        }

        public int getUpdateTime() {
            return updateTime;
        }

        public int getLifeCircleId() {
            return lifeCircleId;
        }

        public String getDetailContent() {
            return detailContent;
        }
    }
}
