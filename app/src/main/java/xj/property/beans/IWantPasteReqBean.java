package xj.property.beans;

/**
 * Created by Administrator on 2016/3/24.
 */
public class IWantPasteReqBean extends  xj.property.netbase.BaseBean {


    /**
     * communityId : 2
     * address : 2号楼2单元202室
     * content : 老把车停单元楼门口，进出都不方面
     * emobId : d463b16dfc014466a1e441dd685ba505
     */

    private int communityId;
    private String address;
    private String content;
    private String emobId;

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    public int getCommunityId() {
        return communityId;
    }

    public String getAddress() {
        return address;
    }

    public String getContent() {
        return content;
    }

    public String getEmobId() {
        return emobId;
    }
}
