package xj.property.beans;

import java.io.Serializable;

/**
 * Created by maxwell on 15/1/30.
 */
public class UserInfoDetailBean implements Serializable {


    //// 2016/02/24 V3 新字段 start

    private int cityId;
    private String cityName;
    private String communityName;
    private String equipment;
    private String equipmentVersion;
    private int characterValues;
    private int bonuscoinCount;
    private String houtaiAdmin;

    private String deviceToken; /// ios 设备使用

    //// 2016/02/24 V3 新字段 end

    private String nickname = "";
    private int registerTime;
    private String avatar = "";
    private int userId;
    private String username = "";
    private String emobId = "";
    private int age;
    private String phone = "";
    private String gender = "";
    private String idcard = "";
    private String idnumber = "";
    private String status = "";
    private String room = "";
    private long createTime;
    private String role = "owner";

    private int communityId;
    private String password = "";
    private String signature = "";
    private String userFloor = "";
    private String userUnit = "";
    private String identity = "";
    private String intro = "";

    private int test; /// 2016/02/18 添加用户类别信息， 是否是水军

    private String grade = "normal"; /// 默认normal , bangzhu, fubangzhu ,zhanglao,bangzhong ///普通人, 帮主, 副帮主, 长老, 帮众.
    /**
     * characterPercent : 0.0
     * lifeCircleSum : 0
     * bonuscoinEnable : 1
     * famousIntroduce  牛人介绍
     */

    private float characterPercent;  /// 人品值百分比
    private int lifeCircleSum; /// 生活圈总数
    private int bonuscoinEnable;  /// {小区帮帮币是否已开通(1->开通，0->未开通)}

    private String famousIntroduce; /// 牛人介绍


    public String getFamousIntroduce() {
        return famousIntroduce;
    }

    public void setFamousIntroduce(String famousIntroduce) {
        this.famousIntroduce = famousIntroduce;
    }

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public int getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(int registerTime) {
        this.registerTime = registerTime;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getEquipmentVersion() {
        return equipmentVersion;
    }

    public void setEquipmentVersion(String equipmentVersion) {
        this.equipmentVersion = equipmentVersion;
    }

    public int getCharacterValues() {
        return characterValues;
    }

    public void setCharacterValues(int characterValues) {
        this.characterValues = characterValues;
    }

    public int getBonuscoinCount() {
        return bonuscoinCount;
    }

    public void setBonuscoinCount(int bonuscoinCount) {
        this.bonuscoinCount = bonuscoinCount;
    }

    public String getHoutaiAdmin() {
        return houtaiAdmin;
    }

    public void setHoutaiAdmin(String houtaiAdmin) {
        this.houtaiAdmin = houtaiAdmin;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }


    public int getTest() {
        return test;
    }

    public void setTest(int test) {
        this.test = test;
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

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmobId() {
        return emobId;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public UserInfoDetailBean() {
    }

    public UserInfoDetailBean(UserBean userBean) {
        this.nickname = userBean.getNickname();
        this.role = userBean.getRole();
        this.password = userBean.getPassword();
        this.room = userBean.getRoom();
        this.userFloor = userBean.getUserFloor();
        this.username = userBean.getUsername();
        this.userUnit = userBean.getUserUnit();
        this.nickname = userBean.getNickname();

    }

    public UserInfoDetailBean(String nickname, String avatar, int userId, String username, String emobId, int age, String phone, String gender, String idcard, String idnumber, String status, String room, long createTime, String role, int communityId, String password, String signature, String userFloor, String userUnit) {
        this.nickname = nickname;
        this.avatar = avatar;
        this.userId = userId;
        this.username = username;
        this.emobId = emobId;
        this.age = age;
        this.phone = phone;
        this.gender = gender;
        this.idcard = idcard;
        this.idnumber = idnumber;
        this.status = status;
        this.room = room;
        this.createTime = createTime;
        this.role = role;
        this.communityId = communityId;
        this.password = password;
        this.signature = signature;
        this.userFloor = userFloor;
        this.userUnit = userUnit;
    }

    public void setCharacterPercent(float characterPercent) {
        this.characterPercent = characterPercent;
    }

    public void setLifeCircleSum(int lifeCircleSum) {
        this.lifeCircleSum = lifeCircleSum;
    }

    public void setBonuscoinEnable(int bonuscoinEnable) {
        this.bonuscoinEnable = bonuscoinEnable;
    }

    public float getCharacterPercent() {
        return characterPercent;
    }

    public int getLifeCircleSum() {
        return lifeCircleSum;
    }

    public int getBonuscoinEnable() {
        return bonuscoinEnable;
    }
}
