package xj.property.beans;

import java.util.List;

/**
 * 作者：che on 2016/3/16 14:06
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class ValueSheetV3Bean implements XJ {



    /*
    {"catId":2,
    "catName":"空调维修",
    "catDesc":"内外机  加媒介  过滤网... ...",
    "catImg":"repair_light_electricity_bg.png",

"items":[

{"serviceId":16,"serviceName":"内外机","price":"40","catId":2},
{"serviceId":17,"serviceName":"加媒介","price":"20","catId":2},
{"serviceId":18,"serviceName":"过滤网","price":"10","catId":2}
]
}

     */

    private int catId;
    private String catName;
    private String catDesc;

    private String catImg;


    private List<ValueSheetV3ItemBean> items;

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

    public String getCatDesc() {
        return catDesc;
    }

    public void setCatDesc(String catDesc) {
        this.catDesc = catDesc;
    }

    public List<ValueSheetV3ItemBean> getItems() {
        return items;
    }

    public void setItems(List<ValueSheetV3ItemBean> items) {
        this.items = items;
    }

    public class ValueSheetV3ItemBean implements XJ {
        private int serviceId;
        public String serviceName;
        public String price;
        private int catId;


        /*
        {"serviceId":16,"serviceName":"内外机","price":"40","catId":2},
         */


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

        public int getCatId() {
            return catId;
        }

        public void setCatId(int catId) {
            this.catId = catId;
        }
    }
}
