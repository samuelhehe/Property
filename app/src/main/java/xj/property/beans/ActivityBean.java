package xj.property.beans;

import java.io.Serializable;
import java.util.List;

import xj.property.netbase.*;


/**
 * Created by maxwell on 15/1/26.
 * v3 2016/02/29
 */
public class ActivityBean  extends xj.property.netbase.BaseBean {

    private int activityId;
    private String activityTitle;
    private String activityDetail;
    private int activityTime;
    private String status;
    private String place;
    private int createTime;
    private int communityId;
    private String emobGroupId;

    private String emobIdOwner;

    private String activityUserSum;
    private boolean isRead;
    private int bzPraiseSum;
    private String superPraise; // 是否有超赞 yes/no

    private String approval ;//// 是否需要申请, 需要yes/不需要no

    private String photoes; /// 发活动图片, 逗号隔开



    private List<ActivityMate> users;

    /**
     *
     * "activityId": {活动/话题数据ID},
     "activityTitle": "{活动/话题标题}",
     "activityDetail": "{活动/话题描述}",
     "emobIdOwner": "{群主环信ID}",
     "emobGroupId": "{环信群组ID}",
     "place": "{活动地点}",
     "status": "{活动状态}",
     "createTime": {创建时间},
     "communityId": {小区ID},
     "activityTime": {活动时间},
     "type": "activity",
     "bzPraiseSum": {标识赞数量},
     "approval": "{入群是否需要审核}",
     "cityId": {城市ID},
     "cityName": "{城市名称}",
     "communityName": "{小区名称}",
     "houtaiAdmin": "{后台分管管理员环信ID}",
     "activityUserSum": {活动/话题成员数量},
     *
     */



    /**
     * type : activity
     * cityId : 373
     * cityName : 北京
     * communityName : 首邑溪谷
     * houtaiAdmin : bd280ed1d0c667cbd94f4724f91d52fc
     * v3 2016/02/29
     */

    private String type;
    private int cityId;
    private String cityName;
    private String communityName;
    private String houtaiAdmin;

    public List<ActivityMate> getUsers() {
        return users;
    }

    public void setUsers(List<ActivityMate> users) {
        this.users = users;
    }

    public void setEmobIdOwner(String emobIdOwner) {
        this.emobIdOwner = emobIdOwner;
    }
    public String getEmobIdOwner() {
        return this.emobIdOwner;
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public void setHoutaiAdmin(String houtaiAdmin) {
        this.houtaiAdmin = houtaiAdmin;
    }

    public String getType() {
        return type;
    }

    public int getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCommunityName() {
        return communityName;
    }

    public String getHoutaiAdmin() {
        return houtaiAdmin;
    }

    public String getPhotoes() {
        return photoes;
    }

    public void setPhotoes(String photoes) {
        this.photoes = photoes;
    }

    public static class ActivityMate  implements  Serializable{

        /**
         * emobId : bf1fd76cc783d754d377091264a2c933
         * nickname : 肖伟Ⓜ️
         * avatar : http://7d9lcl.com2.z0.glb.qiniucdn.com/FrOYe-RHMeS2KVNkezY2W_vHXHrx
         * identity : normal
         * grade : fubangzhu
         */

        private String emobId;
        private String nickname;
        private String avatar;
        private String identity;
        private String grade;

        public void setEmobId(String emobId) {
            this.emobId = emobId;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getEmobId() {
            return emobId;
        }

        public String getNickname() {
            return nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public String getIdentity() {
            return identity;
        }

        public String getGrade() {
            return grade;
        }
    }


    public int getBzPraiseSum() {
        return bzPraiseSum;
    }

    public void setBzPraiseSum(int bzPraiseSum) {
        this.bzPraiseSum = bzPraiseSum;
    }

    public String getSuperPraise() {
        return superPraise;
    }

    public void setSuperPraise(String superPraise) {
        this.superPraise = superPraise;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

//    public ActivityBean(){}
//
//    public ActivityBean(ActivityModel model) {
//        this.activityId = model.activityId;
//        this.activityTitle = model.activityTitle;
//        this.activityDetail = model.activityDetail;
//        this.activityTime =model. activityTime;
//        this.emobIdOwner = model.emobIdOwner;
//        this.status = model.status;
//        this.place = model.place;
//        this.createTime = model.createTime;
//        this.communityId = model.communityId;
//        this.emobGroupId = model.emobGroupId;
//        this.activityUserSum = model.activityUserSum;
//      List<  ActivityPhoneModel > phoneModels=model.phoneModels();
//        photoList=new ArrayList<>();
//        for(int i=0;i<phoneModels.size();i++){
//            photoList.add(new photo(phoneModels.get(i)));
//        }
//    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getActivityDetail() {
        return activityDetail;
    }

    public void setActivityDetail(String activityDetail) {
        this.activityDetail = activityDetail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public String getEmobGroupId() {
        return emobGroupId;
    }

    public void setEmobGroupId(String emobGroupId) {
        this.emobGroupId = emobGroupId;
    }

    public int getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(int activityTime) {
        this.activityTime = activityTime;
    }

    public String getActivityUserSum() {
        return activityUserSum;
    }

    public void setActivityUserSum(String activityUserSum) {
        this.activityUserSum = activityUserSum;
    }


}
