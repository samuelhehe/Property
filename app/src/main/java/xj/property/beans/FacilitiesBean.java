package xj.property.beans;

/**
 * Created by Administrator on 2015/4/1.
 *
 *         "address": "北京市东城区营房西街5号",
 "adminId": 28,
 "businessEndTime": "11:35",
 "businessStartTime": "11:32",
 "communityId": 1,
 "createTime": 1429068799,
 "distance": "14620.15",
 "facilitiesClassId": 24,
 "facilitiesDesc": "",
 "facilityId": 337,
 "facilityName": "店铺1",
 "latitude": 39.89136,
 "logo": "http://ltzmaxwell.qiniudn.com/FqJA1Ai-cwdwJLv_AFejaw4SPmTp",
 "longitude": 116.43128,
 "phone": "15612346678",
 "status": "normal",
 "typeName": "办公文化用品"
 */
public class FacilitiesBean implements  XJ{
    private  String address;
    private int adminId;
    public  String businessStartTime;
    public  String businessEndTime;
    private  long communityId;
    private  long createTime;
    public double distance;
    private  String facilitiesClassId;
    private String facilitiesDesc;
    private int facilityId;
    public String facilityName;
    private double latitude;
    private double longitude;
    public   String logo;
    private  String phone;
    private  String status;
    private String typeName;

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
