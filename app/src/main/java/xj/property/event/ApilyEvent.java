package xj.property.event;

/**
 * Created by Administrator on 2015/6/9.
 */
public class ApilyEvent {
    private String address;
    private int useBonusId;
    private String orderNo;

    public ApilyEvent(String orderNo,String address, int useBonusId) {
        this.orderNo = orderNo;
        this.address=address;
        this.useBonusId=useBonusId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getUseBonusId() {
        return useBonusId;
    }

    public void setUseBonusId(int useBonusId) {
        this.useBonusId = useBonusId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
