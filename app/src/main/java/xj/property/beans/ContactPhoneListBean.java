package xj.property.beans;

import java.util.ArrayList;

/**
 * Created by chenxiangyu on 2015/3/30.
 */
public class ContactPhoneListBean {
    private String status; //快店状态
    private ArrayList<ContactPhoneBean> categories;
    public int createTime;
    public int updateTime;
    private int shopId;//快店ID
    private String shopName;//快店名称
    private int deliverLimit;//起送金额
    private String deliverTime;//送达时间
    private String logo; /// 快店logo
    private String businessStartTime;//开门时间
    private String businessEndTime;//关门时间
    private String shopDesc;/// 快店介绍
    private String emobId; /// 店主环信ID
    private String address; /// 快店地址
    private String phone; /// 快店电话
    /*
        "shopId": {快店ID},
        "shopName": "{快店名称}",
        "shopDesc": "{快店介绍}",
        "emobId": "{店主环信ID}",
        "address": "{快店地址}",
        "phone": "{快店电话}",
        "logo": "{快店logo}",
        "status": "{快店状态}",
        "createTime": {创建时间},
        "updateTime": {修改时间},
        "businessStartTime": "{开门时间}",
        "businessEndTime": "{关门时间}",
        "deliverLimit": {起送金额},
        "deliverTime": "{送达时间}",
        "categories": {
            "catId": {商品分类ID},
            "catName": "{商品分类名称}",
            "catDesc": "{商品分类描述}"
        }]
     */

    public ArrayList<ContactPhoneBean> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<ContactPhoneBean> categories) {
        this.categories = categories;
    }

    public String getBusinessStartTime() {
        return businessStartTime;
    }

    public void setBusinessStartTime(String businessStartTime) {
        this.businessStartTime = businessStartTime;
    }

    public String getBusinessEndTime() {
        return businessEndTime;
    }

    public void setBusinessEndTime(String businessEndTime) {
        this.businessEndTime = businessEndTime;
    }


    public int getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(int updateTime) {
        this.updateTime = updateTime;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getShopDesc() {
        return shopDesc;
    }

    public void setShopDesc(String shopDesc) {
        this.shopDesc = shopDesc;
    }

    public String getEmobId() {
        return emobId;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public void setDeliverLimit(int deliverLimit) {
        this.deliverLimit = deliverLimit;
    }

    public void setDeliverTime(String deliverTime) {
        this.deliverTime = deliverTime;
    }

    public String getShopName() {
        return shopName;
    }

    public int getShopId() {
        return shopId;
    }

    public int getDeliverLimit() {
        return deliverLimit;
    }

    public String getDeliverTime() {
        return deliverTime;
    }
}
