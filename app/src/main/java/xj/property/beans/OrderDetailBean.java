package xj.property.beans;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2015/4/3.
 */
public class OrderDetailBean implements XJ {

        public int serviceId;
        public String serviceName;
        public double price;
        public int count;

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "OrderDetailBean{" +
                "serviceId=" + serviceId +
                ", serviceName='" + serviceName + '\'' +
                ", price=" + price +
                ", count=" + count +
                '}';
    }
}