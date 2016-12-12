package xj.property.beans;

/**
 * Created by asia on 2015/11/23.
 */
public class RunForAllInfoBean {

    private RunForAllPageBean page;//小区投票的用户信息

    private String MyElectedEmobId;//投过票的id

    public RunForAllPageBean getPage() {
        return page;
    }

    public void setPage(RunForAllPageBean page) {
        this.page = page;
    }

    public String getMyElectedEmobId() {
        return MyElectedEmobId;
    }

    public void setMyElectedEmobId(String myElectedEmobId) {
        MyElectedEmobId = myElectedEmobId;
    }
}
