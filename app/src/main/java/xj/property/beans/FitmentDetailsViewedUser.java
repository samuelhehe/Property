package xj.property.beans;

/**
 * 作者：asia on 2015/12/14 14:00
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class FitmentDetailsViewedUser extends BaseBean {

    private String emobId;
    private Long timestemp;
    private String nickname;
    private String avatar;

    public String getEmobId() {
        return emobId;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    public Long getTimestemp() {
        return timestemp;
    }

    public void setTimestemp(Long timestemp) {
        this.timestemp = timestemp;
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
}
