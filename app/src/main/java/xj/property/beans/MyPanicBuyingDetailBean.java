package xj.property.beans;

import java.util.List;

/**
 * Created by n on 2015/8/15.
 */
public class MyPanicBuyingDetailBean {

    /**
     * status : yes
     * info : {"total":100,"shop":{"logo":"http://ltzmaxwell.qiniudn.com/FiDBecXPACUwqx7FrYXyyenuGiat","businessEndTime":"20:00","phone":"15612770012","emobId":"a721ee0347eb4844599ac2866551becb","address":"法华南里34号天华大厦","shopName":"我的店","longitude":116.42467,"businessStartTime":"08:00","latitude":39.885387},"createTime":1439462281,"icon":"4","crazySalesId":18,"endTime":1440000000,"emobId":"a721ee0347eb4844599ac2866551becb","title":"物美价廉5","remain":100,"perLimit":2,"crazySalesImg":[{"imgUrl":"http://ltzmaxwell.qiniudn.com/FiDBecXPACUwqx7FrYXyyenuGiat","crazySalesId":18,"crazySalesImgId":9}],"descr":"物美价廉5","viewCount":0}
     */
    private String status;
    private BuyingDetailInfo info;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setInfo(BuyingDetailInfo info) {
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public BuyingDetailInfo getInfo() {
        return info;
    }

    public class BuyingDetailInfo {
        /**
         * total : 100
         * shop : {"logo":"http://ltzmaxwell.qiniudn.com/FiDBecXPACUwqx7FrYXyyenuGiat","businessEndTime":"20:00","phone":"15612770012","emobId":"a721ee0347eb4844599ac2866551becb","address":"法华南里34号天华大厦","shopName":"我的店","longitude":116.42467,"businessStartTime":"08:00","latitude":39.885387}
         * createTime : 1439462281
         * icon : 4
         * crazySalesId : 18
         * endTime : 1440000000
         * emobId : a721ee0347eb4844599ac2866551becb
         * title : 物美价廉5
         * remain : 100
         * perLimit : 2
         * crazySalesImg : [{"imgUrl":"http://ltzmaxwell.qiniudn.com/FiDBecXPACUwqx7FrYXyyenuGiat","crazySalesId":18,"crazySalesImgId":9}]
         * descr : 物美价廉5
         * viewCount : 0
         */
        private int total;
        private ShopInfo shop;
        private int createTime;
        private String icon;
        private int crazySalesId;
        private int endTime;
        private String emobId;
        private String title;
        private int remain;
        private int perLimit;
        private List<CrazySalesImgInfo> crazySalesImg;
        private String descr;
        private int viewCount;
        private int distance;

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public void setShop(ShopInfo shop) {
            this.shop = shop;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public void setCrazySalesId(int crazySalesId) {
            this.crazySalesId = crazySalesId;
        }

        public void setEndTime(int endTime) {
            this.endTime = endTime;
        }

        public void setEmobId(String emobId) {
            this.emobId = emobId;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setRemain(int remain) {
            this.remain = remain;
        }

        public void setPerLimit(int perLimit) {
            this.perLimit = perLimit;
        }

        public void setCrazySalesImg(List<CrazySalesImgInfo> crazySalesImg) {
            this.crazySalesImg = crazySalesImg;
        }

        public void setDescr(String descr) {
            this.descr = descr;
        }

        public void setViewCount(int viewCount) {
            this.viewCount = viewCount;
        }

        public int getTotal() {
            return total;
        }

        public ShopInfo getShop() {
            return shop;
        }

        public int getCreateTime() {
            return createTime;
        }

        public String getIcon() {
            return icon;
        }

        public int getCrazySalesId() {
            return crazySalesId;
        }

        public int getEndTime() {
            return endTime;
        }

        public String getEmobId() {
            return emobId;
        }

        public String getTitle() {
            return title;
        }

        public int getRemain() {
            return remain;
        }

        public int getPerLimit() {
            return perLimit;
        }

        public List<CrazySalesImgInfo> getCrazySalesImg() {
            return crazySalesImg;
        }

        public String getDescr() {
            return descr;
        }

        public int getViewCount() {
            return viewCount;
        }

        public class ShopInfo {
            /**
             * logo : http://ltzmaxwell.qiniudn.com/FiDBecXPACUwqx7FrYXyyenuGiat
             * businessEndTime : 20:00
             * phone : 15612770012
             * emobId : a721ee0347eb4844599ac2866551becb
             * address : 法华南里34号天华大厦
             * shopName : 我的店
             * longitude : 116.42467
             * businessStartTime : 08:00
             * latitude : 39.885387
             */
            private String logo;
            private String businessEndTime;
            private String phone;
            private String emobId;
            private String address;
            private String shopName;
            private double longitude;
            private String businessStartTime;
            private double latitude;

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public void setBusinessEndTime(String businessEndTime) {
                this.businessEndTime = businessEndTime;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public void setEmobId(String emobId) {
                this.emobId = emobId;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public void setShopName(String shopName) {
                this.shopName = shopName;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            public void setBusinessStartTime(String businessStartTime) {
                this.businessStartTime = businessStartTime;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public String getLogo() {
                return logo;
            }

            public String getBusinessEndTime() {
                return businessEndTime;
            }

            public String getPhone() {
                return phone;
            }

            public String getEmobId() {
                return emobId;
            }

            public String getAddress() {
                return address;
            }

            public String getShopName() {
                return shopName;
            }

            public double getLongitude() {
                return longitude;
            }

            public String getBusinessStartTime() {
                return businessStartTime;
            }

            public double getLatitude() {
                return latitude;
            }
        }

        public class CrazySalesImgInfo {
            /**
             * imgUrl : http://ltzmaxwell.qiniudn.com/FiDBecXPACUwqx7FrYXyyenuGiat
             * crazySalesId : 18
             * crazySalesImgId : 9
             */
            private String imgUrl;
            private int crazySalesId;
            private int crazySalesImgId;

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public void setCrazySalesId(int crazySalesId) {
                this.crazySalesId = crazySalesId;
            }

            public void setCrazySalesImgId(int crazySalesImgId) {
                this.crazySalesImgId = crazySalesImgId;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public int getCrazySalesId() {
                return crazySalesId;
            }

            public int getCrazySalesImgId() {
                return crazySalesImgId;
            }
        }
    }
}
