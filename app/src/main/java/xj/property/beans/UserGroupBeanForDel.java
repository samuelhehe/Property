package xj.property.beans;

import java.io.Serializable;
import java.util.List;

/**
 *
 * v3 2016/03/21
 */
public class UserGroupBeanForDel implements XJ{
    private int activityMemberId=0;
    private String emobGroupId="";
    private String emobUserId="";
    private String groupStatus="";
    public String nickname="";
    private String room="";
    public String avatar="";
    private String signature="";
    private String userFloor="";
    private String userUnit="";
    private String gender="";
    private String idcard="";
    private String idnumber="";
    private String communityName="";
    private int age=0;
    private String identity;
    private String intro;

    public boolean isChecked;
    /**
     * "emobId": "{用户环信ID}",
     "nickname": "{用户昵称}",
     "avatar": "{用户头像}",
     "grade": "{用户帮主角色}",
     "identity": "{用户牛人角色}",
     "test": {用户水军角色},
     "labels": [{
     "labelContent": "{标签}",
     "count": {被贴此标签次数}
     }]
     */


    private int test;  //// 是否是水军

    public List<LabelInfoBean> getLabels() {
        return labels;
    }

    private List<LabelInfoBean> labels;

    public int getTest() {
        return test;
    }

    public void setTest(int test) {
        this.test = test;
    }

    public void setLabels(List<LabelInfoBean> labels) {
        this.labels = labels;
    }

    public static class LabelInfoBean  {

        /**
         * labelContent : 中国好邻居
         * count : 1
         */

        private String labelContent;

        private Integer count;

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public void setLabelContent(String labelContent) {
            this.labelContent = labelContent;
        }

        public String getLabelContent() {
            return labelContent;
        }

    }


    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    private String grade = "normal"; /// 默认normal , bangzhu, fubangzhu ,zhanglao,bangzhong ///普通人, 帮主, 副帮主, 长老, 帮众.

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    private int lifeCircleSum=0;
    private int characterValues;
    private float characterPercent;
    private int bonuscoinCount;//帮帮币总量
    private int bonuscoin;//帮帮币基数
    private int exchange;//帮帮币兑换值

    private String showBonuscoin; //小区是否支持帮帮币

    public String getShowBonuscoin() {
        return showBonuscoin;
    }

    public void setShowBonuscoin(String showBonuscoin) {
        this.showBonuscoin = showBonuscoin;
    }

    public int getBonuscoinCount() {  return bonuscoinCount; }

    public void setBonuscoinCount(int bonuscoinCount) {this.bonuscoinCount = bonuscoinCount;}

    public void setBonuscoin(int bonuscoin){this.bonuscoin=bonuscoin;}

    public int getBonuscoin(){
        return bonuscoin;
    }

    public void setExchange(int exchange){this.exchange=exchange;}

    public int  getExchange(){
        return exchange;
    }

    public int getLifeCircleSum() {
        return lifeCircleSum;
    }

    public void setLifeCircleSum(int lifeCircleSum) {
        this.lifeCircleSum = lifeCircleSum;
    }

    public int getCharacterValues() {
        return characterValues;
    }

    public void setCharacterValues(int characterValues) {
        this.characterValues = characterValues;
    }

    public float getCharacterPercent() {
        return characterPercent;
    }

    public void setCharacterPercent(float characterPercent) {
        this.characterPercent = characterPercent;
    }

    public int getActivityMemberId() {
        return activityMemberId;
    }

    public void setActivityMemberId(int activityMemberId) {
        this.activityMemberId = activityMemberId;
    }

    public String getEmobGroupId() {
        return emobGroupId;
    }

    public void setEmobGroupId(String emobGroupId) {
        this.emobGroupId = emobGroupId;
    }

    public String getEmobUserId() {
        return emobUserId;
    }

    public void setEmobUserId(String emobUserId) {
        this.emobUserId = emobUserId;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public String getGroupStatus() {
        return groupStatus;
    }

    public void setGroupStatus(String groupStatus) {
        this.groupStatus = groupStatus;
    }

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public String getEmobId() {
        return emobId;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getUserFloor() {
        return userFloor;
    }

    public void setUserFloor(String userFloor) {
        this.userFloor = userFloor;
    }

    public String getUserUnit() {
        return userUnit;
    }

    public void setUserUnit(String userUnit) {
        this.userUnit = userUnit;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int shopId;
    public String shopName;
    public String shopsDesc;
    public String address;
    public int  communityId;
    public String emobId;
    public String phone;
    public String logo;
    public String status;
    public String sort;
    public int  createTime;
    public String authCode;
    public int  score;
    public String businessStartTime;
    public String businessEndTime;
    public int orderSum;

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopsDesc() {
        return shopsDesc;
    }

    public void setShopsDesc(String shopsDesc) {
        this.shopsDesc = shopsDesc;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getBusinessStartTime() {
        return businessStartTime;
    }

    public void setBusinessStartTime(String businessStartTime) {
        this.businessStartTime = businessStartTime;
    }

    public String getBusinessEndTime() {
        return businessEndTime;
    }

    public void setBusinessEndTime(String businessEndTime) {
        this.businessEndTime = businessEndTime;
    }

    public int getOrderSum() {
        return orderSum;
    }

    public void setOrderSum(int orderSum) {
        this.orderSum = orderSum;
    }

    @Override
    public String toString() {
        return "UserGroupBeanForDel{" +
                "activityMemberId=" + activityMemberId +
                ", emobGroupId='" + emobGroupId + '\'' +
                ", emobUserId='" + emobUserId + '\'' +
                ", groupStatus='" + groupStatus + '\'' +
                ", nickname='" + nickname + '\'' +
                ", room='" + room + '\'' +
                ", avatar='" + avatar + '\'' +
                ", signature='" + signature + '\'' +
                ", userFloor='" + userFloor + '\'' +
                ", userUnit='" + userUnit + '\'' +
                ", gender='" + gender + '\'' +
                ", idcard='" + idcard + '\'' +
                ", idnumber='" + idnumber + '\'' +
                ", communityName='" + communityName + '\'' +
                ", age=" + age +
                ", identity='" + identity + '\'' +
                ", intro='" + intro + '\'' +
                ", isChecked=" + isChecked +
                ", test=" + test +
                ", labels=" + labels +
                ", grade='" + grade + '\'' +
                ", lifeCircleSum=" + lifeCircleSum +
                ", characterValues=" + characterValues +
                ", characterPercent=" + characterPercent +
                ", bonuscoinCount=" + bonuscoinCount +
                ", bonuscoin=" + bonuscoin +
                ", exchange=" + exchange +
                ", showBonuscoin='" + showBonuscoin + '\'' +
                ", shopId=" + shopId +
                ", shopName='" + shopName + '\'' +
                ", shopsDesc='" + shopsDesc + '\'' +
                ", address='" + address + '\'' +
                ", communityId=" + communityId +
                ", emobId='" + emobId + '\'' +
                ", phone='" + phone + '\'' +
                ", logo='" + logo + '\'' +
                ", status='" + status + '\'' +
                ", sort='" + sort + '\'' +
                ", createTime=" + createTime +
                ", authCode='" + authCode + '\'' +
                ", score=" + score +
                ", businessStartTime='" + businessStartTime + '\'' +
                ", businessEndTime='" + businessEndTime + '\'' +
                ", orderSum=" + orderSum +
                '}';
    }
}
