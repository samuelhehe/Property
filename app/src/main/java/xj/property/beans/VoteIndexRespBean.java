package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/11/24.
 * v3 2016/3/14
 */
public class VoteIndexRespBean {


    private int limit;
    private int page;
    private List<VoteIndexRespInfoBean> data;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<VoteIndexRespInfoBean> getData() {
        return data;
    }

    public void setData(List<VoteIndexRespInfoBean> data) {
        this.data = data;
    }

}
