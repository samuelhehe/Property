package xj.property.netbasebean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/2/23.
 */
public class UserInfoRegRespBean  implements Serializable {


    /**
     * userId : 3
     * username : 18600113751
     * emobId : fcb6adf78bef4ee4940d2af8ee7905f9
     * nickname : 天昭
     * avatar : http://7d9lcl.com2.z0.glb.qiniucdn.com/FpgAe-5Aan5MCL4g0BonwAnYjos9
     * createTime : 1456197959
     * registerTime : 1456197959
     * status : normal
     * role : owner
     * cityId : 373
     * cityName : 北京
     * communityId : 1
     * communityName : 天华
     * userFloor : 1
     * userUnit : 1
     * room : 1
     * equipment : ios
     * equipmentVersion : 1.5.0
     * characterValues : 0
     * grade : normal
     * identity : normal
     * test : 0
     * bonuscoinCount : 0
     * houtaiAdmin : bd280ed1d0c667cbd94f4724f91d52fc
     * deviceToken : 69ec9ed8c0200349842e4c6539b6cbdbdf4888926796cccca97d33ad8e0c7e05
     */

    private int userId;
    private String username;
    private String emobId;
    private String nickname;
    private String avatar;
    private int createTime;
    private int registerTime;

    @Override
    public String toString() {
        return "UserInfoRegRespBean{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", emobId='" + emobId + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", createTime=" + createTime +
                ", registerTime=" + registerTime +
                ", status='" + status + '\'' +
                ", role='" + role + '\'' +
                ", cityId=" + cityId +
                ", cityName='" + cityName + '\'' +
                ", communityId=" + communityId +
                ", communityName='" + communityName + '\'' +
                ", userFloor='" + userFloor + '\'' +
                ", userUnit='" + userUnit + '\'' +
                ", room='" + room + '\'' +
                ", equipment='" + equipment + '\'' +
                ", equipmentVersion='" + equipmentVersion + '\'' +
                ", characterValues=" + characterValues +
                ", grade='" + grade + '\'' +
                ", identity='" + identity + '\'' +
                ", test=" + test +
                ", bonuscoinCount=" + bonuscoinCount +
                ", houtaiAdmin='" + houtaiAdmin + '\'' +
                ", deviceToken='" + deviceToken + '\'' +
                '}';
    }

    private String status;
    private String role;
    private int cityId;
    private String cityName;
    private int communityId;
    private String communityName;
    private String userFloor;
    private String userUnit;
    private String room;
    private String equipment;
    private String equipmentVersion;
    private int characterValues;
    private String grade;
    private String identity;
    private int test;
    private int bonuscoinCount;
    private String houtaiAdmin;
    private String deviceToken;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;


    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public void setRegisterTime(int registerTime) {
        this.registerTime = registerTime;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public void setUserFloor(String userFloor) {
        this.userFloor = userFloor;
    }

    public void setUserUnit(String userUnit) {
        this.userUnit = userUnit;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public void setEquipmentVersion(String equipmentVersion) {
        this.equipmentVersion = equipmentVersion;
    }

    public void setCharacterValues(int characterValues) {
        this.characterValues = characterValues;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public void setTest(int test) {
        this.test = test;
    }

    public void setBonuscoinCount(int bonuscoinCount) {
        this.bonuscoinCount = bonuscoinCount;
    }

    public void setHoutaiAdmin(String houtaiAdmin) {
        this.houtaiAdmin = houtaiAdmin;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
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

    public int getCreateTime() {
        return createTime;
    }

    public int getRegisterTime() {
        return registerTime;
    }

    public String getStatus() {
        return status;
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

    public int getCommunityId() {
        return communityId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public String getUserFloor() {
        return userFloor;
    }

    public String getUserUnit() {
        return userUnit;
    }

    public String getRoom() {
        return room;
    }

    public String getEquipment() {
        return equipment;
    }

    public String getEquipmentVersion() {
        return equipmentVersion;
    }

    public int getCharacterValues() {
        return characterValues;
    }

    public String getGrade() {
        return grade;
    }

    public String getIdentity() {
        return identity;
    }

    public int getTest() {
        return test;
    }

    public int getBonuscoinCount() {
        return bonuscoinCount;
    }

    public String getHoutaiAdmin() {
        return houtaiAdmin;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

}
