package xj.property.cache;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Administrator on 2015/4/7.
 */
@Table(name = "order_detail_model")
public class OrderDetailModel extends Model {
    @Column(name="serial")
    public String serial;
    @Column(name="oder_detail_beanlist")
    public String orderDetailBeanList;
    @Column(name="total_price")
    public String total_price;
    @Column(name="total_count")
    public int total_count;
    public boolean isOnline=false;
    @Column(name="oder_detail_servicename")
    public String oder_detail_servicename;
    @Column(name="oder_detail_count")
    public String oder_detail_count;
    @Column(name="oder_detail_price")
    public String oder_detail_price;

    public String getOder_detail_servicename() {
        return oder_detail_servicename;
    }

    public void setOder_detail_servicename(String oder_detail_servicename) {
        this.oder_detail_servicename = oder_detail_servicename;
    }

    public String getOder_detail_count() {
        return oder_detail_count;
    }

    public void setOder_detail_count(String oder_detail_count) {
        this.oder_detail_count = oder_detail_count;
    }

    public String getOder_detail_price() {
        return oder_detail_price;
    }

    public void setOder_detail_price(String oder_detail_price) {
        this.oder_detail_price = oder_detail_price;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public String getOrderDetailBeanList() {
        return orderDetailBeanList;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public void setOrderDetailBeanList(String orderDetailBeanList) {
        this.orderDetailBeanList = orderDetailBeanList;
    }

    public String getSerial() {

        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    @Override
    public String toString() {
        return "OrderDetailModel{" +
                "serial='" + serial + '\'' +
                ", orderDetailBeanList='" + orderDetailBeanList + '\'' +
                ", total_price='" + total_price + '\'' +
                ", total_count=" + total_count +
                ", isOnline=" + isOnline +
                ", oder_detail_servicename='" + oder_detail_servicename + '\'' +
                ", oder_detail_count='" + oder_detail_count + '\'' +
                ", oder_detail_price='" + oder_detail_price + '\'' +
                '}';
    }
}
