package xj.property.beans;

/**
 * 作者：asia on 2015/12/14 14:03
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class FitmentDetailsUser extends BaseBean {

    private String emobId;
    private String nickname;
    private String avatar;
    private String grade;

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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
