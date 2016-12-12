package xj.property.beans;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by che on 2015/7/20.
 */
public class NotEndOrdersBean  {

    /**
     * status : yes
     * info : [{"emobIdUser":"ce04f45b22793b5a2425962b38c74d08","emobIdShop":"ae0cd43a092ba5f2eb2a778850d4dc4b","action":"","orderPrice":"35.00","orderDetailBeanList":[{"attrId":0,"price":"10","count":1,"serviceId":2813,"attrName":"","serviceName":"测试数据"},{"attrId":0,"price":"10","count":1,"serviceId":2814,"attrName":"","serviceName":"测试数据"},{"attrId":0,"price":"15","count":1,"serviceId":2811,"attrName":"","serviceName":"测试数据"}],"serial":"1505112105400606","orderId":8498,"online":"yes","communityId":1},{"emobIdUser":"ce04f45b22793b5a2425962b38c74d08","emobIdShop":"ae0cd43a092ba5f2eb2a778850d4dc4b","action":"","orderPrice":"20.0","orderDetailBeanList":[],"serial":"1504141828076959","orderId":4580,"online":"yes","communityId":1},{"emobIdUser":"ce04f45b22793b5a2425962b38c74d08","emobIdShop":"ae0cd43a092ba5f2eb2a778850d4dc4b","action":"","orderPrice":"20.0","orderDetailBeanList":[],"serial":"1504141827462706","orderId":4579,"online":"yes","communityId":1}]
     */
    private String status;
    private List<InfoEntity> info;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setInfo(List<InfoEntity> info) {
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public List<InfoEntity> getInfo() {
        return info;
    }

    public class InfoEntity {
        /**
         * emobIdUser : ce04f45b22793b5a2425962b38c74d08
         * emobIdShop : ae0cd43a092ba5f2eb2a778850d4dc4b
         * action :
         * orderPrice : 35.00
         * orderDetailBeanList : [{"attrId":0,"price":"10","count":1,"serviceId":2813,"attrName":"","serviceName":"测试数据"},{"attrId":0,"price":"10","count":1,"serviceId":2814,"attrName":"","serviceName":"测试数据"},{"attrId":0,"price":"15","count":1,"serviceId":2811,"attrName":"","serviceName":"测试数据"}]
         * serial : 1505112105400606
         * orderId : 8498
         * online : yes
         * communityId : 1
         */
        private String emobIdUser;
        private String emobIdShop;
        private String action;
        private String orderPrice;
        private ArrayList<OrderDetailBeanList> orderDetailBeanList;
        private String serial;
        private int orderId;
        private String online;
        private int communityId;
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setEmobIdUser(String emobIdUser) {
            this.emobIdUser = emobIdUser;
        }

        public void setEmobIdShop(String emobIdShop) {
            this.emobIdShop = emobIdShop;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public void setOrderPrice(String orderPrice) {
            this.orderPrice = orderPrice;
        }

        public void setOrderDetailBeanList(ArrayList<OrderDetailBeanList> orderDetailBeanList) {
            this.orderDetailBeanList = orderDetailBeanList;
        }

        public void setSerial(String serial) {
            this.serial = serial;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public void setOnline(String online) {
            this.online = online;
        }

        public void setCommunityId(int communityId) {
            this.communityId = communityId;
        }

        public String getEmobIdUser() {
            return emobIdUser;
        }

        public String getEmobIdShop() {
            return emobIdShop;
        }

        public String getAction() {
            return action;
        }

        public String getOrderPrice() {
            return orderPrice;
        }

        public ArrayList<OrderDetailBeanList> getOrderDetailBeanList() {
            return orderDetailBeanList;
        }

        public String getSerial() {
            return serial;
        }

        public int getOrderId() {
            return orderId;
        }

        public String getOnline() {
            return online;
        }

        public int getCommunityId() {
            return communityId;
        }

/*        public class OrderDetailBeanListEntity {
            *//**
         * attrId : 0
         * price : 10
         * count : 1
         * serviceId : 2813
         * attrName :
         * serviceName : 测试数据
         *//*
            private int attrId;
            private String price;
            private int count;
            private int serviceId;
            private String attrName;
            private String serviceName;

            public void setAttrId(int attrId) {
                this.attrId = attrId;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public void setServiceId(int serviceId) {
                this.serviceId = serviceId;
            }

            public void setAttrName(String attrName) {
                this.attrName = attrName;
            }

            public void setServiceName(String serviceName) {
                this.serviceName = serviceName;
            }

            public int getAttrId() {
                return attrId;
            }

            public String getPrice() {
                return price;
            }

            public int getCount() {
                return count;
            }

            public int getServiceId() {
                return serviceId;
            }

            public String getAttrName() {
                return attrName;
            }

            public String getServiceName() {
                return serviceName;
            }
        }*/
    }
}
