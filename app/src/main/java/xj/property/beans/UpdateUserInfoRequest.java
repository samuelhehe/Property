package xj.property.beans;

/**
 * Created by Administrator on 2015/3/24.
 */
public class UpdateUserInfoRequest {
public String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String nickname;
    public String phone;
    public int age;
    public String avatar;
    public String gender;
    public String idcard;
    public String idnumber;
    public String room;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return "UpdateUserInfoRequest{" +
                "password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", phone='" + phone + '\'' +
                ", age=" + age +
                ", avatar='" + avatar + '\'' +
                ", gender='" + gender + '\'' +
                ", idcard='" + idcard + '\'' +
                ", idnumber='" + idnumber + '\'' +
                ", room='" + room + '\'' +
                '}';
    }
}
