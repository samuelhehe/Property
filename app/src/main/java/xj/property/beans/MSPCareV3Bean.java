package xj.property.beans;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：asia on 2016/3/9 19:58
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class MSPCareV3Bean {
    private int page;
    private int limit;
    private List<MSPCareV3DataBean> data;

    public List<MSPCareV3DataBean> getData() {
        return data;
    }

    public void setData(List<MSPCareV3DataBean> data) {
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

    public class MSPCareV3DataBean implements Serializable {
        private int shopVipcardId;
        private int communityId;
        private String emobId;
        private float discount;
        private String photo;
        private int distance;
        private String colorR;
        private String colorG;
        private String colorB;
        private int orderCount;
        private int commentTimes;
        private int starSham;
        private String shopName;

        public int getShopVipcardId() {
            return shopVipcardId;
        }

        public void setShopVipcardId(int shopVipcardId) {
            this.shopVipcardId = shopVipcardId;
        }

        public int getCommunityId() {
            return communityId;
        }

        public void setCommunityId(int communityId) {
            this.communityId = communityId;
        }

        public String getEmobId() {
            return emobId;
        }

        public void setEmobId(String emobId) {
            this.emobId = emobId;
        }

        public float getDiscount() {
            return discount;
        }

        public void setDiscount(float discount) {
            this.discount = discount;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
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

        public int getOrderCount() {
            return orderCount;
        }

        public void setOrderCount(int orderCount) {
            this.orderCount = orderCount;
        }

        public int getCommentTimes() {
            return commentTimes;
        }

        public void setCommentTimes(int commentTimes) {
            this.commentTimes = commentTimes;
        }

        public int getStarSham() {
            return starSham;
        }

        public void setStarSham(int starSham) {
            this.starSham = starSham;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }
    }
}
