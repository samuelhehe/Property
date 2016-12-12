package xj.property.beans;

import java.io.Serializable;

/**
 * Created by asia on 2015/11/23.
 */
public class PropertyPayMessageBean implements Serializable {
    /**
     * "communityPaymentOptionId": 4,
     * "communityPaymentId": 2,
     * "name": "垃圾费",
     * "unitPrice": 20
     */

    private int communityPaymentOptionId;
    private int communityPaymentId;
    private String name;
    private float unitPrice;
    private int unitCount;

    public int getUnitCount() {
        return unitCount;
    }

    public void setUnitCount(int unitCount) {
        this.unitCount = unitCount;
    }

    public int getCommunityPaymentOptionId() {
        return communityPaymentOptionId;
    }

    public void setCommunityPaymentOptionId(int communityPaymentOptionId) {
        this.communityPaymentOptionId = communityPaymentOptionId;
    }

    public int getCommunityPaymentId() {
        return communityPaymentId;
    }

    public void setCommunityPaymentId(int communityPaymentId) {
        this.communityPaymentId = communityPaymentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }


}
