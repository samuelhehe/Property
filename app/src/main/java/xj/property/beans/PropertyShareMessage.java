package xj.property.beans;

/**
 * 作者：che on 2016/3/18 12:27
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class PropertyShareMessage implements XJ {
    private int bonuscoin;
    private String content;
    private String photo;

    public int getBonuscoin() {
        return bonuscoin;
    }

    public void setBonuscoin(int bonuscoin) {
        this.bonuscoin = bonuscoin;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
