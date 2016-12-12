package xj.property.beans;

/**
 * Created by Administrator on 2015/3/24.
 */
public class UserLoginRequest extends xj.property.netbase.BaseBean {


    /**
     * {
     * "username": "15811078116",
     * "password": "e10adc3949ba59abbe56e057f20f883e",
     * "role": "owner",
     * "equipment": "android",
     * "equipmentVersion": "4.4.2"
     * }
     */

    public String username;
    public String password;
    public String equipmentVersion;

    public String role = "";

    public String equipment;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
