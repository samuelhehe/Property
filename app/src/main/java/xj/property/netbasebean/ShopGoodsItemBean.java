package xj.property.netbasebean;

/**
 * Created by Administrator on 2016/3/17.
 *
 * v3 2016/03/17 下订单，商品记录
 */
public class ShopGoodsItemBean {

    /**
     * shopItemSkuId : 4
     * serviceId : 56
     * serviceName : 雪花啤酒 听 500ml
     * price : 5
     * count : 4
     */

    private Integer shopItemSkuId;
    private Integer serviceId;
    private String serviceName;
    private String price;
    private Integer count;



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

    public Integer getShopItemSkuId() {
        return shopItemSkuId;
    }

    public void setShopItemSkuId(Integer shopItemSkuId) {
        this.shopItemSkuId = shopItemSkuId;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
