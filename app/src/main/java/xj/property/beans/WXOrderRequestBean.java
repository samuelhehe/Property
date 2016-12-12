package xj.property.beans;

/**
 * Created by che on 2015/9/24.
 */
public class WXOrderRequestBean {


    /**
     * sign : 637520C28B0F2A006D8CC61FDF9DF543
     * orderNo : 1020020160229122445493427
     * prepay_id : wx20160229122446d1f8c09e3e0031144653
     * nonce_str : vdjkffyqid2nnljdyrppgc7lx2o18nja
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
