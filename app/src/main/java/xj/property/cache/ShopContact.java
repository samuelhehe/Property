package xj.property.cache;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import xj.property.beans.XJ;

/**
 * Created by Administrator on 2015/3/27.
 */
@Table(name = "contact")
public class ShopContact extends Model implements XJ{
    @Column(name = "shop_id")
    public int shopId;
    @Column(name = "shop_name")
    public String shopName;
    @Column(name = "shop_desc")
    public String shopsDesc;
    @Column(name = "address")
    public String address;
    @Column(name = "communityid")
    public String communityId;
    @Column(name = "emobid")
    public String emobId;
    @Column(name = "phone")
    public String phone;
    @Column(name = "logo")
    public String logo;
    @Column(name = "status")
    public String status;
    @Column(name = "sort")
    public String sort;
    @Column(name = "createtime")
    public String createTime;
    @Column(name = "authcode")
    public String authCode;
    @Column(name = "ordersum")
    public String orderSum;
    @Column(name = "score")
    public String score;
    public String businessStartTime;
    public String businessEndTime;
    public int getShopId() {
        return shopId;
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

    public void setShopId(int shopId) {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getEmobId() {
        return emobId;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getOrderSum() {
        return orderSum;
    }

    public void setOrderSum(String orderSum) {
        this.orderSum = orderSum;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "shopId=" + shopId +
                ", shopName='" + shopName + '\'' +
                ", shopsDesc='" + shopsDesc + '\'' +
                ", address='" + address + '\'' +
                ", communityId='" + communityId + '\'' +
                ", emobId='" + emobId + '\'' +
                ", phone='" + phone + '\'' +
                ", logo='" + logo + '\'' +
                ", status='" + status + '\'' +
                ", sort='" + sort + '\'' +
                ", createTime='" + createTime + '\'' +
                ", authCode='" + authCode + '\'' +
                ", orderSum='" + orderSum + '\'' +
                ", score='" + score + '\'' +
                ", businessStartTime='" + businessStartTime + '\'' +
                ", businessEndTime='" + businessEndTime + '\'' +
                '}';
    }

}
