package xj.property.beans;

import java.util.List;

/**
 * Created by n on 2015/8/13.
 */
public class SinglePanicBuyingBean {


    /**
     * status : yes
     * info : {"crazySalesId":15,"emobId":"a8ef6937e3c00b10576d0159e270fedf","total":6446,"perLimit":3,"title":"寂寞","createTime":1440075342,"endTime":1440535907,"descr":"噢情有可原","remain":6446,"icon":"http://7d9lcl.com2.z0.glb.qiniucdn.com/crazysale_class_activity_icon_2.png","distance":0,"viewCount":1,"crazySalesImg":[{"crazySalesImgId":15,"imgUrl":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fipv-peq3-iULha_nxcxDS5ijdkK","crazySalesId":15}],"shop":{"shopName":"副反应","address":"法华南里34号天华大厦","emobId":"a8ef6937e3c00b10576d0159e270fedf","phone":"13810461543","logo":"http://7d9lcl.com2.z0.glb.qiniucdn.com/crazysale_avatar_shop_face.png","businessStartTime":"08:30","businessEndTime":"21:00","longitude":116.43082,"latitude":39.891582,"updateTime":1441967087},"communityInfo":{"longitude":116.4307,"latitude":39.89178},"users":[{"userId":464,"emobId":"30e31c77974df9708de8252148af072d","nickname":"诺熙","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FjBbz5lkKqUIB9ap07gO82S4s73T"},{"userId":453,"emobId":"ce04f45b22793b5a2425962b38c74d08","nickname":"11","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FsQSPCYz9rhuGGIbKXHksOXA9dbV"},{"userId":453,"emobId":"ce04f45b22793b5a2425962b38c74d08","nickname":"11","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FsQSPCYz9rhuGGIbKXHksOXA9dbV"},{"userId":453,"emobId":"ce04f45b22793b5a2425962b38c74d08","nickname":"11","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FsQSPCYz9rhuGGIbKXHksOXA9dbV"}]}
     */

