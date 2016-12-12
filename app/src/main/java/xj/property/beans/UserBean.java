package xj.property.beans;

/**
 * Created by maxwell on 15/2/9.
 */
public class UserBean  extends BaseBean{
    private String username;
    private String password;
    private String role="owner";
    private String userFloor;
    private String userUnit;
    private String room;
    private String nickname;
    private String signature;
    private String clientId;

    private int bonuscoinCount;

    //// 2016/1/15 注册用户
    private Integer registerTime;

    public int getBonuscoinCount() {  return bonuscoinCount; }

    public void setBonuscoinCount(int bonuscoinCount) {this.bonuscoinCount = bonuscoinCount;}

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    //  private String role;
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    //    public String getRole() {
//        return role;
//    }
//
//    public void setRole(String role) {
//        this.role = role;
//    }


    @Override
    public String toString() {
        return "UserBean{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", userFloor='" + userFloor + '\'' +
                ", userUnit='" + userUnit + '\'' +
                ", room='" + room + '\'' +
                ", bonuscoinCount='" + bonuscoinCount + '\'' +
                '}';
    }

    public Integer getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Integer registerTime) {
        this.registerTime = registerTime;
    }
}
