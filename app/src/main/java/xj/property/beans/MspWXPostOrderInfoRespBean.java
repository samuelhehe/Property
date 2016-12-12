package xj.property.beans;

/**
 * Created by che on 2015/9/24.
 * v3 2016/02/29
 */
public class MspWXPostOrderInfoRespBean  {


    /**
     * sign : 1A530B901C7627F38A409E2DCF17C0E5
     * orderNo : 1030020160225213319459830
     * prepay_id : wx20160225213319aedaaec12a0312206355
     * nonce_str : jxaz3vnu734yw7d9wq0pa9vv3zutwelw
     */

    private String sign;
    private String orderNo;
    private String prepay_id;
    private String nonce_str;

    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }
}
