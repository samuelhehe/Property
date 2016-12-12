package xj.property.beans;

/**
 * Created by Administrator on 2015/5/15.
 */
public class GetOrderInfoRequest extends BaseBean{
    public String subject;
    public String price;
    public String orderId;
    public int userBonusId;

    public String beanId = "bonusProcessor";

    public int bonusCoinCount;


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getUserBonusId() {
        return userBonusId;
    }

    public void setUserBonusId(int userBonusId) {
        this.userBonusId = userBonusId;
    }

    @Override
    public String toString() {
        return "GetOrderInfoRequest{" +
                "subject='" + subject + '\'' +
                ", price='" + price + '\'' +
                ", orderId=" + orderId +
                ", userBonusId=" + userBonusId +
                '}';
    }

    public int getBonusCoinCount() {
        return bonusCoinCount;
    }

    public void setBonusCoinCount(int bonusCoinCount) {
        this.bonusCoinCount = bonusCoinCount;
    }
}
