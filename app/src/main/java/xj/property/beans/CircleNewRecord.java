package xj.property.beans;

/**
 * Created by Administrator on 2015/5/4.
 */
public class CircleNewRecord extends xj.property.netbase.BaseBean {
    /**
     * communityId : 2
     * emobId : d463b16dfc014466a1e441dd685ba505
     * lifeContent : 我的第一条生活圈
     * photoes : http://7d9lcl.com2.z0.glb.qiniucdn.com/FpgAe-5Aan5MCL4g0BonwAnYjos9,http://7d9lcl.com2.z0.glb.qiniucdn.com/FpgAe-5Aan5MCL4g0BonwAnYjos9
     * createGroup : 1
     *
     * //    {
     //        "communityId": {小区ID},
     //        "emobId": "{用户环信ID}",
     //            "lifeContent": "{生活圈内容}",
     //            "photoes": "{生活圈图片地址，多个以英文逗号隔开}",
     //            "createGroup": {是否创建群组：1->创建,0->不创建}
     //    }
     *
     */


    /**
     *
     * {
     "type": {生活圈类型：2->快店分享，19->福利分享，20->会员卡分享，23->帮主竞选拉票}, /// 发生活圈不传类型。
     "communityId": {小区ID},
     "emobId": "{用户环信ID}",
     "lifeContent": "{生活圈内容}",
     "photoes": "{生活圈图片地址，多个以英文逗号隔开}",
     "createGroup": {是否创建群组：1->创建,0->不创建}
     }
     */

    private int type;

    private int communityId;
    private String emobId;
    private String lifeContent;
    private String photoes;
    private int createGroup;

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

    public void setCreateGroup(int createGroup) {
        this.createGroup = createGroup;
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

    public int getCreateGroup() {
        return createGroup;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
