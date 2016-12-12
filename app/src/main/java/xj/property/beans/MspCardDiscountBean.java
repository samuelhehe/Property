package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/10/16.
 */
public class MspCardDiscountBean {

    private int nearbyVipcardId;
    private int communityId;
    private double discount;
    private String emobId;
    private String shopName;
    private String shopLogo;
    private String businessStartTime;
    private String businessEndTime;
    private String phone;
    private int distance;
    private String colorR;
    private String colorG;
    private String colorB;
    private double longitude;
    private double latitude;
    private double communityLongitude;
    private double communityLatitude;
    private double star;
    private int orderCount;
    private String address;

    /**
     *
     * {
     "nearbyVipcardId": {会员卡ID},
     "communityId": {小区ID},
     "discount": {会员卡折扣价},
     "photo": "{会员卡图片地址}",
     "emobId": "{店家环信ID}",
     "shopName": "{店家名称}",
     "shopLogo": "{店家logo}",
     "businessStartTime": "{店家开始营业时间}",
     "businessEndTime": "{店家关门时间}",
     "phone": "{店家联系电话}",
     "distance": {店家距离},
     "colorR": "{RGB色值 R}",
     "colorG": "{RGB色值 G}",
     "colorB": "{RGB色值 B}",
     "longitude": {店家经度},
     "latitude": {店家纬度},
     "address": "{店家地址}",
     "communityLongitude": {小区经度},
     "communityLatitude": {小区纬度},
     "star": {店家星级},
     "orderCount": {店家订单数量},

     "users": [{
     "userId": {用户ID},
     "username": "{用户账号}",
     "emobId": "{用户环信ID}",
     "nickname": "{用户昵称}",
     "avatar": "{用户头像}",
     "grade": "{用户帮主角色}",
     "identity": "{用户牛人角色}"
     }]

     }
     */







    public int getNearbyVipcardId() {
        return nearbyVipcardId;
    }

    public void setNearbyVipcardId(int nearbyVipcardId) {
        this.nearbyVipcardId = nearbyVipcardId;
    }

    private List<UsersEntity> users;

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }

    public void setBusinessStartTime(String businessStartTime) {
        this.businessStartTime = businessStartTime;
    }

    public void setBusinessEndTime(String businessEndTime) {
        this.businessEndTime = businessEndTime;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setColorR(String colorR) {
        this.colorR = colorR;
    }

    public void setColorG(String colorG) {
        this.colorG = colorG;
    }

    public void setColorB(String colorB) {
        this.colorB = colorB;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setCommunityLongitude(double communityLongitude) {
        this.communityLongitude = communityLongitude;
    }

    public void setCommunityLatitude(double communityLatitude) {
        this.communityLatitude = communityLatitude;
    }

    public void setStar(double star) {
        this.star = star;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setUsers(List<UsersEntity> users) {
        this.users = users;
    }

    public int getCommunityId() {
        return communityId;
    }

    public double getDiscount() {
        return discount;
    }

    public String getEmobId() {
        return emobId;
    }

    public String getShopName() {
        return shopName;
    }

    public String getShopLogo() {
        return shopLogo;
    }

    public String getBusinessStartTime() {
        return businessStartTime;
    }

    public String getBusinessEndTime() {
        return businessEndTime;
    }

    public String getPhone() {
        return phone;
    }

    public int getDistance() {
        return distance;
    }

    public String getColorR() {
        return colorR;
    }

    public String getColorG() {
        return colorG;
    }

    public String getColorB() {
        return colorB;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getCommunityLongitude() {
        return communityLongitude;
    }

    public double getCommunityLatitude() {
        return communityLatitude;
    }

    public double getStar() {
        return star;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public String getAddress() {
        return address;
    }

    public List<UsersEntity> getUsers() {
        return users;
    }

    public static class UsersEntity {
        /**
         *
         * {
         "userId": {用户ID},
         "username": "{用户账号}",
         "emobId": "{用户环信ID}",
         "nickname": "{用户昵称}",
         "avatar": "{用户头像}",
         "grade": "{用户帮主角色}",
         "identity": "{用户牛人角色}"
         }
         */


        private int userId;
        private String username;
        private String emobId;
        private String nickname;
        private String avatar;
        /**
         * grade : normal
         * identity : famous
         */

        private String grade;
        private String identity;

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setEmobId(String emobId) {
            this.emobId = emobId;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getUserId() {
            return userId;
        }

        public String getUsername() {
            return username;
        }

        public String getEmobId() {
            return emobId;
        }

        public String getNickname() {
            return nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }

        public String getGrade() {
            return grade;
        }

        public String getIdentity() {
            return identity;
        }
    }
}
