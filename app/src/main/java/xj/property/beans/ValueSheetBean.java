package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/3/20.
 */
public class ValueSheetBean implements  XJ{
    private String status;
    private List<ItemBean>   info;

    public static class ItemBean implements  XJ{
        private  int catId;
        private  String catName;
       private List<ToolsBean>  list;

        public int getCatId() {
            return catId;
        }

        public void setCatId(int catId) {
            this.catId = catId;
        }

        public String getCatName() {
            return catName;
        }

        public void setCatName(String catName) {
            this.catName = catName;
        }

        public List<ToolsBean> getList() {
            return list;
        }

        public void setList(List<ToolsBean> list) {
            this.list = list;
        }
    }
    public static class ToolsBean implements  XJ{
        private  int serviceId;
        private  int catId;
        private  int brandId;
        private  int shopId;
        public  String serviceName;
        private  String serviceImg;
        public  String currentPrice;
        private  long createTime;
        private  String status;
 private String stock;
        public String originPrice;
        public int getServiceId() {
            return serviceId;
        }

        public String getCurrentPrice() {
            return currentPrice;
        }

        public void setCurrentPrice(String currentPrice) {
            this.currentPrice = currentPrice;
        }

        public String getStock() {
            return stock;
        }

        public void setStock(String stock) {
            this.stock = stock;
        }

        public String getOriginPrice() {
            return originPrice;
        }

        public void setOriginPrice(String originPrice) {
            this.originPrice = originPrice;
        }

        public void setServiceId(int serviceId) {
            this.serviceId = serviceId;
        }

        public int getCatId() {
            return catId;
        }

        public void setCatId(int catId) {
            this.catId = catId;
        }

        public int getBrandId() {
            return brandId;
        }

        public void setBrandId(int brandId) {
            this.brandId = brandId;
        }

        public int getShopId() {
            return shopId;
        }

        public void setShopId(int shopId) {
            this.shopId = shopId;
        }

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public String getServiceImg() {
            return serviceImg;
        }

        public void setServiceImg(String serviceImg) {
            this.serviceImg = serviceImg;
        }


        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ItemBean> getInfo() {
        return info;
    }

    public void setInfo(List<ItemBean> info) {
        this.info = info;
    }
}
