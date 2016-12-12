package xj.property.beans;

/**
 * Created by Administrator on 2015/11/3.
 */
public class IWantProviderReqBean extends  xj.property.netbase.BaseBean {


    /**
     * cooperationId : 28
     * title : 牙科医生
     * content : 专注牙医30年，祖传技术值得信赖！
     * emobId : 98b0ce078094640917cdb2f59f5ff572
     * communityId : 1
     */

    private Integer cooperationId;
    private String title;
    private String content;
    private String emobId;
    private Integer communityId;


    /**
     * {
     "cooperationId": {邻居帮ID},
     "title": "{邻居帮标题}",
     "content": "{邻居帮内容}",
     "communityId": {小区ID},
     "emobId": "{用户环信ID}"
     }
     *
     */
    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }


    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getEmobId() {
        return emobId;
    }


    public Integer getCooperationId() {
        return cooperationId;
    }

    public void setCooperationId(Integer cooperationId) {
        this.cooperationId = cooperationId;
    }

    public Integer getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }
}
