package xj.property.beans;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2015/4/3.
 */
public class OrderDetailBeanList implements XJ {

    public int serviceId;
    public String serviceName;
    public String price;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public OrderDetailBeanList() {
    }

    public OrderDetailBeanList(int serviceId, String serviceName, double price, int count) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.price = price + "";
        this.count = count;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}