package xj.property.beans;

/**
 * Created by n on 2015/8/13.
 */
public class PanicBuyingRequestBean extends BaseBean {

    /**
     * crazySalesId : 1
     * count : 2
     * userEmobId : 1231314
     */
    private int crazySalesId;
    private int count;
    private String userEmobId;

    public void setCrazySalesId(int crazySalesId) {
        this.crazySalesId = crazySalesId;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setUserEmobId(String userEmobId) {
        this.userEmobId = userEmobId;
    }

    public int getCrazySalesId() {
        return crazySalesId;
    }

    public int getCount() {
        return count;
    }

    public String getUserEmobId() {
        return userEmobId;
    }
}
