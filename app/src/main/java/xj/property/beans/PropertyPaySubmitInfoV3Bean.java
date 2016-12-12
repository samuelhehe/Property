package xj.property.beans;

/**
 * 作者：che on 2016/3/17 11:05
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class PropertyPaySubmitInfoV3Bean {
    PropertyPaySubmitPaymentV3Bean payment;
    PropertyPaySubmitOwnerV3Bean owner;

    public PropertyPaySubmitPaymentV3Bean getPayment() {
        return payment;
    }

    public void setPayment(PropertyPaySubmitPaymentV3Bean payment) {
        this.payment = payment;
    }

    public PropertyPaySubmitOwnerV3Bean getOwner() {
        return owner;
    }

    public void setOwner(PropertyPaySubmitOwnerV3Bean owner) {
        this.owner = owner;
    }

    public class PropertyPaySubmitPaymentV3Bean {
        private int communityPaymentId;
        private int unitCount;
        private String paymentExplain;
        private String phone;

        public int getCommunityPaymentId() {
            return communityPaymentId;
        }

        public void setCommunityPaymentId(int communityPaymentId) {
            this.communityPaymentId = communityPaymentId;
        }

        public int getUnitCount() {
            return unitCount;
        }

        public void setUnitCount(int unitCount) {
            this.unitCount = unitCount;
        }

        public String getPaymentExplain() {
            return paymentExplain;
        }

        public void setPaymentExplain(String paymentExplain) {
            this.paymentExplain = paymentExplain;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

    public class PropertyPaySubmitOwnerV3Bean {
        private int communityOwnerId;
        private String floor;
        private String unit;
        private String room;
        private String name;
        private int area;
        private String unitPrice;
        private String arrearage;
        private int arrearageCount;

        public int getCommunityOwnerId() {
            return communityOwnerId;
        }

        public void setCommunityOwnerId(int communityOwnerId) {
            this.communityOwnerId = communityOwnerId;
        }

        public String getFloor() {
            return floor;
        }

        public void setFloor(String floor) {
            this.floor = floor;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getRoom() {
            return room;
        }

        public void setRoom(String room) {
            this.room = room;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getArea() {
            return area;
        }

        public void setArea(int area) {
            this.area = area;
        }

        public String getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(String unitPrice) {
            this.unitPrice = unitPrice;
        }

        public String getArrearage() {
            return arrearage;
        }

        public void setArrearage(String arrearage) {
            this.arrearage = arrearage;
        }

        public int getArrearageCount() {
            return arrearageCount;
        }

        public void setArrearageCount(int arrearageCount) {
            this.arrearageCount = arrearageCount;
        }
    }

}
