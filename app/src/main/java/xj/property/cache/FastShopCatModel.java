package xj.property.cache;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import xj.property.beans.XJ;

/**
 * Created by chenxiangyu on 2015/4/2.
 */
@Table(name = "fastshopcar")
public class FastShopCatModel extends Model implements XJ {


    @Column(name = "brandId")
    public String brandId;
    @Column(name = "catId")
    public String catId;
    @Column(name = "shopId")
    public int shopId;
    @Column(name = "shopName")
    public String shopName;
    @Column(name = "serviceId")
    public int serviceId;

    @Column(name = "shopItemSkuId")
    public int shopItemSkuId; //// 规格多状态下的serviceId

    @Column(name = "serviceName")
    public String serviceName;
    @Column(name = "price")
    public double price;
    @Column(name = "serviceImg")
    public String serviceImg;
    @Column(name = "status")
    public String status;
    @Column(name = "createTime")
    public long createTime;

    @Column(name = "shopEmobId")
    public String shopEmobId;
    @Column(name = "emobId")
    public String emobId;
    @Column(name = "buy_num")
    public int buy_num;

    @Column(name = "count")
    public int count;
    //1订单正在进行 2订单提交  3订单完成 4 商品取消
    @Column(name = "state")
    public String state;
    @Column(name = "userId")
    public int userId;

    public int getBuy_num() {
        return buy_num;
    }

    public void setBuy_num(int buy_num) {
        this.buy_num = buy_num;
    }
//    public boolean isChecked() {
//        return checked;
//    }
//    public void setChecked(boolean checked) {
//        this.checked = checked;
//    }
//    private boolean checked;

    public FastShopCatModel() {
        super();
    }

    public FastShopCatModel(String emobId, String state, int count) {
        super();
        this.emobId = emobId;
        this.state = state;
        this.count = count;
    }



    public int getShopItemSkuId() {
        return shopItemSkuId;
    }

    public void setShopItemSkuId(int shopItemSkuId) {
        this.shopItemSkuId = shopItemSkuId;
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

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getServiceImg() {
        return serviceImg;
    }

    public void setServiceImg(String serviceImg) {
        this.serviceImg = serviceImg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getShopEmobId() {
        return shopEmobId;
    }

    public void setShopEmobId(String shopEmobId) {
        this.shopEmobId = shopEmobId;
    }

    public String getEmobId() {
        return emobId;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "FastShopCatModel{" +
                "brandId='" + brandId + '\'' +
                ", catId='" + catId + '\'' +
                ", shopId=" + shopId +
                ", shopName='" + shopName + '\'' +
                ", serviceId=" + serviceId +
                ", serviceName='" + serviceName + '\'' +
                ", price=" + price +
                ", serviceImg='" + serviceImg + '\'' +
                ", status='" + status + '\'' +
                ", createTime=" + createTime +
                ", shopEmobId='" + shopEmobId + '\'' +
                ", emobId='" + emobId + '\'' +
                ", buy_num=" + buy_num +
                ", count=" + count +
                ", state='" + state + '\'' +
                ", userId=" + userId +
                '}';
    }
}
