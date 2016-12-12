package xj.property.beans;

import android.view.View;

/**
 * Created by Administrator on 2015/3/13.
 */
public class IndexBean2 {
    public int communityServiceId;
    public String communityId;
    public String relationStatus;
    public String serviceName;
    public String imgName;
    public int serviceId;
    public View.OnClickListener listener;

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public boolean isComplete=false;
    public int getCommunityServiceId() {
        return communityServiceId;
    }

    public void setCommunityServiceId(int communityServiceId) {
        this.communityServiceId = communityServiceId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getRelationStatus() {
        return relationStatus;
    }

    public void setRelationStatus(String relationStatus) {
        this.relationStatus = relationStatus;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public IndexBean2(int communityServiceId, String communityId, String relationStatus, String serviceName, String imgName, boolean isComplete) {
        this.communityServiceId = communityServiceId;
        this.communityId = communityId;
        this.relationStatus = relationStatus;
        this.serviceName = serviceName;
        this.imgName = imgName;
        this.isComplete=isComplete;
    }

    public IndexBean2() {
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    @Override
    public String toString() {
        return "IndexBean{" +
                "communityServiceId='" + communityServiceId + '\'' +
                ", communityId='" + communityId + '\'' +
                ", relationStatus='" + relationStatus + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", imgName='" + imgName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IndexBean2 indexBean = (IndexBean2) o;

        if (serviceId != indexBean.serviceId) return false;
        if (imgName != null ? !imgName.equals(indexBean.imgName) : indexBean.imgName != null)
            return false;
        if (serviceName != null ? !serviceName.equals(indexBean.serviceName) : indexBean.serviceName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = communityServiceId;
        result = 31 * result + (communityId != null ? communityId.hashCode() : 0);
        return result;
    }
}
