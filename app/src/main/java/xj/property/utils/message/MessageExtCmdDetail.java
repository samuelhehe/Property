package xj.property.utils.message;

import java.util.List;

import xj.property.hxMessageExtModel.HxOrderDetailModel;

/**
 * Created by maxwell on 15/3/10.
 */
public class MessageExtCmdDetail {
    /**
     * serial of an order
     */
    private String serial;
    /**
     * count of items
     */
    private int totalCount;

    private int sort;
    /**
     * total price
     */
    private float totalPrice;
    /**
     * emobId of shop
     */
    private String emobIdShop;
    /**
     * emobId of user
     */
    private String emobIdUser;

    /**
     * addtional to 203
     */
    private String buyer;
    private String address;
    private int payMethod;
    /**
     * order bean list
     */
    private List<HxOrderDetailModel> orderDetailBeanList;

    /**
     * getter setter
     * @return
     */
    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getEmobIdShop() {
        return emobIdShop;
    }

    public void setEmobIdShop(String emobIdShop) {
        this.emobIdShop = emobIdShop;
    }

    public String getEmobIdUser() {
        return emobIdUser;
    }

    public void setEmobIdUser(String emobIdUser) {
        this.emobIdUser = emobIdUser;
    }


    public List<HxOrderDetailModel> getOrderDetailBeanList() {
        return orderDetailBeanList;
    }

    public void setOrderDetailBeanList(List<HxOrderDetailModel> orderDetailBeanList) {
        this.orderDetailBeanList = orderDetailBeanList;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(int payMethod) {
        this.payMethod = payMethod;
    }

    /**
     * builder
     */
    public static class Builder {
        /**
         * serial of an order
         */
        private String serial;
        /**
         * count of items
         */
        private int totalCount;

        private int sort;
        /**
         * total price
         */
        private float totalPrice;
        /**
         * emobId of shop
         */
        private String emobIdShop;
        /**
         * emobId of user
         */
        private String emobIdUser;

        /**
         * addtional to 203
         */
        private String buyer;
        private String address;
        private int payMethod;
        /**
         * order bean list
         */
        private List<HxOrderDetailModel> orderDetailBeanList;

        /**
         * building
         *
         * @param serial order serial
         */
        public Builder(String serial) {
            this.serial = serial;
        }

        /**
         * constructor of builder
         * @param totalPrice total price of this order
         * @return a builder reference
         */
        public Builder totalPrice(int totalPrice) {
            totalPrice = totalPrice;
            return this;
        }

        /**
         * order detail bean list
         * @param orderDetailBeanList order detail bean list
         * @return
         */
        public Builder orderDetailBeanList(int orderDetailBeanList) {
            orderDetailBeanList = orderDetailBeanList;
            return this;
        }

        public MessageExtCmdDetail build() {
            return new MessageExtCmdDetail(this);
        }
    }

    private MessageExtCmdDetail(Builder builder) {
        this.setSerial(builder.serial);
        this.setTotalPrice(builder.totalPrice);
        this.setOrderDetailBeanList(builder.orderDetailBeanList);
    }
}
