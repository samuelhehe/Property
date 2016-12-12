package xj.property.beans;

/**
 * 作者：che on 2015/11/24 15:41
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class MottoRunForBean extends BaseBean {

    private String emobId;
    private String lifeContent;
    private int isCreate;
    private int type;
    private int community_id;
    private Long create_time;

    public Long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }

    public int getCommunity_id() {
        return community_id;
    }

    public void setCommunity_id(int community_id) {
        this.community_id = community_id;
    }

    public String getEmobId() {
        return emobId;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    public String getLifeContent() {
        return lifeContent;
    }

    public void setLifeContent(String lifeContent) {
        this.lifeContent = lifeContent;
    }

    public int getIsCreate() {
        return isCreate;
    }

    public void setIsCreate(int isCreate) {
        this.isCreate = isCreate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
