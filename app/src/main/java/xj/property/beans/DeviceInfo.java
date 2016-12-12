package xj.property.beans;

/**
 * Created by Administrator on 2015/5/4.
 */
public class DeviceInfo  extends BaseBean{
public String platform;
public String cellId;
public int isMobileDevice;
public int haveWifi;
   public String userId;
   public String appkey;
   public String resolution;
   public String lac;
   public String network;
   public String version;
   public String deviceId;
   public String os_version;
   public int havebt;
   public int phoneType;
   public int haveGps;
    public String moduleName;
    public long time;
    public String  wifiMac;
    public String  deviceName;
    public String  longitude;
    public String  mccmnc;
    public String  latitude;
    public String  language;
    public int haveGravity;

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getCellId() {
        return cellId;
    }

    public void setCellId(String cellId) {
        this.cellId = cellId;
    }

    public int getIsMobileDevice() {
        return isMobileDevice;
    }

    public void setIsMobileDevice(int isMobileDevice) {
        this.isMobileDevice = isMobileDevice;
    }

    public int getHaveWifi() {
        return haveWifi;
    }

    public void setHaveWifi(int haveWifi) {
        this.haveWifi = haveWifi;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getLac() {
        return lac;
    }

    public void setLac(String lac) {
        this.lac = lac;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getOs_version() {
        return os_version;
    }

    public void setOs_version(String os_version) {
        this.os_version = os_version;
    }

    public int getHavebt() {
        return havebt;
    }

    public void setHavebt(int havebt) {
        this.havebt = havebt;
    }

    public int getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(int phoneType) {
        this.phoneType = phoneType;
    }

    public int getHaveGps() {
        return haveGps;
    }

    public void setHaveGps(int haveGps) {
        this.haveGps = haveGps;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getWifiMac() {
        return wifiMac;
    }

    public void setWifiMac(String wifiMac) {
        this.wifiMac = wifiMac;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getMccmnc() {
        return mccmnc;
    }

    public void setMccmnc(String mccmnc) {
        this.mccmnc = mccmnc;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getHaveGravity() {
        return haveGravity;
    }

    public void setHaveGravity(int haveGravity) {
        this.haveGravity = haveGravity;
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "platform='" + platform + '\'' +
                ", cellId='" + cellId + '\'' +
                ", isMobileDevice=" + isMobileDevice +
                ", haveWifi=" + haveWifi +
                ", userId='" + userId + '\'' +
                ", appkey='" + appkey + '\'' +
                ", resolution='" + resolution + '\'' +
                ", lac='" + lac + '\'' +
                ", network='" + network + '\'' +
                ", version='" + version + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", os_version='" + os_version + '\'' +
                ", havebt=" + havebt +
                ", phoneType=" + phoneType +
                ", haveGps=" + haveGps +
                ", moduleName='" + moduleName + '\'' +
                ", time=" + time +
                ", wifiMac='" + wifiMac + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", longitude='" + longitude + '\'' +
                ", mccmnc='" + mccmnc + '\'' +
                ", latitude='" + latitude + '\'' +
                ", language='" + language + '\'' +
                ", haveGravity=" + haveGravity +
                '}';
    }
}
