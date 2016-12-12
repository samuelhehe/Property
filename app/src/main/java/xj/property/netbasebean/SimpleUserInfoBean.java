package xj.property.netbasebean;

/**
 * Created by Administrator on 2016/3/18.
 */
public class SimpleUserInfoBean {

    /**
     * nickname : 浮白
     * avatar : http://7d9lcl.com2.z0.glb.qiniucdn.com/FpgAe-5Aan5MCL4g0BonwAnYjos9
     * characterValues : 6
     * grade : normal
     * identity : famous
     * test : 0
     * characterPercent : 0
     */

    private String nickname;
    private String avatar;
    private Integer characterValues;
    private String grade;
    private String identity;
    private int test;
    private Float characterPercent;

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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


    public String getNickname() {
        return nickname;
    }

    public String getAvatar() {
        return avatar;
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


    public Float getCharacterPercent() {
        return characterPercent;
    }

    public void setCharacterPercent(Float characterPercent) {
        this.characterPercent = characterPercent;
    }

    public Integer getCharacterValues() {
        return characterValues;
    }

    public void setCharacterValues(Integer characterValues) {
        this.characterValues = characterValues;
    }
}
