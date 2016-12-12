package xj.property.beans;

/**
 * Created by Administrator on 2015/4/9.
 */
public class HistoryBean {
    public String serial;
    public String shopName;
    public String logo;
    public int serviceTime;
    public String score;
    public String orderPrice;
    public int endTime;
    public String emobIdShop;

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public String getEmobIdShop() {
        return emobIdShop;
    }

    public void setEmobIdShop(String emobIdShop) {
        this.emobIdShop = emobIdShop;
    }

    @Override
    public String toString() {
        return "HistoryBean{" +
                "serial='" + serial + '\'' +
                ", shopName='" + shopName + '\'' +
                ", logo='" + logo + '\'' +
                ", serviceTime=" + serviceTime +
                ", score='" + score + '\'' +
                ", orderPrice='" + orderPrice + '\'' +
                ", endTime=" + endTime +
                ", emobIdShop='" + emobIdShop + '\'' +
                '}';
    }
}
