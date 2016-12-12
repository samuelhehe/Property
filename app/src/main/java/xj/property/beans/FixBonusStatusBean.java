package xj.property.beans;

/**
 * Created by n on 2015/4/24.
 */
public class FixBonusStatusBean implements XJ {
    private String userBonusId;
    private String bonusStatus;
    private String orderId;
    private String useTime;

    public String getUserBonusId() {
        return userBonusId;
    }

    public void setUserBonusId(String userBonusId) {
        this.userBonusId = userBonusId;
    }

    public String getBonusStatus() {
        return bonusStatus;
    }

    public void setBonusStatus(String bonusStatus) {
        this.bonusStatus = bonusStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }
}
