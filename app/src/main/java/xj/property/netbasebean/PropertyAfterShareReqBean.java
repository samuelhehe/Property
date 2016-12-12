package xj.property.netbasebean;

import xj.property.netbase.BaseBean;

/**
 * Created by Administrator on 2016/3/22.
 */
public class PropertyAfterShareReqBean extends  BaseBean {
    /**
     * communityId : 2
     * emobId : d463b16dfc014466a1e441dd685ba505
     * photoes : http://7d9lcl.com2.z0.glb.qiniucdn.com/f8ef3480dd414f0b88beaa4042e1474c
     * lifeContent : 我通过邻居帮帮缴了物业费，获取了1000帮帮币。
     */

    private int communityId;
    private String emobId;
    private String photoes;
    private String lifeContent;

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    public void setPhotoes(String photoes) {
        this.photoes = photoes;
    }

    public void setLifeContent(String lifeContent) {
        this.lifeContent = lifeContent;
    }

    public int getCommunityId() {
        return communityId;
    }

    public String getEmobId() {
        return emobId;
    }

    public String getPhotoes() {
        return photoes;
    }

    public String getLifeContent() {
        return lifeContent;
    }

    /*

    {
    "communityId": {小区ID},
    "emobId": "{用户环信ID}",
    "photoes": "{福利分享图片}",
    "lifeContent": "{生活圈内容}"
}
     */





}
