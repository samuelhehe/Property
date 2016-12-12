package xj.property.beans;

/**
 * Created by Administrator on 2015/4/10.
 */
public class AddCommentsRequest extends BaseBean {
    public String emobIdFrom;
    public int score;
    public String content;

    public String getEmobIdFrom() {
        return emobIdFrom;
    }

    public void setEmobIdFrom(String emobIdFrom) {
        this.emobIdFrom = emobIdFrom;
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
}
