package xj.property.netbasebean;

import xj.property.netbase.BaseBean;

/**
 * Created by Administrator on 2016/3/23.
 */
public class DeleteShopOrder extends  BaseBean {
    /**
     * shopOrderId : 6
     * emobIdUser : d463b16dfc014466a1e441dd685ba505
     */

    private int shopOrderId;
    private String emobIdUser;

    public void setShopOrderId(int shopOrderId) {
        this.shopOrderId = shopOrderId;
    }

    public void setEmobIdUser(String emobIdUser) {
        this.emobIdUser = emobIdUser;
    }

    public int getShopOrderId() {
        return shopOrderId;
    }

    public String getEmobIdUser() {
        return emobIdUser;
    }


    /*
    {
    "shopOrderId": {订单ID},
    "emobIdUser": "{用户环信ID}"
}

     */






}
