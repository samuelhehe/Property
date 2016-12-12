package xj.property.beans;

/**
 * Created by maxwell on 15/1/30.
 */
public class UserGroupBean implements XJ {
    private int activityMemberId = 0;
    private String emobGroupId = "";
    private String emobUserId = "";
    private String groupStatus = "";
    public String nickname = "";
    private String room = "";
    public String avatar = "";
    private String signature = "";
    private String userFloor = "";
    private String userUnit = "";
    private String gender = "";
    private String idcard = "";
    private String idnumber = "";
    private String communityName = "";
    private int age = 0;
    private String identity;


    /**
     * userId : 23
     * username : 18603851528
     * registerTime : 1456300620
     * role : owner
     * cityId : 373
     * cityName : 北京
     * deviceToken : 057002c71f5e764a3b3f5897f38b46eb4941d013fe53f944f6e628feaee8fc9f
     * equipment : ios
     * equipmentVersion : 1.5.0
     * houtaiAdmin : bd280ed1d0c667cbd94f4724f91d52fc
     * bonuscoinEnable : 1
     */


    /**
     * {
     * "userId":23,
     * "username":"18603851528",
     * "emobId":"7b02baa52ab44fb886aad99af7da7a08",
     * "nickname":"上证指数",
     * "avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/81997b96c61b4ab78204688502ffbdc8",
     * "createTime":1456300620,
     * "registerTime":1456300620,
     * "status":"normal",
     * "role":"owner",
     * "cityId":373,"cityName":"北京",
     * "communityId":1,"communityName":"天华",
     * "userFloor":"1","userUnit":"1","room":"101",
     * "deviceToken":"057002c71f5e764a3b3f5897f38b46eb4941d013fe53f944f6e628feaee8fc9f",
     * "equipment":"ios","equipmentVersion":"1.5.0","characterValues":3,
     * "grade":"bangzhu","identity":"normal","test":0,"bonuscoinCount":555555452,
     * "houtaiAdmin":"bd280ed1d0c667cbd94f4724f91d52fc","bonuscoinEnable":1,
     * "characterPercent":98.93617021276596,"lifeCircleSum":11}
     *
     *
     */





    private int userId;
    private String username;
    private String famousIntroduce;
    private int registerTime;
    private String role;
    private int cityId;
    private String cityName;
    private String deviceToken;
    private String equipment;
    private String equipmentVersion;
    private String houtaiAdmin;
    private int bonuscoinEnable;


    public int getTest() {
        return test;
    }

    public void setTest(int test) {
        this.test = test;
    }

    private int test;//// 用户是否是水军  如果>0 是水军

    public boolean isChecked;

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    private String grade = "normal"; /// 默认normal , bangzhu, fubangzhu ,zhanglao,bangzhong ///普通人, 帮主, 副帮主, 长老, 帮众.

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    private int lifeCircleSum = 0;
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

    public int getBonuscoinCount() {
        return bonuscoinCount;
    }

    public void setBonuscoinCount(int bonuscoinCount) {
        this.bonuscoinCount = bonuscoinCount;
    }

    public void setBonuscoin(int bonuscoin) {
        this.bonuscoin = bonuscoin;
    }

    public int getBonuscoin() {
        return bonuscoin;
    }

    public void setExchange(int exchange) {
        this.exchange = exchange;
    }

    public int getExchange() {
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
    public int communityId;
    public String emobId;
    public String phone;
    public String logo;
    public String status;
    public String sort;
    public int createTime;
    public String authCode;
    public int score;
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
        return "UserGroupBean{" +
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
                ", lifeCircleSum=" + lifeCircleSum +
                ", characterValues=" + characterValues +
                ", characterPercent=" + characterPercent +
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
                ", bonuscoinCount=" + bonuscoinCount +
                ", bonuscoin=" + bonuscoin +
                ", exchange=" + exchange +
                '}';
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRegisterTime(int registerTime) {
        this.registerTime = registerTime;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public void setEquipmentVersion(String equipmentVersion) {
        this.equipmentVersion = equipmentVersion;
    }

    public void setHoutaiAdmin(String houtaiAdmin) {
        this.houtaiAdmin = houtaiAdmin;
    }

    public void setBonuscoinEnable(int bonuscoinEnable) {
        this.bonuscoinEnable = bonuscoinEnable;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public int getRegisterTime() {
        return registerTime;
    }

    public String getRole() {
        return role;
    }

    public int getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public String getEquipment() {
        return equipment;
    }

    public String getEquipmentVersion() {
        return equipmentVersion;
    }

    public String getHoutaiAdmin() {
        return houtaiAdmin;
    }

    public int getBonuscoinEnable() {
        return bonuscoinEnable;
    }

    public String getFamousIntroduce() {
        return famousIntroduce;
    }

    public void setFamousIntroduce(String famousIntroduce) {
        this.famousIntroduce = famousIntroduce;
    }
}
