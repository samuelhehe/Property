package xj.property.beans;

import java.util.List;

/**
 * Created by che on 2015/6/10.
 * v3 2016/03/04
 */
public class RPValueAllBean {
    private int page;

    private int limit;

    private List<RPValueBean> data;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }


    public List<RPValueBean> getData() {
        return data;
    }

    public void setData(List<RPValueBean> data) {
        this.data = data;
    }
}
