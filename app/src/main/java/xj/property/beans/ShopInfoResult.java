package xj.property.beans;

/**
 * Created by Administrator on 2015/4/10.
 */
public class ShopInfoResult {
    public  String status;
    public ShopInfoBean info;
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ShopInfoBean getInfo() {
        return info;
    }

    public void setInfo(ShopInfoBean info) {
        this.info = info;
    }
}
