package xj.property.beans;

/**
 * Created by che on 2015/6/11.
 * <p/>
 * v3 2016/03/15
 */
public class LifeCircleCountBean {


    //        "lifeCircle": {生活圈角标},
//                "shop": {快店角标},
//                "vote": {投票角标},
//                "cooperation": {邻居帮角标},
//                "welfare": {福利角标},
//                "activity": {活动/话题角标},
//                "nearbyVipcard": {会员卡角标}

    public int lifeCircle;

    public int vote;

    public int shop;

    public int door;

    public int welfare;

    public int nearbyVipcard;

    public int cooperation;

    public int activity;

    public int getDoor() {
        return door;
    }

    public void setDoor(int door) {
        this.door = door;
    }

    public int getLifeCircle() {
        return lifeCircle;
    }

    public void setLifeCircle(int lifeCircle) {
        this.lifeCircle = lifeCircle;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public int getShop() {
        return shop;
    }

    public void setShop(int shop) {
        this.shop = shop;
    }

    public int getWelfare() {
        return welfare;
    }

    public void setWelfare(int welfare) {
        this.welfare = welfare;
    }

    public int getNearbyVipcard() {
        return nearbyVipcard;
    }

    public void setNearbyVipcard(int nearbyVipcard) {
        this.nearbyVipcard = nearbyVipcard;
    }

    public int getCooperation() {
        return cooperation;
    }

    public void setCooperation(int cooperation) {
        this.cooperation = cooperation;
    }

    public int getActivity() {
        return activity;
    }

    public void setActivity(int activity) {
        this.activity = activity;
    }


}
