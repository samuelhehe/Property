package xj.property.beans;

/**
 * Created by che on 2015/7/16.
 * v3 2016/03/04
 */
public class PostShareLifeCircleBean extends xj.property.netbase.BaseBean {
    /**
     * communityId : 2
     * emobId : d463b16dfc014466a1e441dd685ba505
     * lifeContent : 快店真的很快哦
     * photoes : http://7d9lcl.com2.z0.glb.qiniucdn.com/Fi3gqlVD_U5T3DeiEav-aucUsMBT
     * emobIdShop : fcb6adf78bef4ee4940d2af8ee7905f9
     */

    /**
     * {
     * "communityId": {小区ID},
     * "emobId": "{用户环信ID}",
     * "lifeContent": "{生活圈内容}",
     * "photoes": "{生活圈图片地址，多个以英文逗号隔开}",
     * "emobIdShop": "{快店店主环信ID}"
     * }
     */


    private int communityId;
    private String emobId;
    private String lifeContent;
    private String photoes;
    private String emobIdShop;

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    public void setLifeContent(String lifeContent) {
        this.lifeContent = lifeContent;
    }

    public void setPhotoes(String photoes) {
        this.photoes = photoes;
    }

    public void setEmobIdShop(String emobIdShop) {
        this.emobIdShop = emobIdShop;
    }

    public int getCommunityId() {
        return communityId;
    }

    public String getEmobId() {
        return emobId;
    }

    public String getLifeContent() {
        return lifeContent;
    }

    public String getPhotoes() {
        return photoes;
    }

    public String getEmobIdShop() {
        return emobIdShop;
    }


}
