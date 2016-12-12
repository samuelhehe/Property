package xj.property.beans;

import java.util.List;

public class IndexMenuRespBean {

    /**
     * status : yes
     * info : [{"serviceId":20,"relationStatus":"normal","serviceName":"会员卡","imgName":"home_item_shopvipcard","type":"bangbang","url":""},{"serviceId":19,"relationStatus":"normal","serviceName":"福利","imgName":"home_item_fuli","type":"bangbang","url":""},{"serviceId":3,"relationStatus":"normal","serviceName":"快店","imgName":"home_item_store","type":"bangbang","url":""},{"serviceId":8,"relationStatus":"normal","serviceName":"快递","imgName":"home_item_courier","type":"bangbang","url":""},{"serviceId":7,"relationStatus":"normal","serviceName":"物业客服","imgName":"home_item_servicer","type":"bangbang","url":""},{"serviceId":16,"relationStatus":"normal","serviceName":"生活圈","imgName":"home_item_lifecircle","type":"bangbang","url":""},{"serviceId":9,"relationStatus":"normal","serviceName":"维修","imgName":"home_item_repair","type":"bangbang","url":""},{"serviceId":10,"relationStatus":"normal","serviceName":"便捷号码","imgName":"home_item_phonenumber","type":"bangbang","url":""},{"serviceId":6,"relationStatus":"normal","serviceName":"送水","imgName":"home_item_water","type":"bangbang","url":""},{"serviceId":21,"relationStatus":"normal","serviceName":"邻居帮","imgName":"home_item_neighbor_help","type":"bangbang","url":""},{"serviceId":22,"relationStatus":"normal","serviceName":"邀请","imgName":"home_item_invite","type":"bangbang","url":""},{"serviceId":23,"relationStatus":"normal","serviceName":"投票","imgName":"home_item_vote","type":"bangbang","url":""},{"serviceId":24,"relationStatus":"normal","serviceName":"物业缴费","imgName":"home_item_pay_fees","type":"bangbang","url":""}]
     */

    private String status;
    private List<IndexBean> info;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setInfo(List<IndexBean> info) {
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public List<IndexBean> getInfo() {
        return info;
    }

}
