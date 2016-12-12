package xj.property.beans;

/**
 * 作者：asia on 2016/3/16 16:02
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class AddCommentsRequestV3 extends xj.property.netbase.BaseBean {

    private String serial;
    private int score;
    private String content;
    private String emobIdFrom;
    private String emobIdTo;

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEmobIdFrom() {
        return emobIdFrom;
    }

    public void setEmobIdFrom(String emobIdFrom) {
        this.emobIdFrom = emobIdFrom;
    }

    public String getEmobIdTo() {
        return emobIdTo;
    }

    public void setEmobIdTo(String emobIdTo) {
        this.emobIdTo = emobIdTo;
    }
}
