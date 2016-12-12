package xj.property.beans;

import java.util.List;

/**
 * 作者：che on 2016/3/16 12:07
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class OrderRepairHistoryV3Bean {
    private int page;
    private int limit;
    private List<OrderRepairHistoryV3DataBean> data;

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

    public List<OrderRepairHistoryV3DataBean> getData() {
        return data;
    }

    public void setData(List<OrderRepairHistoryV3DataBean> data) {
        this.data = data;
    }

    public class OrderRepairHistoryV3DataBean {
        private String serial;
        private String orderPrice;
        private String emobIdShop;
        private Long endTime;
        private String shopName;
        private String shopLogo;
        private int score;

        public String getSerial() {
            return serial;
        }

        public void setSerial(String serial) {
            this.serial = serial;
        }

        public String getOrderPrice() {
            return orderPrice;
        }

        public void setOrderPrice(String orderPrice) {
            this.orderPrice = orderPrice;
        }

        public String getEmobIdShop() {
            return emobIdShop;
        }

        public void setEmobIdShop(String emobIdShop) {
            this.emobIdShop = emobIdShop;
        }

        public Long getEndTime() {
            return endTime;
        }

        public void setEndTime(Long endTime) {
            this.endTime = endTime;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getShopLogo() {
            return shopLogo;
        }

        public void setShopLogo(String shopLogo) {
            this.shopLogo = shopLogo;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }
    }
}
