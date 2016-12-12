package xj.property.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/10/16.
 */
public class MSPCardBean {

    private int limit;
    private int page;

    public List<MSPCardDetailBean> getData() {
        return data;
    }

    public void setData(List<MSPCardDetailBean> data) {
        this.data = data;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    private List<MSPCardDetailBean> data;


    public static class MSPCardDetailBean implements Serializable {

        /**
         * {
         * "nearbyVipcardId": {会员卡ID},
         * "communityId": {小区ID},
         * "emobId": "{会员卡店家环信ID}",
         * "discount": {折扣价},
         * "photo": "{会员卡图片地址}",
         * "distance": {距离，单位米},
         * "colorR": "{RGB色值 R}",
         * "colorG": "{RGB色值 G}",
         * "colorB": "{RGB色值 B}",
         * "orderCount": {消费次数},
         * "commentTimes": {评星级次数},
         * "star": {星级数量},
         * "shopName": "{店家名称}"
         * }
         * v3 2016/03/14
         */

        private String emobId;
        private int communityId;
        private double discount;
        private String photo;
        private int distance;
        private String shopName;
        private double star;
        private int orderCount;

        ///v3 2016/03/14
        private int commentTimes;
        private int nearbyVipcardId;

        /**
         * 店铺门面图片
         */
        private String shoppic;

        private String colorR;
        private String colorG;
        private String colorB;

        public int getCommentTimes() {
            return commentTimes;
        }

        public void setCommentTimes(int commentTimes) {
            this.commentTimes = commentTimes;
        }

        public int getNearbyVipcardId() {
            return nearbyVipcardId;
        }

        public void setNearbyVipcardId(int nearbyVipcardId) {
            this.nearbyVipcardId = nearbyVipcardId;
        }

        public void setEmobId(String emobId) {
            this.emobId = emobId;
        }

        public void setCommunityId(int communityId) {
            this.communityId = communityId;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public void setStar(double star) {
            this.star = star;
        }

        public void setOrderCount(int orderCount) {
            this.orderCount = orderCount;
        }

        public String getEmobId() {
            return emobId;
        }

        public int getCommunityId() {
            return communityId;
        }

        public double getDiscount() {
            return discount;
        }

        public String getPhoto() {
            return photo;
        }

        public int getDistance() {
            return distance;
        }

        public String getShopName() {
            return shopName;
        }

        public double getStar() {
            return star;
        }

        public int getOrderCount() {
            return orderCount;
        }

        public String getShoppic() {
            return shoppic;
        }

        public void setShoppic(String shoppic) {
            this.shoppic = shoppic;
        }

        public String getColorR() {
            return colorR;
        }

        public void setColorR(String colorR) {
            this.colorR = colorR;
        }

        public String getColorG() {
            return colorG;
        }

        public void setColorG(String colorG) {
            this.colorG = colorG;
        }

        public String getColorB() {
            return colorB;
        }

        public void setColorB(String colorB) {
            this.colorB = colorB;
        }
    }
}
