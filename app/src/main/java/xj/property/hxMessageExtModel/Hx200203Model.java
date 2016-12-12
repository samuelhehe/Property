package xj.property.hxMessageExtModel;

import java.util.List;

/**
 * Created by maxwell on 15/2/1.
 */
public class Hx200203Model {

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
}
