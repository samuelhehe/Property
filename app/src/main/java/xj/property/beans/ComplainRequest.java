package xj.property.beans;

/**
 * Created by Administrator on 2015/5/14.
 */
public class ComplainRequest extends BaseBean{
    public String emobIdShop;
    public String detail;
public String title;
    public String getEmobIdShop() {
        return emobIdShop;
    }

    public void setEmobIdShop(String emobIdShop) {
        this.emobIdShop = emobIdShop;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "ComplainRequest{" +
                "emobIdShop='" + emobIdShop + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }
}
