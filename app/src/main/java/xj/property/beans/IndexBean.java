package xj.property.beans;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/3/13.
 */
public class IndexBean implements Serializable {
    /**
     * serviceId : 20
     * relationStatus : normal
     * serviceName : 会员卡
     * imgName : home_item_shopvipcard
     * type : bangbang
     * url :
     */

    private int serviceId;

    private String relationStatus;

    private String serviceName;

    private String imgName;

    public IndexBean(int serviceId, String relationStatus, String serviceName, String imgName, String type, String url) {
        this.serviceId = serviceId;
        this.relationStatus = relationStatus;
        this.serviceName = serviceName;
        this.imgName = imgName;
        this.type = type;
        this.url = url;
    }

    private String type;

    private String url;

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public void setRelationStatus(String relationStatus) {
        this.relationStatus = relationStatus;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getServiceId() {
        return serviceId;
    }

    public String getRelationStatus() {
        return relationStatus;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getImgName() {
        return imgName;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public IndexBean() {
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IndexBean indexBean = (IndexBean) o;

        if (serviceId != indexBean.serviceId) return false;
        if (imgName != null ? !imgName.equals(indexBean.imgName) : indexBean.imgName != null)
            return false;
        if (serviceName != null ? !serviceName.equals(indexBean.serviceName) : indexBean.serviceName != null)
            return false;

        return true;
    }

}
