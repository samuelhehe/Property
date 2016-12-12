package xj.property.beans;

/**
 * 作者：asia on 2015/12/30 18:55
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * v3 2016/03/18
 */
public class ChatSameLabelBean  {

    private String labelContent;
    private String count;
    private String match;

//    "labelContent": "{标签}",
//            "count": {邻居被贴此标签的次数},
//            "match": "{是否和当前用户的标签匹配：yes->匹配，no->不匹配}"



    public String getLabelContent() {
        return labelContent;
    }

    public void setLabelContent(String labelContent) {
        this.labelContent = labelContent;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }
}
