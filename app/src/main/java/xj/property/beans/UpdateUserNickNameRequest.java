package xj.property.beans;

/**
 * Created by n on 2015/4/16.
 * v3 2016/03/17
 */
public class UpdateUserNickNameRequest extends xj.property.netbase.BaseBean {
    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}