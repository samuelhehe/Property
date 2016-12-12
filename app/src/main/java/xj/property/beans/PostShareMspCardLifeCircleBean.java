package xj.property.beans;

/**
 * Created by che on 2015/7/16.
 */
public class PostShareMspCardLifeCircleBean extends xj.property.netbase.BaseBean{
    /**
     * communityId : 2
     * lifeContent : 会员卡折扣来了!
     * photoes : http://7d9lcl.com2.z0.glb.qiniucdn.com/Fk7se1T0TW8OYkQ3iIKp3B-ZsQ5p,http://7d9lcl.com2.z0.glb.qiniucdn.com/FjkvvbKxxnkuhiMwDC7n1Iw_5Nne
     * emobId : d463b16dfc014466a1e441dd685ba505
     * discountPrice : 0.09
     */

    /**
     * {
     "communityId": {小区ID},
     "lifeContent": "{生活圈内容}",
     "photoes": "{生活圈图片}",
     "emobId": "{用户环信ID}",
     "discountPrice": {会员卡折扣金额}
     }
     */

    private int communityId;
    private String lifeContent;
    private String photoes;
    private String emobId;
    private double discountPrice;

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public void setLifeContent(String lifeContent) {
        this.lifeContent = lifeContent;
    }

    public void setPhotoes(String photoes) {
        this.photoes = photoes;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public int getCommunityId() {
        return communityId;
    }

    public String getLifeContent() {
        return lifeContent;
    }

    public String getPhotoes() {
        return photoes;
    }

    public String getEmobId() {
        return emobId;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }






}
