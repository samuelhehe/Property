package xj.property.beans;

/**
 * 作者：asia on 2015/12/11 18:04
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class GeniusBean {

    private String emobId;
    private String nickname;
    private String avatar;
    private String grade;
    private boolean isMore;
    private boolean isShowMore = true;
    /**
     * famousIntroduce : 电脑专家，专治各种电脑问题，欢迎大家来扰！！！
     * 02/26
     */
    private String famousIntroduce;

    public void setIsMore(boolean isMore) {
        this.isMore = isMore;
    }

    public boolean isShowMore() {
        return isShowMore;
    }

    public void setIsShowMore(boolean isShowMore) {
        this.isShowMore = isShowMore;
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean isMore) {
        this.isMore = isMore;
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

    public void setFamousIntroduce(String famousIntroduce) {
        this.famousIntroduce = famousIntroduce;
    }

    public String getFamousIntroduce() {
        return famousIntroduce;
    }
}
