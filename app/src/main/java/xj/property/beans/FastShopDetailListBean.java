package xj.property.beans;

import java.util.List;

/**
 * Created by chenxiangyu on 2015/4/1.
 * <p/>
 * v3 2016/03/16
 * 商品列表的详细信息bean
 */
public class FastShopDetailListBean implements XJ {

    /**
     * {
     * "page": {页码},
     * "limit": {页面大小},
     * "data": [{
     * "serviceId": {商品ID},
     * "serviceName": "{商品名称}",
     * "serviceImg": "{商品图片}",
     * "originPrice": "{原价}",
     * "currentPrice": "{现价}",
     * "shopId": {快店ID},
     * "catId": {分类ID},
     * "level": {序号},
     * "multiattribute": "{是否有多种规格}",
     * "createTime": {创建时间},
     * "updateTime": {修改时间},
     * "status": "{状态}",
     * "children": [{
     * "shopItemSkuId": {规格ID},
     * "serviceId": {商品ID},
     * "serviceName": "{商品名称}",
     * "serviceImg": "{商品图片}",
     * "originPrice": "{原价}",
     * "currentPrice": "{现价}",
     * "shopId": {快店ID},
     * "catId": {分类ID},
     * "level": {序号},
     * "createTime": {创建时间},
     * "updateTime": {修改时间},
     * "status": "{状态}"
     * }]
     * }]
     * <p/>
     * <p/>
     * <p/>
     * {
     * "page":1,"limit":10,
     * "data":
     * [
     * {"serviceId":1,
     * "serviceName":"雪花啤酒 听 330ml",
     * "serviceImg":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FpPmYMJmHqcpw1w-ZqS8x43PET8r",
     * "originPrice":"4",
     * "currentPrice":"2.8",
     * "catId":1,
     * "multiattribute":"yes",
     * "children":[
     * {
     * "shopItemSkuId":1,
     * "serviceId":1,
     * "serviceName":"雪花啤酒 听 330ml",
     * "currentPrice":"2.8"
     * },
     * <p/>
     * {"shopItemSkuId":2,"serviceId":1,"serviceName":"雪花啤酒 听 500ml","currentPrice":"5"}]},
     * <p/>
     * {"serviceId":2,"serviceName":"大豆油4L装","serviceImg":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FlKiXA3XvxUY_0u5n3CZXe3uq1HE",
     * "originPrice":"60","currentPrice":"36","catId":1,"multiattribute":"no"},
     * <p/>
     * {"serviceId":3,"serviceName":"龙江家园450ml42度","serviceImg":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FrE3uDMLLLj_p6IORLNYMq0ysBnf",
     * "originPrice":"18","currentPrice":"13","catId":1,"multiattribute":"no"},
     * <p/>
     * {"serviceId":4,"serviceName":"海之蓝 棉柔性白酒500ml 42c度","serviceImg":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fp3Gqv2KfHZEDpE_N5aWB8n3Sr9m",
     * "originPrice":"160","currentPrice":"130","catId":1,"multiattribute":"no"},
     * <p/>
     * {"serviceId":5,"serviceName":"古船面粉","serviceImg":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FjbzqhPVytZ5HtaEjOtnwpF_QX0P",
     * "originPrice":"80","currentPrice":"60","catId":1,"multiattribute":"no"},
     * <p/>
     * {"serviceId":6,"serviceName":"杯子100ml","serviceImg":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FpULXWMjhmKc0HF68biwaIhNeF78",
     * "originPrice":"7","currentPrice":"4.9","catId":1,"multiattribute":"no"},
     * <p/>
     * {"serviceId":7,"serviceName":"好丽友 呀！土豆","serviceImg":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Ft8SXlTQn1u8845mNWW0y6dBqLhw",
     * "originPrice":"3","currentPrice":"2","catId":1,"multiattribute":"no"},{"serviceId":8,"serviceName":"咖啡250ml",
     * "serviceImg":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FlEV8MI75gXsMnctd30J_qZ3Gdqt","originPrice":"8","currentPrice":"5.8","catId":1,"multiattribute":"no"},
     * <p/>
     * {"serviceId":9,"serviceName":"劲仔小鱼","serviceImg":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FiyE5r7_cxdrWrZLetIOz80w9YYc",
     * "originPrice":"4","currentPrice":"1","catId":1,"multiattribute":"no"},
     * <p/>
     * {"serviceId":10,"serviceName":"雀巢咖啡268ml","serviceImg":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FlEV8MI75gXsMnctd30J_qZ3Gdqt",
     * "originPrice":"5","currentPrice":"3","catId":1,"multiattribute":"no"}]
     * <p/>
     * }
     */

    private int page;
    private int limit;

    private List<PagerItemBean> data;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<PagerItemBean> getPageData() {
        return data;
    }

    public void setPageData(List<PagerItemBean> data) {
        this.data = data;
    }

    public static class PagerItemBean implements XJ {

        private String brandId; //// 是否是特卖商品  已经去掉 ，

        private String catId;  /// 分类

        private int serviceId;//商品ID

        private int shopItemSkuId;//规格多,状态下的商品ID

        private String serviceImg;  /// 商品图片
        public String serviceName; ///商品名称

        private int shopId; /// 快店ID

        private String shopName;  //快店名字 shopName ，shopEmobId    这两个字段是为了兼容老版本快店添加的字段

        private String shopEmobId;  /// 快店emobid

        public String currentPrice; /// 现价
        private String originPrice; // 原价
        private String multiattribute; /// 是否有多种规格

        /**
         * {
         * "serviceId":1,
         * "serviceName":"雪花啤酒 听 330ml",
         * "serviceImg":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FpPmYMJmHqcpw1w-ZqS8x43PET8r",
         * "originPrice":"4",
         * "currentPrice":"2.8",
         * "catId":1,
         * "multiattribute":"yes",
         * <p/>
         * "children":[
         * {
         * "shopItemSkuId":1,
         * "serviceId":1,
         * "serviceName":"雪花啤酒 听 330ml",
         * "currentPrice":"2.8"
         * }]
         * }
         */
        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getShopEmobId() {
            return shopEmobId;
        }

        public void setShopEmobId(String shopEmobId) {
            this.shopEmobId = shopEmobId;
        }

        public List<PagerItemBean> getChildren() {
            return children;
        }

        public void setChildren(List<PagerItemBean> children) {
            this.children = children;
        }

        private List<PagerItemBean> children;

        public List<PagerItemBean> getList() {
            return children;
        }

        public void setList(List<PagerItemBean> children) {
            this.children = children;
        }

        public String getMultiattribute() {
            return multiattribute;
        }

        public void setMultiattribute(String multiattribute) {
            this.multiattribute = multiattribute;
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

        public int getServiceId() {
            return serviceId;
        }

        public void setServiceId(int serviceId) {
            this.serviceId = serviceId;
        }

        public String getServiceImg() {
            return serviceImg;
        }

        public void setServiceImg(String serviceImg) {
            this.serviceImg = serviceImg;
        }

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public int getShopId() {
            return shopId;
        }

        public void setShopId(int shopId) {
            this.shopId = shopId;
        }

        public String getCurrentPrice() {
            return currentPrice;
        }

        public void setCurrentPrice(String currentPrice) {
            this.currentPrice = currentPrice;
        }

        public String getOriginPrice() {
            return originPrice;
        }

        public void setOriginPrice(String originPrice) {
            this.originPrice = originPrice;
        }

        public int getShopItemSkuId() {
            return shopItemSkuId;
        }

        public void setShopItemSkuId(int shopItemSkuId) {
            this.shopItemSkuId = shopItemSkuId;
        }
    }
}
