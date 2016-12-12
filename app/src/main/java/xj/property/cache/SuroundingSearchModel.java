package xj.property.cache;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import xj.property.beans.XJ;

/**
 * Created by chenxiangyu on 2015/4/2.
 */
@Table(name = "search_his")
public class SuroundingSearchModel extends Model  implements  XJ{
    @Column(name = "address")
    public  String address;
    @Column(name = "adminId")
    public int adminId;
    @Column(name = "businessStartTime")
    public  String businessStartTime;
    @Column(name = "businessEndTime")
    public  String businessEndTime;
    @Column(name = "communityId")
    public  long communityId;
    @Column(name = "createTime")
    public  long createTime;
    @Column(name = "distance")
    public double distance;
    @Column(name = "facilitiesClassId")
    public  String facilitiesClassId;
    @Column(name = "facilitiesDesc")
    public String facilitiesDesc;
    @Column(name = "facilityId")
    public int facilityId;
    @Column(name = "facilityName")
    public String facilityName;
    @Column(name = "latitude")
    public double latitude;
    @Column(name = "longitude")
    public double longitude;
    @Column(name = "logo")
    public   String logo;
    @Column(name = "phone")
    public  String phone;
    @Column(name = "status")
    public  String status;
    @Column(name = "typeName")
    public String typeName;


    public SuroundingSearchModel(){
        super();
    }

    public SuroundingSearchModel(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getBusinessStartTime() {
        return businessStartTime;
    }

    public void setBusinessStartTime(String businessStartTime) {
        this.businessStartTime = businessStartTime;
    }

    public String getBusinessEndTime() {
        return businessEndTime;
    }

    public void setBusinessEndTime(String businessEndTime) {
        this.businessEndTime = businessEndTime;
    }

    public long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(long communityId) {
        this.communityId = communityId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getFacilitiesClassId() {
        return facilitiesClassId;
    }

    public void setFacilitiesClassId(String facilitiesClassId) {
        this.facilitiesClassId = facilitiesClassId;
    }

    public String getFacilitiesDesc() {
        return facilitiesDesc;
    }

    public void setFacilitiesDesc(String facilitiesDesc) {
        this.facilitiesDesc = facilitiesDesc;
    }

    public int getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
