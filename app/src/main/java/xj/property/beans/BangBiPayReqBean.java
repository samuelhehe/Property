package xj.property.beans;

/**
 * Created by Administrator on 2015/11/24.
 *
 * v3 2016/02/29
 */
public class BangBiPayReqBean extends  xj.property.netbase.BaseBean {


    /**
     * emobIdUser : d463b16dfc014466a1e441dd685ba505
     * serial : 1503271817510654
     * bonuscoin : 1050
     */

    private String emobIdUser;
    private String serial;
    private int bonuscoin;

    public void setEmobIdUser(String emobIdUser) {
        this.emobIdUser = emobIdUser;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public void setBonuscoin(int bonuscoin) {
        this.bonuscoin = bonuscoin;
    }

    public String getEmobIdUser() {
        return emobIdUser;
    }

    public String getSerial() {
        return serial;
    }

    public int getBonuscoin() {
        return bonuscoin;
    }
}
