package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/10/16.
 */
public class MspHistoryConsumeBean {


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

    public List<MspHConsumeDetailsBean> getData() {
        return data;
    }

    public void setData(List<MspHConsumeDetailsBean> data) {
        this.data = data;
    }

    /**
     * {
     * "page": 1,
     * "limit": 10,
     * "data": [{
     * "createTime": 1448097101,
     * "orderPrice": 0.1,
     * "bonuscoin": 0,
     * "money": 0.09,
     * "star": 4,
     * "discount": 9.5,
     * "discountPrice": 0.09,
     * "shopName": "鑫手勺东北家常菜",
     * "shopLogo": "http://7d9lcl.com2.z0.glb.qiniucdn.com/FjkvvbKxxnkuhiMwDC7n1Iw_5Nne"
     * }]
     * }
     */


    private int page;
    private int limit;

    private List<MspHConsumeDetailsBean> data;

    public static class MspHConsumeDetailsBean {

        /**
         * {
         * "createTime": 1448097101,
         * "orderPrice": 0.1,
         * "bonuscoin": 0,
         * "money": 0.09,
         * "star": 4,
         * "discount": 9.5,
         * "discountPrice": 0.09,
         * "shopName": "鑫手勺东北家常菜",
         * "shopLogo": "http://7d9lcl.com2.z0.glb.qiniucdn.com/FjkvvbKxxnkuhiMwDC7n1Iw_5Nne"
         * }
         *
         * {
         "createTime": {创建时间},
         "orderPrice": {订单价格},
         "bonuscoin": {支付使用帮帮币数量},
         "money": {支付金额，单位元},
         "star": {评价星级},
         "discount": {折扣价},
         "discountPrice": {折扣金额，单位元},
         "shopName": "{店家名称}",
         "shopLogo": "{店家logo}"
         }
         *
         *
         *
         */


        private int createTime;
        private double orderPrice;
        private double money;
        private int bonuscoin;
        private double discount;  /// 折后价格
        private String shopName;
        private String shopLogo;
        private String star; /// 评分

        private double discountPrice;  //// v3 2016/03/14 添加折扣价格  字段


        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }

        public void setOrderPrice(double orderPrice) {
            this.orderPrice = orderPrice;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public void setBonuscoin(int bonuscoin) {
            this.bonuscoin = bonuscoin;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public void setShopLogo(String shopLogo) {
            this.shopLogo = shopLogo;
        }

        public int getCreateTime() {
            return createTime;
        }

        public double getOrderPrice() {
            return orderPrice;
        }

        public double getMoney() {
            return money;
        }

        public int getBonuscoin() {
            return bonuscoin;
        }

        public double getDiscount() {
            return discount;
        }

        public String getShopName() {
            return shopName;
        }

        public String getShopLogo() {
            return shopLogo;
        }

        public String getStar() {
            return star;
        }

        public void setStar(String star) {
            this.star = star;
        }

        public double getDiscountPrice() {
            return discountPrice;
        }

        public void setDiscountPrice(double discountPrice) {
            this.discountPrice = discountPrice;
        }
    }
}
