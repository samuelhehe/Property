package xj.property.beans;

/**
 * Created by n on 2015/4/29.
 */
public class ShopContactBean implements XJ {
    private String status;
    private Info info;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public static class Info implements XJ {


        private String  address;
        private String  authCode;
        private String  businessEndTime;
        private String  businessStartTime;
        private String  communityId;
        private String  createTime;
        private String  emobId;
        private String  orderSum;
        private String  phone;
        private int  score;
        private String  shopId;
        private String  shopName;
        private String  shopsDesc;
        private String  sort;
        private String  status;
        private String logo;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAuthCode() {
            return authCode;
        }

        public void setAuthCode(String authCode) {
            this.authCode = authCode;
        }

        public String getBusinessEndTime() {
            return businessEndTime;
        }

        public void setBusinessEndTime(String businessEndTime) {
            this.businessEndTime = businessEndTime;
        }

        public String getBusinessStartTime() {
            return businessStartTime;
        }

        public void setBusinessStartTime(String businessStartTime) {
            this.businessStartTime = businessStartTime;
        }

        public String getCommunityId() {
            return communityId;
        }

        public void setCommunityId(String communityId) {
            this.communityId = communityId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getEmobId() {
            return emobId;
        }

        public void setEmobId(String emobId) {
            this.emobId = emobId;
        }

        public String getOrderSum() {
            return orderSum;
        }

        public void setOrderSum(String orderSum) {
            this.orderSum = orderSum;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getShopsDesc() {
            return shopsDesc;
        }

        public void setShopsDesc(String shopsDesc) {
            this.shopsDesc = shopsDesc;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        @Override
        public String toString() {
            return "Info{" +
                    "address='" + address + '\'' +
                    ", authCode='" + authCode + '\'' +
                    ", businessEndTime='" + businessEndTime + '\'' +
                    ", businessStartTime='" + businessStartTime + '\'' +
                    ", communityId='" + communityId + '\'' +
                    ", createTime='" + createTime + '\'' +
                    ", emobId='" + emobId + '\'' +
                    ", orderSum='" + orderSum + '\'' +
                    ", phone='" + phone + '\'' +
                    ", score=" + score +
                    ", shopId='" + shopId + '\'' +
                    ", shopName='" + shopName + '\'' +
                    ", shopsDesc='" + shopsDesc + '\'' +
                    ", sort='" + sort + '\'' +
                    ", status='" + status + '\'' +
                    ", logo='" + logo + '\'' +
                    '}';
        }
    }
}
