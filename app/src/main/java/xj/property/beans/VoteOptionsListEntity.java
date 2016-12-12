package xj.property.beans;

/**
 * Created by Administrator on 2015/12/7.
 * v3 2016/03/14
 */
public  class VoteOptionsListEntity {

    private String content;

    private int voted;

    private float percent;

    private String percentText;
    //// 票数
    private int count ;

    /*
     "content": "李白",
                "percent": 0,
                "percentText": "0.00",
                "voted": 0,
                "count": 0
     */

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getVoted() {
        return voted;
    }

    public void setVoted(int voted) {
        this.voted = voted;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public String getPercentText() {
        return percentText;
    }

    public void setPercentText(String percentText) {
        this.percentText = percentText;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}