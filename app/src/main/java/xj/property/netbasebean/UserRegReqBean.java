package xj.property.netbasebean;

import xj.property.netbase.BaseBean;
import xj.property.utils.other.Config;

/**
 * Created by Administrator on 2016/2/23.
 */
public class UserRegReqBean extends BaseBean {

    public UserRegReqBean(){
        if (Config.PHONETYPE.equals(android.os.Build.MANUFACTURER)) {
            equipment = "mi";
        } else {
            equipment = "android";
        }
    }


    /**
     * equipment : ios
     * equipmentVersion : 1.5.0
     * deviceToken : 69ec9ed8c0200349842e4c6539b6cbdbdf4888926796cccca97d33ad8e0c7e05
     * authCode : 5103
     * username : 18600113751
     * nickname : 天昭
     * avatar : http://7d9lcl.com2.z0.glb.qiniucdn.com/FpgAe-5Aan5MCL4g0BonwAnYjos9
     * userFloor : 1
     * userUnit : 1
     * room : 1
     * password : e10adc3949ba59abbe56e057f20f883e
     */

    private String equipment;
    private String equipmentVersion;
    private String deviceToken;
    private String authCode;
    private String username;
    private String nickname;
    private String avatar;
    private String userFloor;
    private String userUnit;
    private String room;
    private String password;

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public void setEquipmentVersion(String equipmentVersion) {
        this.equipmentVersion = equipmentVersion;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEquipment() {
        return equipment;
    }

    public String getEquipmentVersion() {
        return equipmentVersion;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public String getAuthCode() {
        return authCode;
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatar() {
        return avatar;
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

    public String getPassword() {
        return password;
    }
}
