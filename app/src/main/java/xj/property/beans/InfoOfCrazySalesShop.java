package xj.property.beans;

/**
 * Created by n on 2015/8/13.
 */
public class InfoOfCrazySalesShop {
    /**
     * createTime : 1439381791
     * crazySalesId : 12
     * crazySalesUserId : 0
     * status : unuse
     * userEmobId : 34347c343003e57232a5d21f14fe399e
     * code : 164925222204
     * shopEmobId : 34347c343003e57232a5d21f14fe399e
     * useTime : 0
     */
    private int createTime;
    private int crazySalesId;
    private int crazySalesUserId;
    private String status;
    private String userEmobId;
    private String code;
    private String shopEmobId;
    private int useTime;

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public void setCrazySalesId(int crazySalesId) {
        this.crazySalesId = crazySalesId;
    }

    public void setCrazySalesUserId(int crazySalesUserId) {
        this.crazySalesUserId = crazySalesUserId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUserEmobId(String userEmobId) {
        this.userEmobId = userEmobId;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setShopEmobId(String shopEmobId) {
        this.shopEmobId = shopEmobId;
    }

    public void setUseTime(int useTime) {
        this.useTime = useTime;
    }

    public int getCreateTime() {
        return createTime;
    }

    public int getCrazySalesId() {
        return crazySalesId;
    }

    public int getCrazySalesUserId() {
        return crazySalesUserId;
    }

    public String getStatus() {
        return status;
    }

    public String getUserEmobId() {
        return userEmobId;
    }

    public String getCode() {
        return code;
    }

    public String getShopEmobId() {
        return shopEmobId;
    }

    public int getUseTime() {
        return useTime;
    }
}
