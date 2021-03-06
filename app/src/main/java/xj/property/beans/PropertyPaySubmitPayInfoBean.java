package xj.property.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by asia on 2015/11/23.
 */
public class PropertyPaySubmitPayInfoBean implements Serializable {
    private int communityPaymentId;
    private int unitCount;
    private String paymentExplain;
    private String phone;


    private List<PropertyPayMessageBean> paymentOptions;

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

    public List<PropertyPayMessageBean> getPaymentOptions() {
        return paymentOptions;
    }

    public void setPaymentOptions(List<PropertyPayMessageBean> paymentOptions) {
        this.paymentOptions = paymentOptions;
    }
}