    private String status;
    private InfoEntity info;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setInfo(InfoEntity info) {
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public InfoEntity getInfo() {
        return info;
    }

    public static class InfoEntity {
        /**
         * crazySalesId : 15
         * emobId : a8ef6937e3c00b10576d0159e270fedf
         * total : 6446
         * perLimit : 3
         * title : 寂寞
         * createTime : 1440075342
         * endTime : 1440535907
         * descr : 噢情有可原
         * remain : 6446
         * icon : http://7d9lcl.com2.z0.glb.qiniucdn.com/crazysale_class_activity_icon_2.png
         * distance : 0
         * viewCount : 1
         * crazySalesImg : [{"crazySalesImgId":15,"imgUrl":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fipv-peq3-iULha_nxcxDS5ijdkK","crazySalesId":15}]
         * shop : {"shopName":"副反应","address":"法华南里34号天华大厦","emobId":"a8ef6937e3c00b10576d0159e270fedf","phone":"13810461543","logo":"http://7d9lcl.com2.z0.glb.qiniucdn.com/crazysale_avatar_shop_face.png","businessStartTime":"08:30","businessEndTime":"21:00","longitude":116.43082,"latitude":39.891582,"updateTime":1441967087}
         * communityInfo : {"longitude":116.4307,"latitude":39.89178}
         * users : [{"userId":464,"emobId":"30e31c77974df9708de8252148af072d","nickname":"诺熙","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FjBbz5lkKqUIB9ap07gO82S4s73T"},{"userId":453,"emobId":"ce04f45b22793b5a2425962b38c74d08","nickname":"11","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FsQSPCYz9rhuGGIbKXHksOXA9dbV"},{"userId":453,"emobId":"ce04f45b22793b5a2425962b38c74d08","nickname":"11","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FsQSPCYz9rhuGGIbKXHksOXA9dbV"},{"userId":453,"emobId":"ce04f45b22793b5a2425962b38c74d08","nickname":"11","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FsQSPCYz9rhuGGIbKXHksOXA9dbV"}]
         */

        private int crazySalesId;
        private String emobId;
        private int total;
        private int perLimit;
        private String title;
        private int createTime;
        private int endTime;
        private String descr;
        private int remain;
        private String icon;
        private int distance;
        private int viewCount;
        private ShopEntity shop;
        private CommunityInfoEntity communityInfo;
        private List<CrazySalesImgEntity> crazySalesImg;
        private List<UsersEntity> users;

        public void setCrazySalesId(int crazySalesId) {
            this.crazySalesId = crazySalesId;
        }

        public void setEmobId(String emobId) {
            this.emobId = emobId;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public void setPerLimit(int perLimit) {
            this.perLimit = perLimit;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }

        public void setEndTime(int endTime) {
            this.endTime = endTime;
        }

        public void setDescr(String descr) {
            this.descr = descr;
        }

        public void setRemain(int remain) {
            this.remain = remain;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public void setViewCount(int viewCount) {
            this.viewCount = viewCount;
        }

        public void setShop(ShopEntity shop) {
            this.shop = shop;
        }

        public void setCommunityInfo(CommunityInfoEntity communityInfo) {
            this.communityInfo = communityInfo;
        }

        public void setCrazySalesImg(List<CrazySalesImgEntity> crazySalesImg) {
            this.crazySalesImg = crazySalesImg;
        }

        public void setUsers(List<UsersEntity> users) {
            this.users = users;
        }

        public int getCrazySalesId() {
            return crazySalesId;
        }

        public String getEmobId() {
            return emobId;
        }

        public int getTotal() {
            return total;
        }

        public int getPerLimit() {
            return perLimit;
        }

        public String getTitle() {
            return title;
        }

        public int getCreateTime() {
            return createTime;
        }

        public int getEndTime() {
            return endTime;
        }

        public String getDescr() {
            return descr;
        }

        public int getRemain() {
            return remain;
        }

        public String getIcon() {
            return icon;
        }

        public int getDistance() {
            return distance;
        }

        public int getViewCount() {
            return viewCount;
        }

        public ShopEntity getShop() {
            return shop;
        }

        public CommunityInfoEntity getCommunityInfo() {
            return communityInfo;
        }

        public List<CrazySalesImgEntity> getCrazySalesImg() {
            return crazySalesImg;
        }

        public List<UsersEntity> getUsers() {
            return users;
        }

        public static class ShopEntity {
            /**
             * shopName : 副反应
             * address : 法华南里34号天华大厦
             * emobId : a8ef6937e3c00b10576d0159e270fedf
             * phone : 13810461543
             * logo : http://7d9lcl.com2.z0.glb.qiniucdn.com/crazysale_avatar_shop_face.png
             * businessStartTime : 08:30
             * businessEndTime : 21:00
             * longitude : 116.43082
             * latitude : 39.891582
             * updateTime : 1441967087
             */

            private String shopName;
            private String address;
            private String emobId;
            private String phone;
            private String logo;
            private String businessStartTime;
            private String businessEndTime;
            private double longitude;
            private double latitude;
            private int updateTime;

            public void setShopName(String shopName) {
                this.shopName = shopName;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public void setEmobId(String emobId) {
                this.emobId = emobId;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public void setBusinessStartTime(String businessStartTime) {
                this.businessStartTime = businessStartTime;
            }

            public void setBusinessEndTime(String businessEndTime) {
                this.businessEndTime = businessEndTime;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public void setUpdateTime(int updateTime) {
                this.updateTime = updateTime;
            }

            public String getShopName() {
                return shopName;
            }

            public String getAddress() {
                return address;
            }

            public String getEmobId() {
                return emobId;
            }

            public String getPhone() {
                return phone;
            }

            public String getLogo() {
                return logo;
            }

            public String getBusinessStartTime() {
                return businessStartTime;
            }

            public String getBusinessEndTime() {
                return businessEndTime;
            }

            public double getLongitude() {
                return longitude;
            }

            public double getLatitude() {
                return latitude;
            }

            public int getUpdateTime() {
                return updateTime;
            }
        }

        public static class CommunityInfoEntity {
            /**
             * longitude : 116.4307
             * latitude : 39.89178
             */

            private double longitude;
            private double latitude;

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public double getLongitude() {
                return longitude;
            }

            public double getLatitude() {
                return latitude;
            }
        }

        public static class CrazySalesImgEntity {
            /**
             * crazySalesImgId : 15
             * imgUrl : http://7d9lcl.com2.z0.glb.qiniucdn.com/Fipv-peq3-iULha_nxcxDS5ijdkK
             * crazySalesId : 15
             */

            private int crazySalesImgId;
            private String imgUrl;
            private int crazySalesId;

            public void setCrazySalesImgId(int crazySalesImgId) {
                this.crazySalesImgId = crazySalesImgId;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public void setCrazySalesId(int crazySalesId) {
                this.crazySalesId = crazySalesId;
            }

            public int getCrazySalesImgId() {
                return crazySalesImgId;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public int getCrazySalesId() {
                return crazySalesId;
            }
        }

        public static class UsersEntity {
            /**
             * userId : 464
             * emobId : 30e31c77974df9708de8252148af072d
             * nickname : 诺熙
             * avatar : http://7d9lcl.com2.z0.glb.qiniucdn.com/FjBbz5lkKqUIB9ap07gO82S4s73T
             */

            private int userId;
            private String emobId;
            private String nickname;
            private String avatar;

            public void setUserId(int userId) {
                this.userId = userId;
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

            public String getEmobId() {
                return emobId;
            }

            public String getNickname() {
                return nickname;
            }

            public String getAvatar() {
                return avatar;
            }
        }
    }
}
