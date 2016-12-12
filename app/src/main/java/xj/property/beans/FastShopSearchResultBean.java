package xj.property.beans;

import java.util.ArrayList;

/**
 * Created by n on 2015/4/10.
 */
public class FastShopSearchResultBean implements XJ {

    private String status;
    private ArrayList<Info> info;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Info> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<Info> info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "FastShopSearchResultBean{" +
                "status='" + status + '\'' +
                ", info=" + info +
                '}';
    }

    public static class Info implements XJ {

        private String brandId;
        private String catId;
        private long createTime;

        private int serviceId;
        private String serviceImg;
        public String serviceName;
        private int shopId;
        public String shopName;
        public String status;

        private String currentPrice;
        private String originPrice;
        private String score;
        private String stock;
     private  String  shopEmobId;

        public String getShopEmobId() {
            return shopEmobId;
        }

        public void setShopEmobId(String shopEmobId) {
            this.shopEmobId = shopEmobId;
        }

        public String getBrandId() {
            return brandId;
        }

        public void setBrandId(String brandId) {
            this.brandId = brandId;
        }

        public String getCatId() {
            return catId;
        }

        public void setCatId(String catId) {
            this.catId = catId;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getServiceId() {
            return serviceId;
        }

        public void setServiceId(int serviceId) {
            this.serviceId = serviceId;
        }

        public String getServiceImg() {
            return serviceImg;
        }

        public void setServiceImg(String serviceImg) {
            this.serviceImg = serviceImg;
        }

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public int getShopId() {
            return shopId;
        }

        public void setShopId(int shopId) {
            this.shopId = shopId;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCurrentPrice() {
            return currentPrice;
        }

        public void setCurrentPrice(String currentPrice) {
            this.currentPrice = currentPrice;
        }

        public String getOriginPrice() {
            return originPrice;
        }

        public void setOriginPrice(String originPrice) {
            this.originPrice = originPrice;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getStock() {
            return stock;
        }

        public void setStock(String stock) {
            this.stock = stock;
        }

        @Override
        public String toString() {
            return "Info{" +
                    "brandId='" + brandId + '\'' +
                    ", catId='" + catId + '\'' +
                    ", createTime=" + createTime +
                    ", serviceId=" + serviceId +
                    ", serviceImg='" + serviceImg + '\'' +
                    ", serviceName='" + serviceName + '\'' +
                    ", shopId=" + shopId +
                    ", shopName='" + shopName + '\'' +
                    ", status='" + status + '\'' +
                    ", currentPrice='" + currentPrice + '\'' +
                    ", originPrice='" + originPrice + '\'' +
                    ", score='" + score + '\'' +
                    ", stock='" + stock + '\'' +
                    '}';
        }
    }
}
