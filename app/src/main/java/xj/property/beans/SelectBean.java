package xj.property.beans;

import xj.property.cache.ShopContact;

/**
 * Created by Administrator on 2015/4/10.
 */
public class SelectBean {
    public String status;
    public ShopContact info;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ShopContact getInfo() {
        return info;
    }

    public void setInfo(ShopContact info) {
        this.info = info;
    }
}